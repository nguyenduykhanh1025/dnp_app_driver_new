import React, { Component } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';

// import Colors from '../Styles/Colors';
import Icon from 'react-native-vector-icons/FontAwesome';

import screen from '../../utils/screen';
// import Checkbox from '../checkbox/checkbox';

export default class ModalItem extends Component {

    render() {
        var { item } = this.props;
        return (
            <View style={styles.listItem}>
                <Text style={styles.itemCaption}>
                    {item.id}-{item.name}
                </Text>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    listItem:{
        marginLeft:15,
        position:'relative',
        paddingVertical:20,
        borderBottomWidth:1,
        borderBottomColor:'#ddd',
        flex:1
    },
})