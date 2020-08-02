import React, { Component } from 'react';
import {
  Text,
  View,
  StyleSheet,
  Button,
  StatusBar,
  Image,
  ScrollView
} from 'react-native';
import {
  Content
} from 'native-base';
import NavigationService from '@/utils/navigation';
import {
  mainStack
} from '@/config/navigator';
import {
  Colors,
  sizes,
  sizeWidth,
  sizeHeight,
  commonStyles,
  Fonts,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import {
  HeaderResult,
  HeaderMain
} from '@/components'
import {
  SearchQRCode
} from '@/mock/index';
import Icon from 'react-native-vector-icons/AntDesign';
import Item from './item';

const ic_crane = require('../../assets/images/crane.png');

export default class ResultScreen extends Component {

  constructor(props) {
    super(props);
    this.state = {
      data: [],
      "chassisNo": "",
      "contNo": "",
      "fe": "",
      "sztp": "",
      "truckNo": "",
      "yardPosition": ""
    }
  }

  componentDidMount() {
    var Data = this.props.navigation.state.params.Data[0];
    this.setState({
      "chassisNo": Data && Data.chassisNo,
      "contNo": Data && Data.contNo,
      "fe": Data && Data.fe,
      "sztp": Data && Data.sztp,
      "truckNo": Data && Data.truckNo,
      "yardPosition": Data && Data.yardPosition
    })
  }

  onBack = () => {
    NavigationService.navigate(mainStack.home_tab)
  };

  renderLeft = () => {
    return (
      <View
        style={{
          marginLeft: ws(29.54)
        }}
      >
        <Icon name={'close'} size={20} />
      </View>
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
          alignItems: 'center',
          marginRight: ws(19),
          width: ws(115),
          height: hs(32),
        }}>
        <Icon name={'checkcircle'} style={{ marginLeft: 6 }} size={15} color={'#0587FF'} />
        <Text
          style={{
            fontSize: fs(14),
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
          barStyle='dark-content'
        />
        <HeaderMain
          renderLeft={this.renderLeft()}
          renderRight={this.renderRight()}
          onPressLeft={() => { this.onBack() }}
          disableBG
          disableStep
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
                <View style={{
                  width: ws(170),
                  height: hs(106),
                  marginLeft: ws(26),
                  marginRight: ws(32),
                }}>
                  <View style={{
                    flexDirection: 'column',
                    // marginLeft: sizeWidth(5)
                  }}>
                    <Text
                      style={{
                        fontSize: fs(16),
                        color: Colors.white,
                        fontWeight: 'bold',
                        marginTop: hs(31)
                      }}>Bốc công hàng từ Cảng</Text>
                    <View style={styles.frame1}>
                      <Text style={styles.txtLabel}>Cont</Text>
                      <Text style={styles.txtValue}>{this.state.contNo}</Text>
                    </View>
                    <View style={styles.frame1}>
                      <Text style={styles.txtLabel}>Size</Text>
                      <Text style={styles.txtValue}>{this.state.sztp}</Text>
                    </View>
                    <View style={styles.frame1}>
                      <Text style={styles.txtLabel}>Type</Text>
                      <Text style={styles.txtValue}>{this.state.fe}</Text>
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
            <View
              style={{
                height: hs(110),
                alignItems: 'center',
                justifyContent: 'center',
                marginTop: hs(25)
              }}>
              <View
                style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'space-around',
                  width: sizeWidth(96),
                }}>
                <View style={styles.frame2}>
                  <Text style={[
                    styles.txtLabel,
                    {
                      width: null
                    }]}>
                    Position
                  </Text>
                  <Text style={styles.txtValue1}>{this.state.yardPosition}</Text>
                </View>
                <View style={styles.frame2}>
                  <Text style={[styles.txtLabel, { width: null }]}>Gate Pass</Text>
                  <Text style={styles.txtValue1}>{}</Text>
                </View>
              </View>
            </View>
            <View style={{
              marginTop: sizeHeight(2.5),
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: sizeHeight(5)
            }}>
              <View style={{
                flexDirection: 'column',
                width: ws(333),
              }}>
                <Text style={{
                  fontSize: fs(16),
                  color: '#15307A',
                  fontWeight: 'bold',
                  marginBottom: hs(25),
                }}>
                  Thông tin khác
                </Text>
                {/* <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Khách hàng</Text>
                  <Text style={styles.txtValue2}>Anh A</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Địa chỉ</Text>
                  <Text style={styles.txtValue2}>35 Cao thắng - Hải Châu - Đà nẵng</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Điện thoại</Text>
                  <Text style={styles.txtValue2}>6969696969</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Ghi chú</Text>
                  <Text style={[styles.txtValue2, { fontSize: 15, fontWeight: null }]}>Không có ghi chú gì</Text>
                </View> */}
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
    height: hs(712),
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
    width: ws(345),
    height: hs(156),
    marginTop: hs(20),
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
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  image: {
    width: ws(106.03),
    height: ws(91.21),
    marginTop: 30,
    marginRight: ws(10.97)
  },
  txtLabel: {
    fontSize: fs(13),
    color: Colors.tinyTextGrey,
    marginBottom: hs(5),
    marginRight: ws(11),
    width: ws(28)
  },
  txtValue: {
    fontSize: fs(15),
    fontWeight: 'bold',
    color: Colors.white,
  },
  frame1: {
    flexDirection: 'row',
    marginTop: sizeHeight(1),
  },
  frame2: {
    backgroundColor: Colors["E4EBF7"],
    width: ws(160),
    height: hs(100),
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  txtValue1: {
    fontSize: fs(20),
    color: '#15307A',
    fontWeight: 'bold'
  },
  txtLabel1: {
    fontSize: fs(14),
    color: Colors.tinyTextGrey,
    width: ws(73),
    marginRight: ws(19),
  },
  txtValue2: {
    fontSize: fs(16),
    fontWeight: 'bold',
    width: sizeWidth(65)
  },
  Line: {
    flexDirection: 'row',
    marginBottom: hs(20)
  }
})
