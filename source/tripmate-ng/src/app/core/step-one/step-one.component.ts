import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Observable, Subscription} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

import {LocationsService} from 'src/app/shared/locations.service';
import { InitService } from 'src/app/shared/init.service';

@Component({
  selector: 'app-step-one',
  templateUrl: './step-one.component.html',
  styleUrls: ['./step-one.component.css']
})
export class StepOneComponent implements OnInit {
  @Output() changeStep1Title = new EventEmitter<string>();

  public formStepOne: FormGroup;
  private subscription: Subscription;
  private subscriptionTwo: Subscription;

  private filteredResults: Observable<string[]>;
  private data: string[];
  private defaultChoices: string[];
  private selectedCity: string;

  constructor(
    private formBuilder: FormBuilder,
    private locationsService: LocationsService,
    private initService: InitService
    ) {
    this.formStepOne = this.formBuilder.group({
      searchControl: ['', Validators.required]
    });
    this.filteredResults = this.formStepOne.get('searchControl').valueChanges.pipe(
      startWith(''),
      map(val => this.filterResults(val)),
      map(val => val.slice(0, 4))
    );
    console.log('subscribe?');
    this.subscription = this.initService.selectionChoice$.subscribe(
      selectionChoice => {
        console.log('init data');
        this.data = selectionChoice 
      }
    )
    this.subscriptionTwo = this.initService.defaultSelectionChoice$.subscribe(
      defaultSelectionChoice => {
        console.log('init default');
        this.defaultChoices = defaultSelectionChoice 
      }
    )
    this.initService.updateAll();
  }

  ngOnInit() {
    
  }

  private filterResults(val: string): string[] {
    if (typeof val == 'string')
      return val ? this.data.filter(v => v.toLowerCase().indexOf(val.toLowerCase()) === 0) : this.defaultChoices;
    return this.data;
  }

  private selectCity(val: string) {
    this.selectedCity = val;
  }

  private goNext() {
    if (this.formStepOne.valid){
      this.changeStep1Title.emit(this.selectedCity);
    }
    this.locationsService.updateLocations(this.selectedCity);
  }
}

