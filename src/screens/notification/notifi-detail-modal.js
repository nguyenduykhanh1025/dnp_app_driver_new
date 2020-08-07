import React, { Component } from 'react';
import {
    View, 
    Modal, 
    TouchableWithoutFeedback, 
    StyleSheet, 
    StatusBar,
    Image,
    Text,
    TouchableOpacity
} from 'react-native';
import {
    sizes,
    sizeWidth,
    Colors,
    sizeHeight,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons'
import moment from 'moment';

const ic_c = require('@/assets/icons/Group-c.png')
const ic_l = require('@/assets/icons/Group-l.png')
const back = require('@/assets/icons/ic_back_menu.png')

export default class NotifyDetailModal extends Component {
    render () {
        const {index, data} = this.props
        var date = moment(data.createTime).format('DD-MM-YYYY')
        return (
            <Modal
                transparent
                visible = {this.props.visible}
            >
                <StatusBar translucent backgroundColor = 'rgba(0, 0, 0, 0.5)'/>
                <TouchableWithoutFeedback onPress = {() => this.props.onClose()}>
                    <View style = {styles.bg}>
                        <TouchableWithoutFeedback onPress = {() => null}>
                            <View  style = {styles.content}>
                                <View style = {styles.header}>
                                    <View style = {styles.headerContent}>
                                        <View style={styles.imageView}>
                                            {
                                                index % 2 == 0 ?
                                                <Image source={ic_c} style={styles.image} />
                                                :
                                                <Image source={ic_l} style={styles.image} />
                                            }
                                        </View>
                                        <TouchableOpacity onPress = {() => this.props.onClose()}>
                                            <Image
                                                source = {back}
                                                style = {styles.backImage}
                                                resizeMode = 'contain'
                                            />
                                        </TouchableOpacity>
                                        <Text style = {styles.title}>{data.title}</Text>
                                    </View>
                                </View>
                                    <View style = {styles.body}>
                                        <Text style = {styles.text}>{data.content}</Text>
                                        <Text style = {styles.date}>Ngày: {date}</Text>
                                    </View>
                            </View>
                        </TouchableWithoutFeedback>
                    </View>
                </TouchableWithoutFeedback>
            </Modal>
        )
    }
}

const styles = StyleSheet.create({
    bg: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    content: {
        width: ws(320),

        backgroundColor: '#FFFFFF',
        borderRadius: hs(10)
    },
    header: {
        height: hs(80),
        borderTopLeftRadius: hs(10),
        borderTopRightRadius: hs(10),
        paddingHorizontal: ws(5)
    },
    headerContent: {
        borderBottomWidth: 1.5,
        borderBottomColor: '#EFF1F5',
        height: hs(80),
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    imageView: {
        width: hs(80),
        height: hs(80),
        justifyContent: 'center',
        alignItems: 'center',
    },
    image: {
        width: sizeWidth(10),
        height: sizeWidth(10),
    },
    backImage: {
        height: hs(20),
        width: hs(20),
        marginRight: ws(10),
    },
    title: {
        position: 'absolute',
        width: ws(180),
        left: ws(70),
        fontWeight: 'bold',
        fontSize: fs(20),
        textAlign: 'center',
    },
    body: {
        marginVertical: hs(10),
        marginHorizontal: ws(10),
    },
    text: {
        width: '100%',
        fontSize: 15,
        color: Colors.textColor,
    },
    date: {
        width: '100%',
        fontSize: 14,
        color: Colors.textColor,
        textAlign: 'right',
        marginTop: hs(5)
    }
})
