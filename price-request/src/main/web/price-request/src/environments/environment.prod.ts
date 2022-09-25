import {websocketScheme} from "./environment.config";

export const environment = {
  production: true,
  useStubStompService: false,
  websocketEndpoint: `${websocketScheme()}:///stomp`
};
