import { Gravite, TypeDocument } from './dossier.models';

export const LIBELLE_GRAVITE: Record<Gravite, string> = {
  LEGERE: 'Légère',
  MODEREE: 'Modérée',
  GRAVE: 'Grave',
};

/** Classes Bootstrap 4 de la pastille de gravité */
export const CLASSE_GRAVITE: Record<Gravite, string> = {
  LEGERE: 'badge-success',
  MODEREE: 'badge-warning',
  GRAVE: 'badge-danger',
};

export const LIBELLE_TYPE_DOCUMENT: Record<TypeDocument, string> = {
  ANALYSE: 'Analyse',
  RADIO: 'Radiographie',
  COMPTE_RENDU: 'Compte rendu',
  AUTRE: 'Autre',
};