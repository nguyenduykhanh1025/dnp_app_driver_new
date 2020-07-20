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
import { Colors, sizeHeight, sizeWidth } from '@/commons';
import { Spinner } from 'native-base';

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
                        <Spinner color={'#F3B03F'}  />
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
        marginTop: windowHeight * 0.2
    },
    image: {
        width: sizeWidth(70),
        height: sizeWidth(75),
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
        color: '#15307A',
        fontSize: 16,
    }
})
