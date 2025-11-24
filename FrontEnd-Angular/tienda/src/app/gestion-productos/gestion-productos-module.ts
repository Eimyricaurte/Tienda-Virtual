// src/app/gestion-productos/gestion-productos-module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

// IMPORTS de componentes (estos deben ser standalone)
import { AgregarComponent } from './agregar/agregar';
import { MostrarComponent } from './mostrar/mostrar';
import { ProductosAdministrador } from './productos-administrador/productos-administrador';
import { Editar } from './editar/editar';
import { MostrarProducto } from './mostrar-producto/mostrar-producto';

const routes: Routes = [
  { path: '', component: MostrarComponent },
  { path: 'agregar', component: AgregarComponent },
  { path: 'gestion-admin', component: ProductosAdministrador},
  { path: 'editar', component:  Editar}, 
  { path: 'mostrar-producto', component: MostrarProducto}
];

@NgModule({
  // ya NO usamos `declarations` porque los componentes son standalone
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    AgregarComponent,
    MostrarComponent,
    ProductosAdministrador,
    Editar,
    MostrarProducto
  ]
})
export class GestionProductosModule {}
