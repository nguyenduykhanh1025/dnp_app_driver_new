import React, { Component } from 'react'
import { Text, View, StyleSheet, TextInput, TouchableOpacity, Image } from 'react-native'
import { Colors } from '@/commons'


export default class InputAnimation extends Component {
    render() {
        const {
            value,
            onChangeText,
            keyboardType,
            secureTextEntry,
            placeholderTextColor,
            placeholder,
            editable,
            img,
            onPress,
            width
          } = this.props;
        return (
            <View style={styles.rowInput}>
                { value != '' ? 
                <View style={styles.viewLable}>
                    <Text style={styles.textLable}>
                            {placeholder}
                        </Text>
                </View> : null }
                <TextInput 
                    style={[styles.TextInput]}
                    value={value}
                    editable={editable}
                    autoCapitalize="none"
                    keyboardType={keyboardType}
                    secureTextEntry={secureTextEntry}
                    onChangeText={onChangeText}
                    onFocus={onPress}
                    placeholder={placeholder}
                    underlineColorAndroid="transparent"
                    placeholderTextColor={placeholderTextColor}
                />
                { img ?
                <TouchableOpacity style={styles.iconView} onPress={onPress}>
                    <Image source={img} style={styles.icon}/>
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
        marginTop:-8, 
        marginLeft:10,
        position:'absolute'
    },
    textLable:{
        fontSize:12,
        left:10
    },
    rowInput:{
        borderColor:Colors.mainColor, 
        borderWidth:1,
        // borderRadius:10, 
        height:40,
        flexDirection:'row',
        marginTop:15,
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
    }
})
