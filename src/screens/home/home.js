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
  RefreshControl,
  ActivityIndicator,
} from 'react-native';
import { connect } from 'react-redux';
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
  command_icon,
  cont4_icon,
  cont5_icon
} from '@/assets/icons'
import HomeButton from './home_button'
import { callApi } from '@/requests'
import { signOut } from '@/modules/auth/action';
import { getToken, saveUpEnable, saveDownEnable } from '@/stores';
import { hasSystemFeature } from 'react-native-device-info';
import Toast from 'react-native-tiny-toast';
import { update } from 'immutable';
import { DropDownProfile, ProfileModal } from '@/components'
const icUser = require('@/assets/icons/account/user.png')
const icCont1 = require('@/assets/icons/cont2_icon.png')
const icCont2 = require('@/assets/icons/cont3_icon.png')
const icCont3 = require('@/assets/icons/cont4_icon.png')
const icCont4 = require('@/assets/icons/cont5_icon.png')

class HomeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      PickupList: [],
      HistoryList: [],
      dataTruckNo: [],
      dataNoList: [],
      pageNum: 1,
      pageSize: 10,
      truckNo: '',
      chassisNo: '',
      date: '',
      boc_focused: true,
      ha_focused: false,
      boc_rong_focused: false,
      ha_rong_focused: false,
      profileVisible: false,
      userData: [],
      userName: 'Họ và tên',
      token: '',
      refreshing: false,
    };
    this.subs = [
      this.props.navigation.addListener('didFocus', this.componentDidMount),
    ];
  };

  componentDidMount = async () => {
    this.token = await getToken();
    saveUpEnable('0')
    saveDownEnable('0')
    this.onGetPickupList()
    this.onGetHistoryList()
    this.getUserInfo()

  };

  componentWillUnmount() {
    this.subs.forEach(sub => sub.remove());
  }

  onRefresh = async () => {
    this.onGetHistoryList();
  }

  onGetPickupList = async () => {
    const params = {
      api: 'pickup',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    // console.log('resultonGetPickupList', result)
    if (result.code == 0) {
      if (result.data.length > 0) {
        await this.setState({
          PickupList: result.data,
          truckNo: result.data[0].truckNo,
          chassisNo: result.data[0].chassisNo,
        })
        await this.onCheckEnableService(result.data)
      }
      else {
        await this.setState({
          PickupList: [],
        })
      }
    }
    else {
      Alert.alert(
        "Thông báo",
        result.msg,
        [
          { text: "OK", onPress: () => this.props.dispatch(signOut()) }
        ],
        { cancelable: false }
      );
    }
  }

  onCheckEnableService = async (PickupList) => {
    var upEnable = 0;
    var downEnable = 0;
    // console.log('PickupList.length', PickupList.length)
    if (PickupList.length < 4) {
      PickupList.map((item, index) => {
        if (item.serviceType % 2 == 0) {
          if (item.sztp != null && item.sztp.slice(0, 1) == '2') {
            downEnable++

          }
          else {
            downEnable = 2
          }
        }
        else {
          if (item.sztp != null && item.sztp.slice(0, 1) == '2') {
            upEnable++
          }
          else {
            upEnable = 2
          }
        }
      })
    }
    else {
    }
    // console.log('upEnable', upEnable)
    // console.log('downEnable', downEnable)
    saveUpEnable(upEnable.toFixed())
    saveDownEnable(downEnable.toFixed())
  }

  getUserInfo = async () => {
    const params = {
      api: 'user/info',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    if (result.code == 0) {
      await this.setState({
        userName: result.data.driverName,
        dataTruckNo: result.data.truckNoList,
        dataNoList: result.data.chassisNoList,
      })
    }
  }

  onGetHistoryList = async () => {
    const params = {
      api: 'pickup/history?pageNum=' + this.state.pageNum + '&pageSize=' + this.state.pageSize,
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    // console.log('resultonGetHistoryList', result)
    if (result.code == 0) {
      Toast.hide()
      await this.setState({
        HistoryList: result.data,
      })
    }
  }

  componentWillReceiveProps = (updateProps) => {
    // console.log('home.updateProps', updateProps)
  }


  renderItem = (item, index) => (
    <Item
      data={item.item}
      onPress={() => {
        NavigationService.navigate(mainStack.resultReturn, { pickupId: item.item.pickupHistoryId })
      }}
    />
  )

  onGoCheckIn = async () => {
    Toast.showLoading('Đang lấy dữ liệu check in!')
    var pickupHistoryIds = [];
    this.state.PickupList.map((item, index) => {
      pickupHistoryIds = pickupHistoryIds.concat(item.pickupId)
    })
    const params = {
      api: 'checkin',
      param: {
        pickupHistoryIds: pickupHistoryIds
      },
      token: this.token,
      method: 'POST'
    }
    var result = undefined;
    result = await callApi(params);
    // console.log('resultonGoCheckIn', result)
    if (result.code == 0) {
      Toast.hide()
      NavigationService.navigate(mainStack.qr_code, { dataQR: result })
    }
    else {
      Toast.hide()
      Alert.alert('Thông báo!', result.msg)
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <StatusBar
          translucent
          barStyle={'dark-content'}
          backgroundColor='#fff'
        />
        <ScrollView
          showsVerticalScrollIndicator={false}
          refreshControl={
            <RefreshControl refreshing={this.state.refreshing} onRefresh={() => { this.componentDidMount() }} />
          }
        >
          {/* {
            -- Phần ảnh đại diện, họ và tên
          } */}
          <View style={styles.Header}>
            <View style={styles.HeaderName}>
              <TouchableOpacity
                onPress={() => {
                  this.setState({ profileVisible: true })
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
            {!this.state.PickupList.length < 1 ?
              <TouchableOpacity
                onPress={() => {
                  this.onGoCheckIn()
                }}
              >
                <View style={styles.HeaderButton}>
                  <Text style={styles.HeaderButtonText}>Check in</Text>
                </View>
              </TouchableOpacity> : null}
          </View>
          {
            this.state.PickupList.length < 1 ?
              <View style={{
                width: ws(375),
                alignItems: 'center',
              }}>
                <View style={styles.PortersTagEmpty}>
                  <Text style={styles.txtEmpty}>Hãy chọn nhận cuốc</Text>
                  <Text style={styles.txtEmpty}>để bắt đầu giao nhận container!</Text>
                  <View style={styles.bgRound}>
                    <Image
                      source={command_icon}
                      style={[styles.iconBgRound]}
                      resizeMode='contain'
                    />
                  </View>
                </View>
              </View>
              :
              <View>
                <View style={styles.LicensePlateContainer}>
                  <View style={styles.LicensePlateTag}>
                    <View style={styles.LicensePlate}>
                      <Text style={styles.LicensePlateTextUp}>Biển số xe đầu kéo</Text>
                      <DropDownProfile
                        line
                        data={this.state.dataTruckNo}
                        style={styles.dropdownStyle}
                        PickerStyle={styles.pickerStyle}
                        onSelect={(item) => { this.setState({ truckNo: item }) }}
                      />
                      {/* <Text style={styles.LicensePlateTextDown}>{this.state.truckNo}</Text> */}
                    </View>
                    <View style={styles.LicensePlateLine} />
                    <View style={styles.LicensePlate}>
                      <Text style={styles.LicensePlateTextUp}>Biển số xe Romooc</Text>
                      <DropDownProfile
                        line
                        data={this.state.dataNoList}
                        style={styles.dropdownStyle}
                        PickerStyle={[styles.pickerStyle]}
                        onSelect={(item) => { this.setState({ chassisNo: item }) }}
                      />
                      {/* <Text style={styles.LicensePlateTextDown}>{this.state.chassisNo}</Text> */}
                    </View>
                  </View>
                </View>
                {
                  this.state.PickupList.map((item, index) => (
                    <View>
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
                              {
                                item.status == 0 || item.status == 1
                                  ?
                                  <View style={styles.PorterButtonStatus}>
                                    <Text style={styles.PorterButtonStatusText}>
                                      {
                                        item.status == 0 ?
                                          'Sẵn sàng'
                                          :
                                          item.status == 1 ?
                                            'Gate in'
                                            :
                                            ''
                                      }
                                    </Text>
                                  </View>
                                  :
                                  null
                              }
                            </View>
                            <View style={styles.PorterHeaderDown}>
                              <View style={styles.PorterHeaderDownItem}>
                                <Text style={styles.PorterHeaderDownItemLabel}>Mã lô:</Text>
                                <Text style={styles.PorterHeaderDownItemValue}>{item.batchCode}</Text>
                              </View>
                              {/* <View style={[styles.PorterHeaderDownItem]}>
                                <Text style={styles.PorterHeaderDownItemLabel}>Bill No:</Text>
                                <Text style={styles.PorterHeaderDownItemValue}>1234567890123</Text>
                              </View> */}
                            </View>
                          </View>

                          <TouchableOpacity
                            onPress={() => { NavigationService.navigate(mainStack.resultReturn, { pickupId: item.pickupId, serviceType: item.serviceType }) }}
                          >
                            <View style={styles.PorterItemContainer}>
                              <View style={styles.PorterItemLeft}>
                                {
                                  item.serviceType == 1 ?
                                    <Image source={icCont1} style={styles.PorterIcon} />
                                    : item.serviceType == 2 ?
                                      <Image source={icCont4} style={styles.PorterIcon} />
                                      : item.serviceType == 3 ?
                                        <Image source={icCont3} style={styles.PorterIcon} />
                                        : <Image source={icCont2} style={styles.PorterIcon} />
                                }
                              </View>
                              <View style={styles.PorterItemRight}>
                                <View style={styles.PorterItemRightUp}>
                                  <Text style={styles.PorterItemLabel}>Số Công:</Text>
                                  <Text style={styles.PorterItemValue}>{item.containerNo}</Text>
                                </View>
                                <View style={[styles.PorterItemRightDown, { marginTop: hs(10) }]}>
                                  <View style={styles.PorterItemRightUp}>
                                    <Text style={styles.PorterItemLabel}>Kích cỡ:</Text>
                                    <Text style={styles.PorterItemValue}>{item.sztp}</Text>
                                  </View>
                                  <Text style={styles.PorterItemRightDownStatus}>{
                                    item.serviceType == 1 ?
                                      'container hàng'
                                      :
                                      item.serviceType == 2 ?
                                        'container rỗng'
                                        :
                                        item.serviceType == 3 ?
                                          'container rỗng'
                                          :
                                          item.serviceType == 4 ?
                                            'container hàng'
                                            :
                                            null
                                  }</Text>
                                </View>
                              </View>
                            </View>
                          </TouchableOpacity>
                          {/* <TouchableOpacity
                            onPress={() => { NavigationService.navigate(mainStack.resultReturn, { Data: [] }) }}
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
                                  <Text style={styles.PorterItemRightDownStatus}>container hàng</Text>
                                </View>
                              </View>
                            </View>
                          </TouchableOpacity> */}
                        </View>
                      </View>
                    </View>
                  ))
                }
              </View>
          }
          <View style={styles.HistoryContainer}>
            <View style={styles.TitleHistory}>
              <Text style={styles.TitleHistoryText}>Lịch sử</Text>
            </View>
            {
              this.state.HistoryList.length == 0 ?
                <View style={{ height: hs(425), width: '100%', justifyContent: 'center', alignItems: 'center'}}>
                  <ActivityIndicator size='large' color={Colors.mainColor}/>
                </View>
                :
                <FlatList
                  data={this.state.HistoryList}
                  refreshing={this.state.refreshing}
                  onRefresh={() => {
                    this.onRefresh()
                  }}
                  renderItem={(item, index) => this.renderItem(item, index)}
                />
            }
            <FlatList
              data={this.state.HistoryList}
              refreshing={this.state.refreshing}
              onRefresh={() => {
                this.onRefresh()
              }}
              renderItem={(item, index) => this.renderItem(item, index)}
            />
          </View>
        </ScrollView>
        <ProfileModal 
          visible={this.state.profileVisible}
          onClose={()=> this.setState({ profileVisible: false })}
        />
      </View>
    )
  }
}
const mapStateToProps = (state) => {
  return {
  };
};

export default connect(mapStateToProps)(HomeScreen);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.white
  },
  pickerStyle: {
    width: ws(148),
    color: Colors.blue,
    fontFamily: null,
    fontSize: fs(20),
    fontWeight: 'bold',
  },
  Header: {
    width: ws(375),
    height: hs(37),
    marginTop: hs(55),
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
  bgRound: {
    width: ws(70),
    height: ws(70),
    borderRadius: 200,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: Colors.maincolor,
    borderWidth: ws(5), borderColor:
      Colors.bgGrey
  },
  iconBgRound: {
    width: ws(42),
    height: hs(28),
    tintColor: Colors.white
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
    // height: hs(248),
    backgroundColor: Colors.blue,
    shadowColor: '0px 2px 8px rgba(0, 0, 0, 0.2), 0px 2px 4px rgba(0, 0, 0, 0.05)',
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: hs(12)
  },
  PortersTagEmpty: {
    width: ws(345),
    height: hs(170),
    backgroundColor: Colors["E4EBF7"],
    shadowColor: '0px 2px 8px rgba(0, 0, 0, 0.2), 0px 2px 4px rgba(0, 0, 0, 0.05)',
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: hs(12),
    borderWidth: 1,
    borderColor: Colors["E4EBF7"],
    borderStyle: 'solid'
  },
  txtEmpty: {
    fontSize: sizes.h35,
    fontWeight: '500'
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
    width: ws(60),
    marginRight: ws(4),

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
  style_modal_dropdown: {
    paddingRight: sizeWidth(3)
  },
  dropdownStyle: {
    width: ws(150),
    height: hs(30)
  },
  dropdownTextStyle: {
    fontSize: sizeWidth(3.5),
    color: Colors.colorWhite,
    backgroundColor: Colors.blackColor,
    fontWeight: 'bold',
    borderRadius: 10
  },
  Picker: {
    width: ws(315),
  },
  DropdownItemText: {
    fontSize: fs(16)
  },
})