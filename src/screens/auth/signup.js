import React, { Component } from 'react';
import {
    View, 
    Text, 
    StyleSheet, 
    ImageBackground, 
    TouchableOpacity, 
    KeyboardAvoidingView,
    Platform,
    ScrollView,
    StatusBar,
    Keyboard,
    Image
} from 'react-native';
import {sizeHeight, sizeWidth} from '@/commons/Spanding';
import { commonStyles, Colors, Fonts, colorOpacityMaker} from '@/commons';
import AuthInput from '@/components/auth/AuthInput';
import AuthButton from '@/components/auth/AuthButton';
import {user_icon, password_icon, phone_icon} from '@/assets/icons/index';
import { authStack } from '@/config/navigator';

export default class SignUpScreen extends Component {
    constructor (props) {
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
    render () {
        return (
            <ScrollView 
                style = {styles.container}
                showsVerticalScrollIndicator = {false}    
            >
                <KeyboardAvoidingView 
                    style = {styles.keyboardAvoidingView}
                    behavior= {(Platform.OS === 'ios')? "padding" : null}
                >
                    <StatusBar
                        translucent
                        barStyle = {"default"}
                        backgroundColor = 'transparent'
                    />
                    <ImageBackground
                        source = {require('@/assets/images/auth_bg.png')}
                        style = {styles.imageBg}
                    >
                        <View style = {styles.bgRow}>
                            <View>
                                <Text style = {styles.bgText}>Ứng dụng tài xế</Text>
                                <Text style = {styles.bgBoldText}>CẢNG ĐÀ NẴNG</Text>
                            </View>
                            <Image
                                source = {require('@/assets/images/logo.png')}
                                style = {styles.logo}
                                resizeMode = 'contain'
                            />
                        </View>
                    </ImageBackground>
                    <View style = {styles.signupArea}>
                        <View style = {styles.titleContainer}>
                            <Text style = {styles.title}>Đăng ký</Text>
                        </View>
                        <View style = {styles.inputContainer}>
                            <AuthInput
                                title = 'Họ và tên'
                                placeholder = {this.state.nameFocused ? '' : 'Nhập họ và tên ...'}
                                icon = {user_icon}
                                value = {this.state.name}
                                onFocus = {() => this.setState({nameFocused: true})}
                                onBlur = {() => this.setState({nameFocused: false})}
                                onChangeText = {text => this.setState({name: text})}
                                returnKeyType = 'next'
                                onSubmitEditing={() =>
                                    !!this.phoneRef && this.phoneRef.focus()
                                }
                                focusValue = {this.state.nameFocused}
                            />
                            <AuthInput
                                title = 'Số điện thoại'
                                placeholder = {this.state.phoneFocused ? '' : 'Nhập số điện thoại ...'}
                                icon = {phone_icon}
                                value = {this.state.phone}
                                onFocus = {() => this.setState({phoneFocused: true})}
                                onBlur = {() => this.setState({phoneFocused: false})}
                                onChangeText = {text => this.setState({phone: text})}
                                returnKeyType = 'next'
                                inputRef={ref => (this.phoneRef = ref)}
                                onSubmitEditing={() =>
                                    !!this.passwordRef && this.passwordRef.focus()
                                }
                                keyboardType = 'numeric'
                                focusValue = {this.state.phoneFocused}
                            />
                            <AuthInput
                                title = 'Mật khẩu'
                                placeholder = {this.state.passwordFocused ? '': 'Nhập mật khẩu ...'}
                                icon = {password_icon}
                                value = {this.state.password}
                                onFocus = {() => this.setState({passwordFocused: true})}
                                onBlur = {() => this.setState({passwordFocused: false})}
                                onChangeText = {text => this.setState({password: text})}
                                returnKeyType = 'next'
                                inputRef={ref => (this.passwordRef = ref)}
                                onSubmitEditing={() =>
                                    !!this.password2Ref && this.password2Ref.focus()
                                }
                                secureTextEntry = {true}
                                showPassword = {true}
                                focusValue = {this.state.passwordFocused}
                            />
                            <AuthInput
                                title = 'Xác nhận mật khẩu'
                                placeholder = {this.state.password2Focused ? '' : 'Nhập lại mật khẩu ...'}
                                icon = {password_icon}
                                value = {this.state.password2}
                                onFocus = {() => this.setState({password2Focused: true})}
                                onBlur = {() => this.setState({password2Focused: false})}
                                onChangeText = {text => this.setState({password2: text})}
                                returnKeyType = 'done'
                                inputRef={ref => (this.password2Ref = ref)}
                                secureTextEntry = {true}
                                showPassword = {true}
                                onSubmitEditing = {() => Keyboard.dismiss()}
                                focusValue = {this.state.password2Focused}
                            />
                        </View>
                        <AuthButton
                            onPress = {() => console.log('ok')}
                            title = 'Đăng Ký'
                        />
                        <View style = {styles.noteContainer}>
                            <Text style = {styles.noteText}>Đã có tài khoản? </Text>
                            <TouchableOpacity
                                style = {styles.noteButton}
                                onPress = {() => this.props.navigation.navigate(authStack.login)}
                            >
                                <Text style = {styles.noteButtonText}>Đăng Nhập</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </KeyboardAvoidingView>
            </ScrollView>
        );
    };
};

const styles = StyleSheet.create({
    container: {
        height: sizeHeight(100),
    },
    titleContainer: {
        marginHorizontal: sizeWidth(8),
        marginTop: sizeHeight(3),
    },
    inputContainer: {
        marginTop: sizeHeight(3),
        marginHorizontal: sizeWidth(8),
        justifyContent: 'center',
        alignItems: 'center',
    },
    imageBg: {  
        height: sizeHeight(60),
        width: sizeWidth(100),
        position: 'absolute',
        paddingLeft: sizeWidth(5),
        paddingTop: sizeHeight(6),
    },
    signupArea: {
        backgroundColor: '#FFFFFF',
        height: sizeHeight(75),
        bottom: 0,
        top: sizeHeight(30),
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
    },
    title: {
        color: Colors.maincolor,
        fontSize: 22,
        lineHeight: 26,
        fontWeight: 'bold',
    },
    noteContainer: {
        flexDirection: 'row',
        alignSelf: 'center',
        marginVertical: sizeHeight(5),
    },
    noteText: {
        color: Colors.tinyTextGrey,
        fontSize: 15,
        lineHeight: 18,
    },
    noteButtonText: {
        color: Colors.maincolor,
        fontSize: 15,
        lineHeight: 18,
    },
    noteButton: {
        borderBottomWidth: 1,
        borderBottomColor: Colors.maincolor,
    },
    keyboardAvoidingView: {
        height: sizeHeight(110),
        backgroundColor: '#FFFFFF'
    },
    bgText: {
        fontSize: 18,
        lineHeight: 21,
        color: '#FFFFFF',
        marginBottom: sizeHeight(1),
    },
    bgBoldText: {
        fontSize: 24,
        lineHeight: 34,
        color: '#FFFFFF',
        fontWeight: 'bold',
    },
    bgRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: sizeWidth(90),
        height: sizeHeight(12),
        paddingHorizontal: sizeWidth(2),
    },
    logo: {
        height: sizeHeight(20),
        width: sizeWidth(25)
    }
})