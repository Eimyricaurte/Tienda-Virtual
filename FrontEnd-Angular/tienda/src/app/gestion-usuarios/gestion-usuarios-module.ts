import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

// Componentes standalone
import { Registrar } from './registrar/registrar';
import { Mostrar } from './mostrar/mostrar';
import { MostrarUsuario } from './mostrar-usuario/mostrar-usuario';
import { Editar } from './editar/editar';

const routes: Routes = [
  { path: '', component: Mostrar },
  { path: 'registrar', component: Registrar },
  { path: 'mostrar-usuario', component: MostrarUsuario },
  { path: 'editar', component: Editar}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    Registrar,
    Mostrar,
    MostrarUsuario,
    Editar
  ]
})
export class GestionUsuariosModule {}
