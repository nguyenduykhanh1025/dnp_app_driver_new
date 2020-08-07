import React from 'react';
import {
    View,
    Alert,
    Platform,
    AppState,
    BackHandler,
    DeviceEventEmitter,
} from 'react-native';
import { connect } from 'react-redux';
import { compose } from 'redux';
import FlashMessage from 'react-native-flash-message';
import Geolocation from '@react-native-community/geolocation';

import { AppContainer } from './navigation/root-switch';
import NavigationService from './utils/navigation';
import { callApi } from '@/requests';
import {
    homeStack,
    mainStack,
    homeTab,
} from '@/config/navigator';
import { getGPSEnable, getToken, getAccount, getPassword } from '@/stores';

import { CheckInternetEvery } from '@/utils';
import BackgroundTimer from 'react-native-background-timer';
import PushNotification from 'react-native-push-notification';
import LocationServicesDialogBox from "react-native-android-location-services-dialog-box";

class AppAppContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isCheck: false,
            isConnected: true,
            appState: AppState.currentState,
            GPSEnable: false,
        };
        this.token = null;
    }

    onPushLocation = async (x, y) => {
        const params = {
            api: 'location',
            param: {
                x: x,
                y: y
            },
            token: this.token,
            method: 'POST'
        }
        var result = undefined;
        result = await callApi(params);
        console.log('resultonPushLocation', result)
    }

    componentDidMount = async () => {
        this.token = await getToken();
        var x = null;
        var y = null;
        var speed = null;
        CheckInternetEvery();
        var GPSEnable = await getGPSEnable();
        // console.log('GPSEnable', GPSEnable)
        Geolocation.setRNConfiguration({
            authorizationLevel: 'always'
        });

        LocationServicesDialogBox.checkLocationServicesIsEnabled({
            message: "<h2>Bạn đã bật GPS chưa ?</h2>Nếu chưa hãy bật GPS, Wi-Fi hoặc 3G,4G nhé!",
            ok: "Có",
            cancel: "Không",
            enableHighAccuracy: true, // true => GPS AND NETWORK PROVIDER, false => GPS OR NETWORK PROVIDER
            showDialog: true, // false => Opens the Location access page directly
            openLocationServices: true, // false => Directly catch method is called if location services are turned off
            preventOutSideTouch: false, //true => To prevent the location services popup from closing when it is clicked outside
            preventBackClick: false, //true => To prevent the location services popup from closing when it is clicked back button
            providerListener: true // true ==> Trigger "locationProviderStatusChange" listener when the location state changes
        }).then(function (success) {
            // success => {alreadyEnabled: true, enabled: true, status: "enabled"} 
        }.bind(this)
        ).catch((error) => {
        });

        DeviceEventEmitter.addListener('locationProviderStatusChange', status => { // only trigger when "providerListener" is enabled
            status.enabled ?
                null
                :
                LocationServicesDialogBox.checkLocationServicesIsEnabled({
                    message: "<h2>Bạn đã bật GPS chưa ?</h2>Nếu chưa hãy bật GPS, Wi-Fi hoặc 3G,4G nhé!",
                    ok: "Có",
                    cancel: "Không",
                    enableHighAccuracy: true, // true => GPS AND NETWORK PROVIDER, false => GPS OR NETWORK PROVIDER
                    showDialog: true, // false => Opens the Location access page directly
                    openLocationServices: true, // false => Directly catch method is called if location services are turned off
                    preventOutSideTouch: false, //true => To prevent the location services popup from closing when it is clicked outside
                    preventBackClick: false, //true => To prevent the location services popup from closing when it is clicked back button
                    providerListener: true // true ==> Trigger "locationProviderStatusChange" listener when the location state changes
                }).then(function (success) {
                    // success => {alreadyEnabled: true, enabled: true, status: "enabled"} 
                }.bind(this)
                ).catch((error) => {
                });

        });
        if (GPSEnable == 'true') {
            Geolocation.getCurrentPosition(
                info => {
                    // Alert.alert('GPS location', JSON.stringify(info))
                    var GPS = info.coords;
                    // console.log('GPS', GPS)
                    x = GPS.latitude;
                    y = GPS.longitude;
                    speed = GPS.speed;
                    this.token != null ?
                        this.onPushLocation(GPS.latitude, GPS.longitude)
                        :
                        null
                },
                error => {
                    // Alert.alert('Error', JSON.stringify(error))
                },
                Platform.OS === 'android' ? {}
                    :
                    {
                        enableHighAccuracy: true,
                        timeout: 200,
                        distanceFilter: 0.5,
                        useSignificantChanges: true,
                    },
            )

            BackgroundTimer.runBackgroundTimer(() => {
                this.token != null ?
                    this.onPushLocation(x, y)
                    :
                    null
            },
                120000);
        }
    }

    componentWillMount() {
        PushNotification.popInitialNotification(notification => {
            // console.log('Initial notification: ', notification);
        });
        LocationServicesDialogBox.stopListener()
    }

    render() {
        return (
            <View style={{ flex: 1 }}>
                <AppContainer
                    ref={navigatorRef => {
                        NavigationService.setTopLevelNavigator(navigatorRef);
                    }}
                />
                <FlashMessage position="top" />
            </View>
        );
    }
}

const mapStateToProps = state => {
    return {

    };
};

export default compose(
    connect(mapStateToProps),
)(AppAppContainer);