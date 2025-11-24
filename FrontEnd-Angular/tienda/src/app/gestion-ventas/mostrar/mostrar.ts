// src/app/gestion-ventas/mostrar/mostrar.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-mostrar-ventas',
  templateUrl: './mostrar.html',
  styleUrls: ['./mostrar.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule]
})
export class MostrarComponent {

  mobileOpen = false;
  carrito: any[] = [];
  userCorreo: string | null = null;

  private apiPago = "http://localhost:8081/factura/payment";
  private apiCarrito = 'http://localhost:8081/venta/carro/';
  private apiEditar = 'http://localhost:8081/venta/editarProductoUsuario/';
  private apiEliminar = 'http://localhost:8081/venta/eliminarVenta/';
  

  constructor(private http: HttpClient) {}

  ngOnInit() {
    const st: any = history.state;
    if (st?.correo) this.userCorreo = st.correo;

    const qpCorreo = new URLSearchParams(window.location.search).get('correo');
    if (qpCorreo) this.userCorreo = qpCorreo;

    if (this.userCorreo) this.cargarCarrito(this.userCorreo);
    else console.error("❌ No se recibió un correo de usuario.");
  }

  toggleMobile() {
    this.mobileOpen = !this.mobileOpen;
  }

  cargarCarrito(correo: string) {
    this.http.get<any>(this.apiCarrito + correo).subscribe({
      next: (data) => {
        console.log("Carrito recibido:", data);
        this.carrito = data.lista ?? [];
        
      },
      error: (err) => console.error("Error cargando carrito:", err)
    });
  }

  calcularTotal() {
    return this.carrito.reduce((sum, item) => sum + item.precioTotal, 0);
  }

  actualizarEnBackend(item: any) {
    const body = {
      id: item.id,
      cantidad: item.cantidad,
      precioTotal: item.precioTotal
    };

    this.http.put<any>(this.apiEditar, body).subscribe({
      next: (resp) => {
        if (resp?.mensaje === "Producto editado correctamente") {
          console.log("✔ Backend confirmó edición correcta");
        } else {
          console.error("⚠ RESPUESTA INCORRECTA DEL BACKEND:", resp);
        }
      },
      error: (err) => {
        console.error("❌ Error actualizando producto:", err);
      }
    });
  }

  aumentarCantidad(index: number) {
    const item = this.carrito[index];
    item.cantidad++;
    this.actualizarPrecio(index);
    this.actualizarEnBackend(item);
  }

  disminuirCantidad(index: number) {
    const item = this.carrito[index];

    if (item.cantidad > 1) {
      item.cantidad--;
      this.actualizarPrecio(index);
      this.actualizarEnBackend(item);
    }
  }

  actualizarPrecio(index: number) {
    const item = this.carrito[index];
    item.precioTotal = item.cantidad * item.producto.precio;
  }

  eliminarItem(index: number) {
    const item = this.carrito[index];
    const url = this.apiEliminar + item.id;

    this.http.delete(url, { observe: 'response' }).subscribe({
      next: (resp) => {
        console.log("Respuesta del backend:", resp);

        if (resp.status === 200 || resp.status === 204) {
          console.log("✔ Eliminado, recargando...");
          window.location.reload();
        } else {
          console.error("⚠ El backend respondió, pero no 200/204:", resp);
        }
      },
      error: (err) => {
        console.error("❌ Error eliminando:", err);
      }
    });
  }
procederPago() {
  if (!this.userCorreo) {
    console.error("❌ No se puede pagar: no se encontró el correo del usuario.");
    return;
  }

  if (!Array.isArray(this.carrito) || this.carrito.length === 0) {
    console.error("❌ Carrito vacío. No se puede pagar.");
    return;
  }

  // DEBUG: muestra la estructura real del carrito
  console.log("DEBUG - carrito completo:", JSON.stringify(this.carrito, null, 2));

  // Buscar un codigoFactura válido en el primer item que lo tenga
  let codigoFactura: any = null;

  for (const item of this.carrito) {
    // posibles ubicaciones comunes (añade más si tu backend usa otro nombre)
    const candidates = [
      item?.facturaEntity?.id,
      item?.facturaEntity?.codigoFactura,
      item?.facturaId,
      item?.factura?.id,
      item?.factura?.codigoFactura,
      item?.codigoFactura,
      item?.facturaEntity // si viene directo como número
    ];

    for (const c of candidates) {
      if (c !== undefined && c !== null && c !== '') {
        codigoFactura = c;
        break;
      }
    }
    if (codigoFactura !== null) break;
  }

  console.log("DEBUG - codigoFactura encontrado:", codigoFactura);

  // Si no se encontró, intenta usar un fallback: usar el id de la factura en el primer item.facturaEntity (aunque sea objeto)
  if (codigoFactura === null) {
    console.warn("⚠ No se encontró codigoFactura en los items. Intentando fallback usando this.carrito[0].facturaEntity...");
    const first = this.carrito[0];
    if (first?.facturaEntity && typeof first.facturaEntity === 'object') {
      // si facturaEntity es objeto sin id, lo mandamos como objeto completo (depende del backend)
      codigoFactura = first.facturaEntity;
      console.log("DEBUG - fallback: usando objeto facturaEntity entero:", codigoFactura);
    }
  }

  // Si aún no hay nada, pregunta al backend por una factura nueva o por el id del carrito
  if (codigoFactura === null) {
    console.error("❌ No se pudo determinar codigoFactura. Opciones:\n 1) revisar la estructura del carrito (ver log)\n 2) crear/obtener una factura en el backend antes del pago");
    // aquí podrías llamar a un endpoint que cree/obtenga la factura; por ahora abortamos.
    return;
  }

  const body = {
    usuario: { correo: this.userCorreo },
    total: this.calcularTotal(),
    codigoFactura: codigoFactura
  };

  console.log("📤 Enviando datos de pago:", body);

  this.http.post<any>(this.apiPago, body).subscribe({
    next: (data) => {
      console.log("✔ Datos recibidos del backend:", data);

      if (!document.getElementById('visa_branding_sound')) {
        const audio = document.createElement('audio');
        audio.id = 'visa_branding_sound';
        audio.style.display = 'none';
        document.body.appendChild(audio);
      }

      if (!data || !data.action) {
        console.error("Respuesta inválida del backend (no hay campo 'action'):", data);
        return;
      }

      const form = document.createElement('form');
      form.method = 'POST';
      form.action = data.action;

      Object.keys(data).forEach(key => {
        if (key !== "action") {
          const input = document.createElement('input');
          input.type = 'hidden';
          input.name = key;
          input.value = String(data[key] ?? '');
          form.appendChild(input);
        }
      });

      document.body.appendChild(form);

      try {
        form.submit();
      } catch (e) {
        console.error("Error al hacer submit del form:", e);
      }
    },
    error: (err) => {
      console.error("❌ Error en el pago (petición a /factura/payment):", err);
    }
  });
}


}