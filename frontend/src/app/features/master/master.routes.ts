import { Routes } from '@angular/router';

export const masterRoutes: Routes = [
  {
    path: '',
    redirectTo: 'categorias',
    pathMatch: 'full'
  },
  {
    path: 'categorias',
    loadComponent: () => import('./pages/categoria-list/categoria-list.component').then(c => c.CategoriaListComponent)
  },
  {
    path: 'planes-tiempo',
    loadComponent: () => import('./pages/plan-tiempo-list/plan-tiempo-list.component').then(c => c.PlanTiempoListComponent)
  },
  {
    path: 'pisos',
    loadComponent: () => import('./pages/piso-list/piso-list.component').then(c => c.PisoListComponent)
  },
  {
    path: 'habitaciones',
    loadComponent: () => import('./pages/habitacion-list/habitacion-list.component').then(c => c.HabitacionListComponent)
  },
  {
    path: 'tarifas',
    loadComponent: () => import('./pages/tarifa-list/tarifa-list.component').then(c => c.TarifaListComponent)
  },
  {
    path: 'promociones',
    loadComponent: () => import('./pages/promocion-list/promocion-list.component').then(c => c.PromocionListComponent)
  },
  {
    path: 'promociones/:id',
    loadComponent: () => import('./pages/promocion-detail/promocion-detail.component').then(c => c.PromocionDetailComponent)
  }
];
