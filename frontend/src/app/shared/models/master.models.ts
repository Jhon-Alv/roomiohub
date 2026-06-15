export interface CategoriaDto {
  id: string;
  nombre: string;
  precioHoraExtra: number;
  activo: boolean;
}

export interface CreateCategoriaRequest {
  nombre: string;
  precioHoraExtra: number;
}

export interface UpdateCategoriaRequest {
  nombre: string;
  precioHoraExtra: number;
}

export interface PlanTiempoDto {
  id: string;
  nombre: string;
  horas: number;
  activo: boolean;
}

export interface CreatePlanTiempoRequest {
  nombre: string;
  horas: number;
}

export interface UpdatePlanTiempoRequest {
  nombre: string;
  horas: number;
}

export interface PisoDto {
  id: string;
  nombre: string;
  sedeId: string;
  sedeNombre: string;
  activo: boolean;
}

export interface CreatePisoRequest {
  nombre: string;
  sedeId: string;
}

export interface UpdatePisoRequest {
  nombre: string;
}
export interface CatalogDto {
  id: string;
  label: string;
}

export interface HabitacionDto {
  id: string;
  numero: string;
  categoriaNombre: string;
  pisoNombre: string;
  estadoActual: string;
  activo: boolean;
}

export interface CreateHabitacionRequest {
  numero: string;
  pisoId: string;
  categoriaId: string;
  sedeId: string;
}

export interface UpdateHabitacionRequest {
  numero: string;
  pisoId: string;
  categoriaId: string;
}

export interface TarifaDto {
  id: string;
  categoriaNombre: string;
  planTiempoNombre: string;
  planTiempoHoras: number;
  precioBase: number;
  activo: boolean;
}

export interface CreateTarifaRequest {
  precioBase: number;
  categoriaId: string;
  planTiempoId: string;
}

export interface UpdateTarifaRequest {
  precioBase: number;
  categoriaId: string;
  planTiempoId: string;
}
