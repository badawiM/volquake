import {Component, OnInit} from '@angular/core';
import {BidAskPrice, PriceService} from "./price-service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'price-request';

  prices$: Observable<BidAskPrice>

  constructor(private priceService: PriceService) { }


  ngOnInit(): void {
    console.log("Subscribing to stream")
    this.prices$ = this.priceService.subscribeToPriceStream("foo")
    console.log("Subscribed to stream")
  }
}
