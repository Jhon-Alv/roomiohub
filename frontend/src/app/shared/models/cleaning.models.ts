export interface CleaningRoomDto {
  id: string;
  numero: string;
  categoriaId: string;
  categoria: string;
  pisoId: string;
  piso: string;
  estadoActual: string;
  fechaUltimoCambio: string;
}

export interface IniciarLimpiezaRequest {
  sedeId: string;
  observacion?: string;
}

export interface FinalizarLimpiezaRequest {
  sedeId: string;
  observacion?: string;
}

export interface IniciarLimpiezaResponse {
  habitacionId: string;
  estadoAnterior: string;
  estadoNuevo: string;
}

export interface FinalizarLimpiezaResponse {
  habitacionId: string;
  estadoAnterior: string;
  estadoNuevo: string;
}
