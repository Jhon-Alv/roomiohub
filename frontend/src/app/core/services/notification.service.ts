import { Injectable, signal } from '@angular/core';

export type ToastType = 'error' | 'warning' | 'success';

export interface Toast {
  id: string;
  type: ToastType;
  title: string;
  message: string;
  closing?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  toasts = signal<Toast[]>([]);

  showSuccess(message: string, title: string = 'Éxito') {
    this.addToast('success', title, message);
  }

  showError(message: string, title: string = 'Error') {
    this.addToast('error', title, message);
  }

  showWarning(message: string, title: string = 'Advertencia') {
    this.addToast('warning', title, message);
  }

  private addToast(type: ToastType, title: string, message: string) {
    const id = Math.random().toString(36).substring(2, 9);
    const newToast: Toast = { id, type, title, message };
    
    this.toasts.update(current => [...current, newToast]);

    setTimeout(() => {
      this.removeToast(id);
    }, 5000);
  }

  removeToast(id: string) {
    this.toasts.update(current => 
      current.map(t => t.id === id ? { ...t, closing: true } : t)
    );
    setTimeout(() => {
      this.toasts.update(current => current.filter(t => t.id !== id));
    }, 200); 
  }
}
