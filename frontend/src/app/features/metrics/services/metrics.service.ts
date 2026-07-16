import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  DashboardResponse, HabitacionCatalogoItem,
  HistorialEstadoResponse, PageResponse, IngresosDiariosResponse
} from '../../../shared/models/metrics.models';

@Injectable({ providedIn: 'root' })
export class MetricsService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/metrics`;

  getDashboard(sedeId: string): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/dashboard/${sedeId}`);
  }

  getCatalogoHabitaciones(sedeId: string): Observable<HabitacionCatalogoItem[]> {
    return this.http.get<HabitacionCatalogoItem[]>(`${this.apiUrl}/catalog/habitaciones/${sedeId}`);
  }

  getHistorialEstados(sedeId: string, habitacionId?: string, page = 0, size = 5): Observable<PageResponse<HistorialEstadoResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (habitacionId) {
      params = params.set('habitacionId', habitacionId);
    }
    return this.http.get<PageResponse<HistorialEstadoResponse>>(`${this.apiUrl}/historial-estados/${sedeId}`, { params });
  }

  getIngresosDiarios(sedeId: string, dias: number): Observable<IngresosDiariosResponse> {
    return this.http.get<IngresosDiariosResponse>(`${this.apiUrl}/ingresos-diarios/${sedeId}`, { params: { dias } });
  }
}
