import React from 'react';
import { View, Alert, Platform, AppState } from 'react-native';
import { connect } from 'react-redux';
import { compose } from 'redux';
import FlashMessage from 'react-native-flash-message';
import Geolocation from '@react-native-community/geolocation';

import { AppContainer } from './navigation/root-switch';
import NavigationService from './utils/navigation';
import { callApi } from '@/requests';
import { getGPSEnable, getToken } from '@/stores';

import { CheckInternetEvery } from '@/utils';
import BackgroundTimer from 'react-native-background-timer';
import PushNotification from 'react-native-push-notification';

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
        if (!result.code == 0) {
        }
        else {
            console.log('erronPushLocation', result.msg)
        }
    }

    componentDidMount = async () => {
        this.token = await getToken();
        var x = null;
        var y = null;
        var speed = null;
        CheckInternetEvery();
        var GPSEnable = await getGPSEnable();
        console.log('GPSEnable', GPSEnable)
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
                    this.onPushLocation(GPS.latitude, GPS.longitude)
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
                console.log('x', x)
                console.log('y', y)
                this.onPushLocation(x, y);
            },
                120000);
        }
    }


    componentWillMount() {
        PushNotification.popInitialNotification(notification => {
            console.log('Initial notification: ', notification);
        });
    }

    componentWillUnmount() {
        // AppState.removeEventListener('change', this._handleAppStateChange);
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