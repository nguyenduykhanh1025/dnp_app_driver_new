import React, { Component } from 'react'
import {
    Text,
    View,
    StyleSheet,
    ImageBackground,
    Image,
    ScrollView,
    Switch,
    TouchableOpacity,
    StatusBar,
    Alert,
    Modal
} from 'react-native';
import Icon from 'react-native-vector-icons/AntDesign';
import NavigationService from '@/utils/navigation';
import {
    Colors,
    sizes,
    sizeWidth,
    sizeHeight,
    commonStyles,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';
import {
    Right,
    Thumbnail,
    Content
} from 'native-base';
import { getInfoUser } from '@/mock/index';
import { signOut } from '@/modules/auth/action';
import { connect } from 'react-redux';
import {
    getRequestAPI,
    callApi
} from '@/requests'
import {
    getToken,
    getGPSEnable,
    saveGPSEnable,
} from '@/stores';
import {
    down_arrow,
    lefticon,
} from '@/assets/icons';
import Toast from 'react-native-tiny-toast';
import {
    DropDownProfile
} from '@/components'

const profile_background = require('@/assets/icons/account/user.png')

const ic_camera = require('@/assets/icons/camera.png')

class ProfileModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            truckNoList: [],
            chassisNoList: [],
            isEnabled: false,
            name: '',
            phone: '',
            visible: this.props.visible,
        }
    };

    componentDidMount = async () => {
        this.getUserInfo();
        var gpsEnable = await getGPSEnable();
        await this.setState({
            isEnabled: gpsEnable == 'true' ? true : false
        })
    };

    componentWillReceiveProps = async (nextProps) => {
        if (nextProps.visible != this.state.visible) {
            await this.setState({ visible: nextProps.visible })
        }
    }

    getUserInfo = async () => {
        var token = await getToken();
        const params = {
            api: 'user/info',
            param: '',
            token: token,
            method: 'GET'
        }
        var result = undefined;
        result = await callApi(params);
        if (result.code == 0) {
            await this.setState({
                name: result.data.driverName,
                phone: result.data.driverPhoneNumber,
                chassisNoList: result.data.chassisNoList,
                truckNoList: result.data.truckNoList,
            })
        }
        else {
            Alert.alert('Thông báo!', result.msg)
        }
    }

    toggleSwitch = async () => {
        await this.setState({
            isEnabled: this.state.isEnabled ? false : true
        })
        saveGPSEnable(this.state.isEnabled ? "true" : "false")
    }

    onLogout = async () => {
        Toast.showLoading()
        var token = await getToken();
        const params = {
            api: 'user/logout',
            param: '',
            token: token,
            method: 'POST'
        }
        var result = undefined;
        result = await callApi(params);
        if (result.code == 0) {
            Toast.hide()
            this.props.dispatch(signOut())
        }
        else {
            Toast.hide()
            this.props.dispatch(signOut())
        }
    }

    render() {
        var { data } = this.state;
        return (
            <Modal
                animationType="slide"
                transparent={false}
                statusBarTranslucent={true}
                visible={this.state.visible}
                onRequestClose={() => {
                    this.props.onClose()
                }}
            >
                <ScrollView style={styles.container}>
                    <StatusBar
                        translucent
                        barStyle={'light-content'}
                        backgroundColor='transparent'
                    />
                    <ImageBackground
                        source={require('@/assets/images/home_bg.png')}
                        style={styles.bg}
                    >
                        <View style={styles.bgContainer}>
                            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', }}>
                                <TouchableOpacity onPress={() => this.props.onClose()} style={{ width: ws(70), height: hs(30), alignItems: 'center', justifyContent: 'center' }} >
                                    {/* <Icon name="arrowleft" size={fs(25)} color={Colors.white} /> */}
                                    <Icon name={'close'} size={25} color={Colors.white}/>
                                </TouchableOpacity>
                                <TouchableOpacity
                                    onPress={() => {
                                        this.onLogout()
                                        this.props.onClose()
                                    }}
                                >
                                    <View>
                                        <Text
                                            style={{
                                                marginRight: ws(17),
                                                color: Colors.white,
                                                fontSize: fs(15)
                                            }}>
                                            Đăng xuất</Text>
                                    </View>
                                </TouchableOpacity>
                            </View>
                            <View
                                style={{
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    marginTop: hs(14)
                                }}>
                                <View style={{ backgroundColor: Colors.white, borderRadius: ws(60) }}>
                                    <Image
                                        source={profile_background}
                                        style={{
                                            width: ws(120),
                                            height: ws(120),
                                            borderRadius: ws(60),
                                        }}
                                    />
                                </View>
                            </View>
                        </View>
                    </ImageBackground>
                    <View style={styles.body}>
                        <View style={{ alignItems: 'center' }}>
                            <View style={{
                                flexDirection: 'column',
                                width: ws(315),
                                marginTop: 20
                            }}>
                                <Text style={styles.textTitle}>Họ và tên</Text>
                                <Text style={styles.textValue}>{this.state.name}</Text>
                                <View style={{
                                    borderBottomWidth: 1.5,
                                    borderBottomColor: '#EFF1F5',
                                    borderStyle: "solid"
                                }}
                                />
                            </View>
                            <View style={{
                                flexDirection: 'column',
                                width: ws(315),
                                marginTop: 20
                            }}>
                                <Text style={styles.textTitle}>Số điện thoại</Text>
                                <Text style={styles.textValue}>{this.state.phone}</Text>
                                <View style={{
                                    borderBottomWidth: 1.5,
                                    borderBottomColor: '#EFF1F5',
                                    borderStyle: "solid"
                                }}
                                />
                            </View>
                            <DropDownProfile
                                data={this.state.truckNoList}
                                title={'Số xe đầu kéo'}
                                style={{
                                    marginVertical: hs(24),
                                }}
                                onSelect={(item) => { console.log('BSX Đầu Kéo', item) }}
                            />
                            <DropDownProfile
                                data={this.state.chassisNoList}
                                title={'Số xe Rơ-mooc'}
                                onSelect={(item) => {
                                    console.log('BSX Rơ Mooc', item)
                                }}
                            />
                            {/* <View style={{
              flexDirection: 'row',
              width: ws(315),
              marginTop: 20,
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
              <Text style={[
                styles.textTitle,
                {
                  color: '#000'
                }
              ]}>
                Bật chia sẻ vị trí
              </Text>
              <Switch
                style={{
                  marginRight: -ws(11),
                }}
                trackColor={{ false: Colors.grey2, true: '#0095FF' }}
                thumbColor={this.state.isEnabled ? Colors.white : "#f4f3f4"}
                ios_backgroundColor="#0095FF"
                onValueChange={this.toggleSwitch}
                value={this.state.isEnabled}
              />
            </View> */}
                        </View>
                    </View>
                </ScrollView>
            </Modal>
        )
    }
}

const mapStateToProps = (state) => {
    return {
    };
};

export default connect(mapStateToProps)(ProfileModal);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.white,
    },
    scroll: {
    },
    profile: {
        width: sizeWidth(100),
        height: sizeHeight(35)
    },
    contentView: {
        paddingLeft: sizeWidth(5),
        backgroundColor: Colors.white,
        height: sizeHeight(100)
    },
    icon: {
        marginHorizontal: sizeWidth(15),
    },
    textTitle: {
        fontSize: fs(15),
        lineHeight: 18,
        color: '#BEC2CE',
        marginVertical: 10,
    },
    textValue: {
        fontSize: fs(18),
        lineHeight: 22,
        letterSpacing: -0.434118,
        marginVertical: 5,
    },
    bg: {
        height: hs(250),
        width: ws(375),
    },
    body: {
        width: sizeWidth(100),
    },
    bgContainer: {
        flexDirection: 'column',
        flex: 1,
        backgroundColor: 'rgba(0, 0, 0, 0.3)',
        paddingTop: sizeHeight(5)
    },
    downIcon: {
        height: sizeHeight(2),
        width: sizeWidth(4),
        marginLeft: sizeWidth(2),
        tintColor: '#919BBB'
    },
    Lefticon: {
        width: ws(11.43),
        height: hs(20),
        marginLeft: ws(36.86)
    },
})