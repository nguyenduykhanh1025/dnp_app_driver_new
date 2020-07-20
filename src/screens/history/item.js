import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import { commonStyles, Colors, sizeWidth, sizeHeight } from '@/commons';
import {righticon, cont2_icon} from '@/assets/icons'

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
        <View style = {styles.bgIcon}>
        <Image
			source = {cont2_icon}
			resizeMode = 'contain'
			style = {styles.itemIcon}
        />
        </View>
         <View>
          <View style = {styles.textContainer}>
            <Text style = {styles.text}>Cont</Text>
            <Text style = {styles.codeText}>{data.contCode}</Text>
          </View>
          <View style = {styles.textContainer}>
            <Text style = {styles.text}>Size</Text>
            <Text style = {[styles.billText, {marginRight: sizeWidth(10)}]}>{data.contSize}</Text>
            <Text style = {styles.text}>Type</Text>
            <Text style = {styles.billText}>{data.contType}</Text>
          </View>
          <View style = {styles.textContainer}>
            <Text style = {styles.text}>Ngày bốc</Text>
            <Text style = {styles.noteText}>{data.datetime}</Text>
          </View>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  itemContainer: {
    width: '99%',
    alignSelf: 'center',
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
    backgroundColor: '#FFFFFF',
	flexDirection: 'row',
	height: sizeHeight(17),
	alignItems: 'center',
	paddingLeft: sizeWidth(25)
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
  },
  bgIcon: {
	backgroundColor: '#F3B03F',
	position: 'absolute',
	left: 0,
	top: 0,
	height: '100%',
	bottom: 0,
	width: sizeWidth(20),
	borderRadius: 10,
	justifyContent: 'center',
	alignItems: 'center',
  },
  itemIcon: {
	width: sizeWidth(10),
	height: sizeHeight(5),
  }
})