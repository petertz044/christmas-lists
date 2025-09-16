import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './core/header/header.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterLinkActive,
    RouterLink,
    RouterOutlet,
    HeaderComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'christmas-lists';
}
