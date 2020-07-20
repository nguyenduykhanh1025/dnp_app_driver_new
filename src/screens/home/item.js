import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import { commonStyles, Colors, sizeWidth, sizeHeight } from '@/commons';
import {righticon} from '@/assets/icons'

export default class Item extends Component {
  render() {
    var { data, onPress } = this.props;
    return (
      <TouchableOpacity onPress={onPress} style={styles.itemContainer}>

        <Image 
          source={righticon} 
          style={styles.next} 
          resizeMode = 'contain'
        />
        <View style = {styles.textContainer}>
          <Text style = {styles.text}>Mã lô</Text>
          <Text style = {styles.codeText}>{data.batchCode}</Text>
        </View>
        <View style = {styles.textContainer}>
          <Text style = {styles.text}>Số bill</Text>
          <Text style = {styles.billText}>{data.billNumber}</Text>
        </View>
        <View style = {styles.textContainer}>
          <Text style = {styles.text}>Ghi chú</Text>
          <Text style = {styles.noteText}>{data.note}</Text>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  itemContainer: {
    width: '100%',
    alignSelf: 'center',
    paddingVertical: sizeWidth(2),
    borderBottomWidth: 2,
    borderColor: '#EFF1F5',
    borderRadius: 4,
    margin: 9,
    justifyContent: 'center',
  },
  batchCode: {
    justifyContent: 'space-between',
    width: sizeWidth(83),
  },
  next: {
    width: sizeWidth(4),
    height: sizeWidth(4),
    alignSelf: 'center',
    right: 0,
    position: 'absolute',
  },
  text: {
    color: '#86889E',
    fontSize: 13,
    lineHeight: 15,
    marginRight: sizeWidth(7),
  },
  textContainer: {
    marginVertical: sizeHeight(1),
    flexDirection: 'row',
    alignItems: 'center',
  },
  codeText: {
    color: '#1B5198',
    fontSize: 15,
    lineHeight: 18,
    fontWeight: 'bold',
  },
  billText: {
    color: '#F3B03F',
    fontSize: 15,
    lineHeight: 18,
    fontWeight: 'bold',
  },
  noteText: {
    fontSize: 13,
    lineHeight: 15,
    color: '#000000',
  }
})