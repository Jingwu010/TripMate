import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule, 
  MatButtonModule, 
  MatSidenavModule, 
  MatIconModule, 
  MatListModule,
  MatCardModule,
  MatAutocompleteModule,
  MatInputModule,
  MatFormFieldModule,
  MatCheckboxModule,
  MatPaginatorModule,
  MatSnackBarModule,
  MatStepperModule } from '@angular/material';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

// material  section
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AgmCoreModule } from '@agm/core';
import { AgmDirectionModule } from 'agm-direction';   // agm-direction

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './core/header/header.component';
import { FooterComponent } from './core/footer/footer.component';
import { HttpComponent } from './core/http/http.component';
import { PageComponent } from './core/page/page.component';
import { SidenavComponent } from './core/sidenav/sidenav.component';
import { MatTableModule } from '@angular/material'  

import { SidenavService } from './core/sidenav/sidenav.service';
import { InitComponent } from './core/init/init.component';
import { StepOneComponent } from './core/step-one/step-one.component';
import { StepTwoComponent } from './core/step-two/step-two.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HttpComponent,
    PageComponent,
    SidenavComponent,
    InitComponent,
    StepOneComponent,
    StepTwoComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatCheckboxModule,
    MatPaginatorModule,
    MatSnackBarModule,
    MatStepperModule,
    AgmCoreModule.forRoot({
      apiKey: ''
    }),
    AgmDirectionModule
  ],
  providers: [SidenavService],
  bootstrap: [AppComponent]
})
export class AppModule { }
