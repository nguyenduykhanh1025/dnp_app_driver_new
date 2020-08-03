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
  mainStack
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
import Item from './detail1-item';
import navigation from '@/utils/navigation';
import {
  SCANNER_QR,
} from '@/modules/home/constants';
import {
  callApi,
} from '@/requests';
import {
  getToken,
} from '@/stores';

const next = require('@/assets/icons/icon_next.png')

export default class DetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      refreshing: false
    };
    this.token = null;
  }

  componentDidMount = async () => {
    this.setState({
      refreshing: true,
    })
    this.token = await getToken();
    console.log('this.props.navigation.state.params.serviceType', this.props.navigation.state.params.serviceType)
    this.onGetContainerList(this.props.navigation.state.params.serviceType);
  }

  onRefresh = async () => {
    this.componentDidMount()
  }

  onGetContainerList = async (serviceType) => {
    const params = {
      api: 'shipments/service-type/' + serviceType,
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('resultonGetContainerList', result)
    if (result.code == 0) {
      await this.setState({
        data: result.shipmentList,
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
    this.setState({
      refreshing: false,
    })
  }

  renderItem = (item, index) => (
    <Item
      data={item.item}
      onPress={() => {
        NavigationService.navigate(mainStack.detail11, {
          shipmentId: item.item.batchId,
          serviceType: this.props.navigation.state.params.serviceType,
        })
      }}
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
            <Text style={styles.TitleLine}>Bốc công từ cảng</Text>

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
          {/* <View
            style={{
              marginTop: hs(17),
              marginBottom: hs(17)
            }}
          >
            <Button
              value={'Tiếp tục'}
              onPress={
                () => {
                  NavigationService.navigate(mainStack.detail11, {})
                }
              }
            />
          </View> */}
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