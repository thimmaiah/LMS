import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile/profile.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html'
})
export class CourseUpdateComponent implements OnInit {
  isSaving: boolean;

  profiles: IProfile[];

  companies: ICompany[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    durationInDays: [null, [Validators.required, Validators.min(0), Validators.max(20)]],
    hoursPerDay: [null, [Validators.required, Validators.min(0), Validators.max(24)]],
    surveyLink: [],
    tags: [],
    city: [],
    location: [],
    startDate: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    updatedAt: [null, [Validators.required]],
    smes: [],
    companyId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected courseService: CourseService,
    protected profileService: ProfileService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);
    });
    this.profileService
      .query()
      .subscribe((res: HttpResponse<IProfile[]>) => (this.profiles = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.companyService
      .query()
      .subscribe((res: HttpResponse<ICompany[]>) => (this.companies = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(course: ICourse) {
    this.editForm.patchValue({
      id: course.id,
      name: course.name,
      durationInDays: course.durationInDays,
      hoursPerDay: course.hoursPerDay,
      surveyLink: course.surveyLink,
      tags: course.tags,
      city: course.city,
      location: course.location,
      startDate: course.startDate != null ? course.startDate.format(DATE_TIME_FORMAT) : null,
      createdAt: course.createdAt != null ? course.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: course.updatedAt != null ? course.updatedAt.format(DATE_TIME_FORMAT) : null,
      smes: course.smes,
      companyId: course.companyId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  private createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      durationInDays: this.editForm.get(['durationInDays']).value,
      hoursPerDay: this.editForm.get(['hoursPerDay']).value,
      surveyLink: this.editForm.get(['surveyLink']).value,
      tags: this.editForm.get(['tags']).value,
      city: this.editForm.get(['city']).value,
      location: this.editForm.get(['location']).value,
      startDate:
        this.editForm.get(['startDate']).value != null ? moment(this.editForm.get(['startDate']).value, DATE_TIME_FORMAT) : undefined,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      smes: this.editForm.get(['smes']).value,
      companyId: this.editForm.get(['companyId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProfileById(index: number, item: IProfile) {
    return item.id;
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
