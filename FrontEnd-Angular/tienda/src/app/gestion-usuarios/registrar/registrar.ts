import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-registrar-usuario',
  templateUrl: './registrar.html',
  styleUrls: ['./registrar.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule]
})
export class Registrar {

  correo = '';
  numeroDocumento: number | null = null;
  nombre = '';
  telefono = '';
  clave = '';
  confirmarClave = '';

  errorMsg = '';
  successMsg = '';
  loading = false;

  private url = 'http://localhost:8081/usuario/save/';

  constructor(private http: HttpClient, private router: Router) {}

  soloNumeros(event: any) {
    const value = event.target.value;
    event.target.value = value.replace(/[^0-9]/g, ""); 
    this.telefono = event.target.value;
  }

  validarCampos(): string | null {
    if (!this.correo?.trim()) return 'El campo correo es obligatorio.';
    if (this.numeroDocumento === null || String(this.numeroDocumento).trim() === '') return 'El número de documento es obligatorio.';
    if (!this.nombre?.trim()) return 'El nombre es obligatorio.';
    if (!this.telefono?.trim()) return 'El teléfono es obligatorio.';
    if (!this.clave) return 'La contraseña es obligatoria.';
    if (!this.confirmarClave) return 'Debe confirmar la contraseña.';
    if (this.clave !== this.confirmarClave) return 'Las contraseñas no coinciden.';
    return null;
  }

  onSubmit() {
    this.errorMsg = '';
    this.successMsg = '';

    const validationError = this.validarCampos();
    if (validationError) {
      this.errorMsg = validationError;
      return;
    }

    const payload = {
      correo: this.correo.trim(),
      numeroDocumento: Number(this.numeroDocumento),
      nombre: this.nombre.trim(),
      clave: this.clave,
      telefono: this.telefono.trim()
    };

    this.loading = true;

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post(this.url, payload, { headers, responseType: 'json' })
      .pipe(
        catchError(err => {
          console.error('Error al guardar usuario:', err);
          return of({ __error: true, error: err });
        })
      )
      .subscribe((res: any) => {
        this.loading = false;

        if (res && res.__error) {
          this.errorMsg = 'No se pudo guardar el usuario. Verifica que el backend esté activo.';
          return;
        }

        this.successMsg = 'Usuario guardado exitosamente 🎉';
        setTimeout(() => {
          this.router.navigate(['/autenticacion']);
        }, 1000);

        this.resetForm();
      });
  }

  resetForm() {
    this.correo = '';
    this.numeroDocumento = null;
    this.nombre = '';
    this.telefono = '';
    this.clave = '';
    this.confirmarClave = '';
  }
}
