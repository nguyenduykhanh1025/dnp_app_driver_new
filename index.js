/**
 * @format
 */

import { AppRegistry, Platform } from 'react-native';
import App from './src/App';
import { name as appName } from './app.json';
import BackgroundTimer from 'react-native-background-timer';
import Geolocation from '@react-native-community/geolocation';

const bgTimer = BackgroundTimer.runBackgroundTimer(() => {
    // Geolocation.setRNConfiguration({
    //     authorizationLevel: 'always'
    // });

    // Geolocation.getCurrentPosition(
    //     info => {
    //         console.log('GPS location', JSON.stringify(info))
    //     },
    //     error => {
    //         console.log('Error', JSON.stringify(error))
    //     },
    //     Platform.OS === 'android' ? {}
    //         :
    //         {
    //             enableHighAccuracy: true,
    //             timeout: 200,
    //             distanceFilter: 0.5,
    //             useSignificantChanges: true,
    //         },
    // )
},
    1000);

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerHeadlessTask('bgTimer', () => bgTimer);
