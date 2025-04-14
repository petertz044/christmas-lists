import { Component } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { ListsComponent } from './lists/lists.component';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterLinkActive, RouterLink, RouterOutlet, HomeComponent, ListsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'christmas-lists';
}
