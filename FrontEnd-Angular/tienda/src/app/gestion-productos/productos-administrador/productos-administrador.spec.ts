import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductosAdministrador } from './productos-administrador';

describe('ProductosAdministrador', () => {
  let component: ProductosAdministrador;
  let fixture: ComponentFixture<ProductosAdministrador>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductosAdministrador]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductosAdministrador);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
