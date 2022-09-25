import {Component, Input, OnInit} from '@angular/core';
import {BidAskPrice} from "../price-service";


@Component({
  selector: 'price',
  templateUrl: './price.component.html',
  styleUrls: ['./price.component.scss']
})
export class PriceComponent implements OnInit{
  title = 'price-tile';

  @Input() price: BidAskPrice

  ngOnInit(): void {

  }

}
