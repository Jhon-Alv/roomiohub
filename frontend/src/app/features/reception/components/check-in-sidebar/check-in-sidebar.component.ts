import { Component, Input, Output, EventEmitter, OnInit, OnChanges, OnDestroy, SimpleChanges, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { TarifaService } from '../../../master/services/tarifa.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { RoomMapDto, CheckInRequest, MetodoPago } from '../../../../shared/models/booking.models';
import { TarifaDto } from '../../../../shared/models/master.models';

@Component({
  selector: 'app-check-in-sidebar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './check-in-sidebar.component.html'
})
export class CheckInSidebarComponent implements OnInit, OnChanges, OnDestroy {
  @Input() habitacion!: RoomMapDto;
  @Output() close = new EventEmitter<void>();
  @Output() completed = new EventEmitter<void>();

  private bookingService = inject(BookingService);
  private tarifaService = inject(TarifaService);
  private sedeState = inject(SedeStateService);
  private notificationService = inject(NotificationService);

  documento = signal('');
  nombres = signal('');
  cantidadHuespedes = signal(1);
  tarifas = signal<TarifaDto[]>([]);
  selectedTarifaId = signal<string | null>(null);
  metodoPago = signal<MetodoPago>('EFECTIVO');
  loading = signal(false);
  loadingTarifas = signal(false);

  selectedTarifa = signal<TarifaDto | null>(null);
  total = signal(0);

  private searchTimeout: ReturnType<typeof setTimeout> | null = null;

  constructor() {
    effect(() => {
      const tarifa = this.tarifas().find(t => t.id === this.selectedTarifaId());
      this.selectedTarifa.set(tarifa || null);
      this.total.set(tarifa?.precioBase || 0);
    });
  }

  ngOnInit() {
    this.cargarDatos();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['habitacion'] && !changes['habitacion'].firstChange) {
      this.cargarDatos();
    }
  }

  ngOnDestroy() {
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }
  }

  private cargarDatos() {
    this.selectedTarifaId.set(null);
    this.loadingTarifas.set(true);

    this.tarifaService.getTarifas({ categoriaId: this.habitacion.categoriaId, activo: true }).subscribe({
      next: (data) => {
        this.tarifas.set(data);
        if (data.length > 0) {
          this.selectedTarifaId.set(data[0].id);
        }
        this.loadingTarifas.set(false);
      },
      error: () => {
        this.loadingTarifas.set(false);
      }
    });
  }

  onDocumentoChange() {
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }
    const doc = this.documento().trim();
    if (doc.length < 8) return;

    this.searchTimeout = setTimeout(() => {
      this.bookingService.searchCliente(doc).subscribe({
        next: (res) => {
          if (res.encontrado && res.nombres) {
            this.nombres.set(res.nombres);
          }
        }
      });
    }, 300);
  }

  incrementGuests() {
    this.cantidadHuespedes.update(v => v + 1);
  }

  decrementGuests() {
    this.cantidadHuespedes.update(v => Math.max(1, v - 1));
  }

  selectTarifa(id: string) {
    this.selectedTarifaId.set(id);
  }

  selectMetodoPago(metodo: MetodoPago) {
    this.metodoPago.set(metodo);
  }

  confirmar() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId || !this.documento() || !this.nombres() || !this.selectedTarifaId()) return;

    this.loading.set(true);

    const payload: CheckInRequest = {
      numeroDocumento: this.documento(),
      nombresCliente: this.nombres(),
      sedeId,
      tarifaId: this.selectedTarifaId()!,
      cantidadHuespedes: this.cantidadHuespedes(),
      metodoPago: this.metodoPago()
    };

    this.bookingService.realizarCheckIn(this.habitacion.id, payload).subscribe({
      next: () => {
        this.notificationService.showSuccess('Check-in realizado con éxito');
        this.loading.set(false);
        this.completed.emit();
        this.close.emit();
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  getMetodoClass(metodo: MetodoPago): string {
    return this.metodoPago() === metodo
      ? 'border-2 border-primary bg-primary/5 text-primary'
      : 'border border-outline-variant bg-surface text-on-surface-variant hover:bg-surface-variant';
  }

  getChipClass(tarifaId: string): string {
    return this.selectedTarifaId() === tarifaId
      ? 'border-primary bg-primary/10 text-primary font-semibold'
      : 'border-outline-variant bg-surface text-on-surface-variant hover:bg-surface-variant';
  }
}
