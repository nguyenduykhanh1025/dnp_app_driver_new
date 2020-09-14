import React, { Component } from 'react';
import { View, Text, StyleSheet, TextInput, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import { Colors } from '@/commons';
export default class SeachBox extends Component {
    constructor(props) {
        super(props);
        this.state = {
            searchVal: '',
        }
    }

    _handleChange = (searchVal) => {
        this.setState({
            searchVal: searchVal
        });
    }
    _renderClearButton = () => {
        if (this.state.searchVal !== '') {
            return (<TouchableOpacity onPress={() => { this.setState({ searchVal: '' }) }} style={styles.iconClose}><Icon name="close" size={20} color='red'></Icon></TouchableOpacity>)
        }
        return null;
    }

    render() {
        var { placeholder, style, left, btnNull } = this.props;
        return (
            <View style={{ flexDirection: 'row' }}>
                <View style={[styles.searchBox, { left: left == undefined ? 0 : left }]}>
                    <TextInput
                        underlineColorAndroid="transparent"
                        placeholder={placeholder}
                        style={[styles.inputSearch, style]}
                        value={this.state.searchVal}
                        onChangeText={(text) => this._handleChange(text)}
                    />
                    <Icon name="search" size={18} style={styles.iconSearch} color={'#D87245'}></Icon>
                    {this._renderClearButton()}
                </View>
                {
                    btnNull ? null :
                        <TouchableOpacity style={{ backgroundColor: '#0F8FFF', width: 40, height: 40, margin: 10, marginLeft: 0, borderRadius: 5, alignItems: 'center', justifyContent: 'center' }}>
                            <Icon name='sliders' size={20} color='#fff' />
                        </TouchableOpacity>
                }
            </View>

        )
    }
}
const styles = StyleSheet.create({
    searchBox: {
        backgroundColor: '#fff',
        borderRadius: 10,
        borderWidth: 0,
        padding: 1.5,
        margin: 10,
        justifyContent: 'center',
        flex: 1,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.25,
        shadowRadius: 3.84,

        elevation: 2,
    },
    inputSearch: {
        backgroundColor: '#fff',
        borderRadius: 10,
        paddingVertical: 5,
        paddingHorizontal: 40
    },
    iconSearch: {
        position: 'absolute',
        left: 15,
        marginRight: 5,
    },
    iconClose: {
        position: 'absolute',
        right: 15,
    }
});