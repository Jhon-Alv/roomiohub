import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { PlanTiempoService } from '../../services/plan-tiempo.service';
import { PlanTiempoDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-plan-tiempo-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './plan-tiempo-list.component.html'
})
export class PlanTiempoListComponent implements OnInit {
  private planTiempoService = inject(PlanTiempoService);
  private notificationService = inject(NotificationService);

  planes = signal<PlanTiempoDto[]>([]);
  isLoading = signal<boolean>(true);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedId = signal<string | null>(null);
  isSaving = signal(false);

  form = signal({
    nombre: '',
    horas: 1
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
    this.isLoading.set(true);
    let activo: boolean | undefined = undefined;
    if (this.selectedEstado() === 'Activo') activo = true;
    if (this.selectedEstado() === 'Inactivo') activo = false;

    this.planTiempoService.getPlanesTiempo({
      search: this.searchTerm() || undefined,
      activo: activo
    }).subscribe({
      next: (data) => {
        this.planes.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching planes', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedId.set(null);
    this.form.set({ nombre: '', horas: 1 });
    this.isModalOpen.set(true);
  }

  openEditModal(item: PlanTiempoDto) {
    this.modalMode.set('edit');
    this.selectedId.set(item.id);
    this.form.set({ 
      nombre: item.nombre, 
      horas: item.horas
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  save() {
    const data = this.form();
    if (!data.nombre || data.horas <= 0) {
      this.notificationService.showError('Por favor, complete los campos obligatorios correctamente.');
      return;
    }

    this.isSaving.set(true);

    if (this.modalMode() === 'create') {
      this.planTiempoService.createPlanTiempo(data).subscribe({
        next: () => {
          this.notificationService.showSuccess('Plan de tiempo creado exitosamente');
          this.isSaving.set(false);
          this.closeModal();
          this.loadData();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al crear el plan');
        }
      });
    } else {
      const id = this.selectedId();
      if (id) {
        this.planTiempoService.updatePlanTiempo(id, data).subscribe({
          next: () => {
            this.notificationService.showSuccess('Plan de tiempo actualizado');
            this.isSaving.set(false);
            this.closeModal();
            this.loadData();
          },
          error: (err) => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar el plan');
          }
        });
      }
    }
  }

  toggleStatus(item: PlanTiempoDto) {
    const newStatus = !item.activo;
    this.planTiempoService.toggleStatus(item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Plan de tiempo ${newStatus ? 'activado' : 'desactivado'}`);
        this.loadData();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado');
      }
    });
  }
}
