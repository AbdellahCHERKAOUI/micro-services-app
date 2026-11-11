import { Component, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Api } from './api';
import { Invoice } from './models';

@Component({
  selector: 'app-invoices',
  imports: [DatePipe, DecimalPipe, RouterLink],
  template: `
    <h2>Factures</h2>
    @if (error()) { <p class="error">{{ error() }}</p> }
    <p class="hint">Pour créer une facture, demandez-le à l'assistant (onglet Assistant).</p>
    <table>
      <tr><th>N°</th><th>Date</th><th>Client</th><th>Articles</th><th>Total</th><th></th></tr>
      @for (i of invoices(); track i.id) {
        <tr><td>{{ i.id }}</td><td>{{ i.date | date:'dd/MM/yyyy HH:mm' }}</td><td>{{ i.customer?.name }}</td>
            <td>{{ i.invoiceItems.length }}</td><td>{{ i.total | number:'1.2-2' }} DH</td>
            <td><a [routerLink]="['/invoices', i.id]">Détail</a></td></tr>
      }
    </table>`,
})
export class Invoices {
  private api = inject(Api);
  invoices = signal<Invoice[]>([]);
  error = signal('');

  constructor() {
    this.api.invoices().subscribe({
      next: i => this.invoices.set(i),
      error: () => this.error.set('Impossible de charger les factures (billing-service).'),
    });
  }
}
