import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

import { Location } from './location';

@Injectable({
  providedIn: 'root'
})

export class SelectionService {
  private selectedLocations = new Subject<Location[]>();

  selectedLocations$ = this.selectedLocations.asObservable();

  constructor() { }
  
  updateSelection(selection: Location[]): void {
    console.log('update selected locations');
    this.selectedLocations.next(selection);
  }
}
