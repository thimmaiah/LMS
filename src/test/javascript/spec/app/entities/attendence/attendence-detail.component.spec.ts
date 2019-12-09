import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { AttendenceDetailComponent } from 'app/entities/attendence/attendence-detail.component';
import { Attendence } from 'app/shared/model/attendence.model';

describe('Component Tests', () => {
  describe('Attendence Management Detail Component', () => {
    let comp: AttendenceDetailComponent;
    let fixture: ComponentFixture<AttendenceDetailComponent>;
    const route = ({ data: of({ attendence: new Attendence(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [AttendenceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AttendenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttendenceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attendence).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
