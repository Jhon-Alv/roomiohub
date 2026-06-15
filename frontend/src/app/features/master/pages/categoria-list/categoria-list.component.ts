import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { CategoriaService } from '../../services/categoria.service';
import { CategoriaDto } from '../../../../shared/models/master.models';
import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';
import { NotificationService } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-categoria-list',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './categoria-list.component.html'
})
export class CategoriaListComponent implements OnInit {
  private categoriaService = inject(CategoriaService);
  private notificationService = inject(NotificationService);

  categorias = signal<CategoriaDto[]>([]);
  isLoading = signal<boolean>(true);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedId = signal<string | null>(null);
  isSaving = signal(false);

  form = signal({
    nombre: '',
    precioHoraExtra: 0
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

    this.categoriaService.getCategorias({
      search: this.searchTerm() || undefined,
      activo: activo
    }).subscribe({
      next: (data) => {
        this.categorias.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching categories', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedId.set(null);
    this.form.set({ nombre: '', precioHoraExtra: 0 });
    this.isModalOpen.set(true);
  }

  openEditModal(item: CategoriaDto) {
    this.modalMode.set('edit');
    this.selectedId.set(item.id);
    this.form.set({ 
      nombre: item.nombre, 
      precioHoraExtra: item.precioHoraExtra
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  save() {
    const data = this.form();
    if (!data.nombre || data.precioHoraExtra < 0) {
      this.notificationService.showError('Por favor, complete los campos obligatorios.');
      return;
    }

    this.isSaving.set(true);

    if (this.modalMode() === 'create') {
      this.categoriaService.createCategoria(data).subscribe({
        next: () => {
          this.notificationService.showSuccess('Categoría creada exitosamente');
          this.isSaving.set(false);
          this.closeModal();
          this.loadData();
        },
        error: (err) => {
          this.isSaving.set(false);
          this.notificationService.showError('Error al crear la categoría');
        }
      });
    } else {
      const id = this.selectedId();
      if (id) {
        this.categoriaService.updateCategoria(id, data).subscribe({
          next: () => {
            this.notificationService.showSuccess('Categoría actualizada');
            this.isSaving.set(false);
            this.closeModal();
            this.loadData();
          },
          error: (err) => {
            this.isSaving.set(false);
            this.notificationService.showError('Error al actualizar la categoría');
          }
        });
      }
    }
  }

  toggleStatus(item: CategoriaDto) {
    const newStatus = !item.activo;
    this.categoriaService.toggleStatus(item.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Categoría ${newStatus ? 'activada' : 'desactivada'}`);
        this.loadData();
      },
      error: () => {
        this.notificationService.showError('Error al cambiar el estado');
      }
    });
  }
}
