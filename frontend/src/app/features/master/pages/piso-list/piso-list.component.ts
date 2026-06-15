import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { PisoService } from '../../services/piso.service';
import { PisoDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';

@Component({
  selector: 'app-piso-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './piso-list.component.html'
})
export class PisoListComponent implements OnInit {
  private pisoService = inject(PisoService);
  private notificationService = inject(NotificationService);
  private sedeState = inject(SedeStateService);

  pisos = signal<PisoDto[]>([]);
  isLoading = signal<boolean>(true);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedId = signal<string | null>(null);
  isSaving = signal(false);

  form = signal({
    nombre: ''
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
    this.loadData();

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadData();
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

    this.pisoService.getPisos({
      search: this.searchTerm() || undefined,
      sedeId: sedeId,
      activo: activo
    }).subscribe({
      next: (data) => {
        this.pisos.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching pisos', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedId.set(null);
    this.form.set({ nombre: '' });
    this.isModalOpen.set(true);
  }

  openEditModal(item: PisoDto) {
    this.modalMode.set('edit');
    this.selectedId.set(item.id);
    this.form.set({ 
      nombre: item.nombre
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
      this.notificationService.showError('No hay una sede seleccionada para crear el piso.');
      return;
    }

    if (!data.nombre) {
      this.notificationService.showError('El nombre es obligatorio.');
      return;
    }

    this.isSaving.set(true);

    if (this.modalMode() === 'create') {
      this.pisoService.createPiso({ ...data, sedeId }).subscribe({
        next: () => {
          this.notificationService.showSuccess('Piso creado exitosamente');
          this.isSaving.set(false);
          this.closeModal();
          this.loadData();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al crear el piso');
        }
      });
    } else {
      const id = this.selectedId();
      if (id) {
        this.pisoService.updatePiso(id, data).subscribe({
          next: () => {
            this.notificationService.showSuccess('Piso actualizado');
            this.isSaving.set(false);
            this.closeModal();
            this.loadData();
          },
          error: (err) => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar el piso');
          }
        });
      }
    }
  }

  toggleStatus(item: PisoDto) {
    const newStatus = !item.activo;
    this.pisoService.toggleStatus(item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Piso ${newStatus ? 'activado' : 'desactivado'}`);
        this.loadData();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado');
      }
    });
  }
}
