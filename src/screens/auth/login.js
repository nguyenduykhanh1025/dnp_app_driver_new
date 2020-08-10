import React, { PureComponent } from 'react';
import Icon from 'react-native-vector-icons/FontAwesome';
import {
    View,
    StyleSheet,
    Text,
    Button,
    Dimensions,
    Platform,
    KeyboardAvoidingView,
    TouchableOpacity,
    Image,
    ImageBackground,
    AsyncStorage,
    ScrollView,
    Keyboard,
    TextInput,
    Alert
} from 'react-native';
import { connect } from 'react-redux';
import Toast from 'react-native-tiny-toast'
import firebase from 'react-native-firebase';
import {
    Input,
    FlashMessage,
    LoadingBase
} from '@/components';
import {
    commonStyles,
    Colors,
    Fonts,
    colorOpacityMaker,
    fontSizeValue as fs,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    sizeHeight,
} from '@/commons';
import {
    login,
    loginSupplier
} from '@/modules/auth/action';
import {
    Checkbox
} from '@/components/checkbox';
import { screen } from '@/utils';
import {
    getAccount,
    getPassword,
    saveAccount,
    savePassword,
    saveToken
} from '@/stores';
const STORAGE_TOKEN = 'storage_token';
import {
    mainStack
} from '@/config/navigator';
import {
    user_icon,
    password_icon,
    phone_icon
} from '@/assets/icons/index';
import AuthInput from '@/components/auth/AuthInput';
import AuthButton from '@/components/auth/AuthButton';
import {
    authStack
} from '@/config/navigator';
import NavigationService from '@/utils/navigation';
import {
    callApi
} from '@/requests';
import {
    FaceDetector
} from 'react-native-camera';
import Geolocation from '@react-native-community/geolocation';

const ibg = require('@/assets/images/auth_bg.png');
const hicon = require('@/assets/images/logo.png');

class LoginContainer extends PureComponent {
    constructor(props) {
        super(props);
        this.state = {
            // loginname: this.props.loginname != undefined || this.props.loginname != null ? this.props.loginname : '',
            // pwd: this.props.pwd != undefined || this.props.pwd != null ? this.props.pwd : '',
            loginname: '0935802290',
            pwd: '123456',
            showpassword: 1,
            onFocusu: false,
            onFocusp: false,
            supplier: 0,
            holdLoginStatus: 0,
        };
        this.token = null;
    }

    componentDidMount = async () => {
        const account = await getAccount();
        const pwd = await getPassword();
    }

    _saveIP = async (a) => {
        try {
            await AsyncStorage.setItem(
                'IP_SAVE',
                a.toString()
            );
        } catch (error) {
            // console.log('error', error)
        }
    }


    _onCheckUser = () => {
        const a = (this.state.supplier + 1) % 2
        this._saveIP(a)
        this.setState({
            supplier: (this.state.supplier + 1) % 2
        })
    }


    onCheckLogin = () => {
        const { loginname, pwd } = this.state;
        if (loginname != '' && pwd != '') {
            return 1;
        }
        return 1;
    }

    onLogin = async () => {
        const fcmToken = await firebase.messaging().getToken();
        console.log('token', fcmToken)
        Toast.showLoading()
        const { loginname, pwd } = this.state;
        const params = {
            api: 'login',
            param: {
                userName: loginname,
                passWord: pwd,
                deviceToken: fcmToken,
            },
            token: '',
            method: 'POST'
        }
        var result = undefined;
        result = await callApi(params);
        if (result.code == 0) {
            Toast.hide()
            this.token = result.token;
            this.onShareLocation()
            saveAccount(loginname);
            savePassword(pwd);
            saveToken(result.token)
            FlashMessage('Đăng nhập thành công', 'success')
            NavigationService.navigate(mainStack.home_tab)
        }
        else {
            Toast.hide()
            Alert.alert('Thông báo!', result.msg)
        }
    }

    onHoldLoginStatus = () => {
        this.setState({ holdLoginStatus: (this.state.holdLoginStatus + 1) % 2 })
    }

    onShareLocation = async () => {
        Geolocation.setRNConfiguration({
            authorizationLevel: 'always'
        });
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
        console.log('Login.resultonPushLocation', result)
    }

    render() {
        const { isLoading } = this.props;

        return (
            <KeyboardAvoidingView
                behavior={Platform.OS == 'ios' ? 'padding' : null}
                style={styles.container}
            >
                <View style={styles.ImageContainer}>
                    <Image source={ibg} style={styles.image} />
                    <View style={styles.Frame0}>
                        <View style={styles.HeaderContainer}>
                            <View style={styles.HeaderText}>
                                <Text style={styles.HeaderTextUp}>Ứng dụng tài xế</Text>
                                <Text style={styles.HeaderTextDown}>CẢNG ĐÀ NẴNG</Text>
                            </View>
                            <View style={styles.HeaderIcon}>
                                <Image resizeMode = 'contain' source={hicon} style={styles.HeaderIconImage} />
                            </View>
                        </View>
                    </View>
                </View>
                <View style={styles.Frame1}>
                    <View style={styles.Frame2}>
                        <View style={styles.Frame3}>
                            <View style={styles.titleContainer}>
                                <Text style={styles.title}>Đăng Nhập</Text>
                            </View>
                            <AuthInput
                                title='Số điện thoại'
                                placeholder={this.state.onFocusu ? '' : 'Nhập số điện thoại ...'}
                                icon={phone_icon}
                                value={this.state.loginname}
                                onFocus={() => this.setState({ onFocusu: true })}
                                onBlur={() => this.setState({ onFocusu: false })}
                                onChangeText={text => this.setState({ loginname: text })}
                                returnKeyType='next'
                                inputRef={ref => (this.phoneRef = ref)}
                                onSubmitEditing={() =>
                                    !!this.passwordRef && this.passwordRef.focus()
                                }
                                keyboardType='numeric'
                                focusValue={this.state.onFocusu}
                            />
                            <AuthInput
                                title='Mật khẩu'
                                placeholder={this.state.onFocusp ? '' : 'Nhập mật khẩu ...'}
                                icon={password_icon}
                                value={this.state.pwd}
                                onFocus={() => this.setState({ onFocusp: true })}
                                onBlur={() => this.setState({ onFocusp: false })}
                                onChangeText={text => this.setState({ pwd: text })}
                                returnKeyType='done'
                                inputRef={ref => (this.passwordRef = ref)}
                                onSubmitEditing={() => {
                                    Keyboard.dismiss();
                                    this.onLogin();
                                }}
                                secureTextEntry={true}
                                showPassword={true}
                                focusValue={this.state.onFocusp}
                            />
                            <View style={{ marginTop: hs(28) }}>
                                <AuthButton
                                    onPress={() => this.onLogin(this.state.loginname, this.state.pwd)}
                                    title='Đăng nhập'
                                />
                            </View>
                            <View style={styles.noteContainer}>
                                <View style={{ flexDirection: 'row' }}>
                                    <Text style={styles.text}>Chưa có tài khoản? </Text>
                                    <TouchableOpacity
                                        style={styles.noteButton}
                                        onPress={() => this.props.navigation.navigate(authStack.signup)}
                                    >
                                        <Text style={styles.noteButtonText}>Đăng ký ngay</Text>
                                    </TouchableOpacity>
                                </View>
                                <View style={{
                                    width: ws(200),
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    marginTop: hs(5),
                                }}>
                                    <TouchableOpacity
                                        onPress={() => this.props.navigation.navigate(authStack.signup)}
                                    >
                                        <Text style={[styles.noteButtonText, {
                                            borderBottomWidth: 1,
                                            borderBottomColor: Colors.subColor,
                                            width: ws(100),
                                            textAlign: 'center',
                                            color: Colors.subColor,
                                        }]}>Quên mật khẩu?</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </View>
                </View>
            </KeyboardAvoidingView>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.white,
    },
    ImageContainer: {

    },
    image: {
        width: ws(375),
        height: hs(424),
        position: 'absolute',
    },
    HeaderContainer: {
        flexDirection: 'row',
        marginHorizontal: ws(25),
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingTop: hs(58),
    },
    HeaderText: {

    },
    HeaderIcon: {

    },
    HeaderTextUp: {
        fontFamily: null,
        color: Colors.white,
        fontSize: fs(18),
        fontWeight: 'bold'
    },
    HeaderTextDown: {
        fontFamily: null,
        color: Colors.white,
        fontSize: fs(25),
        fontWeight: 'bold',
    },
    HeaderIconImage: {
        width: ws(75),
        height: hs(46),
    },
    Frame0: {
        width: ws(375),
    },
    Frame1: {
        flex: 1,
        justifyContent: 'flex-end'
    },
    Frame2: {
        width: ws(375),
        height: hs(441),
        backgroundColor: Colors.white,
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
        flexDirection: 'column',
        alignItems: 'center',
    },
    Frame3: {
        width: ws(325),
        height: hs(441),
    },
    title: {
        color: Colors.maincolor,
        fontSize: fs(22),
        lineHeight: 26,
        fontWeight: 'bold',
    },
    titleContainer: {
        marginTop: hs(35),
        marginLeft: ws(8),
    },
    text: {
        color: Colors.tinyTextGrey,
        fontSize: fs(15),
        lineHeight: 18,
    },
    noteButtonText: {
        color: Colors.maincolor,
        fontSize: fs(15),
        lineHeight: 18,
    },
    noteButton: {
        borderBottomWidth: 1,
        borderBottomColor: Colors.maincolor,
    },
    noteContainer: {
        flexDirection: 'column',
        alignSelf: 'center',
        marginVertical: sizeHeight(5),
    },
})

const mapStateToProps = (state) => {
    return {
        loginname: state.loginReducers.toJS().loginname,
        pwd: state.loginReducers.toJS().pwd,
        userID: state.loginReducers.toJS().userID,
        isLoading: state.loginReducers.toJS().isLoading,
    };
};

export default connect(mapStateToProps)(LoginContainer);