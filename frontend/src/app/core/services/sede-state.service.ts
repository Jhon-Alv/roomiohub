import { Injectable, signal } from '@angular/core';
import { SedeDto } from '../../shared/models/sede.dto';

@Injectable({
  providedIn: 'root'
})
export class SedeStateService {
  private readonly STORAGE_KEY = 'roomiohub_active_sede';
  
  // Signal to hold the currently selected Sede
  activeSede = signal<SedeDto | null>(null);

  constructor() {
    this.restoreSede();
  }

  setSede(sede: SedeDto) {
    this.activeSede.set(sede);
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(sede));
  }

  clearSede() {
    this.activeSede.set(null);
    localStorage.removeItem(this.STORAGE_KEY);
  }

  private restoreSede() {
    const stored = localStorage.getItem(this.STORAGE_KEY);
    if (stored) {
      try {
        this.activeSede.set(JSON.parse(stored));
      } catch (e) {
        console.error('Failed to parse active sede from local storage', e);
        this.clearSede();
      }
    }
  }
}
