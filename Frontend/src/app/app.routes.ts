import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    title: 'Accueil — MediFlow',
    loadComponent: () => import('./pages/accueil/accueil').then(m => m.Accueil),
  },
  {
    path: 'dossier',
    title: 'Dossier médical — MediFlow',
    loadComponent: () => import('./pages/dossier/dossier').then(m => m.Dossier),
  },
  { path: '**', redirectTo: '' },
];