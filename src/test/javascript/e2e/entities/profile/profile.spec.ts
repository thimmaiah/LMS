import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProfileComponentsPage, ProfileDeleteDialog, ProfileUpdatePage } from './profile.page-object';

const expect = chai.expect;

describe('Profile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let profileComponentsPage: ProfileComponentsPage;
  let profileUpdatePage: ProfileUpdatePage;
  let profileDeleteDialog: ProfileDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Profiles', async () => {
    await navBarPage.goToEntity('profile');
    profileComponentsPage = new ProfileComponentsPage();
    await browser.wait(ec.visibilityOf(profileComponentsPage.title), 5000);
    expect(await profileComponentsPage.getTitle()).to.eq('Profiles');
  });

  it('should load create Profile page', async () => {
    await profileComponentsPage.clickOnCreateButton();
    profileUpdatePage = new ProfileUpdatePage();
    expect(await profileUpdatePage.getPageTitle()).to.eq('Create or edit a Profile');
    await profileUpdatePage.cancel();
  });

  it('should create and save Profiles', async () => {
    const nbButtonsBeforeCreate = await profileComponentsPage.countDeleteButtons();

    await profileComponentsPage.clickOnCreateButton();
    await promise.all([
      profileUpdatePage.setPointsInput('5'),
      profileUpdatePage.smeLevelSelectLastOption(),
      profileUpdatePage.setSkillsInput('skills'),
      profileUpdatePage.setExpertInInput('expertIn'),
      profileUpdatePage.setShadowingInInput('shadowingIn'),
      profileUpdatePage.setCityInput('city'),
      profileUpdatePage.setLocationInput('location'),
      profileUpdatePage.userSelectLastOption()
    ]);
    expect(await profileUpdatePage.getPointsInput()).to.eq('5', 'Expected points value to be equals to 5');
    expect(await profileUpdatePage.getSkillsInput()).to.eq('skills', 'Expected Skills value to be equals to skills');
    expect(await profileUpdatePage.getExpertInInput()).to.eq('expertIn', 'Expected ExpertIn value to be equals to expertIn');
    expect(await profileUpdatePage.getShadowingInInput()).to.eq('shadowingIn', 'Expected ShadowingIn value to be equals to shadowingIn');
    expect(await profileUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await profileUpdatePage.getLocationInput()).to.eq('location', 'Expected Location value to be equals to location');
    await profileUpdatePage.save();
    expect(await profileUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Profile', async () => {
    const nbButtonsBeforeDelete = await profileComponentsPage.countDeleteButtons();
    await profileComponentsPage.clickOnLastDeleteButton();

    profileDeleteDialog = new ProfileDeleteDialog();
    expect(await profileDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Profile?');
    await profileDeleteDialog.clickOnConfirmButton();

    expect(await profileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
