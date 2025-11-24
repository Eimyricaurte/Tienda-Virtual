import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VentasUsuario } from './ventas-usuario';

describe('VentasUsuario', () => {
  let component: VentasUsuario;
  let fixture: ComponentFixture<VentasUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VentasUsuario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VentasUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
