import { Component, inject, signal } from '@angular/core';
import { RegisterService, User } from '../../../core/register.service';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    RegisterComponent,
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinner,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  
  hide = true;
  private register = inject(RegisterService)
  private auth = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  registerForm = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  });
  
  get firstName() {
    return this.registerForm.get('firstName')!;
  }

  get lastName() {
    return this.registerForm.get('firstName')!;
  }

  get email() {
    return this.registerForm.get('email')!;
  }

  get password() {
    return this.registerForm.get('password')!;
  }

  isLoading = signal(false);
  loginFailed = signal(false);

  onSubmit() {
    if (this.registerForm.invalid) return;

    this.registerForm.disable()

    this.isLoading.set(true)

    const user = this.registerForm.value;

    /*

    this.register.registerUser( user as {
      firstName: string,
      lastName: string,
      email: string,
      password: string
    }).subscribe({
      next: (success) => {
        this.registerForm.enable()
        this.isLoading.set(false);
        if (success) {
          this.snackBar.open('Login successful', 'Close', { duration: 3000 });
          this.router.navigate(['/home']);
        } else {
          this.snackBar.open('Invalid credentials', 'Close', { duration: 3000 });
        }
      },
      error: (err) => {
        this.registerForm.enable()
        this.isLoading.set(false);
        console.error('Login error:', err);
        this.snackBar.open('Login failed. Please try again.', 'Close', { duration: 3000 });
      }
    });

    */
  }
}
