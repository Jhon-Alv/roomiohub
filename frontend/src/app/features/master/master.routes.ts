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
    loadComponent: () => import('./pages/habitacion-list/habitacion-list').then(c => c.HabitacionListComponent)
  },
  {
    path: 'tarifas',
    loadComponent: () => import('./pages/tarifa-list/tarifa-list').then(c => c.TarifaListComponent)
  }
];
