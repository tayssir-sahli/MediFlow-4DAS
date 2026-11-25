import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from './layout/footer/footer';
import { LayoutService } from './layout/layout.service';
import { Navbar } from './layout/navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [Navbar, Footer, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  host: {
    '[class.offcanvas-menu]': 'layout.menuOpen()',
    '(window:resize)': 'onResize()',
  },
})
export class App {
  protected readonly layout = inject(LayoutService);

  protected onResize(): void {
    if (window.innerWidth >= 992) {
      this.layout.closeMenu();
    }
  }

  protected onSiteWrapClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.layout.closeMenu();
    }
  }
}