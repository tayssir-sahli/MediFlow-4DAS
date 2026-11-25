export interface LienMenu {
  chemin: string;
  libelle: string;
  exact: boolean;
}

export const LIENS_MENU: readonly LienMenu[] = [
  { chemin: '/', libelle: 'Accueil', exact: true },
  { chemin: '/dossier', libelle: 'Dossier médical', exact: false },
];