import { Component } from '@angular/core';
import {FormControl} from '@angular/forms';
import { Observable } from 'rxjs';

import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';

import {Locations} from './mock-list';
import {Location} from './location';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})

export class PageComponent {
  searchControl = new FormControl();
  filteredResults$: Observable<Location[]>;
  data = Locations;

  constructor() {
    this.filteredResults$ = this.searchControl.valueChanges
      .startWith('')
      .map(val => this.filterResults(val))
      .map(val => val.slice(0, 4));
  }

  private filterResults(val: string): Location[] {
    return val ? this.data.filter(v => v.name.toLowerCase().indexOf(val.toLowerCase()) === 0) : this.data;
  }
}
