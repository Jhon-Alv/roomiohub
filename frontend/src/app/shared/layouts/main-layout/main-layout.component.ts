import { Component, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { SedeStateService } from '../../../core/services/sede-state.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent {
  private sedeState = inject(SedeStateService);
  private authService = inject(AuthService);
  private router = inject(Router);

  // Expose active sede to template
  sedeActiva = this.sedeState.activeSede;

  // Expose user to template
  currentUser = this.authService.currentUser;

  // Sidebar state
  isConfigOpen = signal(false);

  // RBAC Computeds
  hasAdminRole = computed(() => {
    const rol = this.currentUser()?.rol;
    return rol === 'ROLE_ADMIN' || rol === 'ADMIN';
  });

  canChangeSede = computed(() => {
    return !this.currentUser()?.sedeId;
  });

  hasReceptionRole = computed(() => {
    const rol = this.currentUser()?.rol;
    return rol === 'ROLE_RECEPCION' || rol === 'RECEPCION' || this.hasAdminRole();
  });

  hasCleaningRole = computed(() => {
    const rol = this.currentUser()?.rol;
    return rol === 'ROLE_LIMPIEZA' || rol === 'LIMPIEZA' || this.hasReceptionRole() || this.hasAdminRole();
  });

  isAdminMenuOpen = signal(false);

  toggleConfigMenu() {
    this.isConfigOpen.set(!this.isConfigOpen());
  }

  toggleAdminMenu() {
    this.isAdminMenuOpen.set(!this.isAdminMenuOpen());
  }

  logout() {
    this.authService.logout();
  }

  changeSede() {
    this.router.navigate(['/gateway']);
  }
}
