import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { LayoutService } from '../layout.service';
import { LIENS_MENU } from '../liens-menu';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  protected readonly layout = inject(LayoutService);
  protected readonly liens = LIENS_MENU;

  protected basculerMenu(event: Event): void {
    event.preventDefault();
    this.layout.toggleMenu();
  }
}