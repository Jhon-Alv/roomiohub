import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { TarifaService } from '../../services/tarifa.service';
import { CatalogService } from '../../services/catalog.service';
import { TarifaDto, CatalogDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-tarifa-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './tarifa-list.component.html'
})
export class TarifaListComponent implements OnInit {
  private tarifaService = inject(TarifaService);
  private catalogService = inject(CatalogService);
  private notificationService = inject(NotificationService);

  tarifas = signal<TarifaDto[]>([]);
  isLoading = signal<boolean>(true);

  categoriasOptions = signal<SelectOption[]>([]);
  planesOptions = signal<SelectOption[]>([]);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedId = signal<string | null>(null);
  isSaving = signal(false);

  form = signal({
    precioBase: 0,
    categoriaId: '',
    planTiempoId: ''
  });

  // Filters state
  searchTerm = signal<string>('');
  selectedEstado = signal<string>('Estado: Todos');
  selectedCategoria = signal<string>('Todas');
  selectedPlanTiempo = signal<string>('Todos');

  filterCategoriasOptions = computed<SelectOption[]>(() => [
    { value: 'Todas', label: 'Categoría: Todas' },
    ...this.categoriasOptions()
  ]);

  filterPlanesOptions = computed<SelectOption[]>(() => [
    { value: 'Todos', label: 'Plan: Todos' },
    ...this.planesOptions()
  ]);

  estadoFilterOptions: SelectOption[] = [
    { value: 'Estado: Todos', label: 'Estado: Todos' },
    { value: 'Activo', label: 'Activo' },
    { value: 'Inactivo', label: 'Inactivo' }
  ];

  private searchSubject = new Subject<string>();

  ngOnInit() {
    this.loadCatalogs();
    this.loadData();

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadData();
    });
  }

  loadCatalogs() {
    this.catalogService.getCategorias().subscribe({
      next: (cats) => {
        this.categoriasOptions.set(cats.map(c => ({ value: c.id, label: c.label })));
      }
    });

    this.catalogService.PlanesTiempoCatalog().subscribe({
      next: (planes) => {
        this.planesOptions.set(planes.map(p => ({ value: p.id, label: p.label })));
      }
    });
  }

  onSearchChange(value: string) {
    this.searchTerm.set(value);
    this.searchSubject.next(value);
  }

  onFilterChange() {
    this.loadData();
  }

  loadData() {
    this.isLoading.set(true);
    let activo: boolean | undefined = undefined;
    if (this.selectedEstado() === 'Activo') activo = true;
    if (this.selectedEstado() === 'Inactivo') activo = false;

    this.tarifaService.getTarifas({
      search: this.searchTerm() || undefined,
      activo: activo,
      categoriaId: this.selectedCategoria() !== 'Todas' ? this.selectedCategoria() : undefined,
      planTiempoId: this.selectedPlanTiempo() !== 'Todos' ? this.selectedPlanTiempo() : undefined
    }).subscribe({
      next: (data) => {
        this.tarifas.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching tarifas', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedId.set(null);
    this.form.set({ precioBase: 0, categoriaId: '', planTiempoId: '' });
    this.isModalOpen.set(true);
  }

  openEditModal(item: TarifaDto) {
    this.modalMode.set('edit');
    this.selectedId.set(item.id);
    
    const catId = this.categoriasOptions().find(c => c.label === item.categoriaNombre)?.value || '';
    const planId = this.planesOptions().find(p => p.label === item.planTiempoNombre)?.value || '';

    this.form.set({ 
      precioBase: item.precioBase, 
      categoriaId: catId, 
      planTiempoId: planId 
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  save() {
    const data = this.form();
    if (data.precioBase <= 0 || !data.categoriaId || !data.planTiempoId) {
      this.notificationService.showError('Por favor, complete todos los campos correctamente.');
      return;
    }

    this.isSaving.set(true);

    if (this.modalMode() === 'create') {
      this.tarifaService.createTarifa(data).subscribe({
        next: () => {
          this.notificationService.showSuccess('Tarifa creada exitosamente');
          this.isSaving.set(false);
          this.closeModal();
          this.loadData();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al crear la tarifa');
        }
      });
    } else {
      const id = this.selectedId();
      if (id) {
        this.tarifaService.updateTarifa(id, data).subscribe({
          next: () => {
            this.notificationService.showSuccess('Tarifa actualizada');
            this.isSaving.set(false);
            this.closeModal();
            this.loadData();
          },
          error: (err) => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar la tarifa');
          }
        });
      }
    }
  }

  toggleStatus(item: TarifaDto) {
    const newStatus = !item.activo;
    this.tarifaService.toggleStatus(item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Tarifa ${newStatus ? 'activada' : 'desactivada'}`);
        this.loadData();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado');
      }
    });
  }
}
