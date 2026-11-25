import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

interface Fonctionnalite {
  titre: string;
  texte: string;
}

@Component({
  selector: 'app-accueil',
  imports: [RouterLink],
  templateUrl: './accueil.html',
  styleUrl: './accueil.scss',
})
export class Accueil {
  protected readonly fonctionnalites: Fonctionnalite[] = [
    {
      titre: 'Rendez-vous et téléconsultation',
      texte: 'Réservez, annulez et rejoignez vos consultations vidéo en ligne.',
    },
    {
      titre: 'Dossier médical numérique',
      texte: 'Allergies, antécédents, consultations et documents réunis dans un dossier sécurisé.',
    },
    {
      titre: 'Ordonnances électroniques',
      texte: 'Des ordonnances avec QR code, vérifiées avant leur délivrance.',
    },
    {
      titre: 'Stocks pharmaceutiques',
      texte: 'Une gestion centralisée du catalogue, des stocks et des délivrances.',
    },
    {
      titre: 'Notifications et tableaux de bord',
      texte: 'Des rappels automatiques et des statistiques pour suivre l\'activité.',
    },
    {
      titre: 'Facturation et paiement',
      texte: 'Une facture générée après chaque consultation, avec paiement en ligne simulé.',
    },
  ];
}