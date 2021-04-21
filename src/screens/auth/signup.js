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
	Alert,
} from 'react-native';
import { sizeHeight, sizeWidth } from '@/commons/Spanding';
import Toast from 'react-native-tiny-toast';
import {
	Colors,
	fontSizeValue as fs,
	widthPercentageToDP as ws,
	heightPercentageToDP as hs,
} from '@/commons';
import { callApi } from '@/requests';
import AuthInput from '@/components/auth/AuthInput';
import AuthButton from '@/components/auth/AuthButton';
import { FlashMessage } from '@/components';

import {
	user_icon,
	password_icon,
	phone_icon,
	license_plate_icon,
} from '@/assets/icons/index';
import { authStack } from '@/config/navigator';

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
			tractorLicensePlate: '',
			romoocLicensePlate: '',
			tractorLicensePlateFocused: false,
			romoocLicensePlateFocused: false,
		};
	}

	onRegister = async () => {
		const {
			name,
			password,
			password2,
			phone,
			tractorLicensePlate,
			romoocLicensePlate,
		} = this.state;
		if (!name == '' && !password == '' && !password2 == '' && !phone == '' && !tractorLicensePlate == '' && !romoocLicensePlate == '') {
			if (password != password2) {
				Alert.alert('Thông báo!', 'Mật khẩu không giống nhau. Vui lòng nhập đúng thông tin!');
				return;
			}
			Toast.showLoading();
			const params = {
				api: 'user/register',
				param: {
					mobileNumber: phone,
					fullName: name,
					password: password,
					truckNo: romoocLicensePlate,
					chassisNo: tractorLicensePlate,
				},
				token: '',
				method: 'POST',
			};
			var result = undefined;
			result = await callApi(params);
			console.log('result', result);
			if (result.code == 0) {
				Toast.hide();
				FlashMessage(result.msg, 'success');
				this.props.navigation.navigate(authStack.login);
			} else {
				Toast.hide();
				Alert.alert(
					'Thông báo!',
					result.msg ? result.msg : 'Đăng ký thất bại.',
				);
			}
		} else {
			Alert.alert('Thông báo!', 'Hãy nhập đầy đủ thông tin để đăng ký!');
		}
	};

	render() {
		const keyboardVerticalOffset = Platform.OS === 'ios' ? 40 : 0
		return (
			// <KeyboardAvoidingView
			//   behavior={Platform.OS == 'ios' ? 'padding' : null}
			//   style={styles.container}>
			<View style={styles.container}>
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
								<Text style={styles.title}>Đăng Ký</Text>
							</View>
							<ScrollView style={styles.scrollView}>
								<KeyboardAvoidingView
									behavior={Platform.OS === "ios" ? "padding" : null}
									keyboardVerticalOffset={Platform.OS === "ios" ? 64 : 0}
									>
									<AuthInput
										title="Họ và tên"
										placeholder={
											this.state.nameFocused ? '' : 'Nhập họ và tên ...'
										}
										icon={user_icon}
										value={this.state.name}
										onFocus={() => this.setState({ nameFocused: true })}
										onBlur={() => this.setState({ nameFocused: false })}
										onChangeText={text => this.setState({ name: text })}
										returnKeyType="next"
										inputRef={ref => (this.fullnameRef = ref)}
										onSubmitEditing={() =>
											!!this.phoneRef && this.phoneRef.focus()
										}
										focusValue={this.state.nameFocused}
									/>
									<AuthInput
										title="Số điện thoại"
										placeholder={
											this.state.phoneFocused ? '' : 'Nhập số điện thoại ...'
										}
										icon={phone_icon}
										value={this.state.phone}
										onFocus={() => this.setState({ phoneFocused: true })}
										onBlur={() => this.setState({ phoneFocused: false })}
										onChangeText={text => this.setState({ phone: text })}
										returnKeyType="next"
										inputRef={ref => (this.phoneRef = ref)}
										onSubmitEditing={() =>
											!!this.passwordRef && this.passwordRef.focus()
										}
										keyboardType="numeric"
										focusValue={this.state.phoneFocused}
									/>
									<AuthInput
										title="Mật khẩu"
										placeholder={
											this.state.passwordFocused ? '' : 'Nhập mật khẩu ...'
										}
										icon={password_icon}
										value={this.state.password}
										onFocus={() => this.setState({ passwordFocused: true })}
										onBlur={() => this.setState({ passwordFocused: false })}
										onChangeText={text => this.setState({ password: text })}
										returnKeyType="next"
										onSubmitEditing={() =>
											!!this.password2Ref && this.password2Ref.focus()
										}
										inputRef={ref => (this.passwordRef = ref)}
										secureTextEntry={true}
										showPassword={true}
										focusValue={this.state.passwordFocused}
									/>
									<AuthInput
										title="Xác nhận mật khẩu"
										placeholder={
											this.state.password2Focused ? '' : 'Nhập mật khẩu ...'
										}
										icon={password_icon}
										value={this.state.password2}
										onFocus={() => this.setState({ password2Focused: true })}
										onBlur={() => this.setState({ password2Focused: false })}
										onChangeText={text => this.setState({ password2: text })}
										returnKeyType="next"
										inputRef={ref => (this.password2Ref = ref)}
										onSubmitEditing={() =>
											!!this.tractorLicensePlateRef &&
											this.tractorLicensePlateRef.focus()
										}
										secureTextEntry={true}
										showPassword={true}
										focusValue={this.state.password2Focused}
									/>
									<AuthInput
										title="Số xe đầu kéo"
										placeholder={
											this.state.tractorLicensePlate
												? ''
												: 'Nhập số xe đầu kéo...'
										}
										icon={license_plate_icon}
										value={this.state.tractorLicensePlate}
										onFocus={() =>
											this.setState({ tractorLicensePlateFocused: true })
										}
										onBlur={() =>
											this.setState({ tractorLicensePlateFocused: false })
										}
										onChangeText={text =>
											this.setState({ tractorLicensePlate: text })
										}
										returnKeyType="next"
										inputRef={ref => (this.tractorLicensePlateRef = ref)}
										onSubmitEditing={() =>
											!!this.romoocLicensePlateRef &&
											this.romoocLicensePlateRef.focus()
										}
										focusValue={this.state.tractorLicensePlate}
									/>
									<AuthInput
										title="Số xe rơ móc"
										placeholder={
											this.state.romoocLicensePlate
												? ''
												: 'Nhập số xe rơ móc...'
										}
										icon={license_plate_icon}
										value={this.state.romoocLicensePlate}
										onFocus={() =>
											this.setState({ romoocLicensePlateFocused: true })
										}
										onBlur={() =>
											this.setState({ romoocLicensePlateFocused: false })
										}
										onChangeText={text =>
											this.setState({ romoocLicensePlate: text })
										}
										returnKeyType="done"
										inputRef={ref => (this.romoocLicensePlateRef = ref)}
										onSubmitEditing={() => {
											Keyboard.dismiss();
											this.onRegister();
										}}
										focusValue={this.state.romoocLicensePlate}
									/>
								</KeyboardAvoidingView>
							</ScrollView>

							<View styles={styles.bottomContaienr}>
								<View style={{ marginTop: hs(28) }}>
									<AuthButton
										onPress={() =>
											this.onRegister(this.state.loginname, this.state.pwd)
										}
										title="Đăng Ký"
									/>
								</View>
								<View style={styles.noteContainer}>
									<Text style={styles.text}>Đã có tài khoản? </Text>
									<TouchableOpacity
										style={styles.noteButton}
										onPress={() =>
											this.props.navigation.navigate(authStack.login)
										}>
										<Text style={styles.noteButtonText}>Đăng nhập</Text>
									</TouchableOpacity>
								</View>
							</View>
						</View>
					</View>
				</View>
			</View>
			// </KeyboardAvoidingView>
		);
	}
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: Colors.white,
	},
	ImageContainer: {},
	image: {
		width: ws(375),
		height: hs(424),
		position: 'absolute',
	},
	scroll: {
		height: hs(200),
		marginTop: hs(80),
	},
	HeaderContainer: {
		flexDirection: 'row',
		marginHorizontal: ws(25),
		justifyContent: 'space-between',
		alignItems: 'center',
		paddingTop: hs(58),
	},
	HeaderText: {},
	HeaderIcon: {},
	HeaderTextUp: {
		fontFamily: null,
		color: Colors.white,
		fontSize: fs(18),
		fontWeight: 'bold',
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
		justifyContent: 'flex-end',
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
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
		flex: 1,
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
		textAlign: 'right',
		width: '100%',
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
	scrollView: {
		height: '100%',
		width: '100%',
	},
});
