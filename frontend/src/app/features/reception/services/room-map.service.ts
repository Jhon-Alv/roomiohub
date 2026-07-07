import { Injectable, inject, signal } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { RoomMapDto } from '../../../shared/models/booking.models';
import { AuthService } from '../../../core/services/auth.service';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoomMapService {
  private authService = inject(AuthService);
  private apiUrl = `${environment.apiUrl}/booking/map`;
  
  private abortController: AbortController | null = null;
  private mapSubject = new BehaviorSubject<RoomMapDto[]>([]);

  public readonly mapState$: Observable<RoomMapDto[]> = this.mapSubject.asObservable();

  connectToMapStream(sedeId: string) {
    // Si ya hay una conexión, la cerramos
    this.disconnect();

    this.abortController = new AbortController();
    const token = this.authService.getToken();

    fetchEventSource(`${this.apiUrl}/stream/${sedeId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'text/event-stream',
      },
      signal: this.abortController.signal,
      onopen: async (response) => {
        if (response.ok && response.headers.get('content-type')?.includes('text/event-stream')) {
          console.log('SSE connection opened successfully for sede:', sedeId);
        } else if (response.status >= 400 && response.status < 500 && response.status !== 429) {
          throw new Error('Server returned ' + response.status);
        }
      },
      onmessage: (msg) => {
        if (msg.event === 'INIT') {
          const initialData: RoomMapDto[] = JSON.parse(msg.data);
          this.mapSubject.next(initialData);
        } else if (msg.event === 'UPDATE') {
          const updatedRoom: RoomMapDto = JSON.parse(msg.data);
          const currentMap = this.mapSubject.getValue();
          const updatedMap = currentMap.map(room => 
            room.id === updatedRoom.id ? updatedRoom : room
          );
          this.mapSubject.next(updatedMap);
        }
      },
      onclose: () => {
        console.log('SSE connection closed by server.');
      },
      onerror: (err) => {
        console.error('SSE Error:', err);
        throw err; // Esto hace que fetch-event-source intente reconectar
      }
    });
  }

  disconnect() {
    if (this.abortController) {
      this.abortController.abort();
      this.abortController = null;
    }
    this.mapSubject.next([]);
  }
}
