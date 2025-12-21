import { Component, inject, OnInit, signal } from '@angular/core';
import { RegisterService, User } from '../../../core/register.service';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import PasswordValidator from '../../../core/validators/password-validator.validators';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit{
  
  hidePassword = true;
  hideConfirmPassword = true;
  passMatch = false;
  private register = inject(RegisterService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          PasswordValidator.passwordStrength
        ]
      ],
      confirmPassword: [
        '',
        [
          Validators.required,
          PasswordValidator.passMatch
        ]
      ]
    });    
  }

  get firstName() {
    return this.registerForm.get('firstName')!;
  }

  get lastName() {
    return this.registerForm.get('lastName')!;
  }

  get username() {
    return this.registerForm.get('username')!;
  }

  get password() {
    return this.registerForm.get('password')!;
  }

  get confirmPassword() {
    return this.registerForm.get('confirmPassword')
  }

  isLoading = signal(false);

  onSubmit() {
    if (this.registerForm.invalid) return;

    this.registerForm.disable()

    this.isLoading.set(true)

    const { username, password } = this.registerForm.value;

    const body = { username, password };

    this.register.registerUser( body ).subscribe({
      next: () => {
        this.registerForm.enable()
        this.isLoading.set(false);
        this.snackBar.open('Registration successful!', 'Close', { duration: 3000 });
        this.router.navigate(['/home'])
      },
      error: (err) => {
        this.registerForm.enable()
        this.isLoading.set(false);
        console.error('Register error:', err);
        this.snackBar.open('Register failed. Please try again.', 'Close', { duration: 3000 });
      }
    });
  }
}
