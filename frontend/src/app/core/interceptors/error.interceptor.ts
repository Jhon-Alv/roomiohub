import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';

const HTTP_ERROR_TITLES_ES: Record<string, string> = {
  'Bad Request': 'Solicitud Incorrecta',
  'Unauthorized': 'No Autorizado',
  'Forbidden': 'Acceso Denegado',
  'Not Found': 'No Encontrado',
  'Method Not Allowed': 'Método no Permitido',
  'Not Acceptable': 'No Aceptable',
  'Request Timeout': 'Tiempo de Espera Agotado',
  'Conflict': 'Conflicto de Datos',
  'Gone': 'Ya no Existe',
  'Length Required': 'Longitud Requerida',
  'Precondition Failed': 'Precondición Fallida',
  'Payload Too Large': 'Carga Útil Demasiado Grande',
  'URI Too Long': 'URI Demasiado Larga',
  'Unsupported Media Type': 'Tipo de Medio no Soportado',
  'Range Not Satisfiable': 'Rango no Satisfacible',
  'Expectation Failed': 'Expectativa Fallida',
  'Im a teapot': 'Soy una Tetera',
  'Misdirected Request': 'Solicitud Redirigida Incorrectamente',
  'Unprocessable Entity': 'Error de Procesamiento',
  'Unprocessable Content': 'Error de Procesamiento',
  'Locked': 'Bloqueado',
  'Failed Dependency': 'Dependencia Fallida',
  'Too Early': 'Demasiado Pronto',
  'Upgrade Required': 'Actualización Requerida',
  'Precondition Required': 'Precondición Requerida',
  'Too Many Requests': 'Demasiadas Solicitudes',
  'Request Header Fields Too Large': 'Campos de Encabezado Demasiado Grandes',
  'Unavailable For Legal Reasons': 'No Disponible por Razones Legales',
  'Internal Server Error': 'Error Interno del Servidor',
  'Not Implemented': 'No Implementado',
  'Bad Gateway': 'Puerta de Enlace Incorrecta',
  'Service Unavailable': 'Servicio no Disponible',
  'Gateway Timeout': 'Tiempo de Espera de la Puerta de Enlace Agotado',
  'HTTP Version Not Supported': 'Versión de HTTP no Soportada',
  'Variant Also Negotiates': 'Variante También Negocia',
  'Insufficient Storage': 'Almacenamiento Insuficiente',
  'Loop Detected': 'Bucle Detectado',
  'Not Extended': 'No Extendido',
  'Network Authentication Required': 'Autenticación de Red Requerida'
};


export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorTitle = 'Error';
      let errorMessage = 'Ocurrió un error inesperado.';

      if (error.error instanceof ErrorEvent) {
        errorMessage = error.error.message;
        errorTitle = 'Error de Conexión';
      } else {
        switch (error.status) {
          case 0:
            errorTitle = 'Sin Conexión';
            errorMessage = 'No se pudo conectar con el servidor.';
            break;
          case 400:
            errorTitle = error.error?.error || 'Error de Validación';
            errorMessage = error.error?.mensaje || error.error?.message || 'Los datos enviados no son válidos.';
            break;
          case 401:
            errorTitle = 'No Autorizado';
            errorMessage = error.error?.mensaje || error.error?.message || 'Credenciales incorrectas o su sesión expiró.';
            break;
          case 403:
            errorTitle = 'Acceso Denegado';
            errorMessage = error.error?.mensaje || error.error?.message || 'No tiene permisos para realizar esta acción.';
            break;
          case 404:
            errorTitle = 'No Encontrado';
            errorMessage = error.error?.mensaje || error.error?.message || 'El recurso solicitado no existe.';
            break;
          case 500:
            errorTitle = 'Error en Servidor';
            errorMessage = error.error?.mensaje || error.error?.message || 'Ocurrió un problema interno procesando su solicitud.';
            break;
          default:
            if (error.error) {
              if (typeof error.error === 'string') {
                errorMessage = error.error;
              } else {
                errorMessage = error.error.mensaje || error.error.message || errorMessage;
                errorTitle = error.error.error || errorTitle;
              }
            }
            break;
        }
      }

      if (HTTP_ERROR_TITLES_ES[errorTitle]) {
        errorTitle = HTTP_ERROR_TITLES_ES[errorTitle];
      }

      notificationService.showError(errorMessage, errorTitle);
      
      return throwError(() => error);
    })
  );
};
