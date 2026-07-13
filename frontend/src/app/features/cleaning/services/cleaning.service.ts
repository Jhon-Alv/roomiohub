import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  IniciarLimpiezaRequest, IniciarLimpiezaResponse,
  FinalizarLimpiezaRequest, FinalizarLimpiezaResponse
} from '../../../shared/models/cleaning.models';

@Injectable({
  providedIn: 'root'
})
export class CleaningService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/cleaning`;

  iniciarLimpieza(habitacionId: string, payload: IniciarLimpiezaRequest): Observable<IniciarLimpiezaResponse> {
    return this.http.post<IniciarLimpiezaResponse>(`${this.apiUrl}/habitacion/${habitacionId}/iniciar`, payload);
  }

  finalizarLimpieza(habitacionId: string, payload: FinalizarLimpiezaRequest): Observable<FinalizarLimpiezaResponse> {
    return this.http.post<FinalizarLimpiezaResponse>(`${this.apiUrl}/habitacion/${habitacionId}/finalizar`, payload);
  }
}
