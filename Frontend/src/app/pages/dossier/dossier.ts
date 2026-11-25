import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DossierMedical } from '../../core/models/dossier.models';
import { CLASSE_GRAVITE, LIBELLE_GRAVITE, LIBELLE_TYPE_DOCUMENT } from '../../core/models/libelles';
import { DossierService } from '../../core/services/dossier.service';
import { messageErreur } from '../../core/utils/message-erreur';

type OngletId = 'allergies' | 'antecedents' | 'consultations' | 'documents';

interface Onglet {
  id: OngletId;
  libelle: string;
}

@Component({
  selector: 'app-dossier',
  imports: [RouterLink, DatePipe],
  templateUrl: './dossier.html',
  styleUrl: './dossier.scss',
})
export class Dossier implements OnInit {
  private readonly service = inject(DossierService);

  /** Provisoire : plus tard, ce sera le patient connecté ou choisi par le médecin */
  protected readonly patientId = 12;

  protected readonly dossier = signal<DossierMedical | null>(null);
  protected readonly erreur = signal<string | null>(null);
  protected readonly chargement = signal(true);
  protected readonly ongletActif = signal<OngletId>('allergies');

  protected readonly onglets: Onglet[] = [
    { id: 'allergies', libelle: 'Allergies' },
    { id: 'antecedents', libelle: 'Antécédents' },
    { id: 'consultations', libelle: 'Consultations' },
    { id: 'documents', libelle: 'Documents' },
  ];

  /** Nombre d'éléments de chaque onglet, recalculé quand le dossier change */
  protected readonly compteurs = computed<Record<OngletId, number>>(() => {
    const d = this.dossier();
    return {
      allergies: d?.allergies.length ?? 0,
      antecedents: d?.antecedents.length ?? 0,
      consultations: d?.consultations.length ?? 0,
      documents: d?.documents.length ?? 0,
    };
  });

  protected readonly libelleGravite = LIBELLE_GRAVITE;
  protected readonly classeGravite = CLASSE_GRAVITE;
  protected readonly libelleTypeDocument = LIBELLE_TYPE_DOCUMENT;

  ngOnInit(): void {
    this.service.getDossier(this.patientId).subscribe({
      next: dossier => {
        this.dossier.set(dossier);
        this.chargement.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.erreur.set(messageErreur(err));
        this.chargement.set(false);
      },
    });
  }

  protected choisirOnglet(event: Event, onglet: OngletId): void {
    event.preventDefault();
    this.ongletActif.set(onglet);
  }
}