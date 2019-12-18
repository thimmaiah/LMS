import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Attendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';
import { AttendenceComponent } from './attendence.component';
import { AttendenceDetailComponent } from './attendence-detail.component';
import { AttendenceUpdateComponent } from './attendence-update.component';
import { IAttendence } from 'app/shared/model/attendence.model';

@Injectable({ providedIn: 'root' })
export class AttendenceResolve implements Resolve<IAttendence> {
  constructor(private service: AttendenceService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendence> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((attendence: HttpResponse<Attendence>) => attendence.body));
    }
    return of(new Attendence());
  }
}

export const attendenceRoute: Routes = [
  {
    path: 'attendence',
    component: AttendenceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Attendences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'attendence/:id/view',
    component: AttendenceDetailComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attendences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'attendence/new',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attendences'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'attendence/:id/edit',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attendences'
    },
    canActivate: [UserRouteAccessService]
  }
];
