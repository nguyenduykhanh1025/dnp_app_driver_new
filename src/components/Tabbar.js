import React, { Component } from 'react'

import { 
    TouchableOpacity, 
    StyleSheet, 
    SafeAreaView , 
    Text, 
    Image,
    StatusBar,
    View
} from 'react-native';
import { mainStack , homeTab} from '@/config/navigator';
import {home_icon, home_user, history_icon, notify_icon} from '@/assets/icons/index';
import { maincolor, grey5, white} from '@/commons/Colors'
import { sizes, sizeWidth, Colors, sizeHeight } from '@/commons'


const Tabbar = (props) => {
    const { navigation } = props;
    const data = [
        {
            iconName: home_icon,
            name: 'Trang chủ',
            router: homeTab.home,
            isShow: true,
        },
        {
            iconName: history_icon,
            name: 'Lịch sử',
            router: homeTab.history,
            isShow: true,
        },
        {
            iconName: notify_icon,
            name: 'Thông báo',
            router: homeTab.notification,
            isShow: true,
        },
        {
            iconName: home_user,
            name: 'Tài khoản',
            router: homeTab.profile,
            isShow: true,
        },
    ];

    //size={navigation.state.index === index ? sizes.h1 : sizes.h3}
    return (
        <SafeAreaView style={[styles.container,]}>
            <StatusBar 
                translucent 
                backgroundColor = 'transparent'
            />
            {data.map((tab, index) => tab.isShow ? (
                <TouchableOpacity
                    key={index}
                    style={[styles.item]}
                    onPress={() => navigation.navigate(tab.router)}
                >
                    <View style = {[navigation.state.index === index ? styles.iconContainer : null]}>
                        <Image
                            source = {tab.iconName}
                            style = {[styles.icon, navigation.state.index === index ? styles.activeIcon : null]}
                            resizeMode = 'contain'
                        />
                    </View>
                </TouchableOpacity>
            ) : null)}
        </SafeAreaView>
    )
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: '#FFFFFF',
        width: sizeWidth(100),
        height: sizeHeight(8),
        flexDirection: 'row',
        alignSelf: 'center',
        bottom: 0,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 1,
        },
        shadowOpacity: 0.22,
        shadowRadius: 2.22,
        elevation: 6,
        // position: 'absolute'
    },
    item: {
        alignItems: 'center',
        flexDirection: 'row',
        alignSelf: 'center',
        justifyContent: 'center', 
        flex:1,
    },
    text: {
        paddingHorizontal: sizes.h6,
    },
    icon: {
        height: sizeHeight(7.5),
        tintColor: Colors.greyColor,
        width: sizeWidth(5),
    },
    activeIcon: {
        tintColor: Colors.maincolor,
    },
    iconContainer: {
        borderTopWidth: sizeHeight(0.4),
        borderColor: Colors.maincolor,
    },
});

export default Tabbar;