import React, { PureComponent } from 'react';
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
    StatusBar,
    TextInput,
    Alert,
} from 'react-native';
import { connect } from 'react-redux';
import Toast from 'react-native-tiny-toast';
import QRCode from 'react-native-qrcode-svg';
import {
    HeaderList,
    sizeWidthInput,
    FlashMessage,
    LoadingBase,
    WaitingModal,
    ModalQRResult,
    HeaderMain
} from '@/components';
import {
    getListNotification,
    SearchQRCode
} from '@/mock/index';
import {
    commonStyles,
    Colors,
    Fonts,
    colorOpacityMaker,
    sizeHeight,
    sizeWidth,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';
import PushNotification from 'react-native-push-notification';
import { signOut } from '@/modules';
import Geolocation from '@react-native-community/geolocation';
import mqtt from 'sp-react-native-mqtt';
import NavigationService from '@/utils/navigation';
import {
    homeStack,
    mainStack
} from '@/config/navigator';
import DeviceInfo from 'react-native-device-info';
import Icon from 'react-native-vector-icons/AntDesign';
import { SCANNER_QR } from '@/modules/home/constants';
import { ScrollView } from 'react-native-gesture-handler';

const bg_qrcode = require('../../assets/images/qr-code.png');

class HomeScreen extends PureComponent {
    constructor(props) {
        super(props);
        this.state = {
            qrvalue: 'HOA',
            getLocationEnable: false,
            loading: false,
            result: false,
            msg: 'Đang xử lý',
            Data: [],
            deviceId: '',
        };
    }

    componentDidMount = async () => {
        this.getId()
        await this.setState({
            qrvalue: JSON.stringify(this.props.navigation.state.params.item),
        })
        this.onTestMqtt()
    }

    getId = async () => {
        let id = DeviceInfo.getDeviceId();
        this.setState({ deviceId: id });
    }

    componentWillMount = async () => {

    }

    onTestMqtt = async () => {
        var settings = {
            // mqttServerUrl : "localhost", 
            mqttServerUrl: "192.168.1.92",
            port: 1883,
            topic: "myTopic"
        }

        mqtt.createClient({
            uri: 'mqtt://' + settings.mqttServerUrl + ":" + settings.port,
            clientId: this.state.deviceId,
        }).then((client) => {
            client.on('connect', () => {
                console.log('connected');
                client.subscribe('/data', 0);
            });
            client.on('message', (msg) => {
                var message = JSON.parse(msg.data)
                console.log('message', message);
                this.setState({
                    loading: message.status ? message.status : this.state.loading,
                    msg: message.msg,
                    Data: message.Data
                })
                if (!message.status && this.state.loading) {
                    NavigationService.navigate(mainStack.result, { item: message.Data })
                    this.setState({ loading: false })
                }
                // PushNotification.localNotification({
                //     title: message.title, // (optional)
                //     message: message.msg, // (required)
                //     foreground: true,
                //     vibrate: true,
                //     vibration: 300,
                //     playSound: true,
                // })
            });

            client.connect();
        }).catch((err) => {
            this.props.navigation.goBack();
        });
    }

    onModalClose = () => {
        this.setState({ loading: false })
    }

    onTestLoading = async () => {
        this.setState({ loading: true });
        setTimeout(() => {
            this.setState({ msg: 'Đang tìm vị trí' })
        }, 3000)
        setTimeout(() => {
            this.setState({ msg: 'Đang xử lý' })
        }, 5000)
        setTimeout(() => {
            if (this.state.loading) {
                NavigationService.navigate(mainStack.result)
                this.setState({ loading: false });
            }
        }, 5001)
    }

    renderLeft = () => {
        return (
            <View
                style={{
                    marginLeft: ws(20),
                }}
            >
                <Icon
                    name={'arrowleft'}
                    size={fs(25)}
                />
            </View>
        )
    }

    render() {
        return (
            <View style={[commonStyles.containerClass,]}>
                <StatusBar
                    translucent
                    barStyle={'dark-content'}
                    backgroundColor='transparent'
                />
                <HeaderMain
                    backgroundColor={Colors.white}
                    renderLeft={this.renderLeft()}
                    onPressLeft={() => { this.props.navigation.goBack() }}
                    disableBG
                    disableStep
                />
                <View >
                    <View style={{ alignItems: 'center' }}>
                        <View style={{
                            backgroundColor: Colors.blue,
                            height: hs(75),
                            width: ws(315),
                            justifyContent: 'center',
                            alignItems: 'center',
                            borderRadius: 8
                        }}>
                            <Text
                                style={{
                                    fontSize: fs(18),
                                    color: Colors.white
                                }}>
                                Quét mã ở cổng để vào cảng
                                </Text>
                        </View>
                    </View>
                    <View style={styles.QRCode}>
                        <ImageBackground
                            source={bg_qrcode}
                            style={{
                                width: ws(268),
                                height: ws(268),
                                justifyContent: 'center',
                                alignItems: 'center',
                            }} >
                            <QRCode
                                value={this.state.qrvalue}
                                size={sizeWidth(50)}
                            />
                        </ImageBackground>
                    </View>
                    <TouchableOpacity
                        onPress={() => { this.onTestLoading() }}
                    >
                        <View style={{
                            justifyContent: 'center',
                            alignItems: 'center',
                            marginTop: hs(46.78),
                        }}>
                            <View style={styles.outline}>
                                <View style={styles.frame}>
                                    <View style={styles.frame1Border}>
                                        <Text style={styles.title}>Lô</Text>
                                        <Text style={styles.txtValue}>0001</Text>
                                    </View>
                                    <View style={styles.frame1Border}>
                                        <Text style={styles.title}>Size</Text>
                                        <Text style={styles.txtValue}>0001</Text>
                                    </View>
                                    <View style={styles.frame1Border}>
                                        <Text style={styles.title}>Type</Text>
                                        <Text style={styles.txtValue}>0001</Text>
                                    </View>
                                    <View style={styles.frame1}>
                                        <Text style={styles.title}>Số lượng</Text>
                                        <Text style={styles.txtValue}>0001</Text>
                                    </View>
                                </View>
                            </View>
                        </View>
                    </TouchableOpacity>
                </View>
                <WaitingModal
                    visible={this.state.loading}
                    msg={this.state.msg}
                    onModalClose={() => { this.onModalClose() }}
                />
                <ModalQRResult
                    visible={this.state.result}
                    data={SearchQRCode}
                />
            </View>
        );
    }
}

const mapStateToProps = (state) => {
    return {
    };
};

const styles = StyleSheet.create({
    QRCode: {
        alignItems: 'center',
        marginTop: hs(43),
    },
    scroll: {
        height: hs(721),
    },
    outline: {
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: Colors.white,
        width: ws(335),
        height: hs(241),
        borderRadius: 10,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 1,
        },
        shadowOpacity: 0.20,
        shadowRadius: 1.41,
        elevation: 3,
        marginBottom: sizeHeight(5),
    },
    Text: {
        width: sizeWidth(80),
        marginVertical: sizeHeight(5),
        backgroundColor: "#E6E8FF",
        borderRadius: 5,
        padding: sizeHeight(2),
    },
    frame: {
        flexDirection: 'column',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    frame1: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: ws(305),
        height: hs(40),
        marginTop: hs(16),
    },
    frame1Border: {
        borderBottomWidth: 1,
        borderBottomColor: '#EFF1F5',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: ws(305),
        height: hs(40),
        marginTop: hs(16),
    },
    title: {
        fontSize: fs(15),
        color: Colors.tinyTextGrey,
        fontWeight: 'bold',
        marginLeft: ws(4),
    },
    txtValue: {
        fontSize: fs(20),
        fontWeight: 'bold',
        marginRight: ws(5)
    }
})

export default connect(mapStateToProps)(HomeScreen);