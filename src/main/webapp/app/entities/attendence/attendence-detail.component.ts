import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttendence } from 'app/shared/model/attendence.model';

@Component({
  selector: 'jhi-attendence-detail',
  templateUrl: './attendence-detail.component.html'
})
export class AttendenceDetailComponent implements OnInit {
  attendence: IAttendence;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ attendence }) => {
      this.attendence = attendence;
    });
  }

  previousState() {
    window.history.back();
  }
}
