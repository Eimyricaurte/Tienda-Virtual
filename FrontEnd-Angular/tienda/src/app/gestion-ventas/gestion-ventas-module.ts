// src/app/gestion-ventas/gestion-ventas-module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MostrarComponent } from './mostrar/mostrar';
import { MostrarVentas } from './mostrar-ventas/mostrar-ventas';
import { VentasProducto } from './ventas-producto/ventas-producto';
import { VentasUsuario } from './ventas-usuario/ventas-usuario';

const routes: Routes = [
  { path: '', component: MostrarComponent },
  { path: 'ventas', component: MostrarVentas},
  { path: 'ventas-producto', component: VentasProducto},
  { path: 'ventas-usuario', component: VentasUsuario}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MostrarComponent,
    MostrarVentas,
    VentasProducto,
    VentasUsuario
  ]
})
export class GestionVentasModule {}
