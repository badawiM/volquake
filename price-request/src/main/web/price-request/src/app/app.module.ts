import { environment } from "../environments/environment";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from "@angular/common/http";
import { ENV_CONFIG_TOKEN } from "../environments/environment.config";
import {
  injectableRxStompConfig,
  RX_STOMP_CONFIG_TOKEN,
  stompServiceProvider,
} from "./rx-stomp-service.provider";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: ENV_CONFIG_TOKEN,
      useValue: environment,
      multi: true
    },
    {
      provide: RX_STOMP_CONFIG_TOKEN,
      useValue: injectableRxStompConfig,
      deps: [ENV_CONFIG_TOKEN]
    },
    stompServiceProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
