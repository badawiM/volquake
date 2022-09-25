import {InjectionToken} from "@angular/core";

export interface EnvironmentConfig {
  production: boolean,
  webSocketEndpoint: string,
  useStubStompService: boolean,
  blotterAppPath: string
}

export type Environment = EnvironmentConfig

export const ENV_CONFIG_TOKEN = new InjectionToken<EnvironmentConfig>('EnvironmentConfig');

export function websocketScheme(): string{
  //our maven builds create prod builds that we still run locally
  //So, if we're running against localhost, use ws other use wss
  console.log(`window.location.hostname is ${window.location.hostname}`)
  return (window.location.hostname === 'localhost' ? 'ws' : 'wss');
}
