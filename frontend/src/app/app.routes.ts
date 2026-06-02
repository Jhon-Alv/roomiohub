import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { publicGuard } from './core/guards/public.guard';

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
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      // Ejemplos de Lazy Loading por Feature (requerirán sus propios archivos .routes.ts o cargar componentes directamente)
      /*
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES)
      },
      {
        path: 'reception',
        loadChildren: () => import('./features/reception/reception.routes').then(m => m.RECEPTION_ROUTES)
      },
      {
        path: 'logistics',
        loadChildren: () => import('./features/logistics/logistics.routes').then(m => m.LOGISTICS_ROUTES)
      },
      {
        path: 'catalog',
        loadChildren: () => import('./features/catalog/catalog.routes').then(m => m.CATALOG_ROUTES)
      },
      {
        path: 'users',
        loadChildren: () => import('./features/users/users.routes').then(m => m.USERS_ROUTES)
      }
      */
    ]
  },
  {
    path: '**',
    redirectTo: 'auth/login'
  }
];
