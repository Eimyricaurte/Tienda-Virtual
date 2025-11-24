import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MostrarUsuario } from './mostrar-usuario';

describe('MostrarUsuario', () => {
  let component: MostrarUsuario;
  let fixture: ComponentFixture<MostrarUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MostrarUsuario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MostrarUsuario);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
