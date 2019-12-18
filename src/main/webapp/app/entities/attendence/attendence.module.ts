import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from 'app/shared/shared.module';
import { AttendenceComponent } from './attendence.component';
import { AttendenceDetailComponent } from './attendence-detail.component';
import { AttendenceUpdateComponent } from './attendence-update.component';
import { AttendenceDeleteDialogComponent } from './attendence-delete-dialog.component';
import { attendenceRoute } from './attendence.route';

@NgModule({
  imports: [RouterModule.forChild(attendenceRoute), TestSharedModule],
  exports: [AttendenceComponent],
  declarations: [AttendenceComponent, AttendenceDetailComponent, AttendenceUpdateComponent, AttendenceDeleteDialogComponent],
  entryComponents: [AttendenceDeleteDialogComponent]
})
export class TestAttendenceModule {}
