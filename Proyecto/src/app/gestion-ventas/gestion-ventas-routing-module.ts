import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { CrearVenta } from "./crear-venta/crear-venta";
import { Listar } from "./listar/listar";

export const routes: Routes = [
    {path:'crearVenta', component: CrearVenta},
    {path:'listar', component: Listar}

]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class GestionVentasRoutingModule { }
