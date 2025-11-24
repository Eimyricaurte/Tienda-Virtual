import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'autenticacion',
    loadChildren: () =>
      import('./autenticacion/autenticacion-module').then(m => m.AutenticacionModule)
  },
  {
    path: 'gestion-usuarios',
    loadChildren: () =>
      import('./gestion-usuarios/gestion-usuarios-module').then(m => m.GestionUsuariosModule)
  },
  {
    path: 'gestion-facturas',
    loadChildren: () =>
      import('./gestion-facturas/gestion-facturas-module').then(m => m.GestionFacturasModule)
  },
  {
    path: 'gestion-productos',
    loadChildren: () =>
      import('./gestion-productos/gestion-productos-module').then(m => m.GestionProductosModule)
  },
  {
    path: 'gestion-ventas',
    loadChildren: () =>
      import('./gestion-ventas/gestion-ventas-module').then(m => m.GestionVentasModule)
  },
  { path: '', redirectTo: 'autenticacion', pathMatch: 'full' }
];
