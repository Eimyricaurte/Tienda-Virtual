import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { Mostrar } from './mostrar/mostrar';
import { Listar } from './listar/listar';

const routes: Routes = [
  { path: '', component: Mostrar },
  { path: 'listar', component: Listar }
  
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    Mostrar,
    Listar
  ]
})
export class GestionFacturasModule {}
