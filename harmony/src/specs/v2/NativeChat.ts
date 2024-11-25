// NativeChat.ts
import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  add(a: number, b: number): Promise<number>;
  init(props: { appKey?: string; options?: Object }): Promise<Object>;
}

export default TurboModuleRegistry.get<Spec>(
  'RTNChat',
) as Spec | null;
