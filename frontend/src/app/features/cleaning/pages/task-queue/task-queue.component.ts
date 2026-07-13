import { Component, OnInit, OnDestroy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CleaningSseService } from '../../services/cleaning-sse.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { CleaningRoomDto } from '../../../../shared/models/cleaning.models';
import { toSignal } from '@angular/core/rxjs-interop';
import { IniciarLimpiezaModalComponent } from '../../components/iniciar-limpieza-modal/iniciar-limpieza-modal.component';
import { FinalizarLimpiezaModalComponent } from '../../components/finalizar-limpieza-modal/finalizar-limpieza-modal.component';

@Component({
  selector: 'app-task-queue',
  standalone: true,
  imports: [CommonModule, IniciarLimpiezaModalComponent, FinalizarLimpiezaModalComponent],
  templateUrl: './task-queue.component.html'
})
export class TaskQueueComponent implements OnInit, OnDestroy {
  private cleaningSseService = inject(CleaningSseService);
  private sedeState = inject(SedeStateService);

  rooms = toSignal(this.cleaningSseService.rooms$, { initialValue: [] });

  sucias = computed(() =>
    this.rooms().filter(r => r.estadoActual === 'SUCIO').sort(this.porAntiguedad)
  );
  enLimpieza = computed(() =>
    this.rooms().filter(r => r.estadoActual === 'EN_LIMPIEZA').sort(this.porAntiguedad)
  );
  ocupadas = computed(() =>
    this.rooms().filter(r => r.estadoActual === 'OCUPADO')
  );

  iniciarModal = signal<CleaningRoomDto | null>(null);
  finalizarModal = signal<CleaningRoomDto | null>(null);

  ngOnInit() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (sedeId) {
      this.cleaningSseService.connect(sedeId);
    }
  }

  ngOnDestroy() {
    this.cleaningSseService.disconnect();
  }

  tiempoRelativo(fecha: string): string {
    if (!fecha) return '';
    const diff = Date.now() - new Date(fecha).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 60) return `hace ${mins} minutos`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `hace ${hrs} ${hrs === 1 ? 'hora' : 'horas'}`;
    const dias = Math.floor(hrs / 24);
    return `hace ${dias} ${dias === 1 ? 'día' : 'días'}`;
  }

  private porAntiguedad(a: CleaningRoomDto, b: CleaningRoomDto): number {
    const dateA = a.fechaUltimoCambio ? new Date(a.fechaUltimoCambio).getTime() : 0;
    const dateB = b.fechaUltimoCambio ? new Date(b.fechaUltimoCambio).getTime() : 0;
    return dateA - dateB;
  }
}
