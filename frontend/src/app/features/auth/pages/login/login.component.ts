import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { LoginResponse } from '../../../../shared/models/auth.models';
import { NotificationService } from '../../../../core/services/notification.service';
import { SedeService } from '../../../gateway/services/sede.service';
import { SedeStateService } from '../../../../core/services/sede-state.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private notificationService = inject(NotificationService);
  private sedeService = inject(SedeService);
  private sedeState = inject(SedeStateService);

  // Form states
  username = signal('');
  password = signal('');
  
  showChangePasswordModal = signal(false);
  newPassword = signal('');
  confirmPassword = signal('');
  tempToken = signal('');

  isLoading = signal(false);

  onLogin() {
    if (!this.username() || !this.password()) {
      this.notificationService.showWarning('Por favor ingrese usuario y contraseña');
      return;
    }

    this.isLoading.set(true);
    this.authService.login({
      username: this.username(),
      password: this.password() 
    }).subscribe({
      next: (response: LoginResponse) => {
        if (response.flagCambiarPassword) {
          this.isLoading.set(false);
          this.tempToken.set(response.token); 
          this.showChangePasswordModal.set(true);
        } else {
          if (response.sedeId) {
            this.sedeService.getSedeById(response.sedeId).subscribe({
              next: (sede) => {
                this.sedeState.setSede(sede);
                this.isLoading.set(false);
                this.router.navigate(['/']);
              },
              error: () => {
                this.isLoading.set(false);
                this.notificationService.showError('Error al cargar la información de la sede');
              }
            });
          } else {
            this.sedeState.clearSede();
            this.isLoading.set(false);
            this.router.navigate(['/gateway']);
          }
        }
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  onChangePassword() {
    if (!this.newPassword() || !this.confirmPassword()) {
      this.notificationService.showWarning('Llene todos los campos.', 'Campos incompletos');
      return;
    }
    if (this.newPassword() !== this.confirmPassword()) {
      this.notificationService.showError('Las contraseñas no coinciden.', 'Validación fallida');
      return;
    }

    this.isLoading.set(true);
    this.authService.changePassword({ 
      newPassword: this.newPassword(), 
      confirmPassword: this.confirmPassword() 
    }, this.tempToken())
      .subscribe({
        next: () => {
          this.isLoading.set(false);
          this.showChangePasswordModal.set(false);
          this.router.navigate(['/auth/login']);
          this.notificationService.showSuccess('Contraseña actualizada correctamente. Por favor inicie sesión de nuevo.', 'Éxito');
          this.password.set('');
        },
        error: () => {
          this.isLoading.set(false);
        }
      });
  }
}
