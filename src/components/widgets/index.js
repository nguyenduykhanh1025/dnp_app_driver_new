import React, { Component } from 'react';
import { View, Text, Button, Image, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import screen from '../../utils/screen';

export default class Widgets extends Component {
    render() {
        const { img, title, onPress } = this.props
        return (
            <View style={styles.widgets}>
                <TouchableOpacity
                    style={styles.widgetsitem}
                    onPress={() => { onPress() }}
                >
                    <Image source={img} style={styles.iconWidgets} />
                    <Text style={styles.titleWidget}>{title}</Text>
                </TouchableOpacity>

            </View>
        );
    }
}

const styles = StyleSheet.create({
    widgetsview: {
        marginLeft: 10,
        marginRight: 10,
        flexDirection: 'row',
    },
    widgetsitem: {
        width: screen.widthsc / 2.68,
        height: screen.heightsc / 7,
        margin: 10,
        borderRadius: 5,
        borderTopWidth: 1,
        borderLeftWidth: 1,
        borderBottomWidth: 2,
        borderRightWidth: 2,
        borderBottomColor: '#eeeeee',
        borderRightColor: '#eeeeee',
        borderLeftColor: '#eeeeee',
        borderTopColor: '#eeeeee',
        alignItems: 'center',
    },
    widgets: {
        alignItems: 'center',
    },
    titleWidget: {
        color: '#BBBBBB',
        fontWeight: 'bold',
        position: 'absolute',
        bottom: 10,
    },
    iconWidgets: {
        marginTop: screen.widthsc / 18,
        width: screen.widthsc / 10,
        height: screen.widthsc / 10,
        // margin: 4,
    }
})