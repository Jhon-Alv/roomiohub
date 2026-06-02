export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  rol: string;
  tenantId: string | null;
  flagCambiarPassword: boolean;
}

export interface ChangePasswordRequest {
  newPassword: string;
  confirmPassword: string;
}
