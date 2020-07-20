import React, { Component } from 'react'
import { Text, View, StyleSheet, TextInput, TouchableOpacity, Image } from 'react-native'
import { Colors, Fonts } from '@/commons'
import DatePicker from 'react-native-datepicker'
import Icon from 'react-native-vector-icons/FontAwesome';
import { screen } from '@/utils';

export default class InputDateInline extends Component {
    render() {
        const {
            value,
            onDateChange,
            format,
            placeholder,
            editable,
            nameIcon,
            onPress,
            width,
            onFocus
          } = this.props;
        return (
            <View style={styles.rowInput}>
                <View style={styles.viewLable}>
                    <Text style={styles.textLable}>
                            {placeholder}
                        </Text>
                </View>
                <DatePicker
                    date={value}
                    mode="date"
                    format={format}
                    confirmBtnText="Ok"
                    cancelBtnText="Hủy"
                    iconComponent={
                    <Icon name={nameIcon} size={18} color='#AAB0CC' style={{top:15, right:20}}/>
                    }
                    style={{width:screen.widthsc}}
                    customStyles={{
                      dateInput: {
                        borderWidth: 0,
                        alignItems: "flex-start",
                        top:15,
                      },
                    }}
                    onDateChange={(date)=> onDateChange(date)}
                   
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
        position:'absolute'
    },
    textLable:{
        fontSize:13,
        color:'#AABBCC',
        fontWeight: 'normal',
        fontFamily:Fonts.SairaSemiCondensedRegular
    },
    rowInput:{
        borderColor:'#AABBCC', 
        borderBottomWidth:1,
        height:50,
        flexDirection:'row',
        position:'relative',
        marginTop:15
    },
    iconView:{
        position:'absolute',
        right:0,
        top:25
    },
    icon:{
        width: 18,
        height:18,
    },
    TextInput:{
        height: 40,
        top:10,
        width:'100%',
        paddingHorizontal:-10,
        color:'#556299',
        fontFamily:Fonts.SairaSemiCondensedMedium,
        fontSize:20,
        paddingVertical:-5
    }
})
