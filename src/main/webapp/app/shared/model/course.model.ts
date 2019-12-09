import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';

export interface ICourse {
  id?: number;
  name?: string;
  durationInDays?: number;
  hoursPerDay?: number;
  surveyLink?: string;
  tags?: string;
  city?: string;
  location?: string;
  startDate?: Moment;
  createdAt?: Moment;
  updatedAt?: Moment;
  smes?: IProfile[];
  companyName?: string;
  companyId?: number;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public durationInDays?: number,
    public hoursPerDay?: number,
    public surveyLink?: string,
    public tags?: string,
    public city?: string,
    public location?: string,
    public startDate?: Moment,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public smes?: IProfile[],
    public companyName?: string,
    public companyId?: number
  ) {}
}
