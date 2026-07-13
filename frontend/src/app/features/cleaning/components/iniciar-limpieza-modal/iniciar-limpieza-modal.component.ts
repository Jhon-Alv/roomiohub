import { Component, Input, Output, EventEmitter, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CleaningService } from '../../services/cleaning.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { CleaningRoomDto, IniciarLimpiezaRequest } from '../../../../shared/models/cleaning.models';

@Component({
  selector: 'app-iniciar-limpieza-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './iniciar-limpieza-modal.component.html'
})
export class IniciarLimpiezaModalComponent {
  @Input() habitacion!: CleaningRoomDto;
  @Output() close = new EventEmitter<void>();
  @Output() completed = new EventEmitter<void>();

  private cleaningService = inject(CleaningService);
  private sedeState = inject(SedeStateService);
  private notificationService = inject(NotificationService);

  observacion = signal('');
  loading = signal(false);

  confirmar() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) return;

    this.loading.set(true);

    const payload: IniciarLimpiezaRequest = {
      sedeId,
      observacion: this.observacion() || undefined
    };

    this.cleaningService.iniciarLimpieza(this.habitacion.id, payload).subscribe({
      next: () => {
        this.notificationService.showSuccess('Limpieza iniciada');
        this.loading.set(false);
        this.completed.emit();
        this.close.emit();
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }
}
