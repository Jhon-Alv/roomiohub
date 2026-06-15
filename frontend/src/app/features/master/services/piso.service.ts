import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PisoDto, CreatePisoRequest, UpdatePisoRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class PisoService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/pisos`;

  getPisos(filters?: { search?: string, sedeId?: string, activo?: boolean }): Observable<PisoDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.sedeId) params = params.set('sedeId', filters.sedeId);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    
    return this.http.get<PisoDto[]>(this.apiUrl, { params });
  }

  createPiso(request: CreatePisoRequest): Observable<PisoDto> {
    return this.http.post<PisoDto>(this.apiUrl, request);
  }

  updatePiso(id: string, request: UpdatePisoRequest): Observable<PisoDto> {
    return this.http.put<PisoDto>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
