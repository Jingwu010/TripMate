import { Component } from '@angular/core';
import {FormControl} from '@angular/forms'

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})

export class PageComponent {
  searchControl = new FormControl();

  results = [
    'what time is it',
    'what is my ip',
    'what song is this',
    'when is the solar eclipse',
    'what is my name',
    'what is inertia',
    'what does the fox say',
    'what do turtles eat',
    'where\'s my refund',
  ];

  constructor() {
  }
}
