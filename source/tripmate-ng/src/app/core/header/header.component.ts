import { Component, Input } from '@angular/core';
import { SidenavService } from '../sidenav/sidenav.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  @Input() APP_TITLE: "";

  constructor(private sidenavService: SidenavService) {}

  ngOnInit() {
  }

  toggleRightSidenav() {
    this.sidenavService.toggle();
  }
}
