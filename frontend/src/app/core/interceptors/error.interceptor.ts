import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';

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

      notificationService.showError(errorMessage, errorTitle);
      
      return throwError(() => error);
    })
  );
};
