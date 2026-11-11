import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Customer, Invoice, Product } from './models';

// Tous les appels passent par la Gateway (routes dynamiques Eureka)
const GATEWAY = 'http://localhost:8888';

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);

  customers() { return this.http.get<Customer[]>(`${GATEWAY}/customer-service/customers`); }
  addCustomer(c: Customer) { return this.http.post<Customer>(`${GATEWAY}/customer-service/customers`, c); }
  deleteCustomer(id: number) { return this.http.delete<void>(`${GATEWAY}/customer-service/customers/${id}`); }

  products() { return this.http.get<Product[]>(`${GATEWAY}/inventory-service/products`); }
  addProduct(p: Product) { return this.http.post<Product>(`${GATEWAY}/inventory-service/products`, p); }
  deleteProduct(id: number) { return this.http.delete<void>(`${GATEWAY}/inventory-service/products/${id}`); }

  invoices() { return this.http.get<Invoice[]>(`${GATEWAY}/billing-service/invoices`); }
  invoice(id: number) { return this.http.get<Invoice>(`${GATEWAY}/billing-service/invoices/${id}`); }

  chat(conversationId: string, message: string) {
    return this.http.post(`${GATEWAY}/ai-agent-service/chat`, { conversationId, message }, { responseType: 'text' });
  }
}
