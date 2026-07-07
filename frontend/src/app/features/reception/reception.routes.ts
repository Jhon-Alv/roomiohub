import { Routes } from '@angular/router';

export const RECEPTION_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'room-map',
    pathMatch: 'full'
  },
  {
    path: 'room-map',
    loadComponent: () => import('./pages/room-map/room-map.component').then(m => m.RoomMapComponent)
  }
];
