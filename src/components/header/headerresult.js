import React, { Component } from 'react';
import { Text, View, TouchableOpacity, Image, StyleSheet } from 'react-native';
import Toast from 'react-native-tiny-toast';
import { Colors, sizeWidth, sizeHeight, sizes } from '@/commons';

const ic_close = require('@/assets/icons/ic_back_menu.png')

export default class headerresult extends Component {
  render() {
    var { onBack } = this.props;
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={onBack} style={styles.wrapper}>
          <Image source={ic_close} style={styles.ic_close} />
        </TouchableOpacity>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    height: sizeHeight(5),
    justifyContent: 'center',
    alignItems: 'flex-end',
    backgroundColor:Colors.white
  },
  wrapper: {
    paddingHorizontal:sizeWidth(3),
    alignSelf: 'flex-end',
    justifyContent: 'center',
    // flex: 1,
  },
  contentView: {
    paddingLeft: sizeWidth(5)
  },
  ic_close: {
    width: sizeWidth(4),
    height: sizeWidth(4),
    paddingRight: sizeWidth(3),
  },
  close: {
    alignItems: 'flex-end',
    padding: sizeWidth(3),
  },
  text: {
    paddingVertical: sizeWidth(1)
  }
})
