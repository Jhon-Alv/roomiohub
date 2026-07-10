import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CheckInRequest, CheckInResponse, CheckOutRequest, CheckOutResponse, FindReservaActivaResponse, FindClienteResponse } from '../../../shared/models/booking.models';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/booking/operaciones`;

  searchCliente(documento: string): Observable<FindClienteResponse> {
    return this.http.get<FindClienteResponse>(`${this.apiUrl}/clientes/search`, { params: { documento } });
  }

  getReservaActiva(habitacionId: string): Observable<FindReservaActivaResponse> {
    return this.http.get<FindReservaActivaResponse>(`${this.apiUrl}/reserva-activa/habitacion/${habitacionId}`);
  }

  realizarCheckIn(habitacionId: string, payload: CheckInRequest): Observable<CheckInResponse> {
    return this.http.post<CheckInResponse>(`${this.apiUrl}/checkin/habitacion/${habitacionId}`, payload);
  }

  realizarCheckOut(habitacionId: string, payload: CheckOutRequest): Observable<CheckOutResponse> {
    return this.http.post<CheckOutResponse>(`${this.apiUrl}/checkout/habitacion/${habitacionId}`, payload);
  }
}
