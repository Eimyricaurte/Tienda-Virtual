import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-editar',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  templateUrl: './editar.html',
  styleUrls: ['./editar.css'],
})
export class Editar implements OnInit {
  form: any = {
    correo: '',
    numeroDocumento: '',
    nombre: '',
    clave: '',
    confirmarClave: '',
    telefono: ''
  };

  userCorreo: string | null = null;

  loading = false;
  errorMessage: string | null = null;

  showClave = false;

  private baseUrl = 'http://localhost:8081';

  constructor(
    private http: HttpClient,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
  const state = window.history.state;

  if (state && state.usuario) {
    console.log("Usuario recibido en EDITAR:", state.usuario);
    
    Object.assign(this.form, state.usuario);

    this.userCorreo = state.usuario.correo;
    return; 
  }

  const stored = localStorage.getItem('userCorreo');
  if (stored) {
    this.userCorreo = stored;
    this.loadUsuario(stored);
  } else {
    this.errorMessage = "No se encontró el usuario.";
  }
}


  loadUsuario(correo: string): void {
    this.loading = true;
    this.errorMessage = null;

    const encoded = encodeURIComponent(correo);
    const url = `${this.baseUrl}/usuario/buscarUsuario/${encoded}`;

    this.http.get<any>(url, { observe: 'response' as const }).pipe(
      catchError(err => {
        this.loading = false;
        this.errorMessage = 'Error al cargar datos.';
        return of(null);
      })
    ).subscribe(resp => {
      this.loading = false;
      if (!resp) return;

      const body = resp.body;

      if (body?.estudianteEntity) {
  const e = body.estudianteEntity;

  Object.assign(this.form, {
    correo: e.correo ?? '',
    numeroDocumento: e.numeroDocumento ?? '',
    nombre: e.nombre ?? '',
    clave: e.clave ?? '',
    confirmarClave: e.clave ?? '',
    telefono: e.telefono ?? ''
  });

  return;
}

if (body?.correo) {
  Object.assign(this.form, {
    correo: body.correo ?? '',
    numeroDocumento: body.numeroDocumento ?? '',
    nombre: body.nombre ?? '',
    clave: body.clave ?? '',
    confirmarClave: body.clave ?? '',
    telefono: body.telefono ?? ''
  });
}

    });
  }

  toggleMostrarClave(): void {
    this.showClave = !this.showClave;
  }

  onSubmit(event: Event): void {
  event.preventDefault();

  if (this.form.clave !== this.form.confirmarClave) {
    this.errorMessage = 'Las contraseñas no coinciden.';
    return;
  }

  const url = `${this.baseUrl}/usuario/update/`;

  const payload = {
    correo: this.form.correo,
    numeroDocumento: this.form.numeroDocumento,
    nombre: this.form.nombre,
    clave: this.form.clave,
    telefono: this.form.telefono
  };

  console.log("[Editar] Payload a enviar:", payload);

  this.http.put<any>(url, payload, { 
    headers: { 'Content-Type': 'application/json' }, 
    observe: 'response' 
  }).pipe(
    catchError(err => {
      this.errorMessage = 'No se pudo actualizar el usuario.';
      console.error('[Editar] Error HTTP:', err);
      return of(null);
    })
  ).subscribe(resp => {
    if (!resp) return;

    console.log('[Editar] Respuesta completa del servidor:', resp);

    if (resp.status >= 200 && resp.status < 300) {
      console.log('[Editar] Usuario actualizado correctamente.');
      this.location.back();
    } else {
      this.errorMessage = 'El servidor respondió con un error al actualizar.';
      console.warn('[Editar] Respuesta no OK:', resp);
    }
  });
}

}