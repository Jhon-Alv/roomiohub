import { Routes } from '@angular/router';

export const CLEANING_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'task-queue',
    pathMatch: 'full'
  },
  {
    path: 'task-queue',
    loadComponent: () => import('./pages/task-queue/task-queue.component').then(m => m.TaskQueueComponent)
  }
];
