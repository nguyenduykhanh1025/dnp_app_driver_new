import React, { Component } from 'react';
import { View, StyleSheet, TouchableOpacity, Text, Dimensions } from 'react-native';
import { Colors } from '@/commons';
import { Icons } from '@/components';

class HeaderSearch extends Component {

    render() {
        const {title, iconR, iconL, onClose } = this.props;
        return (
            <View style={styles.container} >
                <TouchableOpacity style={styles.left} onPress={onClose}>
                    <Icons name={iconL?iconL:'filter'} color={Colors.mainColor} size={20} />
                </TouchableOpacity>
                <Text style={styles.center}>{title}</Text>
                <View  style={styles.right}>
                    <TouchableOpacity  style={{margin:10, marginRight:0}} onPress={this.gohome}>
                        <Icons name={iconR?iconR:''} color='#fff' size={24}/>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flexDirection:'row',
        alignItems: 'center',
        backgroundColor: Colors.mainColor,
        height:50,
        borderBottomColor:'#ddd',
        borderBottomWidth:1,
        width:'100%',
        borderTopLeftRadius:5,
    },
    left: {
        marginLeft:20,
        height:38,
        width:38,
        borderWidth:1,
        justifyContent:'center',
        alignItems:'center',
        borderRadius:20,
        borderColor:'#fff',
        backgroundColor:'#f1f1f1'
    },
    center: {
        flex:6,
        left:-20,
        fontWeight:'bold',
        textAlign:'center',
        fontSize:17,
        color:'#fff'
    },
    right: {
        flexDirection:'row',
        flex:1,
        justifyContent:'flex-end',
        right:10,
    },
    filter:{
        width:30,
        height:30,
        borderWidth:1,
        borderColor:'#fff',
        borderRadius:30,
        justifyContent:'center',
        alignItems:'center',
        top:7,
        right:2,
    }
})

export default HeaderSearch;