import { connect } from 'react-redux'
import React, { Component } from 'react';
import { View, Text, ScrollView, AsyncStorage, Animated, StatusBar, TouchableWithoutFeedback, ToastAndroid, Image, TextInput, Modal, StyleSheet, TouchableOpacity } from 'react-native';
import { Icon } from '@/components';;
import {
    commonStyles, Colors, sizeHeight,
    sizeWidth,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
} from '@/commons'
import Toast from 'react-native-tiny-toast';
import { Checkbox } from '@/components';
import { callAPI } from '@/requests'
import { HeaderPopUp } from '../header';
import { getToken } from '@/stores';
import utf8 from 'utf8';
import { SearchBox } from '../searchPopup';

class TruckModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedCustomer: undefined,
            loading: true,
            data: [],
            UserId: '',
            truckNoList: [],
            chassisNoList: [],
            page: 2,
            search: '',
            VcDate: '',
            topAnimated: new Animated.Value(0),
            bottomAnimated: new Animated.Value(0),
        },
            this.token == null
    }
    _onSelected = (item) => {
        this.setState({
            selectedCustomer: item
        });
        this._onSaveClick(item)
    }
    componentDidMount = async () => {
        this.token = await getToken();
    }
    componentWillReceiveProps = async (nextProps, nextState) => {
        if (nextProps.visible) {
            await this.animate()
            this.getData()
        }
        if (nextProps.visible == false) {
            this.setState({
                topAnimated: new Animated.Value(0),
                bottomAnimated: new Animated.Value(0),
            })
        }
        if (nextProps.VcDate) {
            await this.setState({
                VcDate: nextProps.VcDate,
            })
        }
    }

    animate = () => {
        Animated.timing(
            this.state.topAnimated,
            {
                toValue: 1,
                duration: 0
            }
        ).start()
        Animated.timing(
            this.state.bottomAnimated,
            {
                toValue: 1,
                duration: 1000
            }
        ).start()
    }

    getData = async () => {
        const params = {
            api: 'user/info',
            param: '',
            token: this.token,
            method: 'GET'
        }
        var result = undefined;
        result = await callApi(params);
        if (result.code == 0) {
            await this.setState({
                truckNoList: result.data.truckNoList,
                chassisNoList: result.data.chassisNoList,
            })
        }
    }

    isCloseToBottom = ({ layoutMeasurement, contentOffset, contentSize }) => {
        const paddingToBottom = 10;
        return (
            layoutMeasurement.height + contentOffset.y >=
            contentSize.height - paddingToBottom
        );
    };

    _loadMore = async () => {
    }
    _onSaveClick = (item) => {
        this.props.onClose();
        this.props._onSave(item);
    }

    render() {
        var { data, onClose, visible, truckCheck } = this.props;
        const coordinates = this.state.topAnimated.interpolate({
            inputRange: [0, 1],
            outputRange: [0, sizeHeight(33)]
        })
        const coordinates2 = this.state.bottomAnimated.interpolate({
            inputRange: [0, 1],
            outputRange: [sizeHeight(67), 0]
        })
        return (
            <Modal
                animationType="none"
                transparent={true}
                visible={visible}
                onRequestClose={() => {
                    this.props.onClose()
                }}>
                <TouchableWithoutFeedback onPress={() => {
                    this.props.onClose()
                }}>
                    <Animated.View style={[styles.container]}>
                        {/* <Animated.View style={[styles.view_top, { transform: [{ translateY: coordinates }] }]}>
                        </Animated.View> */}
                        <TouchableWithoutFeedback onPress={() => ''}>
                            <Animated.View style={[styles.content, { transform: [{ translateY: coordinates2 }] }]}>
                                <StatusBar translucent backgroundColor={Colors.bgModal} />
                                <HeaderPopUp title='Danh sách xe' onClose={this.props.onClose} btnR style={{ borderTopLeftRadius: sizeWidth(5), borderTopRightRadius: sizeWidth(5) }} />
                                {/* <SearchBox
                                    placeholder="Nhập thông tin cần tìm"
                                    onSearch={this._onSearch}
                                    btnNull /> */}
                                <ScrollView style={{ flex: 1, padding: 15 }}
                                    keyboardShouldPersistTaps='always'
                                    onMomentumScrollEnd={({ nativeEvent }) => {
                                        if (this.isCloseToBottom(nativeEvent)) {
                                            this._loadMore();
                                        }
                                    }}>
                                    {
                                        truckCheck ?
                                            this.state.truckNoList.length != 0 ?
                                                this.state.truckNoList.map((item, index) => {
                                                    return (
                                                        <TouchableOpacity style={[commonStyles.flexRow]} key={index} onPress={() => this._onSelected(item)}>
                                                            <Checkbox
                                                                value={item}
                                                                onSelect={(item) => this._onSelected(item)}
                                                                selectedValue={this.state.selectedCustomer}
                                                            />
                                                            <View style={[commonStyles.listItem]}>
                                                                <Text style={styles.itemCaption}>
                                                                    {item}
                                                                </Text>
                                                            </View>
                                                        </TouchableOpacity>
                                                    )
                                                }) : <View style={[commonStyles.flexRow, { justifyContent: 'center' }]}><Text style={styles.itemCaption}>Không có dữ liệu </Text></View>
                                            :
                                            this.state.chassisNoList.length != 0 ?
                                                this.state.chassisNoList.map((item, index) => {
                                                    return (
                                                        <TouchableOpacity style={[commonStyles.flexRow]} key={index} onPress={() => this._onSelected(item)}>
                                                            <Checkbox
                                                                value={item}
                                                                onSelect={(item) => this._onSelected(item)}
                                                                selectedValue={this.state.selectedCustomer}
                                                            />
                                                            <View style={[commonStyles.listItem]}>
                                                                <Text style={styles.itemCaption}>
                                                                    {item}
                                                                </Text>
                                                            </View>
                                                        </TouchableOpacity>
                                                    )
                                                }) : <View style={[commonStyles.flexRow, { justifyContent: 'center' }]}><Text style={styles.itemCaption}>Không có dữ liệu </Text></View>
                                    }
                                </ScrollView>
                            </Animated.View>
                        </TouchableWithoutFeedback>
                    </Animated.View>
                </TouchableWithoutFeedback>
            </Modal>
        )
    }
}
export default connect(null, null)(TruckModal)


const styles = StyleSheet.create({
    itemCaption: {
        fontSize: 18,
    },
    container: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    view_top: {
        borderBottomLeftRadius: 5,
        borderBottomRightRadius: 5,
        backgroundColor: Colors.bgModal,
        height: sizeHeight(36),
        justifyContent: 'flex-end',
        marginTop: -sizeHeight(35),
    },
    content: {
        flex: 1,
        backgroundColor: '#fff',
        top: sizeHeight(20),
        borderRadius: sizeWidth(5),
    },
})
