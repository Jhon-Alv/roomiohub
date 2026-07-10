import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { HabitacionService } from '../../services/habitacion.service';
import { CatalogService } from '../../services/catalog.service';
import { HabitacionDto, CatalogDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';

@Component({
  selector: 'app-habitacion-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './habitacion-list.component.html'
})
export class HabitacionListComponent implements OnInit {
  private habitacionService = inject(HabitacionService);
  private catalogService = inject(CatalogService);
  private notificationService = inject(NotificationService);
  private sedeState = inject(SedeStateService);

  habitaciones = signal<HabitacionDto[]>([]);
  isLoading = signal<boolean>(true);

  categoriasOptions = signal<SelectOption[]>([]);
  pisosOptions = signal<SelectOption[]>([]);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedId = signal<string | null>(null);
  isSaving = signal(false);

  form = signal({
    numero: '',
    categoriaId: '',
    pisoId: ''
  });

  // Filters state
  searchTerm = signal<string>('');
  selectedEstado = signal<string>('Estado: Todos');
  selectedCategoria = signal<string>('Todas');
  selectedPiso = signal<string>('Todos');

  filterCategoriasOptions = computed<SelectOption[]>(() => [
    { value: 'Todas', label: 'Categoría: Todas' },
    ...this.categoriasOptions()
  ]);

  filterPisosOptions = computed<SelectOption[]>(() => [
    { value: 'Todos', label: 'Piso: Todos' },
    ...this.pisosOptions()
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

    const sedeId = this.sedeState.activeSede()?.id;
    if (sedeId) {
      this.catalogService.getPisos(sedeId).subscribe({
        next: (pisos) => {
          this.pisosOptions.set(pisos.map(p => ({ value: p.id, label: p.label })));
        }
      });
    }
  }

  onSearchChange(value: string) {
    this.searchTerm.set(value);
    this.searchSubject.next(value);
  }

  onFilterChange() {
    this.loadData();
  }

  loadData() {
    const sedeId = this.sedeState.activeSede()?.id;
    if (!sedeId) {
      this.isLoading.set(false);
      this.notificationService.showError('No hay una sede seleccionada');
      return;
    }

    this.isLoading.set(true);
    let activo: boolean | undefined = undefined;
    if (this.selectedEstado() === 'Activo') activo = true;
    if (this.selectedEstado() === 'Inactivo') activo = false;

    this.habitacionService.getHabitaciones({
      search: this.searchTerm() || undefined,
      activo: activo,
      sedeId: sedeId,
      categoriaId: this.selectedCategoria() !== 'Todas' ? this.selectedCategoria() : undefined,
      pisoId: this.selectedPiso() !== 'Todos' ? this.selectedPiso() : undefined
    }).subscribe({
      next: (data) => {
        this.habitaciones.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching habitaciones', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedId.set(null);
    this.form.set({ numero: '', categoriaId: '', pisoId: '' });
    this.isModalOpen.set(true);
  }

  openEditModal(item: HabitacionDto) {
    this.modalMode.set('edit');
    this.selectedId.set(item.id);
    
    // Find IDs from labels
    const catId = this.categoriasOptions().find(c => c.label === item.categoriaNombre)?.value || '';
    const pisoId = this.pisosOptions().find(p => p.label === item.pisoNombre)?.value || '';

    this.form.set({ 
      numero: item.numero, 
      categoriaId: catId,
      pisoId: pisoId 
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  save() {
    const data = this.form();
    const sedeId = this.sedeState.activeSede()?.id;

    if (!sedeId) {
      this.notificationService.showError('No hay una sede seleccionada.');
      return;
    }

    if (!data.numero || !data.categoriaId || !data.pisoId) {
      this.notificationService.showError('Por favor, complete todos los campos obligatorios.');
      return;
    }

    this.isSaving.set(true);

    if (this.modalMode() === 'create') {
      this.habitacionService.createHabitacion({ ...data, sedeId }).subscribe({
        next: () => {
          this.notificationService.showSuccess('Habitación creada exitosamente');
          this.isSaving.set(false);
          this.closeModal();
          this.loadData();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al crear la habitación');
        }
      });
    } else {
      const id = this.selectedId();
      if (id) {
        this.habitacionService.updateHabitacion(id, data).subscribe({
          next: () => {
            this.notificationService.showSuccess('Habitación actualizada');
            this.isSaving.set(false);
            this.closeModal();
            this.loadData();
          },
          error: (err) => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar la habitación');
          }
        });
      }
    }
  }

  toggleStatus(item: HabitacionDto) {
    const newStatus = !item.activo;
    this.habitacionService.toggleStatus(item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Habitación ${newStatus ? 'activada' : 'desactivada'}`);
        this.loadData();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado');
      }
    });
  }

  getEstadoStyles(estado: string): string {
    switch (estado) {
      case 'DISPONIBLE': return 'bg-[#d1fae5] text-[#065f46]';
      case 'OCUPADO': return 'bg-[#fee2e2] text-[#991b1b]';
      case 'SUCIO': return 'bg-[#ffedd5] text-[#9a3412]';
      case 'EN_LIMPIEZA': return 'bg-[#fef08a] text-[#854d0e]';
      case 'MANTENIMIENTO': return 'bg-[#f1f5f9] text-[#334155] border border-[#cbd5e1]';
      default: return 'bg-surface-container text-on-surface';
    }
  }
}
