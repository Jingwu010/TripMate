import {Component} from '@angular/core';
import { MatSidenav } from '@angular/material';
import { ViewChild } from '@angular/core';

import {environment} from '../environments/environment';
import {SidenavService} from './core/sidenav/sidenav.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  @ViewChild('sidenav') public sidenav: MatSidenav;

  private APP_TITLE = environment.APP_TITLE;

  constructor(private sidenavService: SidenavService){
  }

  ngOnInit() {
    this.sidenavService.setSidenav(this.sidenav);
  }

  // shouldRun = [/(^|\.)plnkr\.co$/, /(^|\.)stackblitz\.io$/].some(h => h.test(window.location.host));
}


/**  Copyright 2019 Google Inc. All Rights Reserved.
    Use of this source code is governed by an MIT-style license that
    can be found in the LICENSE file at http://angular.io/license */