import React, { Component } from 'react';
import {
  Text,
  View,
  TouchableOpacity,
  StyleSheet,
  Image,
} from 'react-native';
import {
  commonStyles,
  Colors,
  sizeWidth,
  sizeHeight,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import { righticon } from '@/assets/icons';

export default class Item extends Component {
  render() {
    var { data, onPress } = this.props;
    console.log(data)
    return (
      <TouchableOpacity onPress={onPress}>
        <View style={styles.Container}>
          <View style={styles.ItemContainer}>
            <View style={styles.LeftView}>
              <View style={styles.Line}>
                <View style={{
                  flexDirection: 'row',
                  alignItems: 'center'
                }}>
                  <Text style={[styles.TextLabel]}>
                    Số bill
                  </Text>
                  <Text style={styles.TextValue2}>
                    {data.blNo}
                  </Text>
                </View>
              </View>
              <View style={styles.Line}>
                <View style={{
                  flexDirection: 'row',
                  alignItems: 'center'
                }}>
                  <Text style={[styles.TextLabel,{ width: ws(70)}]}>
                    Chủ hàng
                  </Text>
                  <Text style={styles.TextValue2}>
                    {data.consignee}
                  </Text>
                </View>
              </View>
              <View style={styles.Line}>
                <View style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                }}>
                  <View style={styles.rowline}>
                    <Text style={[styles.TextLabel]}>Mã lô</Text>
                    <Text style={styles.TextValue1}>
                      {data.batchId}
                    </Text>
                  </View>
                  <View style={styles.rowline}>
                    <Text style={styles.TextLabel, { width: ws(30) }}>FE</Text>
                    <Text style={styles.TextValue2}>
                      {data.fe}
                    </Text>
                  </View>
                  <View style={styles.rowline}>
                    <Text style={[styles.TextLabel, { width: ws(60) }]}>
                      Số lượng
                    </Text>
                    <Text style={[styles.TextValue2, { width: ws(140) }]}>
                      {data.contAmount}
                    </Text>
                  </View>
                </View>
              </View>
            </View>
            { <Image source={righticon} style={styles.righticon} /> }
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
  },
  rowline: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  ItemContainer: {
    width: ws(345),
    height: hs(100),
    padding: sizeWidth(2),
    shadowColor: "0px 2px 8px rgba(0, 0, 0, 0.2)",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.22,
    shadowRadius: 2.22,
    elevation: 6,
    borderRadius: 10,
    marginBottom: hs(13),
    marginTop: hs(2),
    backgroundColor: Colors.white,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  righticon: {
    width: ws(10.29),
    height: ws(18),
    marginRight: ws(17.57),
  },
  LeftView: {
    flexDirection: 'column',
    marginLeft: ws(15),
    justifyContent: 'space-between'
  },
  Line: {
    width: ws(275)
  },
  TextLabel: {
    fontSize: fs(13),
    color: Colors.tinyTextGrey,
    marginRight: ws(7),
    width: ws(40)
  },
  TextValue1: {
    fontSize: fs(15),
    color: Colors.blue,
    fontWeight: 'bold',
  },
  TextValue2: {
    fontSize: fs(15),
    color: Colors.subColor,
    fontWeight: 'bold',
    marginRight: ws(28),

  },
})