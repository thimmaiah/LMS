import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';

@Component({
  templateUrl: './attendence-delete-dialog.component.html'
})
export class AttendenceDeleteDialogComponent {
  attendence: IAttendence;

  constructor(
    protected attendenceService: AttendenceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.attendenceService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'attendenceListModification',
        content: 'Deleted an attendence'
      });
      this.activeModal.dismiss(true);
    });
  }
}
