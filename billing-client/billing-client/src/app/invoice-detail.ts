import { Component, inject, input, signal, effect } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Api } from './api';
import { Invoice } from './models';

@Component({
  selector: 'app-invoice-detail',
  imports: [DatePipe, DecimalPipe, RouterLink],
  template: `
    <a routerLink="/invoices">← Retour</a>
    @if (error()) { <p class="error">{{ error() }}</p> }
    @if (invoice(); as inv) {
      <h2>Facture n° {{ inv.id }}</h2>
      <p>Date : {{ inv.date | date:'dd/MM/yyyy HH:mm' }}<br>
         Client : <b>{{ inv.customer?.name }}</b> ({{ inv.customer?.email }})</p>
      <table>
        <tr><th>Produit</th><th>Prix unitaire</th><th>Quantité</th><th>Sous-total</th></tr>
        @for (it of inv.invoiceItems; track it.id) {
          <tr><td>{{ it.product?.name }}</td><td>{{ it.unitPrice | number:'1.2-2' }} DH</td><td>{{ it.quantity }}</td>
              <td>{{ it.unitPrice * it.quantity | number:'1.2-2' }} DH</td></tr>
        }
        <tr class="total"><td colspan="3">Total</td><td>{{ inv.total | number:'1.2-2' }} DH</td></tr>
      </table>
    }`,
})
export class InvoiceDetail {
  private api = inject(Api);
  id = input.required<string>();   // paramètre de route /invoices/:id
  invoice = signal<Invoice | null>(null);
  error = signal('');

  constructor() {
    effect(() => {
      this.api.invoice(Number(this.id())).subscribe({
        next: i => this.invoice.set(i),
        error: () => this.error.set('Facture introuvable.'),
      });
    });
  }
}
