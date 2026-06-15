import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CatalogDto } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/master/catalog`;

  getCategorias(): Observable<CatalogDto[]> {
    return this.http.get<CatalogDto[]>(`${this.apiUrl}/categorias`);
  }

  getPisos(sedeId?: string): Observable<CatalogDto[]> {
    let params = new HttpParams();
    if (sedeId) {
      params = params.set('sedeId', sedeId);
    }
    return this.http.get<CatalogDto[]>(`${this.apiUrl}/pisos`, { params });
  }

  PlanesTiempoCatalog(): Observable<CatalogDto[]> {
    return this.http.get<CatalogDto[]>(`${this.apiUrl}/planes-tiempo`);
  }
}
