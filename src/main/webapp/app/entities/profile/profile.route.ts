import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Profile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';
import { ProfileComponent } from './profile.component';
import { ProfileDetailComponent } from './profile-detail.component';
import { ProfileUpdateComponent } from './profile-update.component';
import { IProfile } from 'app/shared/model/profile.model';

@Injectable({ providedIn: 'root' })
export class ProfileResolve implements Resolve<IProfile> {
  constructor(private service: ProfileService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((profile: HttpResponse<Profile>) => profile.body));
    }
    return of(new Profile());
  }
}

export const profileRoute: Routes = [
  {
    path: '',
    component: ProfileComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Profiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProfileDetailComponent,
    resolve: {
      profile: ProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Profiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProfileUpdateComponent,
    resolve: {
      profile: ProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Profiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProfileUpdateComponent,
    resolve: {
      profile: ProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Profiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
