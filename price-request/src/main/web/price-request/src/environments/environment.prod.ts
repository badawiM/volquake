import {websocketScheme} from "./environment.config";

export const environment = {
  production: true,
  useStubStompService: false,
  //websocketEndpoint: `${websocketScheme()}://${window.location.host}/blotter/stomp`,
  websocketEndpoint: `${websocketScheme()}:///blotter/stomp`,
  blotterAppPath: '/blotter-app'
};
