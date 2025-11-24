import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Usuario {
  correo: string;
  numeroDocumento: string;
  nombre: string;
  clave?: string;
  telefono?: string;
  estado?: 'pagado' | 'sin pagar';
}

@Component({
  selector: 'app-mostrar',
  templateUrl: './mostrar.html',
  styleUrls: ['./mostrar.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule]
})
export class Mostrar implements OnInit {
  mobileOpen = false;
  listaUsuarios: Usuario[] = [];

  private readonly LISTAR_URL = 'http://localhost:8081/usuario/listar/';
  private readonly DELETE_URL = 'http://localhost:8081/usuario/delete'; 

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  toggleMobile(): void {
    this.mobileOpen = !this.mobileOpen;
  }

  @HostListener('window:resize')
  onResize(): void {
    const w = window.innerWidth;
    if (w > 768 && this.mobileOpen) {
      this.mobileOpen = false;
    }
  }

  cargarUsuarios(): void {
    this.http.get<{ lista: Usuario[] }>(this.LISTAR_URL)
      .subscribe({
        next: (res) => {
          this.listaUsuarios = (res.lista || []).map(u => ({ ...u, estado: 'sin pagar' }));
        },
        error: (err) => {
          console.error('Error cargando usuarios', err);
        }
      });
  }

  marcarPagado(u: Usuario): void {
    u.estado = 'pagado';
  }

  marcarSinPagar(u: Usuario): void {
    u.estado = 'sin pagar';
  }

  eliminar(u: Usuario): void {
    if (!u || !u.correo) {
      console.error('Usuario inválido para eliminar', u);
      return;
    }

    const url = `${this.DELETE_URL}/${encodeURIComponent(u.correo)}`;

    this.http.delete<{ mensaje: string }>(url)
      .subscribe({
        next: (res) => {
          try {
            const msg = res && res.mensaje ? res.mensaje : 'Usuario eliminado correctamente';
            alert(msg);
          } catch {
            alert('Usuario eliminado correctamente');
          }
          window.location.reload();
        },
        error: (err) => {
          console.error('Error eliminando usuario', err);
          const msg = err?.error?.mensaje || 'No se pudo eliminar el usuario. Revisa la consola.';
          alert(msg);
        }
      });
  }

verVentas(u: Usuario): void {
  if (!u || !u.correo) {
    console.error('Usuario inválido para ver ventas', u);
    return;
  }
  
  this.router.navigate(['/gestion-ventas/ventas-usuario'], {
    state: { correoUsuario: u.correo }
  });
}

}
