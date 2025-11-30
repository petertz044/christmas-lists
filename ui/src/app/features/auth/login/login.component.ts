import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../../../core/auth.service';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
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
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinner,
    MatSnackBarModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  ngOnInit(): void {
    const token = localStorage.getItem('jwt')
    if (token) {
      this.router.navigate(['/home'])
    }
  }

  hide = true;
  private auth = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar)
  
  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  });

  get username() {
    return this.loginForm.get('username')!;
  }

  get password() {
    return this.loginForm.get('password')!;
  }

  isLoading = signal(false);
  loginFailed = signal(false);

  onSubmit() {
    if (this.loginForm.invalid) return;

    this.loginForm.disable()

    this.isLoading.set(true)

    const credentials = this.loginForm.value;

    this.auth.login(credentials as { username: string; password: string }).subscribe({
      next: (res) => {
        if (res.message === 'Invalid username or password!') {
          this.loginForm.enable();
          this.isLoading.set(false);
          this.snackBar.open(res.message, 'Close', { duration: 3000 });
        } else if (res.message === "Success") {
          this.loginForm.enable()
          this.isLoading.set(false);
          this.snackBar.open(res.message, 'Close', { duration: 3000 });
          this.router.navigate(['/home']);
        }
      },
      error: () => {
        this.loginForm.enable()
        this.isLoading.set(false);
        console.error('Login error.');
        this.snackBar.open(`Login error: Please try again.`, 'Close', { duration: 3000 });
      }
    });
  }
}
