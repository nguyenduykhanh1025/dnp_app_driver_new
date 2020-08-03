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
import { righticon } from '@/assets/icons'
import { hasSystemFeature } from 'react-native-device-info';

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
                  <Text style={[styles.TextLabel, { width: ws(40) }]}>
                    Số cont
                  </Text>
                  <Text style={styles.TextValue1}>
                    {data.containerNo}
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
                      Size
                    </Text>
                    <Text style={styles.TextValue2}>
                      {data.sztp}
                    </Text>
                  </View>
                  {
                    data.wgt ?
                      <View style={{
                        flexDirection: 'row',
                        alignItems: 'center'
                      }}>
                        <Text style={styles.TextLabel}>
                          Số booking
                      </Text>
                        <Text style={styles.TextValue2}>
                          {data.wgt}
                        </Text>
                      </View>
                      :
                      null
                  }

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
    height: hs(75),
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
    marginRight: ws(11),
  },
  LeftView: {
    flexDirection: 'column',
    marginLeft: ws(23),
    justifyContent: 'space-between'
  },
  Line: {

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
    marginRight: ws(28)
  },
})