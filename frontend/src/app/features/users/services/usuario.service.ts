import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { UsuarioDto, CreateUsuarioDto, UpdateUsuarioDto, UpdateUsuarioStatusDto, CreateUsuarioResponse } from '../../../shared/models/usuario.dto';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/admin/usuarios`;

  getUsuarios(filtros?: {
    sedeId?: string | null;
    search?: string;
    rol?: string;
    activo?: boolean | null;
  }): Observable<UsuarioDto[]> {
    let params = new HttpParams();
    
    if (filtros) {
      if (filtros.sedeId) params = params.set('sedeId', filtros.sedeId);
      if (filtros.search) params = params.set('search', filtros.search);
      if (filtros.rol && filtros.rol !== 'Todos los Roles') params = params.set('rol', filtros.rol);
      if (filtros.activo !== undefined && filtros.activo !== null) params = params.set('activo', filtros.activo);
    }

    return this.http.get<UsuarioDto[]>(this.apiUrl, { params });
  }

  getUsuarioById(id: string): Observable<UsuarioDto> {
    return this.http.get<UsuarioDto>(`${this.apiUrl}/${id}`);
  }

  createUsuario(data: CreateUsuarioDto): Observable<CreateUsuarioResponse> {
    return this.http.post<CreateUsuarioResponse>(this.apiUrl, data);
  }

  updateUsuario(id: string, data: UpdateUsuarioDto): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, data);
  }

  changeUsuarioStatus(id: string, activo: boolean): Observable<void> {
    const payload: UpdateUsuarioStatusDto = { activo };
    return this.http.patch<void>(`${this.apiUrl}/${id}`, payload);
  }
}
