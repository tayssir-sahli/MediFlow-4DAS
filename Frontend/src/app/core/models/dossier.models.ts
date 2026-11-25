export type Gravite = 'LEGERE' | 'MODEREE' | 'GRAVE';
export type TypeDocument = 'ANALYSE' | 'RADIO' | 'COMPTE_RENDU' | 'AUTRE';

// ---------- Réponses du backend ----------

export interface Allergie {
  id: string;
  substance: string;
  gravite: Gravite;
}

export interface Antecedent {
  id: string;
  description: string;
  date: string;
}

export interface Consultation {
  id: string;
  date: string;
  diagnostic: string;
  notes: string | null;
}

export interface DocumentMedical {
  id: string;
  type: TypeDocument;
  nomFichier: string;
  url: string;
  date: string;
}

export interface DossierMedical {
  id: string;
  patientId: number;
  groupeSanguin: string | null;
  allergies: Allergie[];
  antecedents: Antecedent[];
  consultations: Consultation[];
  documents: DocumentMedical[];
  dateCreation: string;
  dateMiseAJour: string;
}

// ---------- Requêtes envoyées au backend ----------

export interface CreerDossierRequest {
  patientId: number;
  groupeSanguin?: string | null;
}

export interface AllergieRequest {
  substance: string;
  gravite: Gravite;
}

export interface AntecedentRequest {
  description: string;
  date: string;
}

export interface ConsultationRequest {
  date: string;
  diagnostic: string;
  notes?: string | null;
}

export interface DocumentRequest {
  type: TypeDocument;
  nomFichier: string;
  url: string;
}

// ---------- Format d'erreur du backend (ApiError) ----------

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  details: string[];
}