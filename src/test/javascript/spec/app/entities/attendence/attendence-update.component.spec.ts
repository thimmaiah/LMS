import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { AttendenceUpdateComponent } from 'app/entities/attendence/attendence-update.component';
import { AttendenceService } from 'app/entities/attendence/attendence.service';
import { Attendence } from 'app/shared/model/attendence.model';

describe('Component Tests', () => {
  describe('Attendence Management Update Component', () => {
    let comp: AttendenceUpdateComponent;
    let fixture: ComponentFixture<AttendenceUpdateComponent>;
    let service: AttendenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [AttendenceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AttendenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttendenceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttendenceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Attendence(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Attendence();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
