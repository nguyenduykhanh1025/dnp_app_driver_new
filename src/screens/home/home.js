import React, { Component } from 'react';
import {
  Text,
  View,
  StyleSheet,
  Button,
  ScrollView,
  ImageBackground,
  Image,
  FlatList,
  TouchableOpacity,
  StatusBar,
  Alert,
} from 'react-native';
import NavigationService from '@/utils/navigation';
import { mainStack, authStack } from '@/config/navigator';
import {
  Colors,
  sizes,
  sizeWidth,
  sizeHeight,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons'
import { getListInfoHome } from '@/mock/index';
import Item from './item';
import {
  down_arrow,
  cont2_icon,
  cont3_icon,
  cont4_icon,
  cont5_icon
} from '@/assets/icons'
import HomeButton from './home_button'
import { callApi } from '@/requests'
import { getToken } from '@/stores';
import { hasSystemFeature } from 'react-native-device-info';

const icUser = require('@/assets/icons/account/user.png')
const icCont1 = require('@/assets/icons/cont2_icon.png')
const icCont2 = require('@/assets/icons/cont4_icon.png')

export default class HomeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      PickupList: [],
      HistoryList: [],
      pageNum: 1,
      pageSize: 10,
      carCode: '73A1 - 042.32',
      ContCode: '73A1 - 042.32',
      date: '10 Jun 2020',
      boc_focused: true,
      ha_focused: false,
      boc_rong_focused: false,
      ha_rong_focused: false,
      userData: [],
      userName: 'Họ và tên'
    }
  };

  componentDidMount = async () => {
    this.onGetPickupList()
    this.onGetHistoryList()
  };

  getUserInfo = async () => {
    const token = await getToken();
    const param = {
      url: 'user/info',
      token: token,
    }
    const result = await getRequestAPI(param);
    this.setState({ userData: result.data })
    console.log('userData', this.state.userData)
  }

  onGetPickupList = async () => {
    var token = await getToken()
    const params = {
      api: 'pickup',
      param: '',
      token: token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('resultonGetPickupList', result)
    if (result.code == 0) {
      await this.setState({
        PickupList: result.data,
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  onGetHistoryList = async () => {
    var token = await getToken()
    const params = {
      api: 'pickup/history?pageNum=' + this.state.pageNum + '&pageSize=' + this.state.pageSize,
      param: '',
      token: token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('resultonGetHistoryList', result)
    if (result.code == 0) {
      await this.setState({
        HistoryList: result.data,
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  renderItem = (item, index) => (
    <Item
      data={item.item}
      onPress={() => {
        NavigationService.navigate(mainStack.result, {})
      }}
    />
  )

  render() {
    return (
      <View style={styles.container}>
        <StatusBar
          translucent
          barStyle={'dark-content'}
          backgroundColor='transparent'
        />
        <ScrollView
          showsVerticalScrollIndicator={false}
        >
          {/* {
            -- Phần ảnh đại diện, họ và tên
          } */}
          <View style={styles.Header}>
            <View style={styles.HeaderName}>
              <TouchableOpacity
                onPress={() => {
                  NavigationService.navigate(mainStack.profile, {})
                }}
              >
                <View style={styles.HeaderIcon}>
                  <Image source={icUser} style={styles.icUser} />
                </View>
              </TouchableOpacity>
              <View style={styles.HeaderText}>
                <View style={styles.HeaderTextUp}>
                  <Text style={styles.HeaderTextDate}>{this.state.date}</Text>
                </View>
                <View style={styles.HeaderTextDown}>
                  <Text style={styles.HeaderTextName}>{this.state.userName}</Text>
                </View>
              </View>
            </View>
            <TouchableOpacity
              onPress={() => {
                NavigationService.navigate(mainStack.qr_code, {})
              }}
            >
              <View style={styles.HeaderButton}>
                <Text style={styles.HeaderButtonText}>Check in</Text>
              </View>
            </TouchableOpacity>
          </View>
          {
            this.state.PickupList.length < 0 ?
              <View style={{
                width: ws(375),
                alignItems: 'center',
              }}>
                <View style={styles.PortersTagEmpty}>
                  <Text>Chưa nhận cont nào !</Text>
                </View>
              </View>

              :
              this.state.PickupList.map((item, index) => (
                <View>
                  <View style={styles.LicensePlateContainer}>
                    <View style={styles.LicensePlateTag}>
                      <View style={styles.LicensePlate}>
                        <Text style={styles.LicensePlateTextUp}>Biển số xe đầu kéo</Text>
                        <Text style={styles.LicensePlateTextDown}>{item.truckNo}</Text>
                      </View>
                      <View style={styles.LicensePlateLine} />
                      <View style={styles.LicensePlate}>
                        <Text style={styles.LicensePlateTextUp}>Biển số xe Romooc</Text>
                        <Text style={styles.LicensePlateTextDown}>{item.chassisNo}</Text>
                      </View>
                    </View>
                  </View>

                  <View style={styles.PortersContainer}>
                    <View style={styles.PortersTag}>

                      <View style={styles.PortersHeader}>
                        <View style={styles.PorterHeaderUp}>
                          <Text style={styles.PorterHeaderTitle}>
                            {
                              item.serviceType == 1 ?
                                'Bốc container hàng từ cảng'
                                :
                                item.serviceType == 2 ?
                                  'Hạ container rỗng cho cảng'
                                  :
                                  item.serviceType == 3 ?
                                    'Bốc container rỗng từ cảng'
                                    :
                                    item.serviceType == 4 ?
                                      'Giao container hàng cho cảng'
                                      :
                                      ''
                            }
                          </Text>
                          <View style={styles.PorterButtonStatus}>
                            <Text style={styles.PorterButtonStatusText}>Sẵn sàng</Text>
                          </View>
                        </View>
                        <View style={styles.PorterHeaderDown}>
                          <View style={styles.PorterHeaderDownItem}>
                            <Text style={styles.PorterHeaderDownItemLabel}>Mã lô:</Text>
                            <Text style={styles.PorterHeaderDownItemValue}>{item.batchCode}</Text>
                          </View>
                          <View style={[styles.PorterHeaderDownItem]}>
                            <Text style={styles.PorterHeaderDownItemLabel}>Bill No:</Text>
                            <Text style={styles.PorterHeaderDownItemValue}>1234567890123</Text>
                          </View>
                        </View>
                      </View>

                      <TouchableOpacity
                        onPress={() => { NavigationService.navigate(mainStack.result, {}) }}
                      >
                        <View style={styles.PorterItemContainer}>
                          <View style={styles.PorterItemLeft}>
                            <Image source={icCont1} style={styles.PorterIcon} />
                          </View>
                          <View style={styles.PorterItemRight}>
                            <View style={styles.PorterItemRightUp}>
                              <Text style={styles.PorterItemLabel}>Số Công:</Text>
                              <Text style={styles.PorterItemValue}>{item.containerNo}</Text>
                            </View>
                            <View style={[styles.PorterItemRightDown, { marginTop: hs(10) }]}>
                              <View style={styles.PorterItemRightUp}>
                                <Text style={styles.PorterItemLabel}>Kích cỡ</Text>
                                <Text style={styles.PorterItemValue}>{item.sztp}</Text>
                              </View>
                              <Text style={styles.PorterItemRightDownStatus}>Công hàng</Text>
                            </View>
                          </View>
                        </View>
                      </TouchableOpacity>
                      <TouchableOpacity
                        onPress={() => { NavigationService.navigate(mainStack.resultReturn, {}) }}
                      >
                        <View style={styles.PorterItemContainer}>
                          <View style={styles.PorterItemLeft}>
                            <Image source={icCont2} style={styles.PorterIcon} />
                          </View>
                          <View style={styles.PorterItemRight}>
                            <View style={styles.PorterItemRightUp}>
                              <Text style={styles.PorterItemLabel}>Số Công:</Text>
                              <Text style={styles.PorterItemValue}>KUST123456789</Text>
                            </View>
                            <View style={styles.PorterItemRightDown}>
                              <View style={styles.PorterItemRightUp}>
                                <Text style={styles.PorterItemLabel}>Kích cỡ</Text>
                                <Text style={styles.PorterItemValue}>2020</Text>
                              </View>
                              <Text style={styles.PorterItemRightDownStatus}>Công hàng</Text>
                            </View>
                          </View>
                        </View>
                      </TouchableOpacity>
                    </View>
                  </View>
                </View>
              ))
          }
          <View style={styles.HistoryContainer}>
            <View style={styles.TitleHistory}>
              <Text style={styles.TitleHistoryText}>Lịch sử</Text>
            </View>
            <FlatList
              data={this.state.HistoryList}
              renderItem={(item, index) => this.renderItem(item, index)}
            />
          </View>
        </ScrollView>
      </View>
    )
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.white
  },
  Header: {
    width: ws(375),
    height: hs(37),
    marginTop: hs(66),
    justifyContent: 'space-between',
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: ws(20),
    marginBottom: hs(20),
  },
  HeaderName: {
    flexDirection: 'row',
    marginLeft: ws(20)
  },
  HeaderButton: {
    width: ws(99),
    height: hs(35),
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 4,
    backgroundColor: Colors["FFAA00"],
    marginRight: ws(20)
  },
  HeaderIcon: {
    backgroundColor: Colors.white,
    borderRadius: 100,
    marginRight: ws(8),
  },
  icUser: {
    width: ws(35),
    height: ws(35),
    borderRadius: 100,
  },
  HeaderText: {

  },
  HeaderTextUp: {

  },
  HeaderTextDown: {

  },
  HeaderTextDate: {
    fontFamily: null,
    fontSize: fs(11),
    color: Colors.tinyTextGrey,
  },
  HeaderTextName: {
    fontFamily: null,
    fontSize: fs(14),
    fontWeight: 'bold',
    color: Colors.blue,
  },
  HeaderButtonText: {
    fontFamily: null,
    fontSize: fs(15),
    color: Colors.white,
  },
  LicensePlateContainer: {
    width: ws(375),
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: hs(16),
  },
  LicensePlateTag: {
    flexDirection: 'row',
    width: ws(345),
    height: hs(89),
    backgroundColor: Colors["E4EBF7"],
    borderRadius: 8,
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: Colors["C2D0F8"],
    alignItems: 'center',
    justifyContent: 'center',
  },
  LicensePlate: {
    alignItems: 'center',
    justifyContent: 'center',
    width: ws(172),
  },
  LicensePlateLine: {
    width: 0,
    height: hs(67.5),
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: Colors["C2D0F8"]
  },
  LicensePlateTextUp: {
    color: Colors.tinyTextGrey,
    fontFamily: null,
    fontSize: fs(15)
  },
  LicensePlateTextDown: {
    color: Colors.blue,
    fontFamily: null,
    fontSize: fs(18),
    fontWeight: 'bold',
  },
  PortersContainer: {
    width: ws(375),
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: hs(24),
  },
  PortersTag: {
    width: ws(345),
    height: hs(248),
    backgroundColor: Colors.blue,
    shadowColor: '0px 2px 8px rgba(0, 0, 0, 0.2), 0px 2px 4px rgba(0, 0, 0, 0.05)',
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: hs(12)
  },
  PortersTagEmpty: {
    width: ws(345),
    height: hs(52),
    backgroundColor: 'rgba(255, 15, 15, 0.24)',
    shadowColor: '0px 2px 8px rgba(0, 0, 0, 0.2), 0px 2px 4px rgba(0, 0, 0, 0.05)',
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: hs(12),
    borderColor: 'rgba(255, 15, 15, 0.64)',
    borderWidth: 1,
    borderStyle: 'solid'
  },
  PortersHeader: {
    flexDirection: 'column',
    marginBottom: hs(24)
  },
  PorterHeaderDown: {
    width: ws(305),
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  PorterHeaderUp: {
    width: ws(305),
    height: hs(25),
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginVertical: hs(14)
  },
  PorterHeaderTitle: {
    fontFamily: null,
    fontSize: fs(15),
    color: Colors.white,
    fontWeight: 'bold',
    marginLeft: ws(1),
  },
  PorterButtonStatus: {
    width: ws(64),
    height: hs(25),
    borderRadius: 4,
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: Colors["(0, 224, 150, 0.3)"],
    backgroundColor: Colors["(0, 224, 150, 0.1)"],
    justifyContent: 'center',
    alignItems: 'center'
  },
  PorterButtonStatusText: {
    fontFamily: null,
    fontSize: fs(11),
    color: Colors["00E096"],
  },
  PorterHeaderDownItem: {
    flexDirection: 'row',
  },
  PorterHeaderDownItemLabel: {
    marginRight: ws(9),
    fontFamily: null,
    fontSize: fs(13),
    color: Colors.BAC9F3,
  },
  PorterHeaderDownItemValue: {
    fontFamily: null,
    fontSize: fs(13),
    fontWeight: 'bold',
    color: Colors.white,
  },
  PorterItemContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    width: ws(305),
    height: hs(63),
    backgroundColor: 'rgba(255, 255, 255, 0.06)',
    borderRadius: 10,
    borderWidth: 1,
    borderStyle: 'solid',
    borderColor: 'rgba(255, 255, 255, 0.11)',
    marginBottom: hs(10)
  },
  PorterItemLeft: {
    width: ws(72),
    justifyContent: 'center',
    alignItems: 'center',
  },
  PorterItemRight: {
    flexDirection: 'column',
    width: ws(233),
  },
  PorterItemRightUp: {
    flexDirection: 'row',
  },
  PorterItemLabel: {
    fontFamily: null,
    fontSize: fs(13),
    color: Colors.BAC9F3,
    width: ws(49),
    marginRight: ws(9),

  },
  PorterItemValue: {
    fontFamily: null,
    fontSize: fs(13),
    color: Colors.white,
    fontWeight: 'bold',
  },
  PorterItemRightDown: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  PorterItemRightDownStatus: {
    fontSize: fs(11),
    color: Colors.white,
    marginRight: ws(13),
  },
  PorterIcon: {
    width: ws(34),
    height: ws(34),
    tintColor: Colors.white,
  },
  HistoryContainer: {
    width: ws(375),
  },
  TitleHistory: {
    marginLeft: ws(16),
    marginBottom: hs(14),
  },
  TitleHistoryText: {
    fontSize: fs(18),
    fontWeight: 'bold',
    color: Colors.black,
  },
})