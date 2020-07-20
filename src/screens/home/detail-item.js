import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import { commonStyles, Colors, sizeWidth, sizeHeight } from '@/commons';
import {righticon} from '@/assets/icons'

export default class ListItem extends Component {
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
          <Text style = {styles.text}>Mã Cont</Text>
          <Text style = {styles.codeText}>{data.batchCode}</Text>
        </View>
        <View style = {styles.textContainer}>
          <Text style = {styles.text}>Size</Text>
          <Text style = {[styles.billText, {marginRight: sizeWidth(20)}]}>{data.size}</Text>
          <Text style = {styles.text}>Type</Text>
          <Text style = {styles.billText}>{data.type}</Text>
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
    width: '99%',
    alignSelf: 'center',
    padding: sizeWidth(2),
    shadowColor: "#000",
    shadowOffset: {
        width: 0,
        height: 1,
    },
    shadowOpacity: 0.22,
    shadowRadius: 2.22,
    elevation: 6,
    borderRadius: 10,
    margin: 9,
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
  },
  batchCode: {
    justifyContent: 'space-between',
    width: sizeWidth(83),
  },
  next: {
    width: sizeWidth(4),
    height: sizeWidth(4),
    alignSelf: 'center',
    right: sizeWidth(2),
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