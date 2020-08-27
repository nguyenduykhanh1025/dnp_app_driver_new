import React, { Component } from 'react'
import {
  Text,
  View,
  StyleSheet,
  ScrollView,
  StatusBar,
  RefreshControl,
  Alert,
} from 'react-native';
import NavigationService from '@/utils/navigation';
import { mainStack } from '@/config/navigator';
import {
  Colors,
  sizes,
  sizeWidth,
  sizeHeight,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons'
import { getListNotification, SearchQRCode } from '@/mock/index';
import Item from './item';
import { HeaderMain } from '@/components';
import { getToken } from '@/stores';

export default class ListScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [
        // {
        //   "notificationType": "RM",
        //   "seenFlg": true,
        //   "title": "sdfsd",
        //   "content": "sdfsd",
        //   "notifyLink": "dsfsdf",
        //   "createTime": '2020-08-24T15:27:08'
        // },
        // {
        //   "notificationType": "LM",
        //   "seenFlg": false,
        //   "title": "sdfsd",
        //   "content": "sdfsd",
        //   "notifyLink": "dsfsdf",
        //   "createTime": '2020-08-24T15:27:08'
        // },
      ],
      visible: false,
      refreshing: false,
    }
    this.token = null;
  };

  componentDidMount = async () => {
    this.token = await getToken();
    this.onGetNotificationList()
  };

  onRefresh = async () => {
    this.componentDidMount()
  }

  onGetNotificationList = async () => {
    const params = {
      api: 'notify',
      param: '',
      token: this.token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('resultonGetNotificationList', result)
    if (result.code == 0) {
      await this.setState({
        data: result.notificationList,
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  onClose = () => {
    this.setState({
      visible: false,
    })
  };

  onOpen = () => {
    this.setState({
      visible: true,
    })
  }

  renderLeft = () => {
    return null
  }

  renderRight = () => {
    return null
  }

  renderCenter = () => (
    <Text style={{
      fontSize: fs(18),
      fontWeight: 'bold',
      color: Colors.white,
      marginLeft: ws(20)
    }}>
      Thông báo
    </Text>
  )

  render() {
    var { data } = this.state;
    return (
      <View>
        <HeaderMain
          backgroundColor={Colors.blue}
          title='Notification'
          renderCenter={this.renderCenter()}
          disableBG
          disableStep
        />
        <StatusBar
          translucent
          barStyle={'light-content'}
          backgroundColor='transparent'
        />
        <ScrollView
          style={styles.container}
          showsVerticalScrollIndicator={false}
          refreshControl={
            <RefreshControl
              refreshing={this.state.refreshing}
              onRefresh={() => { this.onRefresh() }}
            />
          }
        >
          <View style={styles.list}>
            {data.map((item, index) => (
              <View>
                <View style={styles.itemView}>
                  <Item
                    data={item}
                    index={index}
                    onPress={() => {
                      // NavigationService.navigate(mainStack.result, { Data: [] })
                    }}
                  />
                </View>
              </View>
            ))}
          </View>
        </ScrollView>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.white,
    height: sizeHeight(97),
  },
  title: {
    fontSize: sizes.h3,
    paddingLeft: sizeWidth(2),
    color: Colors.blueColor
  },
  list: {
    alignItems: 'center',
    marginBottom: sizeHeight(20),
  }
})