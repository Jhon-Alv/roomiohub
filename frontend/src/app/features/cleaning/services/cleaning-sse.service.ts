import { Injectable, inject } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { AuthService } from '../../../core/services/auth.service';
import { CleaningRoomDto } from '../../../shared/models/cleaning.models';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CleaningSseService {
  private authService = inject(AuthService);
  private apiUrl = `${environment.apiUrl}/cleaning`;

  private abortController: AbortController | null = null;
  private roomsSubject = new BehaviorSubject<CleaningRoomDto[]>([]);

  public readonly rooms$: Observable<CleaningRoomDto[]> = this.roomsSubject.asObservable();

  connect(sedeId: string) {
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
          console.log('[Cleaning SSE] connected for sede:', sedeId);
        } else if (response.status >= 400 && response.status < 500 && response.status !== 429) {
          throw new Error('Server returned ' + response.status);
        }
      },
      onmessage: (msg) => {
        if (msg.event === 'INIT') {
          const initialData: CleaningRoomDto[] = JSON.parse(msg.data);
          this.roomsSubject.next(initialData);
        } else if (msg.event === 'UPDATE') {
          const updatedRoom: CleaningRoomDto = JSON.parse(msg.data);
          const currentRooms = this.roomsSubject.getValue();
          const index = currentRooms.findIndex(r => r.id === updatedRoom.id);

          if (updatedRoom.estadoActual === 'DISPONIBLE' || updatedRoom.estadoActual === 'MANTENIMIENTO') {
            if (index >= 0) {
              const updated = [...currentRooms];
              updated.splice(index, 1);
              this.roomsSubject.next(updated);
            }
          } else if (index >= 0) {
            const updated = [...currentRooms];
            updated[index] = updatedRoom;
            this.roomsSubject.next(updated);
          } else {
            this.roomsSubject.next([...currentRooms, updatedRoom]);
          }
        }
      },
      onclose: () => {
        console.log('[Cleaning SSE] connection closed');
      },
      onerror: (err) => {
        console.error('[Cleaning SSE] error:', err);
        throw err;
      }
    });
  }

  disconnect() {
    if (this.abortController) {
      this.abortController.abort();
      this.abortController = null;
    }
    this.roomsSubject.next([]);
  }
}
