import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

import {Location} from './location';
import {Locations} from './mock-list';

@Injectable({
  providedIn: 'root'
})

export class LocationsService {
  private displayLocations = new Subject<Location[]>();

  displayLocations$ = this.displayLocations.asObservable();

  constructor() {
  }

  updateLocations(city: string): void {
    console.log('update displayed locations');
    this.displayLocations.next(Locations);
  }
}
