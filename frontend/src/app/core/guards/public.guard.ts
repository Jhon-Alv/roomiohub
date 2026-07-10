import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const publicGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.getToken()) {
    return true;
  }

  const user = authService.currentUser();
  if (user?.sedeId) {
    router.navigate(['/reception']);
  } else {
    router.navigate(['/gateway']);
  }
  return false;
};
