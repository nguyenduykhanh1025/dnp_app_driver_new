import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, StatusBar, ScrollView, ActivityIndicator, FlatList } from 'react-native';
import {
    Colors, widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';
import { HeaderMain } from '@/components';
import Icon from 'react-native-vector-icons/AntDesign';
import { getToken } from '@/stores';
import { ContainerCheckinItem } from '@/components/containerCheckin';

export default class ContainerCheckinList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            containerList: [],
            refreshing: false,
        }
    }

    componentDidMount = async () => {
        this.token = await getToken();
        this.getContainerlist();
    };

    //--------> On load data from server
    getContainerlist = () => {
        this.setState({
            containerList: [
                {
                    selected: true,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1025',
                },
                {
                    selected: true,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1026',
                },
                {
                    selected: false,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1027',
                },
                {
                    selected: false,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1028',
                },
                {
                    selected: false,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1028',
                },
                {
                    selected: false,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1028',
                },
                {
                    selected: false,
                    gateInDate: new Date(),
                    containerNo: 'KHANH1028',
                }
            ]
        })
    }
    //---------> end onload 

    //---------> render
    renderLeft = () => {
        return (
            <View
                style={{
                    marginLeft: ws(29.54)
                }}
            >
                <Icon name={'close'} size={20} />
            </View>
        )
    }

    renderItem = (item, index) => (
        <ContainerCheckinItem
            data={item.item}
            onSelectCont={(value) => this.onSelectCont(value)}
        />
    )
    //---------> end render

    onBack = () => {
        onClose();
    };

    onRefresh = () => {
        this.getContainerlist();
    }

    onSelectCont = (value) => {
        this.setState({
            containerList: this.state.containerList.map(item => {
                if (item.containerNo === value.containerNo) {
                    item.selected = !item.selected;
                }
                return item;
            })
        })
    }
    render() {
        var {
            onClose
        } = this.props;
        return (
            <View style={styles.container}>
                <StatusBar
                    translucent
                    barStyle='dark-content'
                />
                <HeaderMain
                    renderLeft={this.renderLeft()}
                    onPressLeft={onClose}
                    disableBG
                    disableStep
                />
                <View style={styles.Body}>
                    <ScrollView >
                        <View>
                            {
                                this.state.containerList.length == 0 ?
                                    <View style={{ height: hs(425), width: '100%', justifyContent: 'center', alignItems: 'center' }}>
                                        <ActivityIndicator size='large' color={Colors.mainColor} />
                                    </View>
                                    :
                                    <FlatList
                                        data={this.state.containerList}
                                        refreshing={this.state.refreshing}
                                        onRefresh={() => {
                                            this.onRefresh()
                                        }}
                                        renderItem={(item, index) => this.renderItem(item, index)}
                                    />
                            }
                        </View>
                    </ScrollView>

                </View>
                <View
                    style={styles.btnWrapper}
                >
                    <View style={[styles.btnContainer]}>
                        <TouchableOpacity onPress={onClose}>
                            <View style={styles.btnContainerCancel}>
                                <Text style={[styles.btnButtonTextCancel]}>
                                    {'Hủy'}
                                </Text>
                            </View>
                        </TouchableOpacity>
                    </View>

                    <View style={[styles.btnContainer]}>
                        <TouchableOpacity >
                            <View style={styles.btnButtonContainer}>
                                <Text style={[styles.btnButtonText]}>
                                    {'OK'}
                                </Text>
                            </View>
                        </TouchableOpacity>
                    </View>
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        height: '100%'
    },
    Body: {
        width: ws(375),
        height: hs(629),
    },
    contentView: {
        backgroundColor: Colors.white,
        height: hs(712),
    },
    TitleHistory: {
        marginLeft: ws(16),
        marginBottom: hs(14),
    },
    TitleHistoryText: {
        fontSize: fs(18),
        fontWeight: 'bold',
        color: Colors.black,
    },
    btnWrapper: {
        width: '100%',
        paddingLeft: 12,
        paddingRight: 12,
        position: 'absolute',
        bottom: 15,
        left: 0,
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'space-between',
    },

    btnContainer: {
        width: ws(170),
        justifyContent: 'center',
        alignItems: 'center',

    },
    btnButtonContainer: {
        width: ws(170),
        height: hs(55),
        backgroundColor: Colors.subColor,
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
    },
    btnContainerCancel: {
        width: ws(170),
        height: hs(55),
        backgroundColor: Colors.blue,
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
    },
    btnButtonText: {
        fontSize: fs(16),
        color: Colors.white,
        fontWeight: 'bold',
    },
    btnButtonTextCancel: {
        fontSize: fs(16),
        color: Colors.white,
        fontWeight: 'bold',
    }
})