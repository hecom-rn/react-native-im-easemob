// NativeChat.ts
import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  init(props: { appKey?: string; options?: Object }): Promise<void>;
  login(props: {
    username: string;
    password: string;
    autoLogin: boolean;
  }): Promise<void>;
  logout(): Promise<void>;
  isLoggedIn(): Promise<void>;
}

export default TurboModuleRegistry.get<Spec>(
  'RTNChat',
) as Spec | null;
