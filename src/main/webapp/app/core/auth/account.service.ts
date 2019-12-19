import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject, of } from 'rxjs';
import { shareReplay, tap, catchError } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { Account } from 'app/core/user/account.model';
import { JhiTrackerService } from '../tracker/tracker.service';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private userIdentity: Account;
  private authenticated = false;
  private authenticationState = new Subject<any>();
  private accountCache$: Observable<Account>;

  constructor(private http: HttpClient, private trackerService: JhiTrackerService) {}

  fetch(): Observable<Account> {
    return this.http.get<Account>(SERVER_API_URL + 'api/account');
  }

  save(account: Account): Observable<Account> {
    return this.http.post<Account>(SERVER_API_URL + 'api/account', account);
  }

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return false;
    }

    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }

    return authorities.some((authority: string) => this.userIdentity.authorities.includes(authority));
  }

  identity(force?: boolean): Observable<Account> {
    if (force || !this.authenticated) {
      this.accountCache$ = null;
    }

    if (!this.accountCache$) {
      this.accountCache$ = this.fetch().pipe(
        catchError(() => {
          if (this.trackerService.stompClient && this.trackerService.stompClient.connected) {
            this.trackerService.disconnect();
          }
          return of(null);
        }),
        tap(account => {
          if (account) {
            this.userIdentity = account;
            console.log('Got account info', this.userIdentity);
            this.authenticated = true;
            this.trackerService.connect();
          } else {
            this.userIdentity = null;
            this.authenticated = false;
          }
          this.authenticationState.next(this.userIdentity);
        }),
        shareReplay()
      );
    }
    return this.accountCache$;
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any> {
    return this.authenticationState.asObservable();
  }

  getImageUrl(): string {
    return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
  }
}
