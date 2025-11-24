import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

// Componente standalone
import { Login } from './login/login';

const routes: Routes = [
  { path: '', component: Login }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    Login
  ]
})
export class AutenticacionModule {}
