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
} from 'react-native';
import NavigationService from '@/utils/navigation';
import {
  homeStack,
  mainStack,
  homeTab,
} from '@/config/navigator';
import {
  Colors,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import {
  HeaderMain,
  DropDown,
  Button,
} from '@/components';
import {
  righticon,
  lefticon,
} from '@/assets/icons';
import Item from './detail11-item';
import {
  getChassis,
  getToken,
  getTruck,
} from '@/stores';

const next = require('@/assets/icons/icon_next.png')

export default class DetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      truckNo: '',
      chassisNo: '',
    };
    this.token = null;
  }

  componentDidMount = async () => {
    var truckNo = await getTruck();
    var chassisNo = await getChassis();
    this.token = await getToken();
    console.log('this.props.navigation.state.params.data', this.props.navigation.state.params.data)
    await this.setState({
      truckNo: truckNo,
      chassisNo: chassisNo,
      data: [this.props.navigation.state.params.data],
    })
  }

  renderItem = (item, index) => (
    console.log('item', item),
    <Item
      data={item.item}
    />
  )

  onPickupContainer = async () => {
    const params = {
      api: 'pickup',
      param: {
        "pickupAssignId": this.props.navigation.state.params.data.pickupAssignId,
        "containerNo": this.props.navigation.state.params.data.containerNo,
        "truckNo": this.state.truckNo,
        "chassisNo": this.state.chassisNo,
        "shipmentDetailId": this.props.navigation.state.params.data.shipmentDetailId
      },
      token: this.token,
      method: 'POST'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('resultonPickupContainer', result)
    if (result.code == 0) {
      NavigationService.navigate(homeTab.home, { update: 1 })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  render() {
    return (
      <View style={styles.Container}>

        <HeaderMain
          renderLeft={(
            <Image source={lefticon} style={styles.Lefticon} />
          )}
          onPressLeft={() => {
            this.props.navigation.goBack()
          }}
          status={2}
        />
        <View style={styles.Body}>
          <ScrollView
            showsVerticalScrollIndicator={false}
          >
            <Text style={styles.TitleLine}>Xe đăng ký</Text>
            <View style={[styles.RegisterCarContainer, { marginBottom: hs(20) }]}>
              <View style={styles.RegisterCarTag}>
                <View style={styles.RegisterCarItem}>
                  <Text style={styles.RegisterCarLabel}>
                    BSX Đầu Kéo
                  </Text>
                  <Text style={styles.RegisterCarValue}>
                    {this.state.truckNo}
                  </Text>
                </View>
                <View style={[
                  styles.RegisterCarItem,
                  {
                    marginRight: ws(12)
                  }
                ]}>
                  <Text style={styles.RegisterCarLabel}>
                    BSX Rơ Mooc
                  </Text>
                  <Text style={styles.RegisterCarValue}>
                    {this.state.chassisNo}
                  </Text>
                </View>
              </View>
            </View>
            <FlatList
              data={this.state.data}
              renderItem={
                (item, index) =>
                  this.renderItem(item, index)
              }
              showsVerticalScrollIndicator={false}
            />

            <View style={{
              marginTop: hs(2.5),
              alignItems: 'center',
              justifyContent: 'center',
              marginBottom: hs(5)
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
                  Thông tin chi tiết
                </Text>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Khách hàng</Text>
                  <Text style={styles.txtValue2}>{this.state.data.consignee}</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Đ/c giao</Text>
                  <Text style={styles.txtValue2}>{this.state.data.address}</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >ĐT giao</Text>
                  <Text style={styles.txtValue2}>{this.state.data.mobileNumber}</Text>
                </View>
                <View style={styles.Line}>
                  <Text style={styles.txtLabel1} >Ghi chú</Text>
                  <Text style={[styles.txtValue2, { fontSize: 15, fontWeight: null }]}>{this.state.data.remark}</Text>
                </View>
              </View>
            </View>
          </ScrollView>
          <View
            style={{
              marginTop: hs(17),
              marginBottom: hs(17)
            }}
          >
            <Button
              value={'Xác nhận'}
              onPress={
                () => {
                  // NavigationService.navigate(homeTab.home)
                  Alert.alert(
                    "Thông báo xác nhận!",
                    "Bạn có chắc chắn xác nhận không?",
                    [
                      { text: "Có", onPress: () => this.onPickupContainer() },
                      {
                        text: "Không",
                        style: "cancel"
                      },
                    ],
                    { cancelable: false }
                  );
                }
              }
            />
          </View>
        </View>
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
  },
  RegisterCarContainer: {
    width: ws(375),
    justifyContent: 'center',
    alignItems: 'center',
  },
  RegisterCarTag: {
    width: ws(321),
    height: hs(52),
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  RegisterCarItem: {
    borderBottomWidth: 1.5,
    borderStyle: 'solid',
    borderBottomColor: '#EFF1F5',
  },
  RegisterCarLabel: {
    fontSize: fs(15),
    color: Colors.blue,
    fontWeight: 'bold',
    marginLeft: ws(4),
  },
  RegisterCarValue: {
    fontSize: fs(16),
    color: Colors.black,
    fontWeight: 'bold',
    marginLeft: ws(4),
  },
})