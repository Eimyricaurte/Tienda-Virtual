import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { Login } from "./login/login";

export const routes: Routes = [
    {path:'login', component: Login}
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AutenticacionRoutingModule{}

