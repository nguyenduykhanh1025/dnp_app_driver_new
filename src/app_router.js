import React, {useEffect} from 'react';
import { View, Alert, Platform, AppState } from 'react-native';
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
import firebase from 'react-native-firebase';


// Must be outside of any component LifeCycle (such as `componentDidMount`).
PushNotification.configure({
    // (optional) Called when Token is generated (iOS and Android)
    onRegister: function (token) {
      console.log("TOKEN:", token);
    },
  
    // (required) Called when a remote is received or opened, or local notification is opened
    onNotification: function (notification) {
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
    onRegistrationError: function(err) {
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
            //console.log(notification)
            //this.showAlert(title, body);
        });
      
        this.notificationOpenedListener = firebase.notifications().onNotificationOpened((notificationOpen) => {
            const { title, body } = notificationOpen.notification;
            //console.log(notificationOpen.notification)
            NavigationService.navigate(homeTab.notification)
        });
      
        const notificationOpen = await firebase.notifications().getInitialNotification();
        if (notificationOpen) {
            const { title, body } = notificationOpen.notification;
            //this.showAlert(title, body);
        }
      
        this.messageListener = firebase.messaging().onMessage((message) => {
          //console.log(JSON.stringify(message));
        });
    }

    showAlert = (title, message) => {
        Alert.alert(
          title,
          message,
          [
            {text: 'OK', onPress: () => console.log('OK Pressed')},
          ],
          {cancelable: false},
        );
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
        this.checkPermission();
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