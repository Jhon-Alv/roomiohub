import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed top-6 right-6 z-9999 flex flex-col gap-3 w-full max-w-sm pointer-events-none">
      @for (toast of notificationService.toasts(); track toast.id) {
        
        <!-- ERROR TOAST -->
        @if (toast.type === 'error') {
          <div class="pointer-events-auto flex w-full shadow-lg border-l-4 overflow-hidden toast-slide-in bg-rose-50 border-rose-500 rounded-xl"
               [class.toast-slide-out]="toast.closing">
            <div class="flex p-4 w-full">
              <div class="shrink-0">
                <span class="material-symbols-outlined text-rose-600 text-[24px]">error</span>
              </div>
              <div class="ml-3 flex-1 pt-0.5">
                <h3 class="text-sm font-semibold text-rose-800">{{ toast.title }}</h3>
                <div class="mt-1 text-sm text-rose-700">
                  {{ toast.message }}
                </div>
              </div>
              <div class="ml-4 shrink-0 flex pt-0.5">
                <button (click)="notificationService.removeToast(toast.id)" class="inline-flex text-rose-500 hover:text-rose-700 focus:outline-none transition-colors" type="button">
                  <span class="material-symbols-outlined text-[20px]">close</span>
                </button>
              </div>
            </div>
          </div>
        }

        <!-- WARNING TOAST -->
        @if (toast.type === 'warning') {
          <div class="pointer-events-auto flex w-full shadow-lg border-l-4 overflow-hidden toast-slide-in bg-amber-50 border-amber-500 rounded-xl"
               [class.toast-slide-out]="toast.closing">
            <div class="flex p-4 w-full">
              <div class="shrink-0">
                <span class="material-symbols-outlined text-amber-600 text-[24px]">warning</span>
              </div>
              <div class="ml-3 flex-1 pt-0.5">
                <h3 class="text-sm font-semibold text-amber-800">{{ toast.title }}</h3>
                <div class="mt-1 text-sm text-amber-700">
                  {{ toast.message }}
                </div>
              </div>
              <div class="ml-4 shrink-0 flex pt-0.5">
                <button (click)="notificationService.removeToast(toast.id)" class="inline-flex text-amber-600 hover:text-amber-800 focus:outline-none transition-colors" type="button">
                  <span class="material-symbols-outlined text-[20px]">close</span>
                </button>
              </div>
            </div>
          </div>
        }

        <!-- SUCCESS TOAST -->
        @if (toast.type === 'success') {
          <div class="pointer-events-auto flex w-full shadow-lg border-l-4 overflow-hidden toast-slide-in bg-emerald-50 border-emerald-500 rounded-xl"
               [class.toast-slide-out]="toast.closing">
            <div class="flex p-4 w-full">
              <div class="shrink-0">
                <span class="material-symbols-outlined text-emerald-600 text-[24px]">check_circle</span>
              </div>
              <div class="ml-3 flex-1 pt-0.5">
                <h3 class="text-sm font-semibold text-emerald-800">{{ toast.title }}</h3>
                <div class="mt-1 text-sm text-emerald-700">
                  {{ toast.message }}
                </div>
              </div>
              <div class="ml-4 shrink-0 flex pt-0.5">
                <button (click)="notificationService.removeToast(toast.id)" class="inline-flex text-emerald-600 hover:text-emerald-800 focus:outline-none transition-colors" type="button">
                  <span class="material-symbols-outlined text-[20px]">close</span>
                </button>
              </div>
            </div>
          </div>
        }

      }
    </div>
  `,
  styles: [`
    .toast-slide-in {
      animation: slideInRight 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
    }

    .toast-slide-out {
      animation: slideOutRight 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards !important;
    }
    
    @keyframes slideInRight {
      from {
        opacity: 0;
        transform: translateX(100%);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    @keyframes slideOutRight {
      from {
        opacity: 1;
        transform: translateX(0);
      }
      to {
        opacity: 0;
        transform: translateX(100%);
      }
    }
  `]
})
export class ToastContainerComponent {
  notificationService = inject(NotificationService);
}
