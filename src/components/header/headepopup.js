import React, { Component } from 'react';
import { View, StyleSheet, TouchableOpacity, Text, Dimensions } from 'react-native';
import { Colors } from '@/commons'
class HeaderPopUp extends Component {

    render() {
        const {title, iconR, iconL, onClose , style} = this.props;
        return (
            <View style={[styles.container,style]} >
                <Text style={styles.center}>{title}</Text>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flexDirection:'row',
        alignItems: 'center',
        backgroundColor: Colors.subColor,
        height:50,
        borderBottomColor:'#ddd',
        borderBottomWidth:1,
        width:'100%',
    },
    center: {
        flex:1,
        fontWeight:'bold',
        textAlign:'center',
        fontSize:17,
        color:'#fff'
    },
})

export default HeaderPopUp;