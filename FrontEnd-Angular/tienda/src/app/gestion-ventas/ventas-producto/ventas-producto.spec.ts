import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VentasProducto } from './ventas-producto';

describe('VentasProducto', () => {
  let component: VentasProducto;
  let fixture: ComponentFixture<VentasProducto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VentasProducto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VentasProducto);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
