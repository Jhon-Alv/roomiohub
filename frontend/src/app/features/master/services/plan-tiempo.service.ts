import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PlanTiempoDto, CreatePlanTiempoRequest, UpdatePlanTiempoRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class PlanTiempoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/planes-tiempo`;

  getPlanesTiempo(filters?: { search?: string, activo?: boolean }): Observable<PlanTiempoDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    
    return this.http.get<PlanTiempoDto[]>(this.apiUrl, { params });
  }

  createPlanTiempo(request: CreatePlanTiempoRequest): Observable<PlanTiempoDto> {
    return this.http.post<PlanTiempoDto>(this.apiUrl, request);
  }

  updatePlanTiempo(id: string, request: UpdatePlanTiempoRequest): Observable<PlanTiempoDto> {
    return this.http.put<PlanTiempoDto>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
