import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SedeService } from '../../services/sede.service';
import { AuthService } from '../../../../core/services/auth.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';
import { SedeDto, CreateSedeDto } from '../../../../shared/models/sede.dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sede-selector',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sede-selector.component.html'
})
export class SedeSelectorComponent implements OnInit {
  private sedeService = inject(SedeService);
  private authService = inject(AuthService);
  private sedeState = inject(SedeStateService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  sedes = signal<SedeDto[]>([]);
  isLoading = signal<boolean>(true);
  isModalOpen = signal<boolean>(false);
  isSaving = signal<boolean>(false);

  sedeForm: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(255)]],
    direccion: ['', [Validators.maxLength(500)]]
  });

  // RBAC getter reading from AuthService
  hasRoleAdmin = computed(() => {
    const user = this.authService.currentUser();
    return user?.rol === 'ROLE_ADMIN' || user?.rol === 'ADMIN';
  });

  isRestrictedUser = computed(() => {
    return !!this.authService.currentUser()?.sedeId;
  });

  ngOnInit() {
    if (this.isRestrictedUser()) {
      const rol = (this.authService.currentUser()?.rol || '').replace('ROLE_', '');
      this.router.navigate([rol === 'LIMPIEZA' ? '/cleaning' : '/reception']);
      return;
    }

    this.sedeState.clearSede();
    this.loadSedes();
  }

  loadSedes() {
    this.isLoading.set(true);
    this.sedeService.getSedes().subscribe({
      next: (data) => {
        this.sedes.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching sedes', err);
        this.isLoading.set(false);
      }
    });
  }

  selectSede(sede: SedeDto) {
    this.sedeState.setSede(sede);
    this.router.navigate(['/']); // Navigate to Main Layout (Dashboard)
  }

  openModal() {
    if (this.hasRoleAdmin()) {
      this.sedeForm.reset();
      this.isModalOpen.set(true);
    }
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  onSubmitSede() {
    if (this.sedeForm.invalid) {
      this.sedeForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    const data: CreateSedeDto = this.sedeForm.value;

    this.sedeService.createSede(data).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.closeModal();
        this.loadSedes(); // Reload the list
      },
      error: (err) => {
        console.error('Error creating sede', err);
        this.isSaving.set(false);
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
