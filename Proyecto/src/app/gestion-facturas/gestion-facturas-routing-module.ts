import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { CrearFactura } from "./crear-factura/crear-factura";
import { Listar } from "./listar/listar";

export const routes: Routes = [
    {path:'crearFactura', component: CrearFactura},
    {path:'listar', component: Listar}

]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class GestionFacturasRoutingModule { }
