import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MostrarProducto } from './mostrar-producto';

describe('MostrarProducto', () => {
  let component: MostrarProducto;
  let fixture: ComponentFixture<MostrarProducto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MostrarProducto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MostrarProducto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
