import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

interface Usuario {
  correo: string;
  numeroDocumento?: string;
  nombre?: string;
  clave?: string;
  telefono?: string;
}

interface Factura {
  codigoFactura: number;
  codigoReferencia?: string;
  usuario?: Usuario;
  estadoTransaccion?: string | null;
  estado?: string | null;
  total?: number;
  transaccionId?: string | null;
  ordenIdPay?: string | null;
  codigoAutorizacionBanco?: string | null;
  fechaTransaccion?: string | null;
}

@Component({
  selector: 'app-mostrar',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  templateUrl: './mostrar.html',
  styleUrls: ['./mostrar.css']
})
export class Mostrar implements OnInit {
  baseUrl = 'http://localhost:8081';
  userCorreo = '';
  loading = true;
  errorMessage = '';
  facturas: Factura[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const navState = this.router.getCurrentNavigation?.()?.extras?.state ?? history.state;
    this.userCorreo = navState?.correo ?? '';

    if (!this.userCorreo) {
      this.errorMessage = 'No se encontró el correo del usuario en el estado de navegación.';
      this.loading = false;
      return;
    }

    this.cargarFacturas();
  }

  cargarFacturas(): void {
    this.loading = true;
    this.errorMessage = '';
    const url = `${this.baseUrl}/factura/usuarioFacturas/${encodeURIComponent(this.userCorreo)}`;
    this.http.get<{ lista: Factura[] }>(url)
      .pipe(
        catchError(err => {
          console.error('Error al consultar facturas', err);
          this.errorMessage = 'Error al cargar facturas. Revisa la conexión con el backend.';
          this.loading = false;
          return of({ lista: [] } as { lista: Factura[] });
        })
      )
      .subscribe(resp => {
        this.facturas = resp?.lista ?? [];
        this.loading = false;
      });
  }

  descargarPdf(f: Factura): void {
    if (!f || !f.codigoFactura) return;
    const url = `${this.baseUrl}/factura/descargar/${f.codigoFactura}`;

    this.http.get(url, { responseType: 'blob', observe: 'response' })
      .subscribe({
        next: (resp: HttpResponse<Blob>) => {
          const blob = resp.body as Blob;

          let filename = `factura-${f.codigoFactura}.pdf`;
          const cd = resp.headers.get('content-disposition');
          if (cd) {
            const match = cd.match(/filename\*?=(?:UTF-8'')?["']?([^;"']+)["']?/i);
            if (match && match[1]) {
              filename = decodeURIComponent(match[1]);
            }
          }
          const objectUrl = URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = objectUrl;
          link.download = filename;
          document.body.appendChild(link);
          link.click();
          link.remove();
          URL.revokeObjectURL(objectUrl);
        },
        error: (err) => {
          console.error('Error descargando PDF', err);
          this.errorMessage = 'No se pudo descargar la factura. Verifica que el backend tenga el endpoint /factura/descargar/{id} y que permita CORS.';
        }
      });
  }
}
