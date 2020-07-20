import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import { commonStyles, Colors, sizeWidth, sizeHeight } from '@/commons';

const ic_c = require('@/assets/icons/Group-c.png')
const ic_l = require('@/assets/icons/Group-l.png')

export default class Item extends Component {
  render() {
    var { data, onPress, index } = this.props;
    return (
      <TouchableOpacity onPress={onPress} style={styles.itemContainer}>
        <View style={styles.imageView}>
          {
            index % 2 == 0 ?
              <Image source={ic_c} style={styles.image} />
              :
              <Image source={ic_l} style={styles.image} />
          }
        </View>
        <View style={styles.ContentView}>
          <Text style={styles.title}>{data.title}</Text>
          <Text style={styles.description}>{data.description}</Text>
          <Text style={styles.datetime}>{data.datetime}</Text>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  itemContainer: {
    width: sizeWidth(95),
    flexDirection: 'row',
    borderBottomColor: '#EFF1F5',
    borderBottomWidth: 1.5,
    borderStyle: 'solid'
  },
  imageView: {
    width: sizeWidth(23),
    height: sizeWidth(28),
    justifyContent: 'center',
    alignItems: 'center',
  },
  ContentView: {
    justifyContent: 'center',
  },
  image: {
    width: sizeWidth(10),
    height: sizeWidth(10),
  },
  title: {
    width: sizeWidth(70),
    fontSize: 15,
    // fontWeight: 'bold'
  },
  description: {
    width: sizeWidth(70),
    fontSize: 15,
    color: Colors.tinyTextGrey,
  },
  datetime: {
    width: sizeWidth(70),
    fontSize: 12,
    color: Colors.tinyTextGrey,
    marginTop: sizeHeight(0.5)
  },
})