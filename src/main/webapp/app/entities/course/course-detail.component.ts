import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils, JhiAlertService } from 'ng-jhipster';

import { ICourse } from 'app/shared/model/course.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { CourseService } from './course.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-course-detail',
  templateUrl: './course-detail.component.html'
})
export class CourseDetailComponent implements OnInit {
  course: ICourse;
  account: Account;

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected courseService: CourseService
  ) {
    this.accountService = accountService;
    this.accountService.identity().subscribe(acc => (this.account = acc));
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.course = course;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }

  addSme() {
    this.courseService.addSme(this.course.id, this.account.login).subscribe((res: HttpResponse<ICourse>) => {
      this.course = res.body;
      this.jhiAlertService.success('Added you as SME to this course');
    });
  }

  register() {
    this.courseService.register(this.course.id, this.account.login).subscribe((res: HttpResponse<ICourse>) => {
      this.course = res.body;
      this.jhiAlertService.success('Added you to this course');
    });
  }

  deregister() {
    this.courseService.deregister(this.course.id, this.account.login).subscribe((res: HttpResponse<ICourse>) => {
      this.course = res.body;
      this.jhiAlertService.success('Removed you from this course');
    });
  }
}
