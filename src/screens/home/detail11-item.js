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
    var { data, onPress, disabled } = this.props;
    return (
      <TouchableOpacity onPress={onPress} disabled={disabled}>
        <View style={styles.Container}>
          <View style={[styles.ItemContainer, disabled ? { backgroundColor: '#f1f1f1' } : null]}>
            <View style={styles.LeftView}>
              <View style={styles.Line}>
                <View style={{
                  flexDirection: 'row',
                  alignItems: 'center'
                }}>
                  <View style={{
                    flexDirection: 'row',
                    alignItems: 'center'
                  }}>
                    <Text style={[styles.TextLabel]}>
                     Số Container
                  </Text>
                    <Text style={styles.TextValue1}>
                      {data.containerNo}
                    </Text>
                  </View>
                  <View style={{
                    flexDirection: 'row',
                    alignItems: 'center',
                    left: ws(10)
                  }}>
                    <Text style={[styles.TextLabel,{ width: ws(30)}]}>
                      K/T
                    </Text>
                    <Text style={styles.TextValue2}>
                      {data.sztp}
                    </Text>
                  </View>
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
                      Trọng lượng
                    </Text>
                    <Text style={styles.TextValue2}>
                      {data.wgt? data.wgt.toFixed().replace(/\d(?=(\d{3})+(?!\d))/g, '$&,') : ''} {data.wgt? 'Kg' : ''}
                    </Text>
                  </View>
                  {/* { data.cargoType ?  */}
                      <View style={{
                        flexDirection: 'row',
                        alignItems: 'center'
                      }}>
                        <Text style={[styles.TextLabel,{ width: ws(30) ,left: -ws(27)}]}>
                          L/H
                      </Text>
                        <Text style={styles.TextValue2}>
                          {data.cargoType}
                        </Text>
                      </View>
                      {/* : null } */}
                </View>
              </View>
            </View>
            { disabled ? null : <Image source={righticon} style={styles.righticon} /> }
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
    elevation: 1,
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
    right: ws(11),
    position: 'absolute',
  },
  LeftView: {
    flexDirection: 'column',
    marginLeft: ws(0),
    justifyContent: 'space-between'
  },
  Line: {

  },
  TextLabel: {
    fontSize: fs(13),
    color: Colors.tinyTextGrey,
    marginRight: ws(18),
    marginVertical: ws(4), 
    textAlign: 'right',
    width: ws(80),
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