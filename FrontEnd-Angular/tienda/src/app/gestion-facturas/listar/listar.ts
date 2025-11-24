import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

interface Usuario {
  correo: string;
  numeroDocumento: string;
  nombre: string;
  clave?: string;
  telefono?: string;
}

interface Factura {
  codigoFactura: number;
  codigoReferencia?: string;
  usuario?: Usuario;
  estadoTransaccion?: string | null;
  estado?: string | null;
  total: number;
  transaccionId?: string | null;
  ordenIdPay?: string | null;
  codigoAutorizacionBanco?: string | null;
  fechaTransaccion?: string | null;
}

@Component({
  selector: 'app-listar',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule, FormsModule],
  templateUrl: './listar.html',
  styleUrls: ['./listar.css']
})
export class Listar implements OnInit {
  mobileOpen = false;
  facturas: Factura[] = [];
  loading = false;
  errorMessage: string | null = null;

  fechaInicioInput = ''; 
  fechaFinInput = '';    

  selectedEstado: string = ''; 

  private readonly BASE_URL = 'http://localhost:8081/factura/listar/';
  private readonly DESCARGAR_URL = 'http://localhost:8081/factura/descargar/';
  private readonly FILTRO_FECHA_URL = 'http://localhost:8081/factura/fecha/';
  private readonly FILTRO_ESTADO_URL = 'http://localhost:8081/factura/estado/';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.cargarFacturas();
  }

  toggleMobile(): void {
    this.mobileOpen = !this.mobileOpen;
  }

  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth > 768 && this.mobileOpen) {
      this.mobileOpen = false;
    }
  }

  cargarFacturas(): void {
    this.loading = true;
    this.errorMessage = null;

    this.http.get<{ lista: Factura[] }>(this.BASE_URL).pipe(
      catchError(err => {
        this.loading = false;
        console.error('Error cargando facturas', err);
        this.errorMessage = 'No se pudieron cargar las facturas.';
        return of({ lista: [] });
      })
    ).subscribe(resp => {
      this.loading = false;
      this.facturas = (resp && (resp as any).lista) ? (resp as any).lista : [];
    });
  }

  descargarFactura(id: number): void {
    const url = `${this.DESCARGAR_URL}${id}`;
    this.http.get(url, { responseType: 'blob' }).pipe(
      catchError(err => {
        console.error('Error descargando factura', err);
        this.errorMessage = 'Error al descargar la factura.';
        return of(null);
      })
    ).subscribe((blob: Blob | null) => {
      if (!blob) return;
      const filename = `factura-${id}.pdf`;
      const blobUrl = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = blobUrl;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(blobUrl);
    });
  }

  private formatearParaBackend(valor: string): string {
    if (!valor) return '';
    if (valor.includes('T')) return valor.split('T')[0];
    if (valor.includes(' ')) return valor.split(' ')[0];
    return valor;
  }

  filtrarPorFecha(): void {
    this.errorMessage = null;

    if (!this.fechaInicioInput && !this.fechaFinInput) {
      this.errorMessage = 'Ingresa al menos una fecha de inicio o fin para filtrar.';
      return;
    }

    const fechaInicio = this.fechaInicioInput ? this.formatearParaBackend(this.fechaInicioInput) : '';
    const fechaFin = this.fechaFinInput ? this.formatearParaBackend(this.fechaFinInput) : '';

    const body: any = {};
    if (fechaInicio) body.fechaInicio = fechaInicio;
    if (fechaFin) body.fechaFin = fechaFin;

    console.log('[filtrarPorFecha - POST] body ->', body);

    this.loading = true;

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post(this.FILTRO_FECHA_URL, body, { headers, observe: 'response' }).pipe(
      catchError(err => {
        this.loading = false;
        console.error('[filtrarPorFecha - POST] Error:', err);

        const serverMsg = err && err.error ? err.error : null;
        if (serverMsg) {
          try {
            this.errorMessage = typeof serverMsg === 'string' ? serverMsg : JSON.stringify(serverMsg);
          } catch (e) {
            this.errorMessage = 'Ocurrió un error al aplicar el filtro (400).';
          }
        } else {
          this.errorMessage = 'Ocurrió un error al aplicar el filtro de fechas.';
        }
        return of(null);
      })
    ).subscribe((response: any) => {
      this.loading = false;
      if (!response) return;

      const respBody = response.body ?? response;
      console.log('[filtrarPorFecha - POST] body recibido ->', respBody);

      if (respBody && respBody.lista && Array.isArray(respBody.lista)) {
        this.facturas = respBody.lista;
      } else if (Array.isArray(respBody)) {
        this.facturas = respBody;
      } else if (respBody && Array.isArray(respBody.data)) {
        this.facturas = respBody.data;
      } else if (respBody && respBody.facturas && Array.isArray(respBody.facturas)) {
        this.facturas = respBody.facturas;
      } else {
        console.warn('[filtrarPorFecha - POST] Formato de respuesta inesperado', respBody);
        this.facturas = [];
        if (!this.errorMessage) this.errorMessage = 'Respuesta del servidor en formato inesperado.';
      }
    });
  }

  filtrarPorEstado(): void {
    this.errorMessage = null;

    if (!this.selectedEstado) {
      this.errorMessage = 'Selecciona un estado para filtrar.';
      return;
    }

    const estado = this.selectedEstado.trim();

    this.loading = true;
    const url = `${this.FILTRO_ESTADO_URL}${encodeURIComponent(estado)}`;

    this.http.get<{ lista: Factura[] }>(url).pipe(
      catchError(err => {
        this.loading = false;
        console.error('Error filtrando por estado', err);
        this.errorMessage = 'Ocurrió un error al aplicar el filtro por estado.';
        return of({ lista: [] });
      })
    ).subscribe(resp => {
      this.loading = false;
      this.facturas = resp && (resp as any).lista ? (resp as any).lista : [];
    });
  }

  volver(): void {
    this.fechaInicioInput = '';
    this.fechaFinInput = '';
    this.selectedEstado = '';
    this.errorMessage = null;
    this.cargarFacturas();
  }

  mostrarValor(v: any, defecto = 'No disponible'): string {
    if (v === null || v === undefined || (typeof v === 'string' && v.trim() === '')) {
      return defecto;
    }
    return String(v);
  }

  etiquetaEstado(estado: string | null | undefined): string {
    if (!estado) return 'No disponible';
    switch (estado) {
      case '4': return 'Aprobado';
      case '6': return 'Rechazado';
      case '7': return 'Pendiente';
      default: return estado;
    }
  }
}
