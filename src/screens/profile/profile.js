import React, { Component } from 'react'
import {
  Text,
  View,
  StyleSheet,
  ImageBackground,
  Image,
  ScrollView,
  Switch,
  TouchableOpacity,
} from 'react-native';
import NavigationService from '@/utils/navigation';
import {
  Colors,
  sizes,
  sizeWidth,
  sizeHeight,
  commonStyles,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import {
  Right,
  Thumbnail,
  Content
} from 'native-base';
import { getInfoUser } from '@/mock/index';
import { signOut } from '@/modules/auth/action';
import { connect } from 'react-redux';
import {
  getRequestAPI,
  callApi
} from '@/requests'
import {
  getToken
} from '@/stores';
import {
  down_arrow
} from '@/assets/icons';
import Toast from 'react-native-tiny-toast'

const profile_background = require('@/assets/images/profile_2.jpg')
const ic_camera = require('@/assets/icons/camera.png')

class ProfileScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      isEnabled: false,
    }
  };

  componentDidMount = async () => {
    this.setState({
      data: getInfoUser[0].Data[0]
    })

    this.getUserInfo();
  };

  getUserInfo = async () => {
    const token = await getToken();
    const param = {
      url: 'user/info',
      token: token,
    }
    const result = await getRequestAPI(param);
    this.setState({ data: result.data })
  }

  toggleSwitch = async () => {
    await this.setState({
      isEnabled: this.state.isEnabled ? false : true
    })
  }

  onLogout = async () => {
    Toast.showLoading()
    var token = await getToken();
    const params = {
      api: 'user/logout',
      param: '',
      token: token,
      method: 'POST'
    }
    var result = undefined;
    result = await callApi(params);
    if (result.code == 0) {
      Toast.hide()
      this.props.dispatch(signOut())
    }
    else {
      Toast.hide()
      FlashMessage(result.msg, 'warning')
    }
  }

  render() {
    var { data } = this.state;
    return (
      <View style={styles.container}>
        <ImageBackground
          source={require('@/assets/images/home_bg.png')}
          style={styles.bg}
        >
          <View style={styles.bgContainer}>
            <View style={{ alignItems: 'flex-end' }}>
              <TouchableOpacity
                onPress={() => this.onLogout()}
              >
                <View>
                  <Text style={{ marginRight: 10, color: Colors.white }}>Đăng xuất</Text>
                </View>
              </TouchableOpacity>
            </View>
            <View style={{ alignItems: 'center', justifyContent: 'center', height: sizeHeight(22.5) }}>
              <View>
                <Image
                  source={profile_background}
                  style={{
                    width: (sizeHeight(100) * sizeWidth(100)) * 0.0004,
                    height: (sizeHeight(100) * sizeWidth(100)) * 0.0004,
                    borderRadius: 100,
                  }}
                />
                <Image
                  source={ic_camera}
                  style={{
                    width: (sizeHeight(100) * sizeWidth(100)) * 0.00013,
                    height: (sizeHeight(100) * sizeWidth(100)) * 0.00013,
                    position: 'absolute',
                    bottom: 0,
                    right: -(sizeHeight(100) * sizeWidth(100)) * 0.00003,
                  }}
                />
              </View>
            </View>
          </View>
        </ImageBackground>
        <ScrollView
          style={styles.scroll}
          showsVerticalScrollIndicator={false}
        >
          <View style={styles.body}>
            <View>
              <View style={{ alignItems: 'center' }}>
                <View style={{ flexDirection: 'column', width: sizeWidth(85), marginTop: 20 }}>
                  <Text style={styles.textTitle}>Họ và tên</Text>
                  <Text style={styles.textValue}>{'DANG BIN'}</Text>
                  <View style={{ borderBottomWidth: 1.5, borderBottomColor: '#EFF1F5', borderStyle: "solid" }} />
                </View>
                <View style={{ flexDirection: 'column', width: sizeWidth(85), marginTop: 20 }}>
                  <Text style={styles.textTitle}>Số điện thoại</Text>
                  <Text style={styles.textValue}>{'0123654879'}</Text>
                  <View style={{ borderBottomWidth: 1.5, borderBottomColor: '#EFF1F5', borderStyle: "solid" }} />
                </View>
                <View style={{ flexDirection: 'column', width: sizeWidth(85), marginTop: 20 }}>
                  <Text style={styles.textTitle}>Số xe đầu kéo</Text>
                  <TouchableOpacity
                    style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}
                  >
                    <Text style={styles.textValue}>43-24812</Text>
                    <Image
                      source={down_arrow}
                      style={styles.downIcon}
                      resizeMode='contain'
                    />
                  </TouchableOpacity>
                  <View style={{ borderBottomWidth: 1.5, borderBottomColor: '#EFF1F5', borderStyle: "solid" }} />
                </View>
                <View style={{ flexDirection: 'column', width: sizeWidth(85), marginTop: 20 }}>
                  <Text style={styles.textTitle}>Số xe Rơ-mooc</Text>
                  <TouchableOpacity
                    style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}
                  >
                    <Text style={styles.textValue}>43-24812</Text>
                    <Image
                      source={down_arrow}
                      style={styles.downIcon}
                      resizeMode='contain'
                    />
                  </TouchableOpacity>
                  <View style={{ borderBottomWidth: 1.5, borderBottomColor: '#EFF1F5', borderStyle: "solid" }} />
                </View>
                <View style={{ flexDirection: 'row', width: sizeWidth(85), marginTop: 20, alignItems: 'center', marginBottom: sizeHeight(25) }}>
                  <Text style={[styles.textTitle, { color: '#000' }]}>Bật chia sẻ vị trí</Text>
                  <Switch
                    style={{ position: 'absolute', right: 0 }}
                    trackColor={{ false: Colors.grey2, true: '#0095FF' }}
                    thumbColor={this.state.isEnabled ? Colors.white : "#f4f3f4"}
                    ios_backgroundColor="#0095FF"
                    onValueChange={this.toggleSwitch}
                    value={this.state.isEnabled}
                  />
                </View>
              </View>
            </View>
          </View>
        </ScrollView>
      </View>
    )
  }
}

const mapStateToProps = (state) => {
  return {
  };
};

export default connect(mapStateToProps)(ProfileScreen);

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.white,
  },
  scroll: {
    height: sizeHeight(80),
  },
  profile: {
    width: sizeWidth(100),
    height: sizeHeight(35)
  },
  contentView: {
    paddingLeft: sizeWidth(5),
    backgroundColor: Colors.white,
    height: sizeHeight(100)
  },
  element: {
    width: sizeWidth(80),
    height: sizeHeight(10),
    alignItems: 'center',
    alignSelf: 'center',
    borderBottomWidth: 1,
    borderColor: Colors.blue,
    borderRadius: 4,
    marginVertical: sizeHeight(1.5)
  },
  icon: {
    marginHorizontal: sizeWidth(15),
  },
  textTitle: {
    fontSize: 15,
    lineHeight: 18,
    color: '#BEC2CE',
    marginVertical: 10,
  },
  textValue: {
    fontSize: 18,
    lineHeight: 22,
    letterSpacing: -0.434118,
    marginVertical: 5,
  },
  bg: {
    height: sizeHeight(30),
    width: sizeWidth(100),
  },
  body: {
    width: sizeWidth(100),
  },
  bgContainer: {
    flexDirection: 'column',
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    paddingTop: sizeHeight(5)
  },
  downIcon: {
    height: sizeHeight(2),
    width: sizeWidth(4),
    marginLeft: sizeWidth(2),
    tintColor: '#919BBB'
  },
})