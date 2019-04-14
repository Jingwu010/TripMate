import { Component } from '@angular/core';
import {FormControl} from '@angular/forms';
import { Observable } from 'rxjs';
import {map, startWith} from 'rxjs/operators';

import {Locations} from './mock-list';
import {Location} from './location';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})

export class PageComponent {
  searchControl = new FormControl();
  filteredResults: Observable<Location[]>;
  data = Locations;
  selectedLocations = new Set();

  constructor() {
    this.filteredResults = this.searchControl.valueChanges.pipe(
      startWith(''),
      map(val => this.filterResults(val)),
      map(val => val.slice(0, 4))
    );
  }

  private filterResults(val: string): Location[] {
    if (typeof val == 'string')
      return val ? this.data.filter(v => v.name.toLowerCase().indexOf(val.toLowerCase()) === 0) : this.data;
    return this.data;
  }

  private selectLocation(val) {
    val = this.data.find(loc => loc.name == val)
    this.selectedLocations.add(val);
    console.log(this.selectedLocations);
  }
}
