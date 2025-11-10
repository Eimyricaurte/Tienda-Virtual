import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { CrearUsuario } from "./crear-usuario/crear-usuario";
import { Listar } from "./listar/listar";

export const routes: Routes = [
    {path:'crearUsuario', component: CrearUsuario},
    {path:'listar', component: Listar}

]

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class GestionUsuariosRoutingModule { }
