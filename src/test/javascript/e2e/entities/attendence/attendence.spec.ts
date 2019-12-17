import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AttendenceComponentsPage, AttendenceDeleteDialog, AttendenceUpdatePage } from './attendence.page-object';

const expect = chai.expect;

describe('Attendence e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let attendenceComponentsPage: AttendenceComponentsPage;
  let attendenceUpdatePage: AttendenceUpdatePage;
  let attendenceDeleteDialog: AttendenceDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Attendences', async () => {
    await navBarPage.goToEntity('attendence');
    attendenceComponentsPage = new AttendenceComponentsPage();
    await browser.wait(ec.visibilityOf(attendenceComponentsPage.title), 5000);
    expect(await attendenceComponentsPage.getTitle()).to.eq('Attendences');
  });

  it('should load create Attendence page', async () => {
    await attendenceComponentsPage.clickOnCreateButton();
    attendenceUpdatePage = new AttendenceUpdatePage();
    expect(await attendenceUpdatePage.getPageTitle()).to.eq('Create or edit a Attendence');
    await attendenceUpdatePage.cancel();
  });

  it('should create and save Attendences', async () => {
    const nbButtonsBeforeCreate = await attendenceComponentsPage.countDeleteButtons();

    await attendenceComponentsPage.clickOnCreateButton();
    await promise.all([
      attendenceUpdatePage.setDayInput('5'),
      attendenceUpdatePage.setRatingInput('5'),
      attendenceUpdatePage.setCommentsInput('comments'),
      attendenceUpdatePage.courseSelectLastOption(),
      attendenceUpdatePage.userSelectLastOption()
    ]);
    const selectedAttendended = attendenceUpdatePage.getAttendendedInput();
    if (await selectedAttendended.isSelected()) {
      await attendenceUpdatePage.getAttendendedInput().click();
      expect(await attendenceUpdatePage.getAttendendedInput().isSelected(), 'Expected attendended not to be selected').to.be.false;
    } else {
      await attendenceUpdatePage.getAttendendedInput().click();
      expect(await attendenceUpdatePage.getAttendendedInput().isSelected(), 'Expected attendended to be selected').to.be.true;
    }
    expect(await attendenceUpdatePage.getDayInput()).to.eq('5', 'Expected day value to be equals to 5');
    expect(await attendenceUpdatePage.getRatingInput()).to.eq('5', 'Expected rating value to be equals to 5');
    expect(await attendenceUpdatePage.getCommentsInput()).to.eq('comments', 'Expected Comments value to be equals to comments');
    await attendenceUpdatePage.save();
    expect(await attendenceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await attendenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Attendence', async () => {
    const nbButtonsBeforeDelete = await attendenceComponentsPage.countDeleteButtons();
    await attendenceComponentsPage.clickOnLastDeleteButton();

    attendenceDeleteDialog = new AttendenceDeleteDialog();
    expect(await attendenceDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Attendence?');
    await attendenceDeleteDialog.clickOnConfirmButton();

    expect(await attendenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
