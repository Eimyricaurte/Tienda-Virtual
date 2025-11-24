import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { Location } from '@angular/common';

@Component({
  selector: 'app-agregar',
  templateUrl: './agregar.html',
  styleUrls: ['./agregar.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule]
})
export class AgregarComponent {

  previewUrl: string | null = null;
  fileSelected: File | null = null;

  productoForm: any;

  private apiUrl = 'http://localhost:8081/producto/save/';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private location: Location
  ) {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: [''],
      precio: [0, [Validators.required, Validators.min(0)]],
      cantidad: [0, [Validators.required, Validators.min(0)]],
      imagen: ['']
    });
  }

  get f() {
    return this.productoForm.controls;
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      this.fileSelected = null;
      this.previewUrl = null;
      this.productoForm.patchValue({ imagen: '' });
      return;
    }

    const file = input.files[0];
    this.fileSelected = file;
    this.previewUrl = URL.createObjectURL(file);

    this.productoForm.patchValue({ imagen: file.name });
  }

  onCancel() {
    this.location.back();
  }

  onSubmit() {
    if (this.productoForm.invalid) {
      Object.values(this.productoForm.controls)
        .forEach((c: any) => c.markAsTouched());
      return;
    }

    const imagenText = this.fileSelected
      ? this.fileSelected.name
      : this.productoForm.value.imagen || '';

    const payload = {
      nombre: this.productoForm.value.nombre,
      descripcion: this.productoForm.value.descripcion,
      imagen: imagenText,
      precio: Number(this.productoForm.value.precio),
      cantidad: Number(this.productoForm.value.cantidad)
    };

    this.http.post<any>(this.apiUrl, payload)
      .pipe(
        catchError(err => {
          console.error('Error al guardar producto:', err);
          alert('Error al guardar el producto.');
          return of(null);
        })
      )
      .subscribe(response => {
        if (!response) return;

        if (response.productoEntity) {
          alert('Producto guardado correctamente.');
          this.onCancel(); 
        } else {
          alert('Respuesta inesperada del servicio.');
        }
      });
  }
}
