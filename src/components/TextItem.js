import React, { Component } from "react";
import { Text, StyleSheet } from "react-native";

export default class TextItem extends Component {
  render() {
      const { title, data } = this.props
    return (
    <Text>
        {title} <Text style={{fontWeight:'bold'}}>{data}</Text>
    </Text>
    );
  }
}

const styles = StyleSheet.create({
});