import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Api } from './api';

interface Msg { from: 'user' | 'bot'; text: string; }

@Component({
  selector: 'app-chat',
  imports: [FormsModule],
  template: `
    <h2>Assistant de facturation</h2>
    <div class="chat">
      @for (m of messages(); track $index) { <div class="bubble" [class.user]="m.from === 'user'">{{ m.text }}</div> }
      @if (loading()) { <div class="bubble">…</div> }
    </div>
    <form class="row" (ngSubmit)="send()">
      <input name="msg" [(ngModel)]="text" placeholder="Ex : Crée une facture pour Hassan avec 2 imprimantes" autocomplete="off">
      <button [disabled]="loading()">Envoyer</button>
    </form>`,
})
export class Chat {
  private api = inject(Api);
  // un identifiant par session de navigateur = une mémoire de conversation côté agent
  private cid = 'web-' + Math.random().toString(36).slice(2);
  messages = signal<Msg[]>([{ from: 'bot', text: 'Bonjour ! Je gère clients, produits et factures. Que puis-je faire ?' }]);
  loading = signal(false);
  text = '';

  send() {
    const t = this.text.trim();
    if (!t || this.loading()) return;
    this.messages.update(m => [...m, { from: 'user', text: t }]);
    this.text = '';
    this.loading.set(true);
    this.api.chat(this.cid, t).subscribe({
      next: r => { this.messages.update(m => [...m, { from: 'bot', text: r }]); this.loading.set(false); },
      error: () => { this.messages.update(m => [...m, { from: 'bot', text: "Erreur : l'agent ne répond pas." }]); this.loading.set(false); },
    });
  }
}
