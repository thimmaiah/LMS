import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttendence } from 'app/shared/model/attendence.model';

type EntityResponseType = HttpResponse<IAttendence>;
type EntityArrayResponseType = HttpResponse<IAttendence[]>;

@Injectable({ providedIn: 'root' })
export class AttendenceService {
  public resourceUrl = SERVER_API_URL + 'api/attendences';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/attendences';

  constructor(protected http: HttpClient) {}

  create(attendence: IAttendence): Observable<EntityResponseType> {
    return this.http.post<IAttendence>(this.resourceUrl, attendence, { observe: 'response' });
  }

  update(attendence: IAttendence): Observable<EntityResponseType> {
    return this.http.put<IAttendence>(this.resourceUrl, attendence, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttendence>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendence[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttendence[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
