import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Api } from './api';
import { Customer } from './models';

@Component({
  selector: 'app-customers',
  imports: [FormsModule],
  template: `
    <h2>Clients</h2>
    @if (error()) { <p class="error">{{ error() }}</p> }
    <form class="row" (ngSubmit)="add()">
      <input name="name" [(ngModel)]="name" placeholder="Nom" required>
      <input name="email" [(ngModel)]="email" placeholder="Email" required>
      <button>Ajouter</button>
    </form>
    <table>
      <tr><th>ID</th><th>Nom</th><th>Email</th><th></th></tr>
      @for (c of customers(); track c.id) {
        <tr><td>{{ c.id }}</td><td>{{ c.name }}</td><td>{{ c.email }}</td>
            <td><button class="danger" (click)="remove(c.id!)">Supprimer</button></td></tr>
      }
    </table>`,
})
export class Customers {
  private api = inject(Api);
  customers = signal<Customer[]>([]);
  error = signal('');
  name = ''; email = '';

  constructor() { this.load(); }
  load() {
    this.api.customers().subscribe({
      next: c => { this.customers.set(c); this.error.set(''); },
      error: () => this.error.set('Impossible de joindre la gateway (customer-service).'),
    });
  }
  add() {
    this.api.addCustomer({ name: this.name, email: this.email }).subscribe(() => { this.name = ''; this.email = ''; this.load(); });
  }
  remove(id: number) { this.api.deleteCustomer(id).subscribe({ next: () => this.load(), error: () => this.error.set('Suppression impossible.') }); }
}
