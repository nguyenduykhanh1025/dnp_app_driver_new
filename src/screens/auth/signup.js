import React, { Component } from 'react';
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
import { sizeHeight, sizeWidth } from '@/commons/Spanding';
import {
    Colors,
    fontSizeValue as fs,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
} from '@/commons';
import {
    callApi
} from '@/requests';
import AuthInput from '@/components/auth/AuthInput';
import AuthButton from '@/components/auth/AuthButton';
import {
    user_icon,
    password_icon,
    phone_icon
} from '@/assets/icons/index';
import {
    authStack
} from '@/config/navigator';

const ibg = require('@/assets/images/auth_bg.png');
const hicon = require('@/assets/images/logo.png');

export default class SignUpScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            password: '',
            password2: '',
            phone: '',
            nameFocused: false,
            passwordFocused: false,
            password2Focused: false,
            phoneFocused: false,
        }
    }

    render() {
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
                                <Image source={hicon} style={styles.HeaderIconImage} />
                            </View>
                        </View>
                    </View>
                </View>
                <View style={styles.Frame1}>
                    <View style={styles.Frame2}>
                        <View style={styles.Frame3}>
                            <View style={styles.titleContainer}>
                                <Text style={styles.title}>Đăng ký</Text>
                            </View>
                            <AuthInput
                                title='Họ và tên'
                                placeholder={this.state.nameFocused ? '' : 'Nhập họ và tên ...'}
                                icon={user_icon}
                                value={this.state.name}
                                onFocus={() => this.setState({ nameFocused: true })}
                                onBlur={() => this.setState({ nameFocused: false })}
                                onChangeText={text => this.setState({ name: text })}
                                returnKeyType='next'
                                inputRef={ref => (this.phoneRef = ref)}
                                onSubmitEditing={() =>
                                    !!this.passwordRef && this.passwordRef.focus()
                                }
                                keyboardType='numeric'
                                focusValue={this.state.nameFocused}
                            />
                            <AuthInput
                                title='Số điện thoại'
                                placeholder={this.state.phoneFocused ? '' : 'Nhập số điện thoại ...'}
                                icon={phone_icon}
                                value={this.state.phone}
                                onFocus={() => this.setState({ phoneFocused: true })}
                                onBlur={() => this.setState({ phoneFocused: false })}
                                onChangeText={text => this.setState({ phone: text })}
                                returnKeyType='next'
                                inputRef={ref => (this.phoneRef = ref)}
                                onSubmitEditing={() =>
                                    !!this.passwordRef && this.passwordRef.focus()
                                }
                                keyboardType='numeric'
                                focusValue={this.state.phoneFocused}
                            />
                            <AuthInput
                                title='Mật khẩu'
                                placeholder={this.state.passwordFocused ? '' : 'Nhập mật khẩu ...'}
                                icon={password_icon}
                                value={this.state.password}
                                onFocus={() => this.setState({ passwordFocused: true })}
                                onBlur={() => this.setState({ passwordFocused: false })}
                                onChangeText={text => this.setState({ password: text })}
                                returnKeyType='done'
                                inputRef={ref => (this.passwordRef = ref)}
                                onSubmitEditing={() => {
                                    Keyboard.dismiss();
                                    this.onLogin();
                                }}
                                secureTextEntry={true}
                                showPassword={true}
                                focusValue={this.state.passwordFocused}
                            />
                            <AuthInput
                                title='Xác nhận mật khẩu'
                                placeholder={this.state.password2Focused ? '' : 'Nhập mật khẩu ...'}
                                icon={password_icon}
                                value={this.state.password2}
                                onFocus={() => this.setState({ password2Focused: true })}
                                onBlur={() => this.setState({ password2Focused: false })}
                                onChangeText={text => this.setState({ password2: text })}
                                returnKeyType='done'
                                inputRef={ref => (this.passwordRef = ref)}
                                onSubmitEditing={() => {
                                    Keyboard.dismiss();
                                    this.onLogin();
                                }}
                                secureTextEntry={true}
                                showPassword={true}
                                focusValue={this.state.password2Focused}
                            />
                            <View style={{ marginTop: hs(28) }}>
                                <AuthButton
                                    onPress={() => this.onLogin(this.state.loginname, this.state.pwd)}
                                    title='Đăng Ký'
                                />
                            </View>
                            <View style={styles.noteContainer}>
                                <Text style={styles.text}>Đã có tài khoản? </Text>
                                <TouchableOpacity
                                    style={styles.noteButton}
                                    onPress={() => this.props.navigation.navigate(authStack.login)}
                                >
                                    <Text style={styles.noteButtonText}>Đăng nhập</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </View>
            </KeyboardAvoidingView>
        );
    };
};

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
        height: hs(624),
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
        flexDirection: 'row',
        alignSelf: 'center',
        marginVertical: sizeHeight(5),
    },
})