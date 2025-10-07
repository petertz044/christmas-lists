import { Injectable, signal } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}
@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  
  constructor(private http: HttpClient, private router: Router) { }

  private userRegistered = signal(false)
  private baseUrl = "jdbc:postgresql://database.nicholaszullo.com:5432/christmas"
  
  registerUser( User: User ) {
    
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }
    
    /*
    return this.http.post<User>(`${this.baseUrl}/register`, User, httpOptions)
      .pipe(
        catchError(err => {
          console.error('Failed to add user:', err);
          this.userRegistered.set(false);
          return of(null);
        })
      ).subscribe(res => {
        if (res) {
          this.userRegistered.set(true);
        }
      });
      */
  }
}
