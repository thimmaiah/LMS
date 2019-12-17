import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CourseComponentsPage, CourseDeleteDialog, CourseUpdatePage } from './course.page-object';

const expect = chai.expect;

describe('Course e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let courseComponentsPage: CourseComponentsPage;
  let courseUpdatePage: CourseUpdatePage;
  let courseDeleteDialog: CourseDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Courses', async () => {
    await navBarPage.goToEntity('course');
    courseComponentsPage = new CourseComponentsPage();
    await browser.wait(ec.visibilityOf(courseComponentsPage.title), 5000);
    expect(await courseComponentsPage.getTitle()).to.eq('Courses');
  });

  it('should load create Course page', async () => {
    await courseComponentsPage.clickOnCreateButton();
    courseUpdatePage = new CourseUpdatePage();
    expect(await courseUpdatePage.getPageTitle()).to.eq('Create or edit a Course');
    await courseUpdatePage.cancel();
  });

  it('should create and save Courses', async () => {
    const nbButtonsBeforeCreate = await courseComponentsPage.countDeleteButtons();

    await courseComponentsPage.clickOnCreateButton();
    await promise.all([
      courseUpdatePage.setNameInput('name'),
      courseUpdatePage.setDurationInDaysInput('5'),
      courseUpdatePage.setHoursPerDayInput('5'),
      courseUpdatePage.setSurveyLinkInput('surveyLink'),
      courseUpdatePage.setTagsInput('tags'),
      courseUpdatePage.setCityInput('city'),
      courseUpdatePage.setLocationInput('location'),
      courseUpdatePage.setStartDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      courseUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      courseUpdatePage.setUpdatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
      // courseUpdatePage.smeSelectLastOption(),
    ]);
    expect(await courseUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await courseUpdatePage.getDurationInDaysInput()).to.eq('5', 'Expected durationInDays value to be equals to 5');
    expect(await courseUpdatePage.getHoursPerDayInput()).to.eq('5', 'Expected hoursPerDay value to be equals to 5');
    expect(await courseUpdatePage.getSurveyLinkInput()).to.eq('surveyLink', 'Expected SurveyLink value to be equals to surveyLink');
    expect(await courseUpdatePage.getTagsInput()).to.eq('tags', 'Expected Tags value to be equals to tags');
    expect(await courseUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await courseUpdatePage.getLocationInput()).to.eq('location', 'Expected Location value to be equals to location');
    expect(await courseUpdatePage.getStartDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected startDate value to be equals to 2000-12-31'
    );
    expect(await courseUpdatePage.getCreatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdAt value to be equals to 2000-12-31'
    );
    expect(await courseUpdatePage.getUpdatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected updatedAt value to be equals to 2000-12-31'
    );
    await courseUpdatePage.save();
    expect(await courseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await courseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Course', async () => {
    const nbButtonsBeforeDelete = await courseComponentsPage.countDeleteButtons();
    await courseComponentsPage.clickOnLastDeleteButton();

    courseDeleteDialog = new CourseDeleteDialog();
    expect(await courseDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Course?');
    await courseDeleteDialog.clickOnConfirmButton();

    expect(await courseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
