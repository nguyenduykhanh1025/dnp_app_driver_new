import React, { Component } from 'react'
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native'
import Icon from 'react-native-vector-icons/FontAwesome';
import { Colors } from '@/commons';
import { sizes, sizeWidth, sizeHeight } from '@/commons/Spanding';

export default class Checkbox extends Component {
    render() {
        var { onSelect, value, selectedValue } = this.props;
        return (
            <TouchableOpacity onPress={() => onSelect(value)}>
                {/* <View style={[styles.checkbox, value === selectedValue ? styles.checkboxActive : '']}>
                    <Icon name="check" size={15} color='#fff' />
                </View> */}
                <View style={[styles.checkbox, value === selectedValue ? styles.checkboxActive : '']}>
                    {value === selectedValue ?
                        <Icon name="check" size={15} color='#fff' />
                        : null
                    }
                </View>
            </TouchableOpacity>
        )
    }
}

const styles = StyleSheet.create({
    checkbox: {
        width: 20,
        height: 20,
        borderColor: '#ddd',
        borderWidth: 1,
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 10,
        backgroundColor: '#F0F1F5',
        marginRight: sizeWidth(3)
    },
    checkboxActive: {
        backgroundColor: Colors.subColor,
        borderColor: Colors.subColor,
    }
})