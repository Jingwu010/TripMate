import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { CITIES, POPULAR_CITIES } from 'src/app/shared/cities';

@Injectable({
  providedIn: 'root'
})
export class InitService {
  private selectionChoice = new Subject<string[]>();
  private defaultSelectionChoice = new Subject<string[]>();

  selectionChoice$ = this.selectionChoice.asObservable();
  defaultSelectionChoice$ = this.defaultSelectionChoice.asObservable();

  constructor() {
    
  }

  updateAll() {
    console.log('update all?');
    console.log(POPULAR_CITIES);
    this.defaultSelectionChoice.next(POPULAR_CITIES);
    this.selectionChoice.next(CITIES);
    console.log(this.defaultSelectionChoice$);
  }


}
