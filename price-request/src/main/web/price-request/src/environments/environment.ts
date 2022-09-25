import { websocketScheme } from "./environment.config";

export const environment = {
  production: false,
  useStubStompService: false,
  websocketEndpoint: ` ${websocketScheme()}://localhost:8080/blotter/stomp`,
  //websocketEndpoint: `ws://localhost:8080/blotter/stomp`,
  blotterAppPath: '/blotter-app'
};

