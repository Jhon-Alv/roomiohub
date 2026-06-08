import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioDto, CreateUsuarioResponse } from '../../../../shared/models/usuario.dto';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

import { CustomSelectComponent, SelectOption } from '../../../../shared/components/custom-select/custom-select.component';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule, CustomSelectComponent],
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private sedeState = inject(SedeStateService);
  private notificationService = inject(NotificationService);

  usuarios = signal<UsuarioDto[]>([]);
  isLoading = signal<boolean>(true);

  // Modal State
  isModalOpen = signal(false);
  modalMode = signal<'create' | 'edit'>('create');
  selectedUserId = signal<string | null>(null);
  isSaving = signal(false);
  createdCredentials = signal<{username: string, password: string} | null>(null);

  usuarioForm = signal({
    nombre: '',
    username: '',
    rol: 'RECEPCION',
    password: ''
  });

  // Filters state
  searchTerm = signal<string>('');
  selectedRol = signal<string>('Todos los Roles');
  selectedEstado = signal<string>('Estado: Todos');

  // Options for selects
  rolFilterOptions: SelectOption[] = [
    { value: 'Todos los Roles', label: 'Todos los Roles' },
    { value: 'ADMIN', label: 'Administrador' },
    { value: 'RECEPCION', label: 'Recepcionista' },
    { value: 'LIMPIEZA', label: 'Limpieza' }
  ];

  estadoFilterOptions: SelectOption[] = [
    { value: 'Estado: Todos', label: 'Estado: Todos' },
    { value: 'Activo', label: 'Activo' },
    { value: 'Inactivo', label: 'Inactivo' }
  ];

  rolFormOptions: SelectOption[] = [
    { value: 'ADMIN', label: 'Administrador' },
    { value: 'RECEPCION', label: 'Recepcionista' },
    { value: 'LIMPIEZA', label: 'Limpieza' }
  ];

  private searchSubject = new Subject<string>();

  ngOnInit() {
    this.loadUsuarios();

    // Setup debounce for search input
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadUsuarios();
    });
  }

  onSearchChange(value: string) {
    this.searchTerm.set(value);
    this.searchSubject.next(value);
  }

  onFilterChange() {
    this.loadUsuarios();
  }

  loadUsuarios() {
    this.isLoading.set(true);
    
    // Parse status filter
    let activo: boolean | null = null;
    if (this.selectedEstado() === 'Activo') activo = true;
    if (this.selectedEstado() === 'Inactivo') activo = false;

    // Get active sede
    const activeSedeId = this.sedeState.activeSede()?.id;

    this.usuarioService.getUsuarios({
      sedeId: activeSedeId,
      search: this.searchTerm(),
      rol: this.selectedRol(),
      activo: activo
    }).subscribe({
      next: (data) => {
        this.usuarios.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching users', err);
        this.isLoading.set(false);
      }
    });
  }

  getInitials(nombre: string): string {
    if (!nombre) return '';
    return nombre.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
  }

  // --- Modal Logic ---

  openCreateModal() {
    this.modalMode.set('create');
    this.selectedUserId.set(null);
    this.createdCredentials.set(null);
    this.usuarioForm.set({ nombre: '', username: '', rol: 'RECEPCION', password: '' });
    this.isModalOpen.set(true);
  }

  openEditModal(user: UsuarioDto) {
    this.modalMode.set('edit');
    this.selectedUserId.set(user.id);
    this.createdCredentials.set(null);
    this.usuarioForm.set({ 
      nombre: user.nombre, 
      username: user.username, 
      rol: user.rol,
      password: ''
    });
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
    this.createdCredentials.set(null);
  }

  saveUsuario() {
    const form = this.usuarioForm();
    if (!form.nombre || !form.username || !form.rol) {
      this.notificationService.showWarning('Complete los campos obligatorios.');
      return;
    }

    this.isSaving.set(true);
    const activeSedeId = this.sedeState.activeSede()?.id;

    if (this.modalMode() === 'create') {
      const payload = {
        nombre: form.nombre,
        username: form.username,
        rol: form.rol,
        sedeId: activeSedeId
      };

      this.usuarioService.createUsuario(payload as any).subscribe({
        next: (response: CreateUsuarioResponse) => {
          this.loadUsuarios();
          this.isSaving.set(false);
          this.createdCredentials.set({
            username: response.username,
            password: response.temporalPassword || ''
          });
        },
        error: () => this.isSaving.set(false)
      });
    } else {
      // Edit
      const payload: any = {
        nombre: form.nombre,
        username: form.username,
        rol: form.rol
      };
      
      if (form.password && form.password.trim() !== '') {
        payload.nuevaPassword = form.password;
      }

      this.usuarioService.updateUsuario(this.selectedUserId()!, payload).subscribe({
        next: () => {
          this.notificationService.showSuccess('Usuario actualizado exitosamente');
          this.closeModal();
          this.loadUsuarios();
          this.isSaving.set(false);
        },
        error: () => this.isSaving.set(false)
      });
    }
  }

  toggleStatus(user: UsuarioDto) {
    const newStatus = !user.activo;
    this.usuarioService.changeUsuarioStatus(user.id, newStatus).subscribe({
      next: () => {
        this.notificationService.showSuccess(`Usuario ${newStatus ? 'activado' : 'desactivado'}`);
        this.loadUsuarios();
      }
    });
  }

  async copyCredentials() {
    const creds = this.createdCredentials();
    if (creds) {
      const text = `Usuario: ${creds.username}\nContraseña: ${creds.password}`;
      try {
        await navigator.clipboard.writeText(text);
        this.notificationService.showSuccess('Credenciales copiadas al portapapeles');
      } catch (err) {
        console.error('Error al copiar', err);
      }
    }
  }
}
