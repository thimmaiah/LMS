import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.TestCompanyModule)
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.TestCourseModule)
      },
      {
        path: 'profile',
        loadChildren: () => import('./profile/profile.module').then(m => m.TestProfileModule)
      },
      {
        path: 'attendence',
        loadChildren: () => import('./attendence/attendence.module').then(m => m.TestAttendenceModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TestEntityModule {}
