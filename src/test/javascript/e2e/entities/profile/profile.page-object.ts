import { element, by, ElementFinder } from 'protractor';

export class ProfileComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-profile div table .btn-danger'));
  title = element.all(by.css('jhi-profile div h2#page-heading span')).first();

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

export class ProfileUpdatePage {
  pageTitle = element(by.id('jhi-profile-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  pointsInput = element(by.id('field_points'));
  smeLevelSelect = element(by.id('field_smeLevel'));
  skillsInput = element(by.id('field_skills'));
  expertInInput = element(by.id('field_expertIn'));
  shadowingInInput = element(by.id('field_shadowingIn'));
  cityInput = element(by.id('field_city'));
  locationInput = element(by.id('field_location'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setPointsInput(points) {
    await this.pointsInput.sendKeys(points);
  }

  async getPointsInput() {
    return await this.pointsInput.getAttribute('value');
  }

  async setSmeLevelSelect(smeLevel) {
    await this.smeLevelSelect.sendKeys(smeLevel);
  }

  async getSmeLevelSelect() {
    return await this.smeLevelSelect.element(by.css('option:checked')).getText();
  }

  async smeLevelSelectLastOption() {
    await this.smeLevelSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setSkillsInput(skills) {
    await this.skillsInput.sendKeys(skills);
  }

  async getSkillsInput() {
    return await this.skillsInput.getAttribute('value');
  }

  async setExpertInInput(expertIn) {
    await this.expertInInput.sendKeys(expertIn);
  }

  async getExpertInInput() {
    return await this.expertInInput.getAttribute('value');
  }

  async setShadowingInInput(shadowingIn) {
    await this.shadowingInInput.sendKeys(shadowingIn);
  }

  async getShadowingInInput() {
    return await this.shadowingInInput.getAttribute('value');
  }

  async setCityInput(city) {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput() {
    return await this.cityInput.getAttribute('value');
  }

  async setLocationInput(location) {
    await this.locationInput.sendKeys(location);
  }

  async getLocationInput() {
    return await this.locationInput.getAttribute('value');
  }

  async userSelectLastOption() {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return await this.userSelect.element(by.css('option:checked')).getText();
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

export class ProfileDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-profile-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-profile'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
