import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-ml-camera-vision' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const MlCameraVision = NativeModules.MlCameraVision
  ? NativeModules.MlCameraVision
  : new Proxy(
    {},
    {
      get() {
        throw new Error(LINKING_ERROR);
      },
    }
  );

export function detectObject(url: string): Promise<any> {
  console.log('start js')
  return MlCameraVision.detectObject(url);
}
