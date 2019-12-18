import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { CourseComponent } from './course.component';
import { CourseDetailComponent } from './course-detail.component';
import { CourseUpdateComponent } from './course-update.component';
import { ICourse } from 'app/shared/model/course.model';

@Injectable({ providedIn: 'root' })
export class CourseResolve implements Resolve<ICourse> {
  constructor(private service: CourseService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourse> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((course: HttpResponse<Course>) => course.body));
    }
    return of(new Course());
  }
}

export const courseRoute: Routes = [
  {
    path: 'course',
    component: CourseComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Courses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'course/:id/view',
    component: CourseDetailComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Courses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'course/new',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Courses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'course/:id/edit',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Courses'
    },
    canActivate: [UserRouteAccessService]
  }
];
