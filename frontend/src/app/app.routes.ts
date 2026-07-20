import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { publicGuard } from './core/guards/public.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: 'auth/login',
    canActivate: [publicGuard],
    loadComponent: () => import('./features/auth/pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'gateway',
    canActivate: [authGuard],
    loadComponent: () => import('./features/gateway/pages/sede-selector/sede-selector.component').then(m => m.SedeSelectorComponent)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/layouts/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    children: [
      {
        path: '',
        redirectTo: 'reception',
        pathMatch: 'full'
      },
      {
        path: 'users',
        canActivate: [roleGuard(['ADMIN'])],
        loadComponent: () => import('./features/users/pages/user-management/user-management.component').then(m => m.UserManagementComponent)
      },
      {
        path: 'master',
        canActivate: [roleGuard(['ADMIN'])],
        loadChildren: () => import('./features/master/master.routes').then(m => m.masterRoutes)
      },
      {
        path: 'reception',
        canActivate: [roleGuard(['ADMIN', 'RECEPCION'])],
        loadChildren: () => import('./features/reception/reception.routes').then(m => m.RECEPTION_ROUTES)
      },
      {
        path: 'cleaning',
        canActivate: [roleGuard(['ADMIN', 'RECEPCION', 'LIMPIEZA'])],
        loadChildren: () => import('./features/cleaning/cleaning.routes').then(m => m.CLEANING_ROUTES)
      },
      {
        path: 'metrics',
        canActivate: [roleGuard(['ADMIN'])],
        loadChildren: () => import('./features/metrics/metrics.routes').then(m => m.METRICS_ROUTES)
      },
      /*
      {
        path: 'logistics',
        loadChildren: () => import('./features/logistics/logistics.routes').then(m => m.LOGISTICS_ROUTES)
      }
      */
    ]
  },
  {
    path: '**',
    redirectTo: 'auth/login'
  }
];
