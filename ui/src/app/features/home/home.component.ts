import { Component } from '@angular/core';
import { ChristmasCountdownComponent } from './christmas-countdown/christmas-countdown.component';

@Component({
  selector: 'app-home',
  imports: [
    ChristmasCountdownComponent
  ],
  templateUrl: './home.component.html',
})
export class HomeComponent {

}
