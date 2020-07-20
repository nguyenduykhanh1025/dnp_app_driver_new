import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TextInput, TouchableOpacity } from 'react-native';
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
import Icon from 'react-native-vector-icons/FontAwesome';


export default class AuthInput extends Component {

    constructor(props) {
        super(props);
        this.state = {
            eyecolor: Colors.tinyTextGrey,
            passShow: this.props.secureTextEntry ? true : false,
        }
    }

    onChangePassShow = () => {
        this.setState({ passShow: !this.state.passShow })
        if (this.state.passShow) {
            this.setState({ eyeColor: '#F3B03F' })
        }
        else this.setState({ eyeColor: '#86889E' })
    }

    render() {
        let {
            title,
            placeholder,
            icon,
            value,
            onFocus,
            onBlur,
            onChangeText,
            returnKeyType,
            onSubmitEditing,
            inputRef,
            showPassword,
            keyboardType,
            focusValue
        } = this.props;
        return (
            <View style={styles.container}>
                <View style={styles.iconContainer}>
                    <Image
                        source={icon}
                        style={styles.icon}
                        resizeMode='contain'
                    />
                </View>
                <View style={styles.inputContainer}>
                    <Text style={styles.inputTitle}>{!focusValue & value == '' ? title : ''}</Text>
                    <Text style={[styles.inputTitle, { marginTop: sizeHeight(0) }]}>{focusValue | (!focusValue & value != '') ? title : ''}</Text>
                    <TextInput
                        style={styles.input}
                        value={value}
                        onFocus={onFocus}
                        onBlur={onBlur}
                        onChangeText={onChangeText}
                        returnKeyType={returnKeyType}
                        ref={inputRef}
                        onSubmitEditing={onSubmitEditing}
                        blurOnSubmit={false}
                        secureTextEntry={this.state.passShow}
                        keyboardType={keyboardType}
                    />
                </View>
                {showPassword ?
                    <TouchableOpacity
                        style={styles.eyeButton}
                        onPress={this.onChangePassShow}
                    >
                        <Icon
                            name='eye'
                            size={16}
                            color={this.state.eyeColor}
                        />
                    </TouchableOpacity>
                    : null
                }
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        borderBottomWidth: 1.5,
        borderColor: '#EFF1F5',
        marginBottom: sizeHeight(3),
    },
    icon: {
        width: sizeWidth(7),
        height: sizeHeight(5),

    },
    iconContainer: {
        width: sizeWidth(15),
        justifyContent: 'center',
        alignItems: 'center',
    },
    inputTitle: {
        fontSize: fs(15),
        lineHeight: 18,
        color: Colors.tinyTextGrey,
        fontWeight: 'bold',
        position: 'absolute',
        marginTop: sizeHeight(2.5),
    },
    inputContainer: {
        paddingTop: sizeHeight(2),
        width: '73%',
    },
    input: {
        fontSize: fs(16),
        lineHeight: 19,
        color: '#000000',
        height: sizeHeight(6)
    },
    eyeButton: {
        justifyContent: 'center',
        alignItems: 'center',
    }
})