import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-mostrar-usuario',
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule, FormsModule],
  templateUrl: './mostrar-usuario.html',
  styleUrls: ['./mostrar-usuario.css']
})
export class MostrarUsuario implements OnInit {
  mobileOpen = false;

  userCorreo: string | null = null;

  cartCount = 0;

  terminoBusqueda = '';

  usuario: any = null;

  loading = false;
  errorMessage: string | null = null;

  private baseUrl = 'http://localhost:8081';

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    const stateCorreo = window.history.state && window.history.state.correo;
    if (stateCorreo) {
      this.userCorreo = stateCorreo;
    } else {
      const stored = localStorage.getItem('userCorreo');
      if (stored) {
        this.userCorreo = stored;
      }
    }
    this.updateCartCount();

    if (this.userCorreo) {
      this.loadUsuario(this.userCorreo);
    } else {
      this.errorMessage = 'No se encontró el correo del usuario. Inicia sesión para ver tu perfil.';
    }
  }

  buscar(valor: string): void {
    this.terminoBusqueda = (valor || '').trim();
    if (!this.terminoBusqueda) {
      this.router.navigate(['/gestion-productos']);
      return;
    }
    this.router.navigate(['/gestion-productos'], { queryParams: { q: this.terminoBusqueda } });
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

  irPerfil(): void {
    if (!this.userCorreo) {
      this.router.navigate(['/autenticacion']);
      return;
    }

    this.router.navigate(['/gestion-usuarios/mostrar-usuario'], { state: { correo: this.userCorreo } });
  }
  updateCartCount(): void {
    try {
      const raw = localStorage.getItem('cart'); 
      if (!raw) {
        this.cartCount = 0;
        return;
      }
      const arr = JSON.parse(raw);
      this.cartCount = Array.isArray(arr) ? arr.length : 0;
    } catch {
      this.cartCount = 0;
    }
  }

  refreshCart(): void {
    this.updateCartCount();
  }

  loadUsuario(correo: string): void {
    this.loading = true;
    this.errorMessage = null;
    this.usuario = null;

    const encoded = encodeURIComponent(correo);

    const url = `${this.baseUrl}/usuario/buscarUsuario/${encoded}`

    console.log('[MostrarUsuario] intentos de URL:', url);

    (async () => {
      try {
        console.log('[MostrarUsuario] probando URL ->', url);
        const res: any = await new Promise(resolve => {
          this.http.get(url, { observe: 'response' as const }).pipe(
            catchError(err => of(err))
          ).subscribe((resp: any) => resolve(resp), (err: any) => resolve(err));
        });

        if (res && res.status && res.status >= 200 && res.status < 300) {
          const body = res.body;
          if (body && body.usuarioEntity) {
            this.usuario = body.usuarioEntity;
            this.loading = false;
            console.log('[MostrarUsuario] usuario cargado (directo):', this.usuario);
            return;
          }
        } else {
          console.warn('[MostrarUsuario] intento fallido para URL:', url, 'respuesta/err:', res);
        }
      } catch (e) {
        console.error('[MostrarUsuario] excepción al intentar URL:', url, e);
      }
      this.loading = false;
      this.errorMessage = 'No se pudo obtener el usuario desde el backend. Revisa consola (Network/Console).';
      console.error('[MostrarUsuario] todos los intentos fallaron. Revisa backend (running), CORS y la URL exacta.');
    })();
  }
editarUsuario(): void {
  if (!this.userCorreo) {
    this.errorMessage = 'No se encontró el correo.';
    return;
  }

  this.router.navigate(['/gestion-usuarios/editar'], { 
    state: { usuario: this.usuario }  
  });
}

  volver(): void {
    this.router.navigate(['/']);
  }
eliminarUsuario(): void {
  if (!this.userCorreo) {
    this.errorMessage = 'No se encontró el correo del usuario.';
    return;
  }

  const confirmar = confirm('¿Está seguro que desea eliminar su cuenta? Esta acción no se puede deshacer.');
  if (!confirmar) return;

  const url = `${this.baseUrl}/usuario/delete/${encodeURIComponent(this.userCorreo)}`;

  this.http.delete(url, { observe: 'response' as const }).pipe(
    catchError(err => {
      console.error('Error al eliminar usuario:', err);
      this.errorMessage = 'No se pudo eliminar la cuenta. Intenta nuevamente.';
      return of(null);
    })
  ).subscribe(resp => {
    if (!resp || resp.status < 200 || resp.status >= 300) {
      this.errorMessage = 'No se pudo eliminar la cuenta. El servidor respondió con error.';
      return;
    }

    alert('Cuenta eliminada correctamente.');
    this.router.navigate(['/autenticacion']);
  });
}

}