export interface UsuarioDto {
  id: string;
  nombre: string;
  username: string;
  rol: string;
  sedeId: string | null;
  activo: boolean;
}

export interface CreateUsuarioDto {
  nombre: string;
  username: string;
  rol: string;
  sedeId?: string | null;
}

export interface CreateUsuarioResponse {
  id: string;
  nombre: string;
  username: string;
  temporalPassword?: string;
  rol: string;
  sedeId?: string | null;
}

export interface UpdateUsuarioDto {
  nombre: string;
  username: string;
  rol: string;
  nuevaPassword?: string;
}

export interface UpdateUsuarioStatusDto {
  activo: boolean;
}
