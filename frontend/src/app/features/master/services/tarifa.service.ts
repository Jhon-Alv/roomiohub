import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { TarifaDto, CreateTarifaRequest, UpdateTarifaRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class TarifaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/tarifas`;

  getTarifas(filters?: { search?: string, activo?: boolean }): Observable<TarifaDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    
    return this.http.get<TarifaDto[]>(this.apiUrl, { params });
  }

  createTarifa(request: CreateTarifaRequest): Observable<TarifaDto> {
    return this.http.post<TarifaDto>(this.apiUrl, request);
  }

  updateTarifa(id: string, request: UpdateTarifaRequest): Observable<TarifaDto> {
    return this.http.put<TarifaDto>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
