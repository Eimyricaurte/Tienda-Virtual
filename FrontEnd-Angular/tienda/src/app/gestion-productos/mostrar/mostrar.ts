import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-mostrar-productos',
  templateUrl: './mostrar.html',
  styleUrls: ['./mostrar.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule]
})
export class MostrarComponent {

  cartCount = 0;
  mobileOpen = false;
  productos: any[] = [];
  correo: string = '';

  private apiListarCatalogo = 'http://localhost:8081/producto/listarCatalogoUsuario/';
  private apiBuscarPorNombre = 'http://localhost:8081/producto/buscarProductoNombre/';

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
  this.route.queryParams.subscribe(params => {
    this.correo = params['correo'] || localStorage.getItem('userCorreo') || '';
    if (this.correo) {
      localStorage.setItem('userCorreo', this.correo);
      console.log('Correo identificado:', this.correo);
    } else {
      console.log('No se detectó correo de usuario.');
    }
  });

  this.cargarProductos();
}



  addToCart(p: any) {
    if (!this.correo) {
      alert('No se ha identificado un usuario. Por favor inicia sesión o pasa el correo en la URL.');
      return;
    }

    const payload = {
      facturaEntity: { usuario: { correo: this.correo } },
      producto: { codigo: p.codigo },
      cantidad: 1,
      precioTotal: p.precio
    };

    const url = 'http://localhost:8081/venta/saveProductoCompra/';

    this.http.post(
      url,
      payload
    ).subscribe({
      next: (res) => {
        console.log('Producto agregado:', res);
        this.cartCount++;
        alert(`Se agregó "${p.nombre}" al carrito.`);

        this.cargarProductos();
      },
      error: (err) => {
        console.error('Error al agregar producto:', err);
        alert('No se pudo agregar el producto.');
      }
    });
  }

  resetSearch() {
    const input = document.querySelector('.search-wrap input') as HTMLInputElement | null;
    if (input) input.value = '';

    this.router.navigate(
      ['/gestion-productos'],
      {
        queryParams: { correo: this.correo ?? '' },
        state: { correo: this.correo ?? '' }
      }
    );

    this.cargarProductos();
  }

  verDetalle(prod: any) {
    if (!prod?.nombre) return;

    if (!this.correo) {
      this.correo = localStorage.getItem('correo') || '';

      if (!this.correo) {
        alert('Usuario no identificado. No se puede abrir detalle.');
        return;
      }
    }

    this.router.navigate(['/gestion-productos/mostrar-producto'], {
      queryParams: { nombre: prod.nombre, correo: this.correo }
    });

  }

  cargarProductos() {
    this.http.get<any>(this.apiListarCatalogo).subscribe({
      next: (data) => {
        this.productos = data.lista || [];
      },
      error: (err) => {
        console.error('Error cargando productos:', err);
      }
    });
  }

  buscar(nombre: string | null | undefined) {
    const q = (nombre || '').trim();
    if (!q) {
      this.cargarProductos();
      return;
    }

    const url = this.apiBuscarPorNombre + encodeURIComponent(q);

    this.http.get<any>(url)
      .pipe(catchError(err => {
        console.error('Error en búsqueda:', err);
        return of(null);
      }))
      .subscribe(resp => {
        if (!resp) return;

        this.productos = Array.isArray(resp.lista) ? resp.lista : [];
        if (this.productos.length === 0) {
          alert('No se encontraron productos.');
        }
      });
  }
}
