import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule]
})
export class Login {
  email = '';
  password = '';
  remember = false;

  loading = false;
  errorMsg = '';
  successMsg = '';

  private url = 'http://localhost:8081/usuario/login/';

  constructor(private http: HttpClient, private router: Router) {}

  onLogin() {
    this.errorMsg = '';
    this.successMsg = '';
    this.loading = true;

    if (this.email === 'admin@gmail.com' && this.password === '123456') {
      this.loading = false;
      this.successMsg = 'Inicio de sesión como administrador ✔️';
      this.router.navigate(
        ['/gestion-productos/gestion-admin']
      );
      return;
    }

    const payload = {
      correo: this.email.trim(),
      clave: this.password.trim()
    };

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post(this.url, payload, { headers })
      .pipe(
        catchError(err => {
          this.loading = false;
          this.errorMsg = 'Error al conectar con el servidor.';
          return of({ error: true });
        })
      )
      .subscribe((res: any) => {
        this.loading = false;

        if (res.error) return;

        if (res.token) {
          localStorage.setItem('token', res.token);

          this.successMsg = 'Inicio de sesión exitoso ✔️';

          this.router.navigate(
            ['/gestion-productos'],
            { queryParams:{correo: this.email.trim() } }
          );

        } else {
          this.errorMsg = res.correo || 'Usuario o contraseña incorrecta.';
        }
      });
  }

  goToRegister() {
    this.router.navigateByUrl('/gestion-usuarios/registrar');
  }
}
