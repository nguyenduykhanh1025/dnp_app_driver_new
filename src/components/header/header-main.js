import React, { Component } from 'react';
import { Text, View, StyleSheet, TouchableOpacity } from 'react-native';
import { sizes, sizeWidth, sizeHeight, Colors } from '@/commons'

export default class HeaderMain extends Component {

  render() {
    var { backgroundColor, title, left, right, onPress } = this.props;
    return (
      <View style={[styles.bg, { backgroundColor: backgroundColor }]}>
        <View style = {styles.container}>
          <TouchableOpacity onPress={() => onPress()}>
            <View style={[styles.left]}>{left}</View>
          </TouchableOpacity>
          <View style={[styles.right]}>{right}</View>
          <View style={[styles.center]}>
            {title ?
              <Text style={styles.title}>{title}</Text>
              : null
            }
          </View>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  bg: {
    width: sizeWidth(100),
    height: sizeHeight(13),
    paddingTop: sizeHeight(5),
    justifyContent: 'flex-end',
  },
  container: {
    flexDirection: 'row',
    width: sizeWidth(100),
    height: sizeHeight(8),
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  left: {
    flex: 0.55,
    justifyContent: 'center',
    paddingLeft: sizeWidth(4),
  },
  center: {
    marginHorizontal: sizeWidth(25),
    position: 'absolute',
    width: sizeWidth(50),
    justifyContent: 'center',
    alignItems: 'center',
  },
  right: {
    flex: 0.55,
    justifyContent: 'center',
    alignItems: 'flex-end',
    paddingRight: sizeWidth(4),
  },
  title: {
    color: Colors.white,
    fontSize: sizes.h3,
    fontWeight: 'bold',
  }
})