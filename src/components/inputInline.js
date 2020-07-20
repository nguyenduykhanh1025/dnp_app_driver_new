import React, { Component } from 'react'
import { Text, View, StyleSheet, TextInput, TouchableOpacity, Image } from 'react-native'
import { Colors, Fonts } from '@/commons'
import Icon from 'react-native-vector-icons/FontAwesome';


export default class InputInline extends Component {
    render() {
        const {
            value,
            onChangeText,
            keyboardType,
            secureTextEntry,
            placeholderTextColor,
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
                <TextInput 
                    style={[styles.TextInput]}
                    value={value}
                    editable={editable}
                    autoCapitalize="none"
                    keyboardType={keyboardType}
                    secureTextEntry={secureTextEntry}
                    onChangeText={onChangeText}
                    onFocus={onFocus}
                    underlineColorAndroid="transparent"
                    placeholderTextColor={placeholderTextColor}
                />
                { nameIcon ?
                <TouchableOpacity style={styles.iconView} onPress={onPress}>
                    <Icon name={nameIcon} size={20} color='#AAB0CC'/>
                </TouchableOpacity> : null
                }
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
