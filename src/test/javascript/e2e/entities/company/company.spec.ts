import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CompanyComponentsPage, CompanyDeleteDialog, CompanyUpdatePage } from './company.page-object';

const expect = chai.expect;

describe('Company e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let companyComponentsPage: CompanyComponentsPage;
  let companyUpdatePage: CompanyUpdatePage;
  let companyDeleteDialog: CompanyDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Companies', async () => {
    await navBarPage.goToEntity('company');
    companyComponentsPage = new CompanyComponentsPage();
    await browser.wait(ec.visibilityOf(companyComponentsPage.title), 5000);
    expect(await companyComponentsPage.getTitle()).to.eq('Companies');
  });

  it('should load create Company page', async () => {
    await companyComponentsPage.clickOnCreateButton();
    companyUpdatePage = new CompanyUpdatePage();
    expect(await companyUpdatePage.getPageTitle()).to.eq('Create or edit a Company');
    await companyUpdatePage.cancel();
  });

  it('should create and save Companies', async () => {
    const nbButtonsBeforeCreate = await companyComponentsPage.countDeleteButtons();

    await companyComponentsPage.clickOnCreateButton();
    await promise.all([
      companyUpdatePage.setNameInput('name'),
      companyUpdatePage.setAddressInput('address'),
      companyUpdatePage.setEmailDomainInput('emailDomain')
    ]);
    expect(await companyUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await companyUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await companyUpdatePage.getEmailDomainInput()).to.eq('emailDomain', 'Expected EmailDomain value to be equals to emailDomain');
    await companyUpdatePage.save();
    expect(await companyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Company', async () => {
    const nbButtonsBeforeDelete = await companyComponentsPage.countDeleteButtons();
    await companyComponentsPage.clickOnLastDeleteButton();

    companyDeleteDialog = new CompanyDeleteDialog();
    expect(await companyDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Company?');
    await companyDeleteDialog.clickOnConfirmButton();

    expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
