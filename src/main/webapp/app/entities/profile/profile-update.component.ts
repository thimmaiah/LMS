import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IProfile, Profile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-profile-update',
  templateUrl: './profile-update.component.html'
})
export class ProfileUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.min(0)]],
    smeLevel: [],
    skills: [null, [Validators.required]],
    expertIn: [],
    shadowingIn: [],
    city: [],
    location: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected profileService: ProfileService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ profile }) => {
      this.updateForm(profile);
    });
    this.userService
      .query()
      .subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(profile: IProfile) {
    this.editForm.patchValue({
      id: profile.id,
      points: profile.points,
      smeLevel: profile.smeLevel,
      skills: profile.skills,
      expertIn: profile.expertIn,
      shadowingIn: profile.shadowingIn,
      city: profile.city,
      location: profile.location,
      userId: profile.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const profile = this.createFromForm();
    if (profile.id !== undefined) {
      this.subscribeToSaveResponse(this.profileService.update(profile));
    } else {
      this.subscribeToSaveResponse(this.profileService.create(profile));
    }
  }

  private createFromForm(): IProfile {
    return {
      ...new Profile(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      smeLevel: this.editForm.get(['smeLevel']).value,
      skills: this.editForm.get(['skills']).value,
      expertIn: this.editForm.get(['expertIn']).value,
      shadowingIn: this.editForm.get(['shadowingIn']).value,
      city: this.editForm.get(['city']).value,
      location: this.editForm.get(['location']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfile>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
