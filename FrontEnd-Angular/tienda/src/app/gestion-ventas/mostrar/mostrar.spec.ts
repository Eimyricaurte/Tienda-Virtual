import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MostrarComponent } from './mostrar';

describe('Mostrar', () => {
  let component: MostrarComponent;
  let fixture: ComponentFixture<MostrarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MostrarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MostrarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
