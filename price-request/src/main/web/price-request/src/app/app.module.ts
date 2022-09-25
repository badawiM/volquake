import { environment } from "../environments/environment";
import { BrowserModule } from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from "@angular/common/http";
import { ENV_CONFIG_TOKEN } from "../environments/environment.config";
import {
  injectableRxStompConfig,
  RX_STOMP_CONFIG_TOKEN,
  stompServiceProvider,
} from "./rx-stomp-service.provider";
import {PriceComponent} from "./price-component/price.component";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {PriceService} from "./price-service";


@NgModule({
  declarations: [
    AppComponent,
    PriceComponent
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
    stompServiceProvider,
    PriceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

platformBrowserDynamic().bootstrapModule(AppModule, {
  providers: [{provide: LOCALE_ID, useValue: 'en-GB' }]
});
