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
    ScrollView,
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
import { callApi } from '@/requests';
import { getToken } from '@/stores';

const bg_qrcode = require('../../assets/images/qr-code.png');

class HomeScreen extends PureComponent {
    constructor(props) {
        super(props);
        this.state = {
            qrvalue: 'abc',
            getLocationEnable: false,
            loading: false,
            result: false,
            msg: 'Đang xử lý',
            data: [],
            deviceId: '',
            sessionId: '',
            "domain": "",
            "port": "",
            "topic": ""
        };
        this.token = null;
    }

    componentDidMount = async () => {
        this.getId()
        this.token = await getToken();
        this.onGetURLMqtt()
        var qrString = this.props.navigation.state.params.dataQR.qrString;
        qrString = qrString.slice(0, qrString.length - 1);
        var dataQR = JSON.parse(qrString.replace(/'/g, '"'))
        // console.log('data', dataQR.data)
        await this.setState({
            qrvalue: this.props.navigation.state.params.dataQR.qrString,
            sessionId: this.props.navigation.state.params.dataQR.sessionId,
            data: dataQR.data
        })
    }



    getId = async () => {
        let id = DeviceInfo.getDeviceId();
        this.setState({ deviceId: id });
    }

    componentWillMount = async () => {

    }

    onGetURLMqtt = async () => {
        const params = {
            api: 'connection/info',
            param: '',
            token: this.token,
            method: 'GET'
        }
        var result = undefined;
        result = await callApi(params);
        // console.log('resultonGetURLMqtt', result.domain)
        if (result.code == 0) {
            this.setState({
                "domain": result.domain,
                "port": result.port,
                "topic": result.topic,
            })
        }
        else {
            Alert.alert('Thông báo!', result.msg)
        }
        this.onTestMqtt()
    }

    componentWillUnmount = () => {
        var settings = {
            mqttServerUrl: this.state.domain,
            port: this.state.port,
            topic: "eport/driver/" + this.state.sessionId + "/res",
        }

        mqtt.createClient({
            uri: settings.mqttServerUrl + ":" + settings.port,
            clientId: this.state.deviceId,
        }).then((client) => {
            client.on('closed', () => {
                console.log('closed');
            });
            client.unsubscribe(settings.topic)
            client.disconnect();
        }).catch((err) => {
            Alert.alert(
                'Lỗi!!!',
                'Liên hệ người phụ trách!',
                [
                    {
                        text: 'OK', onPress: () => {
                            this.props.navigation.goBack()
                        }
                    }
                ],
                { cancelable: false }
            );
        });
    }

    onTestMqtt = async () => {
        var settings = {
            mqttServerUrl: this.state.domain,
            port: this.state.port,
            topic: "eport/driver/" + this.state.sessionId + "/res",
            // topic: "eport/driver/ado3709Adlfj/res"
        }
        // var settings = {
        //     mqttServerUrl: '113.176.195.221',
        //     port: 1883,
        //     topic: "eport/driver/" + this.state.sessionId + "/res",
        //     // topic: "eport/driver/ado3709Adlfj/res"
        // }

        mqtt.createClient({
            // uri: 'mqtt://' + settings.mqttServerUrl + ":" + settings.port,
            uri: settings.mqttServerUrl + ":" + settings.port,
            clientId: "DriverApp-" + this.state.sessionId,
        }).then((client) => {
            client.on('connect', () => {
                // console.log('connected');
                client.subscribe(settings.topic, 0);
            });
            client.on('message', (msg) => {
                var message = JSON.parse(msg.data);
                console.log('message', message)
                this.setState({
                    loading: message.status == 'PROCESSING' ? true : message.status == 'FINISH' ? false : this.state.loading,
                    msg: message.msg,
                    Data: message.data
                })
                if (message.status == 'FINISH' && message.result == 'PASS') {
                    client.unsubscribe(settings.topic)
                    client.disconnect();
                    this.setState({ loading: false })
                    NavigationService.navigate(mainStack.result, { Data: message.data })
                }
                if (message.status == 'FINISH' && message.result == 'FAIL') {
                    client.unsubscribe(settings.topic)
                    client.disconnect();
                    Alert.alert(
                        'Thông báo!',
                        'Checkin thất bại!',
                        [
                            {
                                text: 'OK', onPress: () => {
                                    this.props.navigation.goBack()
                                }
                            }
                        ],
                        { cancelable: false }
                    );
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
            Alert.alert(
                'Lỗi!!!',
                'Liên hệ người phụ trách!',
                [
                    {
                        text: 'OK', onPress: () => {
                            this.props.navigation.goBack()
                        }
                    }
                ],
                { cancelable: false }
            );
        });
    }

    onModalClose = () => {
        this.setState({ loading: false })
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
                    onPressLeft={() => {
                        Alert.alert(
                            "Thông báo xác nhận!",
                            "Bạn chưa quét mã, bạn chắc chắn muốn thoát?",
                            [
                                { text: "Có", onPress: () => this.props.navigation.goBack() },
                                {
                                    text: "Không",
                                    style: "cancel"
                                },
                            ],
                            { cancelable: false }
                        )

                    }}
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
                    {/* <TouchableOpacity
                        onPress={() => { this.onTestLoading() }}
                    > */}
                    <ScrollView
                        horizontal={true}
                    >
                        {
                            this.state.data.map((item, index) => (
                                <View style={{
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    marginTop: hs(46.78),
                                    width: ws(375)
                                }}>
                                    <View style={styles.outline}>
                                        <View style={styles.frame}>
                                            <View style={styles.frame1Border}>
                                                <Text style={styles.title}>Cont</Text>
                                                <Text style={styles.txtValue}>{item.contNo}</Text>
                                            </View>
                                            <View style={styles.frame1Border}>
                                                <Text style={styles.title}>Size</Text>
                                                <Text style={styles.txtValue}>{item.sztp}</Text>
                                            </View>
                                            <View style={styles.frame1Border}>
                                                <Text style={styles.title}>Fe</Text>
                                                <Text style={styles.txtValue}>{item.fe}</Text>
                                            </View>
                                            <View style={styles.frame1}>
                                                <Text style={styles.title}>Khối lượng</Text>
                                                <Text style={styles.txtValue}>{item.weight}</Text>
                                            </View>
                                        </View>
                                    </View>
                                </View>
                            ))
                        }
                    </ScrollView>
                    {/* </TouchableOpacity> */}
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