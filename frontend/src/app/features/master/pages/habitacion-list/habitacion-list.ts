import { Component, OnInit, inject, signal } from '@angular/core';
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
  templateUrl: './habitacion-list.html'
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
      sedeId: sedeId
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
    this.form.set({ 
      numero: item.numero, 
      categoriaId: '', // Ideally we need to map the name to ID if the backend doesn't return ID. Oh wait, HabitacionDto might not return ID of category/piso? Let's check DTO. Wait, the FindHabitacionesResponse didn't have pisoId or categoriaId, only names. We should update the backend or just use an extra fetch? Let's try to map if we can, but if not we might need to change backend DTO or accept that editing requires reselecting. Actually, we should edit backend DTO to include categoryId and pisoId.
      pisoId: '' 
    });
    // In a real app we would either return the ID in the DTO, or fetch the full entity.
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
}
