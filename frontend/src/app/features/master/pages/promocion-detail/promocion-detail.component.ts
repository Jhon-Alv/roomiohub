import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PromocionTarifaService } from '../../services/promocion-tarifa.service';
import { PromocionService } from '../../services/promocion.service';
import { TarifaService } from '../../services/tarifa.service';
import { PromocionTarifaDto, PromocionDto, TarifaDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-promocion-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  template: `
<div class="p-container-padding max-w-7xl mx-auto space-y-6">
  <div class="flex items-center gap-3 mb-2">
    <button (click)="goBack()" class="w-10 h-10 flex items-center justify-center rounded-full hover:bg-surface-container-high transition-colors text-on-surface-variant">
      <span class="material-symbols-outlined">arrow_back</span>
    </button>
    <div class="flex flex-col">
      <h2 class="font-h2 text-h2 text-primary flex items-center gap-2">
        {{ promocion()?.nombre || 'Cargando...' }}
        @if (promocion() && promocion()!.activo) {
          <span class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full font-label-sm text-[10px] bg-surface-container-highest text-on-surface"><span class="w-1.5 h-1.5 rounded-full bg-primary-container"></span>Activa</span>
        } @else if (promocion() && !promocion()!.activo) {
          <span class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full font-label-sm text-[10px] bg-error-container text-on-error-container"><span class="w-1.5 h-1.5 rounded-full bg-error"></span>Inactiva</span>
        }
      </h2>
      <p class="font-body-sm text-body-sm text-on-surface-variant mt-0.5">Configuración de tarifas promocionales</p>
    </div>
  </div>

  <div class="flex flex-col md:flex-row md:items-end justify-between gap-4">
    <div class="flex-1"></div>
    <button (click)="openCreateModal()" class="bg-[#0a2463] text-white px-5 py-2.5 rounded-lg font-label-md text-label-md hover:bg-[#0a2463]/90 transition-colors shadow-[0_1px_3px_rgba(0,0,0,0.1)] flex items-center gap-2 active:scale-95 duration-150">
      <span class="material-symbols-outlined text-[20px]">add</span>
      Agregar Tarifa Promocional
    </button>
  </div>

  <div class="bg-surface rounded-xl border border-outline-variant shadow-sm overflow-hidden">
    <div class="overflow-x-auto relative min-h-[236px]">
      @if (isLoading()) {
        <div class="absolute inset-0 bg-surface/50 backdrop-blur-sm flex items-center justify-center z-10">
          <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
        </div>
      }

      <table class="w-full text-left border-collapse table-fixed">
        <thead>
          <tr class="bg-surface-container-low border-b border-outline-variant">
            <th class="px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider">Categoría</th>
            <th class="px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider">Plan de Tiempo</th>
            <th class="px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider text-right">Precio Base</th>
            <th class="px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider text-right text-primary">Precio Promo</th>
            <th class="w-32 px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider text-center">Estado</th>
            <th class="w-28 px-6 py-3 font-label-sm text-label-sm text-on-surface-variant uppercase tracking-wider text-right">Acciones</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-outline-variant bg-surface">
          @for (item of tarifas(); track item.id) {
            <tr class="hover:bg-surface-container-lowest transition-colors group">
              <td class="px-6 py-4 font-body-sm text-body-sm text-on-surface-variant">{{ item.categoriaNombre }}</td>
              <td class="px-6 py-4 font-body-sm text-body-sm text-on-surface-variant">{{ item.planTiempoNombre }}</td>
              <td class="px-6 py-4 font-body-sm text-body-sm text-on-surface-variant text-right line-through opacity-70">{{ item.precioBase | currency:'PEN':'symbol':'1.2-2' }}</td>
              <td class="px-6 py-4 font-body-md text-body-md text-primary text-right font-bold">{{ item.precioPromocion | currency:'PEN':'symbol':'1.2-2' }}</td>
              <td class="px-6 py-4 text-center">
                @if (item.activo) {
                  <span class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full font-label-sm text-label-sm bg-surface-container-highest text-on-surface"><span class="w-1.5 h-1.5 rounded-full bg-primary-container"></span>Activo</span>
                } @else {
                  <span class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full font-label-sm text-label-sm bg-error-container text-on-error-container border border-error/50"><span class="w-1.5 h-1.5 rounded-full bg-error"></span>Inactivo</span>
                }
              </td>
              <td class="px-6 py-4 text-right">
                <div class="flex items-center justify-end gap-2 transition-opacity">
                  <button (click)="openEditModal(item)" class="w-8 h-8 shrink-0 flex items-center justify-center text-outline hover:text-primary transition-colors rounded-full hover:bg-surface-container-low" title="Editar Precio">
                    <span class="material-symbols-outlined text-[20px] leading-none">edit</span>
                  </button>
                  <button (click)="toggleStatus(item)" class="w-8 h-8 shrink-0 flex items-center justify-center text-outline transition-colors rounded-full" 
                    [ngClass]="item.activo ? 'hover:text-error hover:bg-error-container' : 'hover:text-primary hover:bg-surface-container-low'"
                    [title]="item.activo ? 'Desactivar' : 'Activar'">
                    <span class="material-symbols-outlined text-[20px] leading-none">{{ item.activo ? 'delete' : 'restore' }}</span>
                  </button>
                </div>
              </td>
            </tr>
          } @empty {
            <tr>
              <td colspan="6" class="px-6 py-8 text-center text-on-surface-variant font-body-sm text-body-sm">
                No se han configurado tarifas para esta promoción.
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
    
    <div class="p-4 border-t border-outline-variant flex items-center justify-between bg-surface-container-lowest">
      <span class="font-body-sm text-body-sm text-on-surface-variant">
        Mostrando {{tarifas().length}} {{ tarifas().length === 1 ? 'tarifa promocional' : 'tarifas promocionales' }}
      </span>
    </div>
  </div>
</div>

<!-- Modal -->
@if (isModalOpen()) {
  <div class="fixed inset-0 z-50 flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-black/40 backdrop-blur-sm transition-opacity" (click)="closeModal()"></div>
    
    <div class="relative bg-surface rounded-2xl w-full max-w-md shadow-lg flex flex-col max-h-[90vh] animate-in fade-in zoom-in-95 duration-200">
      <div class="px-6 py-4 border-b border-outline-variant flex items-center justify-between sticky top-0 bg-surface rounded-t-2xl z-10">
        <h3 class="font-h2 text-h2 text-on-surface">
          {{ modalMode() === 'create' ? 'Agregar Tarifa Promocional' : 'Editar Precio' }}
        </h3>
        <button (click)="closeModal()" class="text-on-surface-variant hover:bg-surface-container-low p-2 rounded-full transition-colors flex items-center justify-center">
          <span class="material-symbols-outlined text-[20px]">close</span>
        </button>
      </div>

      <div class="p-6 overflow-y-auto custom-scrollbar">
        <form class="space-y-4">
          
          @if (modalMode() === 'create') {
            <div class="space-y-1.5">
              <label class="font-label-sm text-label-sm text-on-surface-variant flex items-center gap-1">
                Tarifa Base (Categoría - Plan) <span class="text-error">*</span>
              </label>
              <app-custom-select 
                [options]="availableTarifasOptions()" 
                [ngModel]="form().tarifaId" 
                name="tarifaId"
                (ngModelChange)="onTarifaSelect($event)">
              </app-custom-select>
            </div>
          } @else {
            <div class="space-y-1.5">
              <label class="font-label-sm text-label-sm text-on-surface-variant">Tarifa Base</label>
              <div class="px-4 py-2 bg-surface-container-lowest border border-outline-variant rounded-lg font-body-sm text-body-sm text-on-surface-variant">
                {{ selectedTarifaLabel() }}
              </div>
            </div>
          }

          @if (form().precioBaseSeleccionado !== null) {
            <div class="space-y-1.5">
              <label class="font-label-sm text-label-sm text-on-surface-variant">Precio Base Actual</label>
              <div class="font-body-md text-body-md text-on-surface line-through">
                {{ form().precioBaseSeleccionado | currency:'PEN':'symbol':'1.2-2' }}
              </div>
            </div>
          }

          <div class="space-y-1.5">
            <label class="font-label-sm text-label-sm text-on-surface-variant flex items-center gap-1">
              Precio Promoción <span class="text-error">*</span>
            </label>
            <div class="relative">
              <span class="absolute left-4 top-1/2 -translate-y-1/2 text-on-surface-variant font-body-md">S/</span>
              <input type="number" min="0" step="0.1" [ngModel]="form().precioPromocion" (ngModelChange)="form.update(f => ({...f, precioPromocion: $event}))" name="precioPromocion" required
                class="w-full pl-10 pr-4 py-2.5 bg-surface border border-outline-variant rounded-lg font-body-md text-body-md text-on-surface focus:border-primary focus:ring-1 focus:ring-primary outline-none transition-all">
            </div>
          </div>
        </form>
      </div>

      <div class="px-6 py-4 border-t border-outline-variant bg-surface-container-lowest rounded-b-2xl flex justify-end gap-3 sticky bottom-0 z-10">
        <button type="button" (click)="closeModal()" class="px-5 py-2.5 rounded-lg font-label-md text-label-md text-on-surface-variant hover:bg-surface-variant transition-colors active:scale-95 duration-150">
          Cancelar
        </button>
        <button type="button" (click)="save()" [disabled]="isSaving()" class="bg-[#0a2463] text-white px-5 py-2.5 rounded-lg font-label-md text-label-md hover:bg-[#0a2463]/90 transition-colors shadow-[0_1px_3px_rgba(0,0,0,0.1)] active:scale-95 duration-150 disabled:opacity-50 flex items-center gap-2">
          @if (isSaving()) {
            <div class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
          }
          Guardar
        </button>
      </div>
    </div>
  </div>
}
  `
})
export class PromocionDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private promocionService = inject(PromocionService);
  private promocionTarifaService = inject(PromocionTarifaService);
  private tarifaBaseService = inject(TarifaService);
  private notificationService = inject(NotificationService);

  promocionId = signal<string>('');
  promocion = signal<PromocionDto | null>(null);
  tarifas = signal<PromocionTarifaDto[]>([]);
  allTarifasBase = signal<TarifaDto[]>([]);
  isLoading = signal<boolean>(true);

  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedAsociacionId = signal<string | null>(null);
  selectedTarifaLabel = signal<string>('');
  isSaving = signal(false);

  form = signal({
    tarifaId: '',
    precioPromocion: 0,
    precioBaseSeleccionado: null as number | null
  });

  availableTarifasOptions = computed<SelectOption[]>(() => {
    // Filter out tarifas that are already added to this promocion
    const currentTarifaIds = new Set(this.tarifas().map(t => t.tarifaId));
    return this.allTarifasBase()
      .filter(t => !currentTarifaIds.has(t.id) && t.activo)
      .map(t => ({
        value: t.id,
        label: `${t.categoriaNombre} - ${t.planTiempoNombre} (S/${t.precioBase})`
      }));
  });

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.promocionId.set(id);
        this.loadInitialData();
      }
    });
  }

  loadInitialData() {
    this.isLoading.set(true);
    // Load promocion info
    this.promocionService.getPromocionById(this.promocionId()).subscribe({
      next: (promocion) => {
        this.promocion.set(promocion);
        this.loadTarifasPromocion();
      },
      error: () => {
        this.notificationService.showError('Error al cargar la promoción');
        this.goBack();
      }
    });

    // Load all active base tarifas to show in select
    this.tarifaBaseService.getTarifas({ activo: true }).subscribe({
      next: (data) => this.allTarifasBase.set(data),
      error: () => console.error('Error loading tarifas base')
    });
  }

  loadTarifasPromocion() {
    this.isLoading.set(true);
    this.promocionTarifaService.getTarifasByPromocion(this.promocionId()).subscribe({
      next: (data) => {
        this.tarifas.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.notificationService.showError('Error al cargar tarifas promocionales');
        this.isLoading.set(false);
      }
    });
  }

  onTarifaSelect(tarifaId: string) {
    if (!tarifaId) {
      this.form.update(f => ({ ...f, tarifaId: '', precioBaseSeleccionado: null }));
      return;
    }
    const selected = this.allTarifasBase().find(t => t.id === tarifaId);
    this.form.update(f => ({ 
      ...f, 
      tarifaId, 
      precioBaseSeleccionado: selected ? selected.precioBase : null,
      precioPromocion: selected ? selected.precioBase : 0
    }));
  }

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedAsociacionId.set(null);
    this.form.set({ tarifaId: '', precioPromocion: 0, precioBaseSeleccionado: null });
    this.isModalOpen.set(true);
  }

  openEditModal(item: PromocionTarifaDto) {
    this.modalMode.set('edit');
    this.selectedAsociacionId.set(item.id);
    this.selectedTarifaLabel.set(`${item.categoriaNombre} - ${item.planTiempoNombre}`);
    this.form.set({ 
      tarifaId: item.tarifaId, 
      precioPromocion: item.precioPromocion,
      precioBaseSeleccionado: item.precioBase
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  save() {
    const data = this.form();
    const mode = this.modalMode();

    if (mode === 'create' && !data.tarifaId) {
      this.notificationService.showError('Seleccione una tarifa base.');
      return;
    }
    if (data.precioPromocion === null || data.precioPromocion < 0) {
      this.notificationService.showError('Ingrese un precio válido.');
      return;
    }

    this.isSaving.set(true);

    if (mode === 'create') {
      this.promocionTarifaService.batchCreateTarifas(this.promocionId(), {
        items: [{
          tarifaId: data.tarifaId,
          precioPromocion: data.precioPromocion
        }]
      }).subscribe({
        next: () => {
          this.notificationService.showSuccess('Tarifa agregada a la promoción');
          this.isSaving.set(false);
          this.closeModal();
          this.loadTarifasPromocion();
        },
        error: () => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al agregar la tarifa');
        }
      });
    } else {
      const ascId = this.selectedAsociacionId();
      if (ascId) {
        this.promocionTarifaService.updatePrecio(this.promocionId(), ascId, {
          precioPromocion: data.precioPromocion
        }).subscribe({
          next: () => {
            this.notificationService.showSuccess('Precio de promoción actualizado');
            this.isSaving.set(false);
            this.closeModal();
            this.loadTarifasPromocion();
          },
          error: () => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar el precio');
          }
        });
      }
    }
  }

  toggleStatus(item: PromocionTarifaDto) {
    const newStatus = !item.activo;
    this.promocionTarifaService.toggleStatus(this.promocionId(), item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Tarifa promocional ${newStatus ? 'activada' : 'desactivada'}`);
        this.loadTarifasPromocion();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado de la tarifa');
      }
    });
  }

  goBack() {
    this.router.navigate(['/master/promociones']);
  }
}
