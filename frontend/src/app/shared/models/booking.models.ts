export interface RoomMapDto {
  id: string;
  numero: string;
  categoriaId: string;
  categoria: string;
  pisoId: string;
  piso: string;
  estadoActual: string;
}

export type EstadoHabitacion = 'DISPONIBLE' | 'OCUPADO' | 'SUCIO' | 'EN_LIMPIEZA' | 'MANTENIMIENTO';

export type MetodoPago = 'EFECTIVO' | 'TARJETA' | 'BILLETERA_DIGITAL';

export interface CheckInRequest {
  numeroDocumento: string;
  nombresCliente: string;
  sedeId: string;
  tarifaId: string;
  promocionId?: string;
  cantidadHuespedes: number;
  metodoPago: MetodoPago;
}

export interface CheckInResponse {
  id: string;
  habitacionId: string;
  clienteId: string;
  cantidadHuespedes: number;
  metodoPago: MetodoPago;
  fechaIngreso: string;
  fechaSalidaEsperada: string;
  montoTotal: number;
}

export interface CheckOutRequest {
  sedeId: string;
  montoPenalidad?: number;
}

export interface CheckOutResponse {
  id: string;
  habitacionId: string;
  clienteId: string;
  cantidadHuespedes: number;
  metodoPago: MetodoPago;
  fechaIngreso: string;
  fechaSalidaEsperada: string;
  fechaSalidaReal: string;
  montoTotal: number;
}

export interface FindReservaActivaResponse {
  id: string;
  habitacionId: string;
  clienteId: string;
  tarifaId: string;
  fechaIngreso: string;
  fechaSalidaEsperada: string;
  montoSubtotal: number;
  montoDescuento: number;
  montoPenalidad: number;
  montoTotal: number;
  precioHoraExtra: number;
  planTiempoHoras: number;
  penalidadSugerida: number;
}

export interface FindClienteResponse {
  nombres: string;
  encontrado: boolean;
}
