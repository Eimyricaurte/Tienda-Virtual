import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { CrearProducto } from "./crear-producto/crear-producto";
import { Listar } from "./listar/listar";

export const routes: Routes = [
    {path:'crearProducto', component: CrearProducto},
    {path:'listar', component: Listar}

]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class GestionProductosRoutingModule { }
