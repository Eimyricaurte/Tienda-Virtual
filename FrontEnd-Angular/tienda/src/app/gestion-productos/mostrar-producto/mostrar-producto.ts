import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-mostrar-producto',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  templateUrl: './mostrar-producto.html',
  styleUrls: ['./mostrar-producto.css']
})
export class MostrarProducto implements OnInit {

  mobileOpen = false;
  productos: any[] = [];
  userCorreo: string | null = null;

  loading = true;
  error: string | null = null;

  producto: any = {};
  cantidadSeleccionada = 1;

  sugeridos: any[] = [];
  private maxSugeridos = 4;

  private apiGetByName = 'http://localhost:8081/producto/buscarProductoNombreA/';
  private apiListarCatalogo = 'http://localhost:8081/producto/listarCatalogoUsuario/';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    const st: any = history.state;
    if (st?.correo) {
      this.userCorreo = st.correo;
      localStorage.setItem('userCorreo', this.userCorreo ?? '');
    }

    const qpCorreo = this.route.snapshot.queryParams['correo'];
    if (qpCorreo) {
      this.userCorreo = qpCorreo;
      localStorage.setItem('userCorreo', this.userCorreo ?? '');
    }

    if (!this.userCorreo) {
      const stored = localStorage.getItem('userCorreo');
      if (stored) {
        this.userCorreo = stored;
      }
    }

    console.log('Correo activo (detalle):', this.userCorreo);

    this.cargarProductos();

    const nombreSnapshot = this.route.snapshot.queryParams['nombre'];
    if (nombreSnapshot) this.cargarProducto(nombreSnapshot);

    this.route.queryParams.subscribe(params => {
      if (params['nombre']) this.cargarProducto(params['nombre']);
      if (params['correo']) {
        this.userCorreo = params['correo'];
        localStorage.setItem('userCorreo', this.userCorreo ?? '');
      }
    });
  }

  toggleMobile() {
    this.mobileOpen = !this.mobileOpen;
  }

  buscarProducto() {
    const input = document.querySelector('.search-wrap input') as HTMLInputElement | null;
    if (!input) return;
    const nombre = input.value.trim();
    if (!nombre) return;

    this.router.navigate(
      ['/gestion-productos/mostrar-producto'],
      {
        queryParams: { nombre, correo: this.userCorreo ?? '' },
        state: { correo: this.userCorreo ?? '' }
      }
    );
  }

  resetSearch() {
    const input = document.querySelector('.search-wrap input') as HTMLInputElement | null;
    if (input) input.value = '';

    this.router.navigate(
      ['/gestion-productos/mostrar-producto'],
      {
        queryParams: { correo: this.userCorreo ?? '' },
        state: { correo: this.userCorreo ?? '' }
      }
    );

    this.cargarProductos();
  }

  addToCart() {
    if (!this.userCorreo) {
      alert('No se ha identificado un usuario. Por favor revisa la URL o inicia sesión.');
      return;
    }

    if (!this.producto?.codigo) {
      alert('Producto inválido.');
      return;
    }

    if (this.cantidadSeleccionada <= 0) {
      alert('La cantidad debe ser mayor a 0.');
      return;
    }

    const precioTotal = this.cantidadSeleccionada * (this.producto.precio ?? 0);

    const payload = {
      facturaEntity: { usuario: { correo: this.userCorreo } },
      producto: { codigo: this.producto.codigo },
      cantidad: this.cantidadSeleccionada,
      precioTotal
    };

    const url = 'http://localhost:8081/venta/saveProductoCompra/';

    this.http.post(url, payload).subscribe({
      next: () => {
        alert(`Se agregaron ${this.cantidadSeleccionada} unidad(es) de "${this.producto.nombre}".`);
        window.location.reload();
      },
      error: () => alert('No se pudo agregar al carrito.')
    });
  }

  cargarProductos() {
    this.http.get<any>(this.apiListarCatalogo).subscribe({
      next: (data) => this.productos = data?.lista || [],
      error: (err) => console.error('Error cargando catálogo:', err)
    });
  }

  private cargarProducto(nombre: string) {
    this.loading = true;
    this.error = null;

    const url = this.apiGetByName + encodeURIComponent(nombre);

    this.http.get<any>(url)
      .pipe(catchError(err => {
        console.error('Error cargando producto:', err);
        this.error = 'Error al cargar el producto.';
        this.loading = false;
        return of(null);
      }))
      .subscribe(resp => {
        this.loading = false;
        if (!resp) return;

        const item = Array.isArray(resp.lista) && resp.lista[0] ? resp.lista[0] : null;

        if (!item) {
          this.error = 'Producto no encontrado.';
          return;
        }

        this.producto = item;
        this.cantidadSeleccionada = item.cantidad > 0 ? 1 : 0;

        this.calcularSugeridos();
      });
  }

  private calcularSugeridos() {
    if (!this.producto.nombre || this.productos.length === 0) {
      this.sugeridos = [];
      return;
    }

    const actual = this.producto.nombre.toLowerCase();
    this.sugeridos = this.productos
      .filter(p => p.nombre.toLowerCase() !== actual)
      .slice(0, this.maxSugeridos);
  }

  irADetalleSugerido(prod: any) {
    this.router.navigate(
      ['/gestion-productos/mostrar-producto'],
      {
        queryParams: { nombre: prod.nombre, correo: this.userCorreo ?? '' },
        state: { correo: this.userCorreo ?? '' }
      }
    );
  }

  imagenUrl(): string {
    return this.producto.imagen
      ? `assets/img/${this.producto.imagen}`
      : 'assets/img/placeholder.png';
  }

  aumentarCantidad() {
    if (this.producto.cantidad && this.cantidadSeleccionada < this.producto.cantidad) {
      this.cantidadSeleccionada++;
    }
  }

  disminuirCantidad() {
    if (this.cantidadSeleccionada > 1) this.cantidadSeleccionada--;
  }

  volver() {
    this.router.navigate(
      ['/gestion-productos'],
      {
        queryParams: { correo: this.userCorreo ?? '' },
        state: { correo: this.userCorreo ?? '' }
      }
    );
  }

  formatoPrecio(valor?: number) {
    if (valor == null) return '';
    return valor.toLocaleString('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0
    });
  }
}
