export interface IAttendence {
  id?: number;
  attendended?: boolean;
  day?: number;
  rating?: number;
  comments?: string;
  courseName?: string;
  courseId?: number;
  profileId?: number;
}

export class Attendence implements IAttendence {
  constructor(
    public id?: number,
    public attendended?: boolean,
    public day?: number,
    public rating?: number,
    public comments?: string,
    public courseName?: string,
    public courseId?: number,
    public profileId?: number
  ) {
    this.attendended = this.attendended || false;
  }
}
