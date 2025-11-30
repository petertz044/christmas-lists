import { Component, signal, OnInit, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-christmas-countdown',
  standalone: true,
  templateUrl: 'christmas-countdown.component.html',
  styleUrl: 'christmas-countdown.component.scss'
})

export class ChristmasCountdownComponent implements OnInit, OnDestroy {
  days = signal(0);
  hours = signal(0);
  minutes = signal(0);
  seconds = signal(0);

  private christmasDay!: Date;
  private subscription!: Subscription;

  ngOnInit(): void {
    this.christmasDay = this.getNextChristmas();
    this.updateCountdown();

    this.subscription = interval(1000).subscribe(() => this.updateCountdown());
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private getNextChristmas(): Date {
    const now = new Date();
    const year = now.getFullYear();
    const christmas = new Date(year, 11, 25, 0, 0, 0);

    return now >= christmas
      ? new Date(year + 1, 11, 25, 0, 0, 0)
      : christmas;
  }

  private updateCountdown(): void {
    const now = new Date();
    const diffMs = this.christmasDay.getTime() - now.getTime();

    if (diffMs <= 0) {
      this.days.set(0);
      this.hours.set(0);
      this.minutes.set(0);
      this.seconds.set(0);
      return;
    }

    const totalSeconds = Math.floor(diffMs / 1000);
    this.days.set(Math.floor(totalSeconds / (60 * 60 * 24)));
    this.hours.set(Math.floor((totalSeconds % (60 * 60 * 24)) / 3600));
    this.minutes.set(Math.floor((totalSeconds % 3600) / 60));
    this.seconds.set(totalSeconds % 60);
  }

  formatNumber(n: number): string {
    return n.toString().padStart(2, '0');
  }
}
