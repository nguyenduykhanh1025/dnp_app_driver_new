import React, { Component } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

import Checkbox from '../../components/checkbox/checkbox';

export default class CheckboxView extends Component {
    render() {
        var { onSelect, name, value, selectedValue } = this.props;
        return (
            <TouchableOpacity onPress={() => { onSelect() }}>
                <View style={styles.CheckboxView}>
                    <Checkbox
                        value={value}
                        onSelect={() => { onSelect() }}
                        selectedValue={selectedValue}
                    />
                    <Text style={styles.nameCheckView}>{name}</Text>
                </View>
            </TouchableOpacity>
        )
    }
}

const styles = StyleSheet.create({
    CheckboxView: {
        height: 40,
        // backgroundColor: 'blue',
        flexDirection: 'row',
        marginTop: 15,
        justifyContent: 'center',
        alignItems: 'center',
    },
    nameCheckView: {
        margin: 5,
    }
})