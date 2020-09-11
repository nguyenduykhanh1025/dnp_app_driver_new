import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import { commonStyles, Colors, sizeWidth, sizeHeight } from '@/commons';
import moment from 'moment';
import NotifyDetailModal from './notifi-detail-modal';

const ic_c = require('@/assets/icons/Group-c.png')
const ic_l = require('@/assets/icons/Group-l.png')

export default class Item extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modalVisible: false
    }
  }

  onClose = () => {
    this.setState({ modalVisible: false })
  }

  render() {
    var { data, onPress, index } = this.props;
    console.log('data', data.seenFlg)
    var date = moment(data.createTime).format('DD-MM-YYYY')
    return (
      <TouchableOpacity onPress={() => this.setState({ modalVisible: true })} style={styles.itemContainer}>
        <View style={styles.imageView}>
          {
            data.notificationType == 'RM' ?
              <Image source={ic_c} style={styles.image} />
              :
              <Image source={ic_l} style={styles.image} />
          }
        </View>
        <View style={[styles.ContentView]}>
          <Text style={[styles.title, !data.seenFlg ? { fontWeight: 'bold' } : null]}>{data.title}</Text>
          <Text style={styles.description}>{data.content}</Text>
          <View style={{
            justifyContent: 'space-between',
            alignItems: 'center',
            flexDirection: 'row',
            width: sizeWidth(70),
          }}>
            <Text style={styles.datetime}>{date}</Text>
            <Text style={styles.datetime}>{data.seenFlg ? 'Đã xem' : 'Chưa xem'}</Text>
          </View>
        </View>
        <NotifyDetailModal
          onClose={this.onClose}
          visible={this.state.modalVisible}
          data={data}
          index={index}
        />
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
    fontSize: 12,
    color: Colors.tinyTextGrey,
    marginTop: sizeHeight(0.5)
  },
})