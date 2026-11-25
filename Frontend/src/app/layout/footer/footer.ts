import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LIENS_MENU } from '../liens-menu';

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
})
export class Footer {
  protected readonly liens = LIENS_MENU;
  protected readonly annee = new Date().getFullYear();
}