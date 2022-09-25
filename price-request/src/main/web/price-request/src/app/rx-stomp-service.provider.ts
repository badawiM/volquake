import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from "@stomp/ng2-stompjs";
import {environment} from "../environments/environment";
import {InjectionToken, Provider} from "@angular/core";
import {ENV_CONFIG_TOKEN, EnvironmentConfig} from "../environments/environment.config";

export const RX_STOMP_CONFIG_TOKEN = new InjectionToken<InjectableRxStompConfig>("BlotterStompConfig");

export const RX_STOMP_SERVICE_TOKEN = new InjectionToken<RxStompService>("BlotterRxStompService");


export const injectableRxStompConfig: InjectableRxStompConfig = {
  brokerURL: environment.websocketEndpoint,
  reconnectDelay: 1000,
  splitLargeFrames: true,
  debug(msg: string): void{
    console.debug(new Date() ,msg)
  }
}


export function blotterRxStompServiceFactory(stompConfig: InjectableRxStompConfig, envConfig: EnvironmentConfig){
    return rxStompServiceFactory(stompConfig);
}

export const stompServiceProvider: Provider = {
  provide: RX_STOMP_SERVICE_TOKEN,
  useFactory: blotterRxStompServiceFactory,
  deps: [RX_STOMP_CONFIG_TOKEN,ENV_CONFIG_TOKEN ]
}
