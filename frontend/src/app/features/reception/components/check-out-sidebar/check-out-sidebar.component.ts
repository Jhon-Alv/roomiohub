import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, OnChanges, SimpleChanges, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { RoomMapDto, FindReservaActivaResponse, CheckOutRequest } from '../../../../shared/models/booking.models';

@Component({
  selector: 'app-check-out-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './check-out-sidebar.component.html'
})
export class CheckOutSidebarComponent implements OnInit, OnDestroy, OnChanges {
  @Input() habitacion!: RoomMapDto;
  @Output() close = new EventEmitter<void>();
  @Output() completed = new EventEmitter<void>();

  private bookingService = inject(BookingService);
  private sedeState = inject(SedeStateService);
  private notificationService = inject(NotificationService);

  preview = signal<FindReservaActivaResponse | null>(null);
  loadingConfirm = signal(false);
  loadingPreview = signal(false);

  ahora = signal(new Date());
  private interval: ReturnType<typeof setInterval> | null = null;

  tiempoExcedidoMinutos = signal(0);
  excedido = computed(() => this.tiempoExcedidoMinutos() > 0);

  ahoraFormateado = computed(() => this.formatTimeStr(this.ahora()));

  ngOnInit() {
    this.interval = setInterval(() => this.ahora.set(new Date()), 30000);
    this.cargarDatos();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['habitacion'] && !changes['habitacion'].firstChange) {
      this.cargarDatos();
    }
  }

  private cargarDatos() {
    this.loadingPreview.set(true);

    this.bookingService.getReservaActiva(this.habitacion.id).subscribe({
      next: (data) => {
        this.preview.set(data);
        const salidaEsperada = new Date(data.fechaSalidaEsperada);
        const now = new Date();
        if (now > salidaEsperada) {
          this.tiempoExcedidoMinutos.set(Math.floor((now.getTime() - salidaEsperada.getTime()) / 60000));
        } else {
          this.tiempoExcedidoMinutos.set(0);
        }
        this.loadingPreview.set(false);
      },
      error: () => {
        this.loadingPreview.set(false);
      }
    });
  }

  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }

  confirmar() {
    const sedeId = this.sedeState.activeSede()?.id;
    const p = this.preview();
    if (!sedeId || !p) return;

    this.loadingConfirm.set(true);

    const payload: CheckOutRequest = {
      sedeId,
      montoPenalidad: p.penalidadSugerida
    };

    this.bookingService.realizarCheckOut(this.habitacion.id, payload).subscribe({
      next: () => {
        this.notificationService.showSuccess('Check-out realizado con éxito');
        this.loadingConfirm.set(false);
        this.completed.emit();
        this.close.emit();
      },
      error: () => {
        this.loadingConfirm.set(false);
      }
    });
  }

  formatTime(dateStr: string): string {
    return this.formatTimeStr(new Date(dateStr));
  }

  private formatTimeStr(date: Date): string {
    return date.toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit', hour12: true });
  }

  getExcedidoText(): string {
    const mins = this.tiempoExcedidoMinutos();
    if (mins < 60) return `+${mins} min`;
    const h = Math.floor(mins / 60);
    const m = mins % 60;
    return m > 0 ? `+${h}h ${m}m` : `+${h}h`;
  }
}
