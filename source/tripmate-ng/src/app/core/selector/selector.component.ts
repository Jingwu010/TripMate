import { Component, OnInit, ViewChild } from '@angular/core';

import {SelectionModel} from '@angular/cdk/collections';
import {MatTableDataSource, 
  MatPaginator,
  MatSnackBar} from '@angular/material';


import {Locations} from '../page/mock-list';
import {Location} from '../page/location';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrls: ['./selector.component.css']
})
export class SelectorComponent implements OnInit {
  data = Locations;
  displayedColumns: string[] = ['select', 'name', 'city', 'state'];
  dataSource = new MatTableDataSource<Location>(this.data);
  selection = new SelectionModel<Location>(true, []);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.dataSource.paginator = this.paginator;
  }


  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }

  doFilter(val) {
    this.dataSource.filter = val.trim().toLocaleLowerCase();
  }

  selectRow(row) {
    this.selection.toggle(row);
    // console.log(row.name);
    let message: string = row.name + " has been " + (this.selection.isSelected(row) ? " added!" : " removed");
    this.snackBar.open(message, "Close", {
      duration: 2000,
    });
    // console.log(this.selection.isSelected(row));
    // console.log(row);/
    // console.log(typeof row);
    
  }
}
