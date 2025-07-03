import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinner,
    MatSnackBarModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar)

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  });

  get email() {
    return this.loginForm.get('email')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }

  isLoading = signal(false);
  loginFailed = signal(false);

  onSubmit() {
    if (this.loginForm.invalid) return;

    this.isLoading.set(true)

    const credentials = this.loginForm.value;

    this.auth.login(credentials as { email: string; password: string }).subscribe({
      next: (success) => {
        this.isLoading.set(false);
        if (success) {
          this.snackBar.open('Login successful', 'Close', { duration: 3000 });
          this.router.navigate(['/home']);
        } else {
          this.snackBar.open('Invalid credentials', 'Close', { duration: 3000 });
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        console.error('Login error:', err);
        this.snackBar.open('Login failed. Please try again.', 'Close', { duration: 3000 });
      }
    });
  }
}
