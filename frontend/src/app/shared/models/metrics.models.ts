export interface DashboardResponse {
  ingresosHoy: number;
  tasaOcupacion: number;
  habitacionesSucias: number;
  promedioLimpiezaMinutos: number;
  habitacionesTotales: number;
  habitacionesOcupadas: number;
  estadoHabitaciones: DashboardEstadoResumen[];
  ingresosUltimos3Dias: number;
  ingresosUltimos7Dias: number;
  ingresosUltimos30Dias: number;
}

export interface DashboardEstadoResumen {
  estado: string;
  cantidad: number;
}

export interface HabitacionCatalogoItem {
  id: string;
  numero: string;
  categoria: string;
}

export interface HistorialEstadoResponse {
  id: string;
  habitacionId: string;
  habitacionNumero: string;
  habitacionCategoria: string;
  estadoAnterior: string;
  estadoNuevo: string;
  fechaCambio: string;
  observacion: string | null;
}

export interface IngresoDiarioResponse {
  fecha: string;
  monto: number;
}

export interface IngresosDiariosResponse {
  dias: IngresoDiarioResponse[];
  total: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
