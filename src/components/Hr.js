import React, { Component } from "react";
import { View } from "react-native";
export default class Hr extends Component {
  render() {
      const { color, height, width, top } = this.props
    return (
        <View style={{width: width? width:'100%',height:height?height:0.5, backgroundColor:color?color:'grey',top:top?top:5 , marginBottom:5}}>
        </View>
    );
  }
}