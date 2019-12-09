import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from './course.service';

@Component({
  templateUrl: './course-delete-dialog.component.html'
})
export class CourseDeleteDialogComponent {
  course: ICourse;

  constructor(protected courseService: CourseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.courseService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'courseListModification',
        content: 'Deleted an course'
      });
      this.activeModal.dismiss(true);
    });
  }
}
