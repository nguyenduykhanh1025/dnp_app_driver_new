import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, Image } from 'react-native';
import {
  commonStyles,
  Colors,
  sizeWidth,
  sizeHeight,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import { righticon, cont2_icon } from '@/assets/icons';
import moment from 'moment';

export default class Item extends Component {
  render() {
    var { data, onPress } = this.props;
    var date = moment(data.gateInDate).format('DD-MM-YYYY')
    return (
      <TouchableOpacity onPress={onPress} style={{}}>
        <View style={styles.Container}>
          <View style={styles.ItemContainer}>
            <View style={styles.ItemLeft}>
              <Image source={cont2_icon} style={styles.IconLeft} />
            </View>
            <View style={styles.ItemRight}>
              <View style={styles.ItemRightContainer}>
                <View style={styles.ItemRightText}>
                  <View style={styles.ItemLineText}>
                    <Text style={styles.ItemLineLabel}>
                      Công
                  </Text>
                    <Text style={styles.ItemLineValue}>
                      {data.containerNo}
                    </Text>
                  </View>
                  <View style={[styles.ItemLineText, { marginTop: hs(10) }]}>
                    <Text style={styles.ItemLineLabel}>
                      Ngày bốc
                    </Text>
                    <Text style={styles.ItemLineDate}>
                      {date}
                    </Text>
                  </View>
                </View>
              </View>
              <Image source={righticon} style={styles.righticon} />
            </View>
          </View>
        </View>
      </TouchableOpacity>
    )
  }
}

const styles = StyleSheet.create({
  Container: {
    width: ws(375),
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: hs(15),
  },
  ItemContainer: {
    width: ws(345),
    height: hs(75),
    backgroundColor: Colors.white,
    borderRadius: 10,
    shadowColor: "rgba(0, 0, 0, 0.02)",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.41,
    shadowRadius: 9.1,
    elevation: 5,
    flexDirection: 'row',
  },
  ItemLeft: {
    width: ws(75),
    height: hs(75),
    backgroundColor: Colors.subColor,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  ItemRight: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: ws(270),
    height: hs(75),
    alignItems: 'center',
  },
  ItemRightContainer: {
    marginLeft: ws(21),
    height: hs(55),
  },
  ItemRightText: {

  },
  ItemLineText: {
    flexDirection: 'row',
  },
  ItemLineLabel: {
    fontStyle: 'normal',
    fontWeight: '500',
    fontSize: fs(14),
    color: Colors.tinyTextGrey,
    marginRight: ws(7)
  },
  ItemLineValue: {
    fontSize: fs(16),
    fontWeight: 'bold',
    color: Colors.blue,
  },
  ItemLineDate: {
    fontSize: fs(14),
    color: Colors.black,
    fontWeight: 'bold',
    fontStyle: 'normal',
  },
  righticon: {
    width: ws(10.29),
    height: ws(18),
    marginRight: ws(17.57)
  },
  IconLeft: {
    width: ws(34),
    height: ws(34),
    tintColor: Colors.white,
  }
})