import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from './api';
import { Product } from './models';

@Component({
  selector: 'app-products',
  imports: [FormsModule, DecimalPipe],
  template: `
    <h2>Produits</h2>
    @if (error()) { <p class="error">{{ error() }}</p> }
    <form class="row" (ngSubmit)="add()">
      <input name="name" [(ngModel)]="name" placeholder="Nom" required>
      <input name="price" type="number" [(ngModel)]="price" placeholder="Prix" required>
      <input name="qty" type="number" [(ngModel)]="quantity" placeholder="Stock" required>
      <button>Ajouter</button>
    </form>
    <table>
      <tr><th>ID</th><th>Nom</th><th>Prix</th><th>Stock</th><th></th></tr>
      @for (p of products(); track p.id) {
        <tr><td>{{ p.id }}</td><td>{{ p.name }}</td><td>{{ p.price | number:'1.2-2' }} DH</td><td>{{ p.quantity }}</td>
            <td><button class="danger" (click)="remove(p.id!)">Supprimer</button></td></tr>
      }
    </table>`,
})
export class Products {
  private api = inject(Api);
  products = signal<Product[]>([]);
  error = signal('');
  name = ''; price = 0; quantity = 0;

  constructor() { this.load(); }
  load() {
    this.api.products().subscribe({
      next: p => { this.products.set(p); this.error.set(''); },
      error: () => this.error.set('Impossible de joindre la gateway (inventory-service).'),
    });
  }
  add() {
    this.api.addProduct({ name: this.name, price: this.price, quantity: this.quantity })
      .subscribe(() => { this.name = ''; this.price = 0; this.quantity = 0; this.load(); });
  }
  remove(id: number) { this.api.deleteProduct(id).subscribe({ next: () => this.load(), error: () => this.error.set('Suppression impossible.') }); }
}
