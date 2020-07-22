import React, { Component } from 'react'
import {
    Text,
    View,
    Modal,
    Dimensions,
    TouchableWithoutFeedback,
    Image,
    StyleSheet,
    TouchableOpacity,
} from 'react-native';
import {
    Colors,
    sizeHeight,
    sizeWidth,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';
import { Spinner } from 'native-base';
import { hasSystemFeature } from 'react-native-device-info';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

const ic_waiting = require('../../assets/gif/waiting.1.gif');
const bg_waiting = require('../../assets/images/waiting.png');
const ic_close = require('../../assets/icons/ic_back_menu.png');

export default class WaitingModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: this.props.visible,
            msg: this.props.msg,
        }
    }

    onModalClose = () => {
        this.setState({
            visible: false
        })
        this.props.onModalClose();
    }

    componentWillReceiveProps = async (nextProps) => {
        if (nextProps.visible != this.state.visible) {
            await this.setState({ visible: nextProps.visible })
        }
        if (nextProps.msg != this.state.msg) {
            await this.setState({ msg: nextProps.msg })
        }
    }

    render() {
        return (
            <Modal
                animationType='fade'
                transparent={true}
                visible={this.state.visible}
                onRequestClose={() => {
                    this.onModalClose();
                }}
            >
                <View style={styles.container}>
                    <View style={styles.imagegif}>
                        <Image source={bg_waiting} style={styles.image} />
                        <Spinner color={'#F3B03F'} size={ws(50)} />
                        <Text style={styles.msg}>{this.state.msg}...</Text>
                    </View>
                </View>
            </Modal>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        width: windowWidth,
        height: sizeHeight(110),
        backgroundColor: Colors.white,
    },
    imagegif: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: hs(134)
    },
    image: {
        width: ws(229),
        height: ws(250),
        marginBottom: hs(40),
    },
    ic_close: {
        width: windowWidth * 0.03,
        height: windowWidth * 0.03,
    },
    close: {
        alignItems: 'flex-end',
        padding: windowWidth * 0.03,
    },
    msg: {
        color: Colors.blue,
        fontSize: fs(16),
        marginTop: hs(21)
    }
})
