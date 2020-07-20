import React, { Component } from 'react';
import {
    View,
    Text,
    Button,
    Image,
    StyleSheet,
    TouchableOpacity,
    Dimensions,
    ScrollView
} from 'react-native';
import screen from '../../utils/screen';

export default class MenuItem extends Component {
    render() {
        const { onPress, title, icon } = this.props;
        return (
            <View style={styles.menuView}>
                <TouchableOpacity style={styles.menuItem}
                    onPress={() => { onPress() }}
                >
                    <View style={styles.bgMenu}>
                        {/* <Image  /> */}
                        <Text style={styles.titleMenu}>{title}</Text>
                    </View>
                </TouchableOpacity>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    menuItem: {
        margin: 10,
        width: screen.widthsc - 20,
        height: screen.heightsc / 8,
        flexDirection: 'row',
    },
    bgMenu: {
        width: screen.widthsc - 20,
        height: screen.heightsc / 8,
        backgroundColor: '#FFF',
        borderRadius: 4,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 10,
        },
        shadowOpacity: 0.58,
        shadowRadius: 16.00,
        elevation: 5,
    },
    titleMenu: {
        position: 'absolute',
        margin: 10,
        bottom: 0,
        fontSize: 25,
        right: 0,
        color: '#9c9c9c',
        // borderBottomWidth: 0.6,
    },
    menuView: {
        // backgroundColor: 'black',
        // width: screen.widthsc,
    }
})