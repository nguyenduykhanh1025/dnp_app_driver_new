import React, { Component } from 'react'
import {
    Text,
    View,
    StyleSheet,
    Image,
    ActivityIndicator,
    ImageBackground,
} from 'react-native';
import NavigationService from '@/utils/navigation';
import {
    mainStack,
    homeTab,
    authStack,
} from '@/config/navigator';
import {
    Colors,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';
import { connect } from 'react-redux';
import {
    getToken,
    getAccount,
    getPassword,
    saveToken,
} from '@/stores';
import {
    callApi
} from '@/requests';
import Geolocation from '@react-native-community/geolocation';

const splash_bg = require('@/assets/images/splash-bg.png');
const logo = require('@/assets/images/logo.png');

class ProfileScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
        this.token = null;
    };

    onLogin = async (loginname, pwd) => {
        const params = {
            api: 'login',
            param: {
                userName: loginname,
                passWord: pwd,
                deviceToken: 'sss'
            },
            token: '',
            method: 'POST'
        }
        var result = undefined;
        result = await callApi(params);
        console.log('autologin', result)
        if (result.code == 0) {
            saveToken(result.token)
            this.token = result.token;
            this.onShareLocation()
            NavigationService.navigate(homeTab.home, {})
        }
        else {
            NavigationService.navigate(authStack.login, {})
        }
    }

    onShareLocation = async () => {
        Geolocation.setRNConfiguration({
            authorizationLevel: 'always'
        });
        Geolocation.getCurrentPosition(
            info => {
                // Alert.alert('GPS location', JSON.stringify(info))
                var GPS = info.coords;
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
        console.log('splash.resultonPushLocation', result)
    }

    componentDidMount = async () => {
        var token = await getToken();
        var account = await getAccount();
        var password = await getPassword();
        if (token == null) {
            NavigationService.navigate(authStack.login, {})
        }
        else {
            this.onLogin(account, password)
        }
    };

    render() {
        return (
            <ImageBackground
                style={styles.container}
                source={splash_bg}
            >
                <View style={{ position: 'absolute' }}>
                    <ActivityIndicator size="large" color={Colors.white} />
                </View>
            </ImageBackground>
        )
    }
}

const mapStateToProps = (state) => {
    return {
    };
};

export default connect(mapStateToProps)(ProfileScreen);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.white,
        alignItems: 'center',
        justifyContent: 'center',
    },
    bg: {
        width: ws(375),
        height: hs(812),
    }
})