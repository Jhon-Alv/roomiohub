export interface LoginRequest {
  ruc: string;
  username: string;
  password: string;
}

export interface VerifyRucResponse {
  nombreComercial: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  nombre: string;
  rol: string;
  tenantId: string | null;
  sedeId: string | null;
  flagCambiarPassword: boolean;
  flagModuloPromociones: boolean;
}

export interface ChangePasswordRequest {
  newPassword: string;
  confirmPassword: string;
}
