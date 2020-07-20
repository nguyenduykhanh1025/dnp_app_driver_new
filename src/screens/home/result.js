import React, { Component } from 'react';
import { Text, View, StyleSheet, Button, StatusBar, Image, ScrollView } from 'react-native';
import { Content } from 'native-base';
import NavigationService from '@/utils/navigation';
import { mainStack } from '@/config/navigator';
import { Colors, sizes, sizeWidth, sizeHeight, commonStyles, Fonts } from '@/commons';
import { HeaderResult, HeaderMain } from '@/components'
import { SearchQRCode } from '@/mock/index';
import Icon from 'react-native-vector-icons/AntDesign';
import Item from './item';

const ic_crane = require('../../assets/images/crane.png');

export default class ResultScreen extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data: [],
    }
  }

  componentDidMount() {
    this.setState({ data: SearchQRCode[0].Data[0] })
  }

  onBack = () => {
    NavigationService.navigate(mainStack.home_tab)
  };

  renderLeft = () => {
    return (
      <Icon name={'close'} size={20} />
    )
  }

  renderRight = () => {
    return (
      <View
        style={{
          backgroundColor: '#E2F1FF',
          borderColor: '#ACD7FF',
          borderRadius: 4,
          borderStyle: 'solid',
          borderWidth: 1,
          flexDirection: 'row',
          alignItems: 'center'
        }}>
        <Icon name={'checkcircle'} style={{ marginLeft: 6 }} size={15} color={'#0587FF'} />
        <Text
          style={{
            fontSize: 14,
            color: '#0587FF',
            paddingHorizontal: 6,
            paddingVertical: 2
          }}>Thành công</Text>
      </View>
    )
  }

  render() {
    var { data } = this.state;

    return (
      <View>
        <StatusBar
          translucent
          barStyle = 'dark-content'
        />
        <HeaderMain
          backgroundColor={Colors.white}
          left={this.renderLeft()}
          right={this.renderRight()}
          onPress={() => { this.onBack() }}
        />
        <ScrollView style={styles.contentView}>
            <View>
              <View
                style={{
                  width: sizeWidth(100),
                  height: sizeHeight(20),
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginTop: sizeHeight(2)
                }}>
                <View style={styles.frame}>
                  <View style={{ width: sizeWidth(59) }}>
                    <View style={{ flexDirection: 'column', marginLeft: sizeWidth(5) }}>
                      <Text
                        style={{
                          fontSize: 16,
                          color: Colors.white,
                          fontWeight: 'bold',
                          marginTop: sizeHeight(3)
                        }}>Bốc công hàng từ Cảng</Text>
                      <View style={styles.frame1}>
                        <Text style={styles.txtLabel}>Cont</Text>
                        <Text style={styles.txtValue}></Text>
                      </View>
                      <View style={styles.frame1}>
                        <Text style={styles.txtLabel}>Size</Text>
                        <Text style={styles.txtValue}>22GO</Text>
                      </View>
                      <View style={styles.frame1}>
                        <Text style={styles.txtLabel}>Type</Text>
                        <Text style={styles.txtValue}>F</Text>
                      </View>
                    </View>
                  </View>
                  <View style={{}}>
                    <Image
                      source={ic_crane}
                      style={styles.image}
                    />
                  </View>
                </View>
              </View>
              <View style={{ height: sizeHeight(15), alignItems: 'center', justifyContent: 'center' }}>
                <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-around', width: sizeWidth(96), }}>
                  <View style={styles.frame2}>
                    <Text style={[styles.txtLabel, { width: null }]}>Position</Text>
                    <Text style={styles.txtValue1}>A-1-4-3</Text>
                  </View>
                  <View style={styles.frame2}>
                    <Text style={[styles.txtLabel, { width: null }]}>Gate Pass</Text>
                    <Text style={styles.txtValue1}>xxxxxxx</Text>
                  </View>
                </View>
              </View>
              <View style={{ marginTop: sizeHeight(2.5), alignItems: 'center', justifyContent: 'center', marginBottom: sizeHeight(5) }}>
                <View style={{ flexDirection: 'column', width: sizeWidth(92) }}>
                  <Text style={{ fontSize: 16, color: '#15307A', fontWeight: 'bold' }}>Thông tin khác</Text>
                  <View style={{ flexDirection: 'row', marginTop: sizeHeight(2) }}>
                    <Text style={styles.txtLabel1} >Khách hàng</Text>
                    <Text style={styles.txtValue2}>Anh A</Text>
                  </View>
                  <View style={{ flexDirection: 'row', marginTop: sizeHeight(2) }}>
                    <Text style={styles.txtLabel1} >Địa chỉ</Text>
                    <Text style={styles.txtValue2}>35 Cao thắng - Hải Châu - Đà nẵng</Text>
                  </View>
                  <View style={{ flexDirection: 'row', marginTop: sizeHeight(2) }}>
                    <Text style={styles.txtLabel1} >Điện thoại</Text>
                    <Text style={styles.txtValue2}>6969696969</Text>
                  </View>
                  <View style={{ flexDirection: 'row', marginTop: sizeHeight(2) }}>
                    <Text style={styles.txtLabel1} >Ghi chú</Text>
                    <Text style={[styles.txtValue2, { fontSize: 15, fontWeight: null }]}>Không có ghi chú gì</Text>
                  </View>
                </View>
              </View>
            </View>
        </ScrollView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  contentView: {
    backgroundColor: Colors.white,
    height: sizeHeight(100)
  },
  element: {
    width: sizeWidth(90),
    height: sizeHeight(10),
    alignItems: 'center',
    borderWidth: 1,
    borderColor: Colors.blue,
    borderRadius: 4,
    marginVertical: sizeHeight(1.5)
  },
  icon: {
    width: sizeWidth(9),
    height: sizeWidth(9),
    tintColor: Colors.blue,
    marginHorizontal: sizeWidth(3),
  },
  frame: {
    width: sizeWidth(92),
    height: sizeHeight(20),
    backgroundColor: Colors.blue,
    borderRadius: 10,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.20,
    shadowRadius: 1.41,
    elevation: 2,
    flexDirection: 'row'
  },
  image: {
    width: sizeWidth(30),
    height: sizeWidth(25),
    marginTop: 30,
  },
  txtLabel: {
    fontSize: 13,
    color: Colors.tinyTextGrey,
    width: sizeWidth(11)
  },
  txtValue: {
    fontSize: 15,
    fontWeight: 'bold',
    color: Colors.white,
  },
  frame1: {
    flexDirection: 'row',
    marginTop: sizeHeight(1),
  },
  frame2: {
    backgroundColor: Colors["E4EBF7"],
    width: sizeWidth(43),
    height: sizeHeight(10),
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  txtValue1: {
    fontSize: 20,
    color: '#15307A'
  },
  txtLabel1: {
    fontSize: 14,
    color: Colors.tinyTextGrey,
    width: sizeWidth(26)
  },
  txtValue2: {
    fontSize: 16,
    fontWeight: 'bold',
    width: sizeWidth(65)
  }
})
