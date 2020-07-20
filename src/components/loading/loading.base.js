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
    ActivityIndicator,
} from 'react-native';

import { screen } from '@/utils';
import { Colors } from '@/commons'

export default class LoadingBase extends Component {
    constructor(props) {
        super(props);
        this.state = {
        };
    }
    render() {
        const { visible, color, size, style } = this.props;
        // console.log('visible', visible)
        return (
            <ActivityIndicator
                animating={visible != undefined ? visible : false}
                color={color != undefined ? color : Colors.mainColor}
                size={size != undefined ? size : 'small'}
                style={style != undefined ? style : styles.default}
            />
        )
    }
}

const styles = StyleSheet.create({
    default: {
        position: 'absolute',
        paddingLeft: 100,
    }
});
