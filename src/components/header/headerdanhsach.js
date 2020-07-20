import React, { Component } from 'react';
import { View, StyleSheet, TouchableOpacity, Text, Dimensions } from 'react-native';
import { Colors, Fonts, sizeScale, sizeWidth } from '@/commons';
import { NavigationActions, StackActions } from 'react-navigation';
import { muahangStack } from '@/config/navigator';
import Icon from 'react-native-vector-icons/FontAwesome'
class HeaderList extends Component {

    goBack = () => {
        const navigation = this.props.navigation
        navigation.goBack();
        console.log('this.props.goBack', navigation)
    }
    render() {
        const { filter, title, bgColor, bdw } = this.props;
        const color = Colors.titleColor;

        return (
            <View style={[styles.container, { backgroundColor: bgColor ? bgColor : Colors.white, borderBottomWidth: bdw ? 1 : bdw }]} >
                <TouchableOpacity style={styles.left} onPress={() => this.goBack()}>
                    <View style={styles.back}>
                        <Icon name='angle-left' color={color} size={24} />
                    </View>
                </TouchableOpacity>
                <Text style={styles.center}>{title}</Text>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        height: 50,
        borderBottomColor: Colors.white,
        borderBottomWidth: 1,
        width: Dimensions.get('screen').width
    },
    left: {
        flex: 1,
        // margin: 10,
        marginRight: 30,
    },
    center: {
        flex: 6,
        left: -20,
        fontWeight: 'bold',
        textAlign: 'left',
        fontSize: 17,
        color: Colors.textColor,
        fontFamily: Fonts.SairaSemiCondensedRegular
    },
    right: {
        flexDirection: 'row',
        flex: 1,
        justifyContent: 'flex-end',
        right: 10
    },
    filter: {
        width: 30,
        height: 30,
        borderWidth: 1,
        borderColor: Colors.mainColor,
        borderRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
        top: 7,
        right: 2,
    },
    back: {
        alignItems: 'center',
        justifyContent: 'center',
        width: sizeWidth(10),
        height: sizeWidth(10),
    }
})

export default HeaderList;