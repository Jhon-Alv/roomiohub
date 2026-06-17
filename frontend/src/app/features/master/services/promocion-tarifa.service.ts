import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PromocionTarifaDto, BatchCreatePromocionTarifaRequest, UpdatePromocionTarifaPriceRequest } from '../../../shared/models/master.models';

@Injectable({
  providedIn: 'root'
})
export class PromocionTarifaService {
  private http = inject(HttpClient);
  
  private apiUrl(promocionId: string): string {
    return `${environment.apiUrl}/master/promociones/${promocionId}/tarifas`;
  }

  getTarifasByPromocion(promocionId: string): Observable<PromocionTarifaDto[]> {
    return this.http.get<PromocionTarifaDto[]>(this.apiUrl(promocionId));
  }

  batchCreateTarifas(promocionId: string, request: BatchCreatePromocionTarifaRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl(promocionId)}/batch`, request);
  }

  updatePrecio(promocionId: string, tarifaAsociacionId: string, request: UpdatePromocionTarifaPriceRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl(promocionId)}/${tarifaAsociacionId}/precio`, request);
  }

  toggleStatus(promocionId: string, tarifaAsociacionId: string, activo: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl(promocionId)}/${tarifaAsociacionId}/status`, { activo });
  }
}
