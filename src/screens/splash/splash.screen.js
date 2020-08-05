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
} from '@/stores';

const splash_bg = require('@/assets/images/splash-bg.png');
const logo = require('@/assets/images/logo.png');

class ProfileScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    };

    componentDidMount = async () => {
        var token = await getToken();
        if (token == null) {
            NavigationService.navigate(authStack.login, {})
        }
        else {
            setTimeout(() => {
                NavigationService.navigate(homeTab.home, {})
            }, 1500);

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