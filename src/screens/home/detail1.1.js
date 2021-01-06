import React, { Component } from 'react'
import {
  Text,
  View,
  StyleSheet,
  FlatList,
  TouchableHighlight,
  TouchableOpacity,
  ScrollView,
  Image,
  Alert,
  RefreshControl,
} from 'react-native';
import NavigationService from '@/utils/navigation';
import {
  homeStack,
  mainStack,
  homeTab
} from '@/config/navigator';
import Icon from 'react-native-vector-icons/FontAwesome'
import {
  commonStyles,
  sizeHeight,
  sizeWidth,
  Colors,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import {
  HeaderMain,
  DropDown,
  Button,
  Checkbox
} from '@/components';
import {
  righticon,
  lefticon,
  cont2_icon,
  cont3_icon,
  cont4_icon,
  cont5_icon,
} from '@/assets/icons';
import Item from './detail11-item';
import ItemSingle from './detail11-item-single';
import navigation from '@/utils/navigation';
import {
  SCANNER_QR,
} from '@/modules/home/constants';
import {
  callApi
} from '@/requests';
import {
  getToken,
  getUpEnable,
  getDownEnable,
} from '@/stores';

const next = require('@/assets/icons/icon_next.png')

export default class DetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      refreshing: false,
      container: '',
      douCont: false,
      checkTypeVisible: false,
      cont4: 0,
      checkType: 0,
    };
    this.token = null;
    this.upEnable = null;
    this.DownEnable = null;
  }

  componentDidMount = async () => {
    this.setState({
      refreshing: true,
    })
    this.token = await getToken();
    this.upEnable = await getUpEnable();
    this.DownEnable = await getDownEnable();
    // console.log('this.props.navigation.state.params.shipmentId', this.props.navigation.state.params.shipmentId)
    this.onGetPickupList(this.props.navigation.state.params.shipmentId);
  }

  onRefresh = async () => {
    this.componentDidMount()
  }

  onGetPickupList = async (shipmentId) => {
    const params = {
      api: 'shipment/' + shipmentId + '/pickup',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    if (result.code == 0) {
      if (result.data.length > 0) {
        await this.setState({
          data: result.data,
        })
        const dataCheck = result.data[0].sztp.slice(0, 1)
        const a = result.data.map(e => e.sztp.slice(0, 1) == dataCheck)
        const b = a.find(e => e == false)
        if (b == undefined && dataCheck == '2') {
          this.setState({
            container: 'Container 20',
            checkType: 2
          })
        } else if (b == undefined && dataCheck == '4') {
          this.setState({
            container: 'Container 40',
            checkType: 4
          })
        } else {
          this.setState({
            container: 'Container 20/40',
            douCont: true,
            checkType: 2
          })
        }
      }
      else {
        Alert.alert(
          "Thông báo!",
          "Hiện tại không có cont nào!",
          [
            { text: "OK", onPress: () => this.props.navigation.goBack() }
          ],
          { cancelable: false }
        );
      }
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
    this.setState({
      refreshing: false,
    })
  }

  onAutoPickup = async () => {
    const params = {
      api: 'shipment/' + this.props.navigation.state.params.shipmentId + '/sztp/' + this.state.checkType + '/pickup-info',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    if (result.code == 0) {
      var data = result.data
      this.upEnable == '1' && data.sztp.slice(0, 1) != '2' ? Alert.alert('Thông báo', 'Đã chọn container 20 không thể chọn container 40') : await this.Continue(data, result)
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }


  Continue = async (data, result) => {
    await this.setState({ checkTypeVisible: false })
    NavigationService.navigate(mainStack.detail2, {
      data: {
        containerNo: data.containerNo,
        pickupAssignId: result.pickupAssignId,
        sztp: data.sztp,
        wgt: data.wgt,
        consignee: data.consignee,
        address: data.address,
        mobileNumber: data.mobileNumber,
        remark: data.remark,
        cargoType: data.cargoType,
      }
    })
  }

  renderItem = (item, index) => (
    <Item
      data={item.item}
      onPress={() => {
        this.upEnable == '1' && item.item.sztp.slice(0, 1) != '2' ?
          Alert.alert('Thông báo !', 'Đã chọn container 20 không thể chọn container 40.')
          :
          NavigationService.navigate(mainStack.detail2, { data: item.item })
      }}
      disabled={!item.item.clickable ? true : false}
    />
  )

  onSelectCont = async () => {
    this.setState({
      cont4: (this.state.cont4 + 1) % 2,
      checkType: this.state.cont4 == 1 ? 2 : 4
    })
  }

  closePopupCheckBox = async () => {
    await this.setState({
      checkTypeVisible: false,
    })
  }

  render() {
    console.log('this.tyures', this.state.checkType)
    return (
      <View style={styles.Container}>

        <HeaderMain
          renderLeft={(
            <Image source={lefticon} style={styles.Lefticon} />
          )}
          onPressLeft={() => {
            this.props.navigation.goBack()
          }}
          status={1}
        />
        <View style={styles.Body}>
          <ScrollView
            showsVerticalScrollIndicator={false}
            refreshControl={
              <RefreshControl
                refreshing={this.state.refreshing}
                onRefresh={() => { this.onRefresh() }}
              />
            }
          >
            <Text style={styles.TextMaster}>Số bill: <Text style={{ color: Colors.subColor, fontWeight: 'bold', }}> {this.props.navigation.state.params.blNo} </Text></Text>
            <Text style={[styles.TextMaster, { marginTop: hs(5) }]}>Chủ hàng: <Text style={{ color: Colors.subColor, fontWeight: 'bold', }}> {this.props.navigation.state.params.consignee} </Text></Text>
            <Text style={{ position: 'absolute', right: ws(20), top: hs(20), color: Colors.blue, fontSize: fs(16), fontWeight: 'bold' }}>{this.state.container}</Text>
            {/* <ItemSingle
              data={this.state.data[0]}
              onPress={() => {
                NavigationService.navigate(mainStack.detail2, {})
              }}
            /> */}
            <Text style={[
              styles.TitleLine,
              { marginTop: hs(25) }
            ]}>
              Danh sách container
          </Text>
            {/*
            ---------------------------------------------------- 
           */}
            <FlatList
              data={this.state.data}
              renderItem={
                (item, index) =>
                  this.renderItem(item, index)
              }
              showsVerticalScrollIndicator={false}
            />
          </ScrollView>
          {
            this.props.navigation.state.params.serviceType % 2 != 0 ?
              <View
                style={{
                  marginTop: hs(17),
                  marginBottom: hs(17)
                }}
              >
                <Button
                  value={'Bốc container theo lô'}
                  onPress={
                    () => {
                      if (this.state.douCont == false) {
                        this.onAutoPickup()
                      } else {
                        this.setState({ checkTypeVisible: true })
                      }
                    }
                  }
                />
              </View>
              :
              null
          }
        </View>
        {
          this.state.checkTypeVisible ?
            <View style={{ backgroundColor: 'red', height: hs(812), width: ws(375), position: 'absolute', bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', }}>
              <View style={{ backgroundColor: '#fff', height: hs(250), width: ws(375), position: 'absolute', bottom: 0, borderRadius: hs(20), padding: hs(10) }}>
                <View style={{ flexDirection: 'row' }}>
                  <TouchableOpacity style={{ width: ws(30), justifyContent: 'center', alignItems: 'center' }} onPress={() => this.closePopupCheckBox()}>
                    <Icon name='remove' size={fs(25)} color='gray' />
                  </TouchableOpacity>
                  <Text style={{ flex: 1, color: Colors.blue, textAlign: 'center', fontSize: fs(16), }}> Chọn Loại Container</Text>
                </View>
                <View style={{ padding: hs(25) }}>

                  <TouchableOpacity style={styles.btnCheckBox} onPress={() => this.onSelectCont()}>
                    <Checkbox
                      value={1}
                      onSelect={() => { this.onSelectCont() }}
                      selectedValue={this.state.cont4}
                    />
                    <Text style={styles.textCheckbox}> Container 40 fit </Text>
                  </TouchableOpacity>
                  <TouchableOpacity style={styles.btnCheckBox} onPress={() => this.onSelectCont()}>
                    <Checkbox
                      value={0}
                      onSelect={() => { this.onSelectCont() }}
                      selectedValue={this.state.cont4}
                    />
                    <Text style={styles.textCheckbox}> Container 20 fit </Text>
                  </TouchableOpacity>
                </View>
                <Button
                  value={'Xác nhận'}
                  onPress={() => { this.onAutoPickup() }}
                />
              </View>
            </View> : null}
      </View>
    )
  }
}

const styles = StyleSheet.create({
  Container: {
    flex: 1,
    backgroundColor: Colors.white,
  },
  Lefticon: {
    width: ws(11.43),
    height: hs(20),
    marginLeft: ws(36.86)
  },
  btnCheckBox: {
    flexDirection: 'row',
    paddingVertical: hs(10)
  },
  textCheckbox: {
    color: Colors.blue,
    fontSize: fs(16),
    fontWeight: 'bold'
  },
  Body: {
    width: ws(375),
    height: hs(629),
  },
  TitleLine: {
    color: Colors.black,
    fontSize: fs(18),
    fontWeight: 'bold',
    marginLeft: ws(15),
    marginTop: hs(35),
    marginBottom: hs(22),
  },
  TextMaster: {
    color: Colors.black,
    fontSize: fs(15),
    fontWeight: '500',
    marginLeft: ws(15),
    marginTop: hs(20),
  },
  SelectIconContainer: {
    width: ws(242),
    height: hs(189),
  },
  SelectIconTag: {
    width: ws(242),
    height: hs(94.5),
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  SelectIcon: {
  },
  SelectIconBG: {
    width: ws(60),
    height: hs(60),
    borderRadius: 14,
    marginBottom: hs(7),
    justifyContent: 'center',
    alignItems: 'center'
  },
  SelectIconText: {
    fontSize: fs(13),
    fontWeight: '500'
  },
  SelectIconItemContainer: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  }
})