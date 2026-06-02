import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CreateSedeDto, SedeDto, UpdateSedeStatusDto } from '../../../shared/models/sede.dto';

@Injectable({
  providedIn: 'root'
})
export class SedeService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/admin/sedes`;

  getSedes(): Observable<SedeDto[]> {
    return this.http.get<SedeDto[]>(this.apiUrl);
  }

  getSedeById(id: string): Observable<SedeDto> {
    return this.http.get<SedeDto>(`${this.apiUrl}/${id}`);
  }

  createSede(data: CreateSedeDto): Observable<{id: string}> {
    return this.http.post<{id: string}>(this.apiUrl, data);
  }

  updateSede(id: string, data: CreateSedeDto): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, data);
  }

  changeSedeStatus(id: string, activo: boolean): Observable<void> {
    const payload: UpdateSedeStatusDto = { activo };
    return this.http.patch<void>(`${this.apiUrl}/${id}`, payload);
  }
}
