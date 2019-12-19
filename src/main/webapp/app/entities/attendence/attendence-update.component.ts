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
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { noUndefined } from '@angular/compiler/src/util';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-attendence-update',
  templateUrl: './attendence-update.component.html'
})
export class AttendenceUpdateComponent implements OnInit {
  isSaving: boolean;

  courses: ICourse[];

  users: IUser[];

  account: Account;

  editForm = this.fb.group({
    id: [],
    attendended: [],
    day: [null, [Validators.min(1), Validators.max(20)]],
    rating: [null, [Validators.min(0), Validators.max(5)]],
    comments: [],
    courseId: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected attendenceService: AttendenceService,
    protected courseService: CourseService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private accountService: AccountService
  ) {
    accountService.identity().subscribe(acc => (this.account = acc));
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.updateForm(attendence);
    });

    this.activatedRoute.queryParams.subscribe(params => {
      if (noUndefined(params['courseId'])) {
        const attendence: IAttendence = {
          courseId: params['courseId'],
          userId: this.account.id
        };
        this.updateForm(attendence);
      }
    });

    this.courseService
      .query()
      .subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(attendence: IAttendence) {
    this.editForm.patchValue({
      id: attendence.id,
      attendended: attendence.attendended,
      day: attendence.day,
      rating: attendence.rating,
      comments: attendence.comments,
      courseId: attendence.courseId,
      userId: attendence.userId
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
      userId: this.editForm.get(['userId']).value
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
