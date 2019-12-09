import { element, by, ElementFinder } from 'protractor';

export class CourseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-course div table .btn-danger'));
  title = element.all(by.css('jhi-course div h2#page-heading span')).first();

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

export class CourseUpdatePage {
  pageTitle = element(by.id('jhi-course-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nameInput = element(by.id('field_name'));
  durationInDaysInput = element(by.id('field_durationInDays'));
  hoursPerDayInput = element(by.id('field_hoursPerDay'));
  surveyLinkInput = element(by.id('field_surveyLink'));
  tagsInput = element(by.id('field_tags'));
  cityInput = element(by.id('field_city'));
  locationInput = element(by.id('field_location'));
  startDateInput = element(by.id('field_startDate'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));
  smeSelect = element(by.id('field_sme'));
  companySelect = element(by.id('field_company'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setDurationInDaysInput(durationInDays) {
    await this.durationInDaysInput.sendKeys(durationInDays);
  }

  async getDurationInDaysInput() {
    return await this.durationInDaysInput.getAttribute('value');
  }

  async setHoursPerDayInput(hoursPerDay) {
    await this.hoursPerDayInput.sendKeys(hoursPerDay);
  }

  async getHoursPerDayInput() {
    return await this.hoursPerDayInput.getAttribute('value');
  }

  async setSurveyLinkInput(surveyLink) {
    await this.surveyLinkInput.sendKeys(surveyLink);
  }

  async getSurveyLinkInput() {
    return await this.surveyLinkInput.getAttribute('value');
  }

  async setTagsInput(tags) {
    await this.tagsInput.sendKeys(tags);
  }

  async getTagsInput() {
    return await this.tagsInput.getAttribute('value');
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

  async setStartDateInput(startDate) {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput() {
    return await this.startDateInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt) {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput() {
    return await this.createdAtInput.getAttribute('value');
  }

  async setUpdatedAtInput(updatedAt) {
    await this.updatedAtInput.sendKeys(updatedAt);
  }

  async getUpdatedAtInput() {
    return await this.updatedAtInput.getAttribute('value');
  }

  async smeSelectLastOption() {
    await this.smeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async smeSelectOption(option) {
    await this.smeSelect.sendKeys(option);
  }

  getSmeSelect(): ElementFinder {
    return this.smeSelect;
  }

  async getSmeSelectedOption() {
    return await this.smeSelect.element(by.css('option:checked')).getText();
  }

  async companySelectLastOption() {
    await this.companySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async companySelectOption(option) {
    await this.companySelect.sendKeys(option);
  }

  getCompanySelect(): ElementFinder {
    return this.companySelect;
  }

  async getCompanySelectedOption() {
    return await this.companySelect.element(by.css('option:checked')).getText();
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

export class CourseDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-course-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-course'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
