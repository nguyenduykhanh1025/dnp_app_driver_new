import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";
import { colorOpacityMaker, commonStyles, Colors } from '@/commons'

export default class TextStatus extends Component {
  render() {
      const { title, data, status, colors } = this.props
      const color = status == 0 ? '#F1C09f' : status == 1 ? '#F1C00f' : status == 2 ? '#0F8FFF' : status == 3 ? '#0F6F2F' : status == 4 ? '#0F8F7F' : status == 5 ? '#8F5FF1' : '#f2f'
    return (
        <View style={styles.container}>
            <View style={[commonStyles.f1,{paddingVertical:7}]}>
                <Text style={[styles.title,{color:colors?colors: Colors.titleColor}]}>{title}</Text>
            </View>
            <View style={{justifyContent:'center', alignItems:'center', padding:10, height:35, borderRadius:20 ,backgroundColor:colorOpacityMaker(color,50)}}>
                <Text style={{fontSize:15 ,color: Colors.blueColor ,fontWeight:'normal' }}>{data}</Text>
            </View>
        </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
        flexDirection:'row', 
        paddingTop:15,
    },
    title: {
        fontSize:14, 
        fontWeight:'normal', 
        color:Colors.titleColor
    }
});