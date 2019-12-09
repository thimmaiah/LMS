import { ICourse } from 'app/shared/model/course.model';
import { SmeLevel } from 'app/shared/model/enumerations/sme-level.model';

export interface IProfile {
  id?: number;
  points?: number;
  smeLevel?: SmeLevel;
  skills?: string;
  expertIn?: string;
  shadowingIn?: string;
  city?: string;
  location?: string;
  userLogin?: string;
  userId?: number;
  companyName?: string;
  companyId?: number;
  courses?: ICourse[];
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public points?: number,
    public smeLevel?: SmeLevel,
    public skills?: string,
    public expertIn?: string,
    public shadowingIn?: string,
    public city?: string,
    public location?: string,
    public userLogin?: string,
    public userId?: number,
    public companyName?: string,
    public companyId?: number,
    public courses?: ICourse[]
  ) {}
}
