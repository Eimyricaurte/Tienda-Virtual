// src/app/gestion-productos/editar/editar.ts
import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-editar',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './editar.html',
  styleUrls: ['./editar.css']
})
export class Editar {

  productoForm: any;
  previewUrl: string | null = null;
  fileSelected: File | null = null;

  codigo: number | null = null;

  private apiGetByName = 'http://localhost:8081/producto/buscarProductoNombreA/';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private fb: FormBuilder
  ) {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: [''],
      precio: [0, [Validators.required, Validators.min(0)]],
      cantidad: [0, [Validators.required, Validators.min(0)]],
      imagen: ['']
    });

    const stateNombre = (history.state && history.state.nombre) ? history.state.nombre : null;
    if (stateNombre) {
      this.cargarProducto(stateNombre);
    }
  }

  get f() {
    return this.productoForm.controls;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const nombreParam = params['nombre'];
      if (nombreParam) {
        this.cargarProducto(nombreParam);
      }
    });
  }

  private cargarProducto(nombre: string) {
    const url = this.apiGetByName + encodeURIComponent(nombre);

    this.http.get<any>(url)
      .pipe(
        catchError(err => {
          console.error('Error al obtener producto por nombre:', err);
          return of(null);
        })
      )
      .subscribe(resp => {
        if (!resp) return;

        const item = Array.isArray(resp.lista) && resp.lista.length > 0 ? resp.lista[0] : null;
        if (!item) return;

        this.codigo = item.codigo;

        this.productoForm.patchValue({
          nombre: item.nombre ?? '',
          descripcion: item.descripcion ?? '',
          precio: item.precio ?? 0,
          cantidad: item.cantidad ?? 0,
          imagen: item.imagen ?? ''
        });

        if (item.imagen) {
          this.previewUrl = `assets/img/${item.imagen}`;
        } else {
          this.previewUrl = null;
        }

        this.fileSelected = null;
      });
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
      Object.values(this.productoForm.controls).forEach((c: any) => c.markAsTouched());
      return;
    }

    if (this.codigo === null) {
      alert("Error: no se pudo obtener el código del producto.");
      return;
    }

    const imagenText = this.fileSelected ? this.fileSelected.name : (this.productoForm.value.imagen || '');

    const payload = {
      codigo: this.codigo,
      nombre: this.productoForm.value.nombre,
      descripcion: this.productoForm.value.descripcion,
      imagen: imagenText,
      precio: Number(this.productoForm.value.precio),
      cantidad: Number(this.productoForm.value.cantidad)
    };

    this.http.put('http://localhost:8081/producto/update/', payload)
      .pipe(
        catchError(err => {
          console.error('Error al actualizar producto:', err);
          alert('Hubo un error al actualizar el producto.');
          return of(null);
        })
      )
      .subscribe(resp => {
        if (!resp) return;

        console.log('Producto actualizado:', resp);
        alert('Producto actualizado correctamente.');
        this.location.back();
      });
  }

}
