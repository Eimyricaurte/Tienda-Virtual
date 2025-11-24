import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { AuthService } from '../services/auth';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private skipUrls = [
    '/auth/login',
    '/auth/register',
    '/login',
    '/register'
  ];

  constructor(private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const shouldSkip = this.skipUrls.some(urlPart => request.url.includes(urlPart));
    let req = request;

    if (!shouldSkip) {
      const token = localStorage.getItem('token');
      if (token) {
        req = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    }

    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/authenticacion/login'])
        }
        return throwError(() => err);
      })
    );
  }
}
