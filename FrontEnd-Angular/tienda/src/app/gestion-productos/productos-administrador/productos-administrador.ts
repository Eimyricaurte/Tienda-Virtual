// src/app/gestion-productos/productos-administrador/productos-administrador.ts

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-productos-administrador',
  templateUrl: './productos-administrador.html',
  styleUrls: ['./productos-administrador.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule]
})
export class ProductosAdministrador {

  cartCount = 0;
  mobileOpen = false;

  productos: any[] = [];
  textoBusqueda: string = '';   

  userCorreo: string | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {

    const st: any = history.state;
    if (st && st.correo) {
      this.userCorreo = st.correo;
      console.log('Correo recibido (admin):', this.userCorreo);
    }

    this.http.get<any>('http://localhost:8081/producto/listar/')
      .subscribe({
        next: (resp) => {
          console.log('Respuesta listar productos:', resp);

          if (resp?.lista && Array.isArray(resp.lista)) {
            this.productos = resp.lista;
          } else if (resp?.productos && Array.isArray(resp.productos)) {
            this.productos = resp.productos;
          } else {
            this.productos = Array.isArray(resp) ? resp : [];
            console.warn('Formato inesperado, productos asignados así:', this.productos);
          }

          console.log("Productos cargados:", this.productos);
        },
        error: (err) => {
          console.error("Error al cargar productos:", err);
        }
      });
  }
  panelAbierto = false;

toggleMobile() {
  this.panelAbierto = !this.panelAbierto;
}


  addToCart(p: any) {
    this.cartCount++;
    console.log('Añadido al carrito (admin):', p);
  }

  editar(prod: any) {
    console.log("Editar producto (objeto recibido):", prod);

    if (!prod) {
      alert('Error: el producto es undefined. Revisa la plantilla que llama a editar(prod).');
      return;
    }

    const posiblesKeys = ['nombre', 'nombreProducto', 'title', 'name', 'nombre_prod'];
    let nombre: string | null = null;

    for (const k of posiblesKeys) {
      if (prod[k]) {
        nombre = String(prod[k]);
        console.log(`Encontrado nombre en la key "${k}":`, nombre);
        break;
      }
    }

    if (!nombre && typeof prod === 'string' && prod.trim().length > 0) {
      nombre = prod;
    }

    if (!nombre) {
      alert('No se pudo obtener el nombre del producto.');
      console.error('Objeto prod completo (sin nombre encontrado):', prod);
      return;
    }

    try {
      this.router.navigate(['/gestion-productos/editar'], {
        queryParams: { nombre }
      }).catch(err => console.error(err));
    } catch (e) {
      console.error('Excepción al navegar:', e);
    }
  }

  eliminarProducto(codigo: number) {
    if (!confirm("¿Seguro que deseas eliminar este producto?")) return;

    this.http.delete<any>(`http://localhost:8081/producto/delete/${codigo}`)
      .pipe(
        catchError(err => {
          console.error("Error al eliminar producto:", err);
          alert("Hubo un error al eliminar el producto.");
          return of(null);
        })
      )
      .subscribe(resp => {

        console.log("Respuesta delete:", resp);

        if (!resp || !resp.mensaje) {
          alert("No se recibió un mensaje válido del servidor.");
          return;
        }

        const mensaje = resp.mensaje;

        if (mensaje.includes(`codigo ${codigo}`)) {
          alert(mensaje);
          location.reload();
        } else {
          alert("⚠ El servidor devolvió un mensaje, pero NO coincide el código.");
          console.warn("Mensaje recibido:", mensaje);
        }
      });
  }

  irCrearProducto() {
    this.router.navigate(['/gestion-productos/agregar']);
  }

  buscarNoStock() {
    const cantidad = 0;

    this.http.get<any>(`http://localhost:8081/producto/buscarProductoCantidad/${cantidad}`)
      .subscribe({
        next: (resp) => {
          console.log('Respuesta productos sin stock:', resp);

          if (resp?.lista && Array.isArray(resp.lista)) {
            this.productos = resp.lista;
          } else {
            this.productos = [];
            console.warn("No llegaron productos en resp.lista");
          }
        },
        error: (err) => {
          console.error("Error al buscar productos sin stock:", err);
        }
      });
  }

  buscarPocoStock() {
    const cantidad = 5;

    this.http.get<any>(`http://localhost:8081/producto/buscarProductoCantidad/${cantidad}`)
      .subscribe({
        next: (resp) => {
          console.log('Respuesta productos con poco stock:', resp);

          if (resp?.lista && Array.isArray(resp.lista)) {
            this.productos = resp.lista;
          } else {
            this.productos = [];
            console.warn("No llegaron productos en resp.lista");
          }
        },
        error: (err) => {
          console.error("Error al buscar productos con poco stock:", err);
        }
      });
  }

  refrescar() {
    setTimeout(() => location.reload(), 50);
  }
  buscarPorNombre() {
    const nombre = this.textoBusqueda.trim();

    if (!nombre) {
      alert("Escribe algo para buscar.");
      return;
    }

    this.http.get<any>(`http://localhost:8081/producto/buscarProductoNombreA/${nombre}`)
      .subscribe({
        next: (resp) => {
          console.log("Respuesta buscar por nombre:", resp);

          if (resp?.lista && Array.isArray(resp.lista)) {
            this.productos = resp.lista;  
          } else {
            this.productos = [];
            console.warn("No llegaron productos válidos.");
          }
        },
        error: (err) => {
          console.error("Error al buscar producto por nombre:", err);
          alert("No se encontró el producto.");
          this.productos = [];
        }
      });
  }

  verVentasProducto(producto: any): void {
  this.router.navigate(['/gestion-ventas/ventas-producto'], { 
    state: { nombreProducto: producto.nombre } 
  });
}


}
