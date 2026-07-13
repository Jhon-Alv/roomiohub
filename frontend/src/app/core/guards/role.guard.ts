import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export function roleGuard(allowedRoles: string[]): CanActivateFn {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const user = authService.currentUser();
    const rol = (user?.rol || '').replace('ROLE_', '');

    if (allowedRoles.includes(rol)) {
      return true;
    }

    if (rol === 'LIMPIEZA') {
      router.navigate(['/cleaning']);
    } else {
      router.navigate(['/reception']);
    }
    return false;
  };
}
