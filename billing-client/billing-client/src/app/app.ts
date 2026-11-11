import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav>
      <b>Facturation</b>
      <a routerLink="/customers" routerLinkActive="active">Clients</a>
      <a routerLink="/products" routerLinkActive="active">Produits</a>
      <a routerLink="/invoices" routerLinkActive="active">Factures</a>
      <a routerLink="/chat" routerLinkActive="active">Assistant IA</a>
    </nav>
    <main><router-outlet /></main>`,
})
export class App {}
