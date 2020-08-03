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
                  <Text style={[styles.TextLabel, { width: ws(32) }]}>
                    Mã lô
                  </Text>
                  <Text style={styles.TextValue1}>
                    {data.batchId}
                  </Text>
                </View>
              </View>
              <View style={styles.Line}>
                <View style={{
                  flexDirection: 'row',
                  alignItems: 'center'
                }}>
                  <Text style={[styles.TextLabel, { width: ws(32) }]}>
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
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}>
                  <View style={{
                    flexDirection: 'row',
                    alignItems: 'center'
                  }}>
                    <Text style={styles.TextLabel}>
                      FE
                    </Text>
                    <Text style={styles.TextValue2}>
                      {data.fe}
                    </Text>
                  </View>
                  {/* <View style={{
                    flexDirection: 'row',
                    alignItems: 'center'
                  }}>
                    <Text style={styles.TextLabel}>
                      SizeType
                    </Text>
                    <Text style={styles.TextValue2}>
                      {data.sztp}
                    </Text>
                  </View> */}
                  <View style={{
                    flexDirection: 'row',
                    alignItems: 'center'
                  }}>
                    <Text style={styles.TextLabel}>
                      Số lượng
                    </Text>
                    <Text style={[styles.TextValue2, { width: ws(140) }]}>
                      {data.contAmount}
                    </Text>
                  </View>
                </View>
              </View>
            </View>
            <Image source={righticon} style={styles.righticon} />
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
    marginLeft: ws(25),
    justifyContent: 'space-between'
  },
  Line: {
    width: ws(270)
  },
  TextLabel: {
    fontSize: fs(13),
    color: Colors.tinyTextGrey,
    marginRight: ws(18),
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