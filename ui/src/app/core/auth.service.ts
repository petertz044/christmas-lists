import { Injectable, signal } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private isLoggedIn = signal(this.hasToken());
  private baseUrl = "https://christmas.nicholaszullo.com"

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: { username: string; password: string }) {
    
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, credentials).pipe(
      tap(res => {
        localStorage.setItem('jwt', res.jwt);
      }),
      catchError(err => {
        console.error('Sign in failed:', err);
        this.isLoggedIn.set(false);
        return throwError(() => err);
      })
      );
  }

  logout() {
    localStorage.removeItem('jwt');
    this.isLoggedIn.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('jwt');
  }

}

export interface LoginResponse {
  jwt: string;
  message: string;
}