import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Allergie, AllergieRequest,
  Antecedent, AntecedentRequest,
  Consultation, ConsultationRequest,
  CreerDossierRequest,
  DocumentMedical, DocumentRequest,
  DossierMedical,
} from '../models/dossier.models';

@Injectable({ providedIn: 'root' })
export class DossierService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/dossiers';

  // ---------- Dossier ----------

  creerDossier(request: CreerDossierRequest): Observable<DossierMedical> {
    return this.http.post<DossierMedical>(this.baseUrl, request);
  }

  getDossier(patientId: number): Observable<DossierMedical> {
    return this.http.get<DossierMedical>(`${this.baseUrl}/${patientId}`);
  }

  modifierGroupeSanguin(patientId: number, groupeSanguin: string): Observable<DossierMedical> {
    return this.http.patch<DossierMedical>(`${this.baseUrl}/${patientId}/groupe-sanguin`, { groupeSanguin });
  }

  supprimerDossier(patientId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${patientId}`);
  }

  // ---------- Allergies ----------

  getAllergies(patientId: number): Observable<Allergie[]> {
    return this.http.get<Allergie[]>(`${this.baseUrl}/${patientId}/allergies`);
  }

  ajouterAllergie(patientId: number, request: AllergieRequest): Observable<Allergie> {
    return this.http.post<Allergie>(`${this.baseUrl}/${patientId}/allergies`, request);
  }

  supprimerAllergie(patientId: number, allergieId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${patientId}/allergies/${allergieId}`);
  }

  // ---------- Antécédents ----------

  getAntecedents(patientId: number): Observable<Antecedent[]> {
    return this.http.get<Antecedent[]>(`${this.baseUrl}/${patientId}/antecedents`);
  }

  ajouterAntecedent(patientId: number, request: AntecedentRequest): Observable<Antecedent> {
    return this.http.post<Antecedent>(`${this.baseUrl}/${patientId}/antecedents`, request);
  }

  // ---------- Consultations ----------

  getConsultations(patientId: number): Observable<Consultation[]> {
    return this.http.get<Consultation[]>(`${this.baseUrl}/${patientId}/consultations`);
  }

  ajouterConsultation(patientId: number, request: ConsultationRequest): Observable<Consultation> {
    return this.http.post<Consultation>(`${this.baseUrl}/${patientId}/consultations`, request);
  }

  // ---------- Documents ----------

  getDocuments(patientId: number): Observable<DocumentMedical[]> {
    return this.http.get<DocumentMedical[]>(`${this.baseUrl}/${patientId}/documents`);
  }

  ajouterDocument(patientId: number, request: DocumentRequest): Observable<DocumentMedical> {
    return this.http.post<DocumentMedical>(`${this.baseUrl}/${patientId}/documents`, request);
  }
}