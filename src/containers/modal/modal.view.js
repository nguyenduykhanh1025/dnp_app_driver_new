import React, { Component } from 'react';
import {
    View,
    Modal,
    TouchableOpacity,
    StyleSheet,
    Animated,
    Dimensions,
    Text,
    Image,
    ScrollView
} from 'react-native';

import screen from '../../utils/screen';
import SeachBox from '../../components/searchPopup/searchBox';
import ModalItem from '../../components/modal/modal.item';
import HeaderList from '../../components/header/headerdanhsach';
import Checkbox from '../../components/checkbox/checkbox';
import datapopup from '../../mock/popup.mock';

export default class ModalView extends Component {
    state = {
        data: [],
        title: '',
    }

    componentDidMount = async () => {
        await this.setState({
            data: datapopup,
            selected:undefined,
        })
    }

    onSelect = (item) => {
        this.setState({
            selected: item
        });
    }

    render() {

        const { params } = this.props.navigation.state;
        return (
            <View style={styles.container}>
                <HeaderList title={params.param.title} navigation={this.props}/>
                <View style={styles.body}>
                    <View style={styles.Search}>
                        <SeachBox />
                    </View>
                    <ScrollView style={{ flex: 1, padding: 15, paddingTop:-15 }}>
                        {
                            this.state.data !== undefined ?
                                this.state.data.map((item, index) => {
                                    return (
                                        <TouchableOpacity key={index} onPress = {() => this.onSelect(item)}>
                                        <View style={{flexDirection: 'row',alignItems: 'center'}} >
                                            <Checkbox
                                                value={item}
                                                onSelect={this.onSelect}
                                                selectedValue={this.state.selected}
                                            />
                                            <ModalItem item={item}/>
                                        </View>
                                        </TouchableOpacity>)
                                }) : <View style={{flexDirection: 'row',alignItems: 'center', justifyContent: 'center' }}><Text>Null</Text></View>
                        }
                    </ScrollView>
                </View>
                {/* <View style={styles.footer}>
                    <TouchableOpacity
                        style={styles.btSelect}
                    >
                        <Text>BỎ CHỌN</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={styles.btSelect}
                    >
                        <Text>CHỌN</Text>
                    </TouchableOpacity>
                </View> */}
            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FFF'
    },
    header: {
        width: screen.widthsc,
        height: screen.heightsc / 19,
        flexDirection: 'row',
        justifyContent: 'center'
    },
    nameHeader: {
        fontSize: 20,
        color: '#BBBBBB',
        margin: screen.heightsc / 100,
    },
    btClose: {
        position: 'absolute',
        right: 0,
    },
    body: {
        flex: 1,
    },
    footer: {
        flexDirection: 'row',
        justifyContent: 'center',
        width: screen.widthsc,
        height: screen.heightsc / 12,
    },
    btSelect: {
        backgroundColor: '#FFF',
        width: screen.widthsc / 2.5,
        height: screen.heightsc / 16,
        margin: 5,
        marginBottom: 10,
        alignItems: 'center',
        justifyContent: 'center',
        borderWidth: 0.8,
        borderRadius: 5,
        borderColor: '#BBBBBB',

    },

    
})
