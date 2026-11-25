import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LayoutService {
  private readonly _menuOpen = signal(false);

  /** true quand le panneau du menu mobile est ouvert */
  readonly menuOpen = this._menuOpen.asReadonly();

  toggleMenu(): void {
    this._menuOpen.update(open => !open);
  }

  closeMenu(): void {
    this._menuOpen.set(false);
  }
}