import { Inject, Injectable } from "@angular/core";
import { RX_STOMP_SERVICE_TOKEN } from "./rx-stomp-service.provider";
import { RxStompService } from "@stomp/ng2-stompjs";
import { Observable } from "rxjs";
import {map, tap} from "rxjs/operators";

@Injectable()
export class PriceService{

  constructor(@Inject(RX_STOMP_SERVICE_TOKEN) private stompService: RxStompService) { }

  subscribeToPriceStream(subscriptionId: string): Observable<BidAskPrice>{
    const topic = PriceStompTopics.PriceStream(subscriptionId)
    return this.stompService.watch(topic).pipe(
      tap( message => console.log(`Received ${JSON.parse(message.body)}`)),
      map( message => JSON.parse(`{ 'underlying": 'foo', 'bid':0.99,'offer':0.99 }`) as BidAskPrice)
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
