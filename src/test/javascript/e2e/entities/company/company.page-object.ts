import { element, by, ElementFinder } from 'protractor';

export class CompanyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-company div table .btn-danger'));
  title = element.all(by.css('jhi-company div h2#page-heading span')).first();

  async clickOnCreateButton() {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton() {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getText();
  }
}

export class CompanyUpdatePage {
  pageTitle = element(by.id('jhi-company-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nameInput = element(by.id('field_name'));
  addressInput = element(by.id('field_address'));
  emailDomainInput = element(by.id('field_emailDomain'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setAddressInput(address) {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput() {
    return await this.addressInput.getAttribute('value');
  }

  async setEmailDomainInput(emailDomain) {
    await this.emailDomainInput.sendKeys(emailDomain);
  }

  async getEmailDomainInput() {
    return await this.emailDomainInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CompanyDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-company-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-company'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
