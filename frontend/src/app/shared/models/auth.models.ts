export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  nombre: string;
  rol: string;
  tenantId: string | null;
  sedeId: string | null;
  flagCambiarPassword: boolean;
}

export interface ChangePasswordRequest {
  newPassword: string;
  confirmPassword: string;
}
