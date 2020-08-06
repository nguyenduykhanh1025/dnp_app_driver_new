import React, { Component } from 'react'
import {
    Text,
    View,
    StyleSheet,
    Image,
    ActivityIndicator,
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

const splash_bg = require('@/assets/images/splash-bg.png');
const logo = require('@/assets/images/logo.png');

class ProfileScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
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
            NavigationService.navigate(homeTab.home, {})
        }
        else {
            NavigationService.navigate(authStack.login, {})
        }
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
            <View style={styles.container}>
                <Image
                    source={splash_bg}
                    style={styles.bg}
                    resizeMode={'stretch'}
                />
                <View style={{ position: 'absolute' }}>
                    <ActivityIndicator size="large" color={Colors.white} />
                </View>
            </View>
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
})