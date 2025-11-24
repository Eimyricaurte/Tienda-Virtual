import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Mostrar } from './mostrar';

describe('Mostrar', () => {
  let component: Mostrar;
  let fixture: ComponentFixture<Mostrar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Mostrar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Mostrar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
