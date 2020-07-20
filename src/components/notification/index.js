import React, { Component, useEffect } from 'react';
import {
  View,
  Alert,
  Text,
  AsyncStorage,
  Platform,
} from 'react-native';

import firebase from 'react-native-firebase';
import PushNotification from 'react-native-push-notification';
import CallApi from '../../configs/environments/callAPI';


var STORAGE_ID = "STORAGE_ID";
var STORAGE_TOKEN = "fcmToken";


export default class Notification extends Component {
  constructor(props) {
    super(props);
    this.uid = '';
    this.token = '';
    this.state = {
      deviceToken: ""
    };
    // //console.warn("sau luu local", AsyncStorage.getItem("fcmToken"))
  }

  isLogin = async () => {
    await AsyncStorage.getItem(STORAGE_ID).then((userId) => {
      var id = userId;
      this.uid = parseInt(id);
    })
    return this.uid;
  }

  async componentDidMount() {
    var is = await this.isLogin();
    // //console.log(is)
    if (is) {
      await this.checkPermission();
      await this.createNotificationListeners(); //add this line
      // await this._SenToken();
    } else {
      this.props.navigation.navigate('LoginStack')
    }
  }

  _SenToken = async () => {
    var param = [{
      UserId: this.uid,
      Token: this.state.deviceToken
    }];
    await AsyncStorage.setItem(STORAGE_TOKEN,this.state.deviceToken);
    // //console.warn("send token", this.state.deviceToken)
    var ApiName = 'setTokenDeviceNotify';
    var res = await CallApi(param,ApiName);
    const responseJson = await res.json();
    // //console.warn('responseJson',responseJson)
  }

  async checkPermission() {
    const enabled = await firebase.messaging().hasPermission();
    if (enabled) {
      this.getToken();
    } else {
      this.requestPermission();
    }
  }

  async getToken() {
    let fcmToken = await AsyncStorage.getItem(STORAGE_TOKEN);
    // console.log("get tu local", fcmToken);
    if (!fcmToken) {
      fcmToken = await firebase.messaging().getToken();
      // console.log("get tu local 111", fcmToken);  
    }
    await this.setState({
      deviceToken: fcmToken.toString(),
    })
    // console.log("deviceToken sau gán", fcmToken)
    await this._SenToken();
    }

  async componentWillUnmount() {
    await this.createNotificationListeners();
  }

  async createNotificationListeners() {
    firebase.notifications().onNotification(notification => {
      // console.log('onNotification', notification);
      const {title, body} = notification;
      const user_id = notification.data.user_id;
      const channelId = new firebase.notifications.Android.Channel(
        'Default',
        'Default',
        firebase.notifications.Android.Importance.High,
      );
      firebase.notifications().android.createChannel(channelId);

      let notification_to_be_displayed = new firebase.notifications.Notification({
        data:notification.data,
        sound: 'default',
        show_in_foreground: true,
        title: title,
        largeIcon: "ic_notification",
        smallIcon: "ic_notification",
        body: body,
      });
      if(Platform.OS === 'android') {
        notification_to_be_displayed.android
          .setPriority(firebase.notifications.Android.Priority.High)
          .android.setChannelId('Default')
          .android.setVibrate(1000);
      }
      if(user_id === this.uid.toString()){
        firebase.notifications().displayNotification(notification_to_be_displayed);
      }
    });

      firebase.notifications().onNotificationOpened((notification) => {
        const user_id = notification.data.user_id;
        let notification_to_be_displayed = new firebase.notifications.Notification({
          data:notification.data,
          sound: 'default',
          show_in_foreground: true,
          title: title,
          largeIcon: "ic_notification",
          smallIcon: "ic_notification",
          body: body,
        });
        // console.log(notification)
        if (user_id === this.uid.toString()) {
        firebase.notifications().removeDeliveredNotification(notification_to_be_displayed);
        }
    });
    firebase.notifications().getInitialNotification()
        .then((notification) => {
      // console.log('getInitialNotification', notification);
          const user_id = notification.data.user_id;
          let notification_to_be_displayed = new firebase.notifications.Notification({
            data:notification.data,
            sound: 'default',
            show_in_foreground: true,
            title: title,
            largeIcon: "ic_notification",
            smallIcon: "ic_notification",
            body: body,
          });
        if (user_id === this.uid.toString()) {
          firebase.notifications().displayNotification(notification_to_be_displayed);
        }
      });
  };

  async requestPermission() {
    try {
      await firebase.messaging().requestPermission();
      // User has authorised
      await this.getToken();
    } catch (error) {
      // User has rejected permissions
      console.log('permission rejected');
    }
  }

  render() {
    return (
      <View>
      </View>
    );
  }
}