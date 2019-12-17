import { element, by, ElementFinder } from 'protractor';

export class AttendenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-attendence div table .btn-danger'));
  title = element.all(by.css('jhi-attendence div h2#page-heading span')).first();

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

export class AttendenceUpdatePage {
  pageTitle = element(by.id('jhi-attendence-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  attendendedInput = element(by.id('field_attendended'));
  dayInput = element(by.id('field_day'));
  ratingInput = element(by.id('field_rating'));
  commentsInput = element(by.id('field_comments'));
  courseSelect = element(by.id('field_course'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  getAttendendedInput() {
    return this.attendendedInput;
  }
  async setDayInput(day) {
    await this.dayInput.sendKeys(day);
  }

  async getDayInput() {
    return await this.dayInput.getAttribute('value');
  }

  async setRatingInput(rating) {
    await this.ratingInput.sendKeys(rating);
  }

  async getRatingInput() {
    return await this.ratingInput.getAttribute('value');
  }

  async setCommentsInput(comments) {
    await this.commentsInput.sendKeys(comments);
  }

  async getCommentsInput() {
    return await this.commentsInput.getAttribute('value');
  }

  async courseSelectLastOption() {
    await this.courseSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async courseSelectOption(option) {
    await this.courseSelect.sendKeys(option);
  }

  getCourseSelect(): ElementFinder {
    return this.courseSelect;
  }

  async getCourseSelectedOption() {
    return await this.courseSelect.element(by.css('option:checked')).getText();
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

export class AttendenceDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-attendence-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-attendence'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}
