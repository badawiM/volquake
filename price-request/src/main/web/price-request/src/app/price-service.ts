import { Inject, Injectable } from "@angular/core";
import { RX_STOMP_SERVICE_TOKEN } from "./rx-stomp-service.provider";
import { RxStompService } from "@stomp/ng2-stompjs";
import { Observable } from "rxjs";
import {filter, map, tap} from "rxjs/operators";

@Injectable()
export class PriceService{

  constructor(@Inject(RX_STOMP_SERVICE_TOKEN) private stompService: RxStompService) { }

  subscribeToPriceStream(subscriptionId: string): Observable<BidAskPrice>{
    //const topic = PriceStompTopics.PriceStream(subscriptionId)
    const topic = '/topic/pricestream'
    return this.stompService.watch(topic).pipe(
      filter(message => message != null),
      tap( message => console.log(`Received ${JSON.parse(message.body)}`)),
      map( message => JSON.parse(message.body) as BidAskPrice)
    )

  }

}

export const PRICING_STOMP_PREFIX = '/topic/pricestream';

export const PriceStompTopics = {
  PriceStream: (subscriptionId: string) => `${PRICING_STOMP_PREFIX}${subscriptionId}`
};

export interface BidAskPrice{
  underlying: string,
  bid: number,
  offer: number,
  priceDateTime: Date
}
