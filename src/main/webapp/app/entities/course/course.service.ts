import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourse } from 'app/shared/model/course.model';

type EntityResponseType = HttpResponse<ICourse>;
type EntityArrayResponseType = HttpResponse<ICourse[]>;

@Injectable({ providedIn: 'root' })
export class CourseService {
  public resourceUrl = SERVER_API_URL + 'api/courses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/courses';

  constructor(protected http: HttpClient) {}

  create(course: ICourse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(course);
    return this.http
      .post<ICourse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(course: ICourse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(course);
    return this.http
      .put<ICourse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourse[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(course: ICourse): ICourse {
    const copy: ICourse = Object.assign({}, course, {
      startDate: course.startDate != null && course.startDate.isValid() ? course.startDate.toJSON() : null,
      createdAt: course.createdAt != null && course.createdAt.isValid() ? course.createdAt.toJSON() : null,
      updatedAt: course.updatedAt != null && course.updatedAt.isValid() ? course.updatedAt.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
      res.body.updatedAt = res.body.updatedAt != null ? moment(res.body.updatedAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((course: ICourse) => {
        course.startDate = course.startDate != null ? moment(course.startDate) : null;
        course.createdAt = course.createdAt != null ? moment(course.createdAt) : null;
        course.updatedAt = course.updatedAt != null ? moment(course.updatedAt) : null;
      });
    }
    return res;
  }
}
