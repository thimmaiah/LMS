import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';

@Component({
  templateUrl: './profile-delete-dialog.component.html'
})
export class ProfileDeleteDialogComponent {
  profile: IProfile;

  constructor(protected profileService: ProfileService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.profileService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'profileListModification',
        content: 'Deleted an profile'
      });
      this.activeModal.dismiss(true);
    });
  }
}
