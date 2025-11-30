import { Injectable, signal } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private tokenKey = 'auth_token'
  private isLoggedIn = signal(this.hasToken());
  private baseUrl = "https://christmas.nicholaszullo.com"

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { username: string; password: string }): Observable<boolean> {
    
    return this.http.post<any>(`${this.baseUrl}/auth/login`, credentials)
      .pipe(
        catchError(err => {
          console.error('Sign in failed:', err);
          this.isLoggedIn.set(false);
          return throwError(() => err);
        })
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

  private hasToken(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

}
