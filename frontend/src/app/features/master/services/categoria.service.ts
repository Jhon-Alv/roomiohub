import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CategoriaDto, CreateCategoriaRequest, UpdateCategoriaRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/categorias`;

  getCategorias(filters?: { search?: string, activo?: boolean }): Observable<CategoriaDto[]> {
    let params = new HttpParams();
    if (filters?.search) params = params.set('search', filters.search);
    if (filters?.activo !== undefined && filters?.activo !== null) params = params.set('activo', filters.activo.toString());
    
    return this.http.get<CategoriaDto[]>(this.apiUrl, { params });
  }

  createCategoria(request: CreateCategoriaRequest): Observable<CategoriaDto> {
    return this.http.post<CategoriaDto>(this.apiUrl, request);
  }

  updateCategoria(id: string, request: UpdateCategoriaRequest): Observable<CategoriaDto> {
    return this.http.put<CategoriaDto>(`${this.apiUrl}/${id}`, request);
  }

  toggleStatus(id: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, { activo });
  }
}
