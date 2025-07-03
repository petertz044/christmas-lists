import { Injectable, signal } from '@angular/core';
import { environment } from '../../environments/environment';
import { catchError, first, firstValueFrom, map, Observable, of, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token'
  private isLoggedIn = signal(this.hasToken());
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient, private router: Router) {}  

  login(credentials: { email: string; password: string }): Observable<boolean> {
    return this.http.post<{ token: string }>('${this.baseUrl}/login', credentials).pipe(
      tap(res => {
        localStorage.setItem('auth_token', res.token);
        this.isLoggedIn.set(true);
      }),
      map(() => true),
      catchError(() => of(false))
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.isLoggedIn.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated() {
    return this.isLoggedIn();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

}
