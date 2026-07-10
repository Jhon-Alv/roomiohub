import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, ChangePasswordRequest, VerifyRucResponse } from '../../shared/models/auth.models';
import { Router } from '@angular/router';
import { SedeStateService } from './sede-state.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private sedeState = inject(SedeStateService);
  
  currentUser = signal<LoginResponse | null>(null);
  
  constructor() {
    this.restoreSession();
  }

  verifyRuc(ruc: string) {
    return this.http.get<VerifyRucResponse>(`${environment.apiUrl}/auth/verify-ruc?ruc=${ruc}`);
  }

  login(credentials: LoginRequest) {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        if (!response.flagCambiarPassword) {
          this.setSession(response);
        }
      })
    );
  }

  changePassword(request: ChangePasswordRequest, token: string) {
    return this.http.patch<void>(`${environment.apiUrl}/auth/change-password`, request, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  setSession(response: LoginResponse) {
    localStorage.setItem('jwt_token', response.token);
    localStorage.setItem('user_info', JSON.stringify(response));
    this.currentUser.set(response);

    if (response.sedeId) {
      this.sedeState.setSede({
        id: response.sedeId,
        nombre: response.sedeNombre || '',
        direccion: '',
        activo: true
      });
    } else {
      this.sedeState.clearSede();
    }
  }

  restoreSession() {
    const token = localStorage.getItem('jwt_token');
    const userInfo = localStorage.getItem('user_info');
    if (token && userInfo) {
      this.currentUser.set(JSON.parse(userInfo));
    }
  }

  logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
    localStorage.removeItem('roomiohub_active_sede');
    this.currentUser.set(null);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
}
