import React from 'react';
import { View, Alert, Platform, AppState } from 'react-native';
import { connect } from 'react-redux';
import { compose } from 'redux';
import FlashMessage from 'react-native-flash-message';
import Geolocation from '@react-native-community/geolocation';

import { AppContainer } from './navigation/root-switch';
import NavigationService from './utils/navigation';

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
        };
    }

    componentDidMount = () => {
        // BackgroundTimer.runBackgroundTimer(() => {
        //     console.log('ádasdasdasdasdasd')
        // },
        //     3000);
        CheckInternetEvery();
        Geolocation.setRNConfiguration({
            authorizationLevel: 'always'
        });

        Geolocation.getCurrentPosition(
            info => {
                Alert.alert('GPS location', JSON.stringify(info))
            },
            error => {
                Alert.alert('Error', JSON.stringify(error))
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