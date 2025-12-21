import { Injectable, signal } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface User {
  username: string;
  password: string;
}
@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  
  constructor(private http: HttpClient) { }

  private userRegistered = signal(false)
  private baseUrl = "https://christmas.nicholaszullo.com"
  
  registerUser( user: User ) {
    
    return this.http.post<any>(`${this.baseUrl}/auth/register`, user).pipe(
      catchError(err => {
        console.error('Failed to add user:', err);
        this.userRegistered.set(false);
        return throwError(() => err);
      })
    );
  }
}
