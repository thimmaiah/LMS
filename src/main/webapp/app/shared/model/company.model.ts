import { ICourse } from 'app/shared/model/course.model';
import { IProfile } from 'app/shared/model/profile.model';

export interface ICompany {
  id?: number;
  name?: string;
  address?: string;
  emailDomain?: string;
  courses?: ICourse[];
  profiles?: IProfile[];
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string,
    public emailDomain?: string,
    public courses?: ICourse[],
    public profiles?: IProfile[]
  ) {}
}
