import React, { Component } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { sizeHeight, sizeWidth } from '@/commons/Spanding';
import {
    commonStyles,
    Colors,
    Fonts,
    colorOpacityMaker,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';

export default class AuthButton extends Component {
    render() {
        let { onPress, title } = this.props;
        return (
            <View style={styles.container}>
                <TouchableOpacity
                    style={styles.button}
                    onPress={onPress}
                >
                    <Text style={styles.text}>{title}</Text>
                </TouchableOpacity>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    button: {
        backgroundColor: '#F3B03F',
        width: ws(325),
        height: hs(60),
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
    },
    text: {
        fontSize: fs(20),
        lineHeight: 23,
        color: '#FFFFFF',
        fontWeight: 'bold',
    }
})