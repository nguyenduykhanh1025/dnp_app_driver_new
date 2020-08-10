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
    // console.log('resultonGetPickupList', result)
    if (result.code == 0) {
      if (result.data.length > 0) {
        await this.setState({
          data: result.data,
        })
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
      api: 'shipment/' + this.props.navigation.state.params.shipmentId + '/auto-pickup',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    // console.log('resultonAutoPickup', result)
    if (result.code == 0) {
      // NavigationService.navigate(homeTab.home)
      NavigationService.navigate(mainStack.detail2, {
        data: {
          "containerNo": null,
          "pickupAssignId": result.pickupAssignId,
          "sztp": null,
          "wgt": null,
        }
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
        this.DownEnable == '1' || this.upEnable == '1' && item.item.sztp.slice(0, 2) != '20' ?
          Alert.alert('Thông báo !', 'Không thể chọn.')
          :
          NavigationService.navigate(mainStack.detail2, { data: item.item })
      }}
      disabled={!item.item.clickable ? true : false}
    />
  )

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
            {/* <Text style={styles.TitleLine}>Cont chung</Text> */}
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
              Cont chỉ định
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
                  value={'Chọn theo lô'}
                  onPress={
                    () => {
                      this.onAutoPickup()
                    }
                  }
                />
              </View>
              :
              null
          }
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
  }
})