import React, { Component } from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Image} from 'react-native';
import {sizeHeight, sizeWidth} from '@/commons/Spanding';
import Colors from '@/commons/Colors';

export default class HomeButton extends Component{
    render () {
        const {
            title,
            icon,
            active,
            onPress,
        } = this.props
        return (
            <TouchableOpacity 
                style = {styles.container}
                onPress = {onPress}
            >
                <View style = {[styles.iconContainer, active ? styles.iconContainerActive : null]}>
                    <Image
                        source = {icon}
                        style = {[styles.icon, active ? styles.iconActive : null]}
                        resizeMode = 'contain'
                    />
                </View>
                <Text style = {styles.title}>{title}</Text>
            </TouchableOpacity>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        height: sizeHeight(10.5),
        width: sizeWidth(46) - sizeHeight(1) ,
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: sizeHeight(1),
    },
    iconContainer: {
        width: sizeHeight(6),
        height: sizeHeight(6),
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 25,
    },
    iconContainerActive: {
        backgroundColor: Colors.maincolor,
        borderRadius: 25,
    },
    icon: {
        width: sizeWidth(6),
        height: sizeHeight(3),
        tintColor: Colors.maincolor,
    },
    iconActive: {
        tintColor: '#FFFFFF',
    },
    title: {
        color: '#86889E',
        fontSize: 13,
        lineHeight: 15,
    }
})