import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Subscription }   from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource, 
  MatPaginator,
  MatSnackBar} from '@angular/material';

import { Location } from 'src/app/shared/location';
import { SelectionService } from 'src/app/shared/selection.service';
import { LocationsService } from 'src/app/shared/locations.service';

@Component({
  selector: 'app-step-two',
  templateUrl: './step-two.component.html',
  styleUrls: ['./step-two.component.css']
})
export class StepTwoComponent implements OnInit {
  public formStepTwo: FormGroup;
  private subscription: Subscription;
  
  // private data = Locations;
  private dataSource = new MatTableDataSource<Location>();
  private selection = new SelectionModel<Location>(true, []);

  private displayedColumns: string[] = ['select', 'name'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private selectionService: SelectionService,
    private locationsService: LocationsService
    ) {
    this.formStepTwo = this.formBuilder.group({
      name: ['', Validators.required]
    });
    this.subscription = locationsService.displayLocations$.subscribe(
      displayedLocations => {
        console.log('update data source');
        this.dataSource.data = displayedLocations;
        console.log('locations', displayedLocations);
      }
    );
    console.log('constructed');
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }

  doFilter(val) {
    this.dataSource.filter = val.trim().toLocaleLowerCase();
  }

  selectRow(row) {
    this.selection.toggle(row);
    let message: string = row.name + " has been " + (this.selection.isSelected(row) ? " added!" : " removed");
    this.snackBar.open(message, "Close", {
      duration: 2000,
    });
    this.selectionService.updateSelection(this.selection.selected);
  }
}
