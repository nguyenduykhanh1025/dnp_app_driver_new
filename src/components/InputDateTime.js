import React, { Component } from 'react'
import { Text, View, StyleSheet, TextInput, TouchableOpacity, Image, Dimensions } from 'react-native'
import { Colors } from '@/commons'
import DatePicker from 'react-native-datepicker'
import Icon from 'react-native-vector-icons/FontAwesome'

const { width, height } = Dimensions.get("window");

const vh = height / 100;
const vw = width / 100;

const sizeWidth = size => {
    return size * vw;
};
const sizeHeight = size => {
    return size * vh;
};


export default class InputDateTime extends Component {
    render() {
        const {
            value,
            onDateChange,
            format,
            placeholderTextColor,
            placeholder,
            img,
            width
          } = this.props;
        const widthIcon =  Dimensions.get('window').width* -(0.58);
        return (
            <View style={[styles.rowInput,{width:width?width:null}]}>
                { value != '' ? 
                <View style={styles.viewLable}>
                    <Text style={styles.textLable}>{placeholder}</Text>
                </View> : null }
                <DatePicker
                    date={value}
                    mode="date"
                    placeholder={placeholder}
                    format={format}
                    confirmBtnText="Ok"
                    cancelBtnText="Hủy"
                    style={[styles.dateInput]}
                    iconComponent={<Icon name="calendar" size={16} style={{ marginRight: 2 }} />}
                    customStyles={{
                      dateInput: {
                        borderWidth: 0,
                        alignItems: "flex-start",
                        paddingLeft:15,
                        width: screen.widthsc
                      },
                    }}
                    onDateChange={onDateChange}
                    
                  />
            </View>
        )
    }
}
const styles = StyleSheet.create({
    viewLable: {
        fontSize:10,
        backgroundColor:'#EAEAEA',
        width:100, 
        marginTop:-8, 
        marginLeft:10,
        position:'absolute'
    },
    textLable:{
        fontSize:11,
        left:10
    },
   
    rowInput:{
        borderColor:Colors.blueColor, 
        borderWidth:1,
        borderRadius:4, 
        height:40,
        flexDirection:'row',
        marginTop:11,
        position:'relative',
    },
    iconView:{
        position:'absolute',
        right:5,
        top:10
    },
    icon:{
        width: 18,
        height:18,
    },
    TextInput:{
        height: 40,
        paddingVertical: 5,
        paddingLeft:15,
        width:'100%'
    },
    dateInput: {
        width: sizeWidth(93),
        height: sizeHeight(5.5),
        color: Colors.mainColor,
    },
})
