import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-ventas-producto',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  templateUrl: './ventas-producto.html',
  styleUrls: ['./ventas-producto.css'],
})
export class VentasProducto implements OnInit {
  mobileOpen = false;
  ventas: any[] = [];
  loading = false;
  errorMessage: string | null = null;
  nombreProducto: string | null = null;

  private baseUrl = 'http://localhost:8081';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    const stateNombre = window.history.state && window.history.state.nombreProducto;
    if (stateNombre) {
      this.nombreProducto = stateNombre;
    }
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

  irARuta(ruta: string): void {
    this.router.navigate([ruta]);
  }

  sinPago(): void {
    if (!this.nombreProducto) {
      this.errorMessage = 'No se pudo identificar el producto.';
      return;
    }
    const url = `${this.baseUrl}/venta/ventasSinPagoProducto/${encodeURIComponent(this.nombreProducto)}`;
    this.cargarVentas(url);
  }

  pago(): void {
    if (!this.nombreProducto) {
      this.errorMessage = 'No se pudo identificar el producto.';
      return;
    }
    const url = `${this.baseUrl}/venta/ventasPagoProducto/${encodeURIComponent(this.nombreProducto)}`;
    this.cargarVentas(url);
  }

  private cargarVentas(url: string): void {
    this.loading = true;
    this.errorMessage = null;
    this.ventas = [];

    this.http.get<any>(url).pipe(
      catchError(err => {
        this.loading = false;
        this.errorMessage = 'Error al cargar ventas.';
        console.error(err);
        return of(null);
      })
    ).subscribe(resp => {
      this.loading = false;
      if (!resp || !resp.lista) {
        this.errorMessage = 'No se encontraron ventas.';
        return;
      }
      this.ventas = resp.lista;
      console.log('Ventas cargadas:', this.ventas);
    });
  }
}
