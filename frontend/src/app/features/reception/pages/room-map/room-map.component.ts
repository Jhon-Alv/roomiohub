import { Component, OnInit, OnDestroy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CatalogService } from '../../../master/services/catalog.service';
import { RoomMapDto } from '../../../../shared/models/booking.models';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { RoomMapService } from '../../services/room-map.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-room-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './room-map.component.html'
})
export class RoomMapComponent implements OnInit, OnDestroy {
  private roomMapService = inject(RoomMapService);
  private catalogService = inject(CatalogService);
  private sedeState = inject(SedeStateService);

  habitaciones = toSignal(this.roomMapService.mapState$, { initialValue: [] });
  pisos = signal<{id: string, label: string}[]>([]);
  selectedPisoId = signal<string | null>(null);

  filteredHabitaciones = computed(() => {
    const pisoId = this.selectedPisoId();
    if (!pisoId) return this.habitaciones();
    // Wait, HabitacionDto only has pisoNombre, not pisoId. We need to filter by name or update the DTO.
    // For now, since pisoId is actually the id, we might need to filter by pisoNombre if we find the name from the pisos list.
    const selectedPisoName = this.pisos().find(p => p.id === pisoId)?.label;
    if (!selectedPisoName) return this.habitaciones();
    return this.habitaciones().filter(h => h.piso === selectedPisoName);
  });

  // Summary logic
  totalHabitaciones = computed(() => this.habitaciones().length);
  disponibles = computed(() => this.habitaciones().filter(h => h.estadoActual === 'DISPONIBLE').length);
  ocupadas = computed(() => this.habitaciones().filter(h => h.estadoActual === 'OCUPADO').length);
  sucias = computed(() => this.habitaciones().filter(h => h.estadoActual === 'SUCIO').length);
  mantenimiento = computed(() => this.habitaciones().filter(h => h.estadoActual === 'MANTENIMIENTO').length);

  ngOnInit() {
    this.loadCatalogs();
    
    const sedeId = this.sedeState.activeSede()?.id;
    if (sedeId) {
      this.roomMapService.connectToMapStream(sedeId);
    }
  }

  ngOnDestroy() {
    this.roomMapService.disconnect();
  }

  loadCatalogs() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (sedeId) {
      this.catalogService.getPisos(sedeId).subscribe({
        next: (pisos) => {
          this.pisos.set(pisos);
          if (pisos.length > 0) {
            this.selectedPisoId.set(pisos[0].id);
          }
        }
      });
    }
  }

  selectPiso(pisoId: string) {
    this.selectedPisoId.set(pisoId);
  }

  getBorderClass(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'bg-[#10b981]';
      case 'OCUPADO': return 'bg-[#ef4444]';
      case 'SUCIO': return 'bg-[#f97316]';
      case 'EN_LIMPIEZA': return 'bg-[#eab308]';
      case 'MANTENIMIENTO': return 'bg-[#64748b]';
      default: return 'bg-gray-400';
    }
  }

  openRoomAction(habitacion: RoomMapDto) {
    if (habitacion.estadoActual === 'DISPONIBLE') {
      console.log('Abrir Check-In para:', habitacion.numero);
      // TODO: Open Check-In Modal
    } else if (habitacion.estadoActual === 'OCUPADO') {
      console.log('Abrir Check-Out para:', habitacion.numero);
      // TODO: Open Check-Out Modal
    } else if (habitacion.estadoActual === 'SUCIO') {
      console.log('Marcar como limpiando o limpiar para:', habitacion.numero);
      // TODO: Open Cleaning options
    } else {
      console.log('Acción no disponible para este estado:', habitacion.estadoActual);
    }
  }

  getBadgeClass(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'bg-[#d1fae5] text-[#065f46]';
      case 'OCUPADO': return 'bg-[#fee2e2] text-[#991b1b]';
      case 'SUCIO': return 'bg-[#ffedd5] text-[#9a3412]';
      case 'EN_LIMPIEZA': return 'bg-[#fef08a] text-[#854d0e]';
      case 'MANTENIMIENTO': return 'bg-[#f1f5f9] text-[#334155] border border-[#cbd5e1]';
      default: return 'bg-gray-100 text-gray-700';
    }
  }

  getIcon(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'check_circle';
      case 'OCUPADO': return 'person';
      case 'SUCIO': return 'warning';
      case 'EN_LIMPIEZA': return 'cleaning_services';
      case 'MANTENIMIENTO': return 'build';
      default: return 'help';
    }
  }
}
