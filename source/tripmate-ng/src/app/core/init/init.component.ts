import { Component, OnInit, ViewChild } from '@angular/core';
import { StepOneComponent } from '../step-one/step-one.component';
import { StepTwoComponent } from '../step-two/step-two.component';

@Component({
  selector: 'app-init',
  templateUrl: './init.component.html',
  styleUrls: ['./init.component.css']
})

export class InitComponent implements OnInit {
  @ViewChild(StepOneComponent) stepOneComponent: StepOneComponent;
  @ViewChild(StepTwoComponent) stepTwoComponent: StepTwoComponent;
  private step1_title: string = 'What city would you like to travel?';
  private selectedCity: string;

  constructor() {}

  ngOnInit() {
  }

  get frmStepOne() {
    return this.stepOneComponent ? this.stepOneComponent.formStepOne : null;
  }

  get frmStepTwo() {
      return this.stepTwoComponent ? this.stepTwoComponent.formStepTwo : null;
  }

  changeStep1Title(selectedCity: string) {
    this.selectedCity = selectedCity;
    this.step1_title = this.selectedCity;
    console.log('changeStep1Title', this.step1_title);
  }

  selectionChange(event) {
    if (event.selectedIndex == 0){
      this.step1_title = 'What city would you like to travel?';
    }
    if (event.selectedIndex == 1){
      this.step1_title = this.selectedCity;
    }
  }
}
