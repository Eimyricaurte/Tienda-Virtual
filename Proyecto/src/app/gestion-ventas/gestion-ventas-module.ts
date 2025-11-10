import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { GestionVentasRoutingModule, routes } from './gestion-ventas-routing-module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class GestionVentasModule { }
