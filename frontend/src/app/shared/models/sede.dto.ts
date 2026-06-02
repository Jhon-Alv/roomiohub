export interface SedeDto {
  id: string;
  nombre: string;
  direccion: string;
  activo: boolean;
}

export interface CreateSedeDto {
  nombre: string;
  direccion: string;
}

export interface UpdateSedeStatusDto {
  activo: boolean;
}
