import { websocketScheme } from "./environment.config";

export const environment = {
  production: false,
  useStubStompService: false,
  websocketEndpoint: ` ${websocketScheme()}://localhost:8081/stomp`
};

