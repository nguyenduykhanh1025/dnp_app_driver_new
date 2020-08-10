import React, { Component } from 'react'

import {
    TouchableOpacity,
    StyleSheet,
    SafeAreaView,
    Text,
    Image,
    StatusBar,
    View
} from 'react-native';
import { mainStack, homeTab } from '@/config/navigator';
import {
    home_icon,
    home_user,
    history_icon,
    notify_icon,
    command_icon
} from '@/assets/icons/index';
import { maincolor, grey5, white } from '@/commons/Colors'
import {
    sizes,
    sizeWidth,
    Colors,
    sizeHeight,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons'

let unseen = 0;

const getUnseenNumber = () => {
    unseen = 6;
    return unseen;
}

const Tabbar = (props) => {
    const { navigation } = props;
    const data = [
        {
            iconName: home_icon,
            name: 'Trang chủ',
            router: homeTab.home,
            isShow: true,
        },
        // {
        //     iconName: history_icon,
        //     name: 'Lịch sử',
        //     router: homeTab.history,
        //     isShow: false,
        // },
        {
            iconName: command_icon,
            name: 'Làm lệnh',
            router: mainStack.detail,
            bgRound: true,
            isShow: true,
        },
        // {
        //     iconName: home_user,
        //     name: 'Tài khoản',
        //     router: homeTab.profile,
        //     isShow: true,
        // },
        {
            iconName: notify_icon,
            name: 'Thông báo',
            router: homeTab.notification,
            isShow: true,
        },
        
    ];

    //size={navigation.state.index === index ? sizes.h1 : sizes.h3}
    return (
        <SafeAreaView style={[styles.container,]}>
            <StatusBar
                translucent
                backgroundColor='transparent'
            />
            {data.map((tab, index) => tab.isShow ? (
                <TouchableOpacity
                    key={index}
                    style={[styles.item]}
                    onPress={() => {
                        navigation.navigate(tab.router)
                    }}
                >
                    {
                        !tab.bgRound ?
                            (<View style={[navigation.state.index === index ? styles.iconContainer : null]}>
                                <Image
                                    source={tab.iconName}
                                    style={[styles.icon, navigation.state.index === index ? styles.activeIcon : null]}
                                    resizeMode='contain'
                                />
                            </View>)
                            :
                            (<View style={styles.iconCommand}>
                                <View style={[styles.bgRound]}>
                                    <Image
                                        source={tab.iconName}
                                        style={[styles.iconBgRound]}
                                        resizeMode='contain'
                                    />
                                    {
                                        false ?
                                            <View style={[styles.badgeIcon]}>
                                                <Text style={styles.badgeText}>1</Text>
                                            </View>
                                            :
                                            null
                                    }
                                </View>

                            </View>)
                    }
                    {
                        tab.name == 'Thông báo' && getUnseenNumber() != 0 ?
                        <View style = {styles.notifyNumber}>
                            <Text style = {styles.number}>{getUnseenNumber() > 99 ? '99+' : getUnseenNumber()}</Text>
                        </View>
                        : null
                    }
                </TouchableOpacity>
            ) :
                null)}
        </SafeAreaView>
    )
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: Colors.white,
        width: ws(375),
        height: hs(73),
        flexDirection: 'row',
        alignSelf: 'center',
        bottom: 0,
        shadowColor: 'rgba(0, 0, 0, 0.05)',
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
        flex: 1,
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
        borderTopWidth: hs(3),
        borderColor: Colors.maincolor,
        height: hs(73)
    },
    bgRound: {
        width: ws(79),
        height: ws(79),
        borderRadius: 200,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: Colors.maincolor,
        borderWidth: ws(5), borderColor:
            Colors.bgGrey
    },
    iconBgRound: {
        width: ws(42),
        height: hs(28),
        tintColor: Colors.white
    },
    activeIconBgRound: {

    },
    iconCommand: {
        paddingBottom: hs(32)
    },
    badgeIcon: {
        position: 'absolute',
        backgroundColor: Colors.badgeColor,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 100,
        height: ws(18),
        left: ws(44),
        top: hs(16),
    },
    badgeText: {
        marginVertical: hs(1),
        marginHorizontal: ws(5)
    },
    notifyNumber: {
        height: hs(18),
        width: hs(18),
        borderRadius: hs(25),
        backgroundColor: '#FF6060',
        position: 'absolute',
        top: hs(12),
        right: ws(47),
        justifyContent: 'center',
        alignItems: 'center'
    },
    number: {
        color: 'white',
        fontSize: fs(13),
        fontWeight: 'bold'
    }
});

export default Tabbar;