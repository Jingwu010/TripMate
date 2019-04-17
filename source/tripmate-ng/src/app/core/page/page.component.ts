import { Component, OnDestroy } from '@angular/core';
import { Subscription }   from 'rxjs';

import { SelectionService } from 'src/app/shared/selection.service';
import { Location } from 'src/app/shared/location';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css']
})

export class PageComponent implements OnDestroy {
  private locations: Location[];
  private subscription: Subscription;

  private zoom: number = 13;
  private lat: number = 47.607060;
  private lng: number = -122.333154;

  private lines: route[] = [];

  constructor(private selectionService: SelectionService) {
    this.subscription = selectionService.selectedLocations$.subscribe(
      selectedLocations => {
        console.log('new data comes in');
        this.locations = selectedLocations;
      }
    );
  }

  clickedMarker(label: string, index: number) {
    console.log(`clicked the marker: ${label || index}`)
  }

  ngOnDestroy() {
    // prevent memory leak when component destroyed
    this.subscription.unsubscribe();
  }
}

interface route {
  lat: number;
  lng: number;
}