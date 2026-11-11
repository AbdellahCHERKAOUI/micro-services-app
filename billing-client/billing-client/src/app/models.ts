export interface Customer { id?: number; name: string; email: string; }
export interface Product { id?: number; name: string; price: number; quantity: number; }
export interface InvoiceItem { id: number; productId: number; product?: Product; quantity: number; unitPrice: number; }
export interface Invoice { id: number; date: string; customerId: number; customer?: Customer; invoiceItems: InvoiceItem[]; total: number; }
