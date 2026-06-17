import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PromocionDto, CreatePromocionRequest, UpdatePromocionRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class PromocionService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/promociones`;

  getPromociones(filters?: { search?: string, activo?: boolean }): Observable<PromocionDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    
    return this.http.get<PromocionDto[]>(this.apiUrl, { params });
  }

  getPromocionById(id: string): Observable<PromocionDto> {
    return this.http.get<PromocionDto>(`${this.apiUrl}/${id}`);
  }

  createPromocion(request: CreatePromocionRequest): Observable<PromocionDto> {
    return this.http.post<PromocionDto>(this.apiUrl, request);
  }

  updatePromocion(id: string, request: UpdatePromocionRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
