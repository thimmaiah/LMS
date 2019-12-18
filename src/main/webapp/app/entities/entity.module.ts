import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TestCourseModule } from './course/course.module';
import { TestAttendenceModule } from './attendence/attendence.module';

@NgModule({
  imports: [TestCourseModule, TestAttendenceModule]
})
export class TestEntityModule {}
