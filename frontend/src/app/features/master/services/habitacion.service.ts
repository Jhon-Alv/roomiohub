import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HabitacionDto, CreateHabitacionRequest, UpdateHabitacionRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class HabitacionService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/habitaciones`;

  getHabitaciones(filters?: { search?: string, activo?: boolean, sedeId?: string, categoriaId?: string, pisoId?: string }): Observable<HabitacionDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    if (filters?.sedeId) params = params.set('sedeId', filters.sedeId);
    if (filters?.categoriaId) params = params.set('categoriaId', filters.categoriaId);
    if (filters?.pisoId) params = params.set('pisoId', filters.pisoId);
    
    return this.http.get<HabitacionDto[]>(this.apiUrl, { params });
  }

  createHabitacion(request: CreateHabitacionRequest): Observable<HabitacionDto> {
    return this.http.post<HabitacionDto>(this.apiUrl, request);
  }

  updateHabitacion(id: string, request: UpdateHabitacionRequest): Observable<HabitacionDto> {
    return this.http.put<HabitacionDto>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
