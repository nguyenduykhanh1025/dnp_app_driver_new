import React, { Component } from 'react'
import {
  Text,
  View,
  StyleSheet,
  FlatList,
  TextInput,
  TouchableHighlight,
  TouchableOpacity,
  ScrollView,
  Image,
  Alert,
  StatusBar,
  RefreshControl,
  TouchableWithoutFeedback
} from 'react-native';
import NavigationService from '@/utils/navigation';
import {
  homeStack,
  mainStack
} from '@/config/navigator';
import { signOut } from '@/modules/auth/action';
import { connect } from 'react-redux';
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
  DropDownProfile,
} from '@/components';
import {
  righticon,
  lefticon,
  cont2_icon,
  cont3_icon,
  cont4_icon,
  cont5_icon,
} from '@/assets/icons';
import Item from './detail1-item-cont';
import Item1 from './detail1-item'
import navigation from '@/utils/navigation';
import {
  SCANNER_QR,
} from '@/modules/home/constants';
import {
  callApi,
} from '@/requests';
import {
  getToken,
  getUpEnable,
  getDownEnable,
} from '@/stores';

const next = require('@/assets/icons/icon_next.png')
const filter = require('@/assets/icons/filter.png')

class DetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      refreshing: false,
      consignee_name: '',
      container_no: '',
      filterVisible: false,
    };
    this.token = null;
    this.upEnable = null;
    this.DownEnable = null;
  }

  componentDidMount = async () => {
    this.setState({
      filterVisible: false,
      refreshing: true,
    })
    this.token = await getToken();
    this.upEnable = await getUpEnable();
    this.DownEnable = await getDownEnable();
    this.onGetContainerList(this.props.navigation.state.params.serviceType);
  }

  onGetContainerList = async (serviceType) => {
    const params = {
      api: 'shipments/service-type/' + serviceType,
      param: '',
      token: this.token,
      method: 'GET'
    }
    const params1 = {
      api: 'pickup-assign/serviceType/' + serviceType + '/list',
      param: {
        containerNo: this.state.container_no,
        consignee: this.state.consignee_name,
      },
      token: this.token,
      method: 'POST'
    }

    var result = undefined;
    result = serviceType % 2 != 0 ? await callApi(params) : await callApi(params1);

    if (result.code == 0) {
      if (result.shipmentList) {
        await this.setState({
          data: result.shipmentList,
        })
      } else if (result.data) {
        await this.setState({
          data: result.data,
        })
      }
      else {
        Alert.alert(
          "Thông báo!",
          "Hiện tại không có công nào!",
          [
            { text: "OK", onPress: () => this.props.navigation.goBack() }
          ],
          { cancelable: false }
        );
      }
    }
    else {
      Alert.alert(
        "Thông báo",
        result.msg,
        [
          { text: "OK", onPress: () => result.errorCode == 401 ? this.props.dispatch(signOut()) : this.props.navigation.goBack() }
        ],
        { cancelable: false }
      );
    }
    this.setState({
      refreshing: false,
    })
  }

  renderItem = (item, index) => (
    console.log('this.DownEnable',this.DownEnable,item.item.sztp),
    this.props.navigation.state.params.serviceType % 2 == 0 ?
      <Item
        data={item.item}
        onPress={() => {
          this.DownEnable == '1' && item.item.sztp.slice(0, 1) != '2' ?
            Alert.alert('Thông báo !', 'Đã chọn container 20 không thể chọn container 40 .')
            :
            this.shipmentDetail(item.item.shipmentDetailId, item.item.pickupAssignId)
        }}
        disabled={item.item.clickable == true ? false : true}
      /> :
      <Item1
        data={item.item}
        onPress={() => {
          NavigationService.navigate(mainStack.detail11, {
            shipmentId: item.item.batchId,
            serviceType: this.props.navigation.state.params.serviceType,
            blNo: item.item.blNo,
            consignee: item.item.consignee
          })
        }}
      />
  )

  shipmentDetail = async (id,asign) => {
    const params = {
      api: 'shipment-detail/' + id + '/pickup-info',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    if (result.code == 0) {
      var data = result.data
      
      NavigationService.navigate(mainStack.detail2, {
        data: {
          containerNo: data.containerNo,
          pickupAssignId: asign,
          sztp: data.sztp,
          wgt: data.wgt,
          consignee: data.consignee,
          address: data.address,
          mobileNumber: data.mobileNumber,
          remark: data.remark,
          cargoType: data.cargoType,
          shipmentDetailId: id,
        }
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  closeFilter = async () => {
    await this.setState({
      filterVisible: false
    })
  }

  onRefresh = async () => {
    await this.setState({
      container_no: '',
      consignee_name:'',
    })
    this.componentDidMount()
  }

  render() {
    return (
      <View style={styles.Container}>
        <StatusBar barStyle="light-content" />
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
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Text style={[styles.TitleLine, { flex: 1 }]}>{
                this.props.navigation.state.params.serviceType % 2 == 0 ?
                  'Hạ công vào cảng'
                  :
                  'Bốc container từ cảng'
              }</Text>
              {
                this.props.navigation.state.params.serviceType % 2 == 0 ?
                  <TouchableOpacity style={{ width: ws(55), height: hs(35), justifyContent: 'center', alignItems: 'center' }} onPress={() => this.setState({ filterVisible: true })}>
                    <Image source={filter} style={{ width: ws(20), height: ws(20) }} />
                  </TouchableOpacity> : null
              }
            </View>


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
        </View>
        {this.state.filterVisible ?
          <TouchableWithoutFeedback onPress={() => this.closeFilter()}>
            <View style={{ height: hs(812), width: ws(375), position: 'absolute', bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', }}>
              <TouchableWithoutFeedback>
                <View style={{ backgroundColor: '#fff', height: hs(250), width: ws(375), position: 'absolute', bottom: 0, borderRadius: hs(20), padding: hs(10), borderBottomRightRadius: 0, borderBottomLeftRadius: 0 }}>
                  <View style={{ flexDirection: 'row' }}>
                    <TouchableOpacity style={{ width: ws(30), justifyContent: 'center', alignItems: 'center' }} onPress={() => this.closeFilter()}>
                      <Icon name='remove' size={fs(25)} color='gray' />
                    </TouchableOpacity>
                    <Text style={{ flex: 1, color: Colors.blue, textAlign: 'center', fontSize: fs(16), }}> Tìm kiếm Container</Text>
                  </View>
                  <View style={{ padding: hs(25) }}>

                    <View style={styles.viewTextFilter}>
                      <Text style={styles.textTilteFilter}>Số container </Text>
                      <TextInput
                        style={styles.txtInputFilter}
                        placeholder='Số container'
                        onChangeText={(text) => this.setState({ container_no: text })}
                      />
                    </View>
                    <View style={styles.viewTextFilter}>
                      <Text style={styles.textTilteFilter}>Chủ hàng </Text>
                      <TextInput
                        style={styles.txtInputFilter}
                        placeholder="Chủ hàng"
                        onChangeText={(text) => this.setState({ consignee_name: text })}
                      />
                    </View>
                  </View>
                  <View style={{ position: 'absolute', bottom: hs(10) }}>
                    <Button
                      value={'Tìm kiếm'}
                      onPress={() => { this.componentDidMount() }}
                    />
                  </View>

                </View>
              </TouchableWithoutFeedback>
            </View>
          </TouchableWithoutFeedback> : null
        }
      </View>
    )
  }
}

const mapStateToProps = (state) => {
  return {
  };
};

export default connect(mapStateToProps)(DetailScreen);

const styles = StyleSheet.create({
  Container: {
    flex: 1,
    backgroundColor: Colors.white,
  },
  txtInputFilter: {
    flex: 1,
    borderWidth: 0.8,
    height: hs(35),
    paddingHorizontal: ws(5)
  },
  viewTextFilter: {
    flexDirection: 'row',
    paddingVertical: hs(10),
    justifyContent: 'center',
    alignItems: 'center',
  },
  textTilteFilter: {
    fontSize: fs(16),
    color: Colors.grey6,
    textAlign: 'right',
    width: ws(100),
    marginRight: ws(10)
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
  }
})