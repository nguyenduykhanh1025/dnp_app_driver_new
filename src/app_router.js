import React, { useEffect } from 'react';
import {
    View,
    Alert,
    Platform,
    AppState,
    BackHandler,
    StatusBar,
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
import { getGPSEnable, getToken } from '@/stores';

import { CheckInternetEvery } from '@/utils';
import BackgroundTimer from 'react-native-background-timer';
import PushNotification from 'react-native-push-notification';
import firebase from 'react-native-firebase';
import LocationServicesDialogBox from "react-native-android-location-services-dialog-box";
import * as Actions from '@/modules/notifications/constants';
import { useDispatch } from 'react-redux';
import { setNoti } from '@/modules/notifications/action'


class AppAppContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isCheck: false,
            isConnected: true,
            appState: AppState.currentState,
            GPSEnable: false,
            timerLocation: 10000,
        };
        this.token = null;
    }


    checkPermission = async () => {
        const enabled = await firebase.messaging().hasPermission();
        if (enabled) {
            this.messageListener();
        } else {
            this.requestPermission();
        }
    }

    requestPermission = async () => {
        try {
            await firebase.messaging().requestPermission();
            // User has authorised
        } catch (error) {
            // User has rejected permissions
        }
    }

    messageListener = async () => {
        this.notificationListener = firebase.notifications().onNotification((notification) => {
            const { title, body } = notification;
        });

        this.notificationOpenedListener = firebase.notifications().onNotificationOpened((notificationOpen) => {
            const { title, body } = notificationOpen.notification;
            NavigationService.navigate(homeTab.notification)
        });

        const notificationOpen = await firebase.notifications().getInitialNotification();
        if (notificationOpen) {
            const { title, body } = notificationOpen.notification;
            NavigationService.navigate(homeTab.notification)
        }

        this.messageListener = firebase.messaging().onMessage((message) => {
        });
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
        console.log('App-router.resultonPushLocation', result)
    }

    getTimerLocation = async () => {
        const params = {
            api: 'connection/info',
            param: '',
            token: this.token,
            method: 'GET'
        }
        var result = undefined;
        result = await callApi(params);
        if (result.code == 0) {
            this.setState({
                timerLocation: Number(result.locationUpdatePeriod),
            })
        } else {
            this.setState({
                timerLocation: 30000,
            })
        }
    }

    componentDidMount = async () => {

        this.checkPermission();
        // Must be outside of any component LifeCycle (such as `componentDidMount`).
        PushNotification.configure({
            // (optional) Called when Token is generated (iOS and Android)
            onRegister: function (token) { },

            // (required) Called when a remote is received or opened, or local notification is opened
            onNotification: (notification) => {
                //this.props.dispatch(setNoti(5))
                console.log("NOTIFICATION:", notification);
                NavigationService.navigate(homeTab.notification)

                // process the notification

                // (required) Called when a remote is received or opened, or local notification is opened
                //notification.finish(PushNotificationIOS.FetchResult.NoData);
            },

            // (optional) Called when Registered Action is pressed and invokeApp is false, if true onNotification will be called (Android)
            onAction: function (notification) {
                console.log("ACTION:", notification.action);
                console.log("NOTIFICATION:", notification);
                // process the action
            },

            // (optional) Called when the user fails to register for remote notifications. Typically occurs when APNS is having issues, or the device is a simulator. (iOS)
            onRegistrationError: function (err) {
                console.error(err.message, err);
            },

            // IOS ONLY (optional): default: all - Permissions to register.
            permissions: {
                alert: true,
                badge: true,
                sound: true,
            },

            // Should the initial notification be popped automatically
            // default: true
            popInitialNotification: true,

            /**
             * (optional) default: true
             * - Specified if permissions (ios) and token (android and ios) will requested or not,
             * - if not, you must call PushNotificationsHandler.requestPermissions() later
             * - if you are not using remote notification or do not have Firebase installed, use this:
             *     requestPermissions: Platform.OS === 'ios'
             */
            requestPermissions: true,
        });
        this.token = await getToken();
        var x = null;
        var y = null;
        var speed = null;
        CheckInternetEvery();
        // var GPSEnable = await getGPSEnable();
        // console.log('GPSEnable', GPSEnable)

        // get time send location
        { this.token != null ? this.getTimerLocation() : null }

        /*---------------------------------------------*/

        Geolocation.setRNConfiguration({
            authorizationLevel: 'always'
        });

        LocationServicesDialogBox.checkLocationServicesIsEnabled({
            message: "<h2>Tính năng chia sẻ vị trí của thiết bị bạn đã tắt !</h2>Vui lòng bật chia sẻ vị trí, Wi-Fi hoặc 3G,4G!",
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
        // if (GPSEnable == 'true') {
        Geolocation.getCurrentPosition(
            info => {
                var GPS = info.coords;
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
                Geolocation.getCurrentPosition(
                    info => {
                        this.onPushLocation(x, y)
                    },
                    error => {
                        console.log('location chưa được bật')
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
                :
                null
        }, this.state.timerLocation);
        // }
    }

    componentWillMount() {
        PushNotification.popInitialNotification(notification => {
            console.log('Initial notification: ', notification);

        });
        LocationServicesDialogBox.stopListener()
    }

    render() {
        return (
            <View style={{ flex: 1 }}>
                <StatusBar barStyle="dark-content" />
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

