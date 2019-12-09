import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IAttendence, Attendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile/profile.service';

@Component({
  selector: 'jhi-attendence-update',
  templateUrl: './attendence-update.component.html'
})
export class AttendenceUpdateComponent implements OnInit {
  isSaving: boolean;

  courses: ICourse[];

  profiles: IProfile[];

  editForm = this.fb.group({
    id: [],
    attendended: [],
    day: [null, [Validators.min(1), Validators.max(20)]],
    rating: [null, [Validators.min(0), Validators.max(5)]],
    comments: [],
    courseId: [],
    profileId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected attendenceService: AttendenceService,
    protected courseService: CourseService,
    protected profileService: ProfileService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.updateForm(attendence);
    });
    this.courseService.query({ 'attendenceId.specified': 'false' }).subscribe(
      (res: HttpResponse<ICourse[]>) => {
        if (!this.editForm.get('courseId').value) {
          this.courses = res.body;
        } else {
          this.courseService
            .find(this.editForm.get('courseId').value)
            .subscribe(
              (subRes: HttpResponse<ICourse>) => (this.courses = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.profileService.query({ 'attendenceId.specified': 'false' }).subscribe(
      (res: HttpResponse<IProfile[]>) => {
        if (!this.editForm.get('profileId').value) {
          this.profiles = res.body;
        } else {
          this.profileService
            .find(this.editForm.get('profileId').value)
            .subscribe(
              (subRes: HttpResponse<IProfile>) => (this.profiles = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(attendence: IAttendence) {
    this.editForm.patchValue({
      id: attendence.id,
      attendended: attendence.attendended,
      day: attendence.day,
      rating: attendence.rating,
      comments: attendence.comments,
      courseId: attendence.courseId,
      profileId: attendence.profileId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const attendence = this.createFromForm();
    if (attendence.id !== undefined) {
      this.subscribeToSaveResponse(this.attendenceService.update(attendence));
    } else {
      this.subscribeToSaveResponse(this.attendenceService.create(attendence));
    }
  }

  private createFromForm(): IAttendence {
    return {
      ...new Attendence(),
      id: this.editForm.get(['id']).value,
      attendended: this.editForm.get(['attendended']).value,
      day: this.editForm.get(['day']).value,
      rating: this.editForm.get(['rating']).value,
      comments: this.editForm.get(['comments']).value,
      courseId: this.editForm.get(['courseId']).value,
      profileId: this.editForm.get(['profileId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendence>>) {
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

  trackCourseById(index: number, item: ICourse) {
    return item.id;
  }

  trackProfileById(index: number, item: IProfile) {
    return item.id;
  }
}
