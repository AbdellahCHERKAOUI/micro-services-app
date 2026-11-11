import { Routes } from '@angular/router';
import { Customers } from './customers';
import { Products } from './products';
import { Invoices } from './invoices';
import { InvoiceDetail } from './invoice-detail';
import { Chat } from './chat';

export const routes: Routes = [
  { path: '', redirectTo: 'customers', pathMatch: 'full' },
  { path: 'customers', component: Customers },
  { path: 'products', component: Products },
  { path: 'invoices', component: Invoices },
  { path: 'invoices/:id', component: InvoiceDetail },
  { path: 'chat', component: Chat },
];
