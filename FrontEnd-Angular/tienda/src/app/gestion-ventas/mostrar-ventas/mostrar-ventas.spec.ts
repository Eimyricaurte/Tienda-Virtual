import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MostrarVentas } from './mostrar-ventas';

describe('MostrarVentas', () => {
  let component: MostrarVentas;
  let fixture: ComponentFixture<MostrarVentas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MostrarVentas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MostrarVentas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
