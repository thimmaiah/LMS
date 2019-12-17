export interface IAttendence {
  id?: number;
  attendended?: boolean;
  day?: number;
  rating?: number;
  comments?: string;
  courseName?: string;
  courseId?: number;
  userLogin?: string;
  userId?: number;
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
    public userLogin?: string,
    public userId?: number
  ) {
    this.attendended = this.attendended || false;
  }
}
