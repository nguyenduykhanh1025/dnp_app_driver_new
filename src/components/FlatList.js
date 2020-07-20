import React, { Component } from "react";
import { FlatList, StyleSheet, Dimensions } from "react-native";

export default class flatList extends Component {
  render() {
    return (
      <FlatList
        {...this.props}
      />
    );
  }
}

const styles = StyleSheet.create({
  textInput: {
    width: "90%",
    alignSelf: "center",
    borderBottomColor: "#c4c3c3",
    borderBottomWidth: 1.5,
    color: "#242222",
    height: 40,
    fontSize: 15,
    fontWeight: "bold"
  }
});