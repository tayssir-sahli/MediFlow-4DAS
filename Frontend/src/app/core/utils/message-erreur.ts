import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../models/dossier.models';

/** Transforme une erreur HTTP en message lisible pour l'utilisateur. */
export function messageErreur(err: HttpErrorResponse): string {
  const api = err.error && typeof err.error === 'object' ? (err.error as Partial<ApiError>) : null;

  // Erreur renvoyée par ton backend : 400, 404, 409...
  if (api?.message) {
    const details = api.details?.length ? ` (${api.details.join(' ; ')})` : '';
    return api.message + details;
  }

  // Pas de réponse du backend : service arrêté ou proxy mal configuré
  if (err.status === 0 || err.status >= 500) {
    return 'Le service Dossier Médical est injoignable. Vérifiez qu\'il est démarré.';
  }

  return `Erreur ${err.status}`;
}