import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
    TouchableOpacity,
    Image,
    StatusBar,
} from 'react-native';
import {
    down_arrow,
} from '@/assets/icons'
import {
    Container,
    Header,
    Content,
    Icon,
    Picker,
    Form
} from "native-base";
import {
    Colors,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';


export default class Button extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selected: ''
        };
    }

    render() {
        var {
            value,
            style,
            onPress,
            styleBtn
        } = this.props;
        return (
            <View style={[styles.Container, style]}>
                <TouchableOpacity onPress={onPress}>
                    <View style={styles.ButtonContainer}>
                        <Text style={[styles.ButtonText, styleBtn]}>
                            {value}
                        </Text>
                    </View>
                </TouchableOpacity>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    Container: {
        width: ws(375),
        justifyContent: 'center',
        alignItems: 'center',
    },
    ButtonContainer: {
        width: ws(325),
        height: hs(55),
        backgroundColor: Colors.subColor,
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
    },
    ButtonText: {
        fontSize: fs(16),
        color: Colors.white,
        fontWeight: 'bold',
    }
})