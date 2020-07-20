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
import { HeaderList, sizeWidthInput, FlashMessage, LoadingBase, WaitingModal, ModalQRResult, HeaderMain } from '@/components';
import { getListNotification, SearchQRCode } from '@/mock/index';
import { commonStyles, Colors, Fonts, colorOpacityMaker, sizeHeight, sizeWidth } from '@/commons';
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
            <Icon name={'arrowleft'} size={25} />
        )
    }

    render() {
        return (
            <View style={[commonStyles.containerClass,]}>
                <StatusBar
                    barStyle = 'dark-content'
                />
                <HeaderMain
                    backgroundColor={Colors.white}
                    left={this.renderLeft()}
                    onPress={() => { this.props.navigation.goBack() }}
                />
                <ScrollView style = {styles.scroll}>
                    <View >
                        <View style={{ alignItems: 'center' }}>
                            <View style={{
                                backgroundColor: Colors.blue,
                                height: sizeHeight(9),
                                width: sizeWidth(85),
                                justifyContent: 'center',
                                alignItems: 'center',
                                borderRadius: 8
                            }}>
                                <Text style={{ fontSize: 18, color: Colors.white }}>Quét mã ở cổng để vào cảng</Text>
                            </View>
                        </View>
                        <View style={styles.QRCode}>
                            <ImageBackground
                                source={bg_qrcode}
                                style={{
                                    width: sizeWidth(70),
                                    height: sizeWidth(70),
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
                            <View style={{ justifyContent: 'center', alignItems: 'center', marginTop: sizeHeight(7) }}>
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
                </ScrollView>
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
        marginTop: sizeHeight(5)
    },
    scroll: {
        height: sizeHeight(87),
    },
    outline: {
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: Colors.white,
        width: sizeWidth(85),
        height: sizeWidth(70),
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
    },
    frame1: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: sizeWidth(75),
        height: sizeWidth(17.5),
    },
    frame1Border: {
        borderBottomWidth: 1, 
        borderBottomColor: '#EFF1F5',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: sizeWidth(75),
        height: sizeWidth(17.5),
    },
    title: {
        fontSize: 15,
        color: '#86889E',
        fontWeight: 'bold',
    },
    txtValue: {
        fontSize: 20,
        fontWeight: 'bold'
    }
})

export default connect(mapStateToProps)(HomeScreen);