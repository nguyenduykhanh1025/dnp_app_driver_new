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
import ListItem from './detail-item';
import navigation from '@/utils/navigation';
import {
  SCANNER_QR,
} from '@/modules/home/constants';
import {
  getToken,
  saveChassis,
  saveTruck,
} from '@/stores';
import {
  callApi
} from '@/requests';


const next = require('@/assets/icons/icon_next.png')

export default class DetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      active1: false,
      active2: false,
      active3: false,
      active4: false,
      truckNoList: [],
      chassisNoList: [],
    };
  }

  componentDidMount = async () => {
    this.onGetTruckList()
  }

  onGetTruckList = async () => {
    var token = await getToken()
    const params = {
      api: 'truck',
      param: '',
      token: token,
      method: 'GET'
    }
    var result = undefined;
    result = await callApi(params);
    console.log('result', result)
    if (result.code == 0) {
      await this.setState({
        truckNoList: result.data.truckNoList,
        chassisNoList: result.data.chassisNoList,
      })
    }
    else {
      Alert.alert('Thông báo!', result.msg)
    }
  }

  onSelectService = async (value) => {
    switch (value) {
      case 1:
        NavigationService.navigate(mainStack.detail1, { serviceType: 1 })
        break;
      case 2:
        NavigationService.navigate(mainStack.detail1, { serviceType: 2 })
        break;
      case 3:
        NavigationService.navigate(mainStack.detail1, { serviceType: 3 })
        break;
      case 4:
        NavigationService.navigate(mainStack.detail1, { serviceType: 4 })
        break;
      default:
        break;
    }
  }

  render() {
    return (
      <ScrollView style={styles.Container}>

        <HeaderMain
          renderLeft={(
            <Image source={lefticon} style={styles.Lefticon} />
          )}
          onPressLeft={() => {
            this.props.navigation.goBack()
          }}
          status={0}
        />

        <View style={styles.Body}>
          <Text style={styles.TitleLine}>Chọn xe</Text>
          <DropDown
            data={this.state.truckNoList}
            title={'BSX Đầu Kéo'}
            style={{ marginBottom: hs(33) }}
            onSelect={async (item) => {
              await saveTruck(item)
            }}
          />
          <DropDown
            data={this.state.chassisNoList}
            title={'BSX Rơ Mooc'}
            onSelect={async (item) => {
              await saveChassis(item)
            }}
          />
          <Text style={styles.TitleLine}>Chọn dịch vụ</Text>
          {/*
            ---------------------------------------------------- 
           */}
          <View style={{
            justifyContent: 'center',
            alignItems: 'center',
            width: ws(375),
            marginBottom: hs(43)
          }}>
            <View style={styles.SelectIconContainer}>
              <View style={styles.SelectIconTag}>
                <TouchableOpacity
                  onPress={() => { this.onSelectService(1) }}
                >
                  <View style={styles.SelectIconItemContainer}>
                    <View style={[
                      styles.SelectIconBG,
                      this.state.active1 ?
                        {
                          backgroundColor: Colors.blue
                        }
                        :
                        {
                          backgroundColor: Colors.E5ECFF
                        }
                    ]}>
                      <Image
                        source={cont2_icon}
                        style={[
                          styles.SelectIcon,
                          this.state.active1 ?
                            {
                              tintColor: Colors.white
                            }
                            :
                            {
                              tintColor: Colors["5E79BF"]
                            },
                          {
                            width: ws(34),
                            height: ws(34),
                          }
                        ]} />
                    </View>
                    <Text style={[
                      styles.SelectIconText,
                      this.state.active1 ?
                        {
                          color: Colors.blue
                        }
                        :
                        {
                          color: Colors["96ABE1"]
                        },
                    ]}>
                      Bốc công hàng
                    </Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => { this.onSelectService(4) }}
                >
                  <View style={styles.SelectIconItemContainer}>
                    <View style={[
                      styles.SelectIconBG,
                      this.state.active2 ?
                        {
                          backgroundColor: Colors.blue
                        }
                        :
                        {
                          backgroundColor: Colors.E5ECFF
                        }
                    ]}>
                      <Image
                        source={cont3_icon}
                        style={[
                          styles.SelectIcon,
                          this.state.active2 ?
                            {
                              tintColor: Colors.white
                            }
                            :
                            {
                              tintColor: Colors["5E79BF"]
                            },
                          {
                            width: ws(43.98),
                            height: ws(34),
                          }
                        ]} />
                    </View>
                    <Text style={[
                      styles.SelectIconText,
                      this.state.active2 ?
                        {
                          color: Colors.blue
                        }
                        :
                        {
                          color: Colors["96ABE1"]
                        }
                    ]}>
                      Hạ công hàng
                    </Text>
                  </View>
                </TouchableOpacity>
              </View>
              <View style={styles.SelectIconTag}>
                <TouchableOpacity
                  onPress={() => { this.onSelectService(3) }}
                >
                  <View style={styles.SelectIconItemContainer}>
                    <View style={[
                      styles.SelectIconBG,
                      this.state.active3 ?
                        {
                          backgroundColor: Colors.blue
                        }
                        :
                        {
                          backgroundColor: Colors.E5ECFF
                        }
                    ]}>
                      <Image
                        source={cont4_icon}
                        style={[
                          styles.SelectIcon,
                          this.state.active3 ?
                            {
                              tintColor: Colors.white
                            }
                            :
                            {
                              tintColor: Colors["5E79BF"]
                            },
                          {
                            width: ws(34),
                            height: ws(34),
                          }
                        ]} />
                    </View>
                    <Text style={[
                      styles.SelectIconText,
                      this.state.active3 ?
                        {
                          color: Colors.blue
                        }
                        :
                        {
                          color: Colors["96ABE1"]
                        },
                    ]}>
                      Bốc công rỗng
                    </Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => { this.onSelectService(2) }}
                >
                  <View style={styles.SelectIconItemContainer}>
                    <View style={[
                      styles.SelectIconBG,
                      this.state.active4 ?
                        {
                          backgroundColor: Colors.blue
                        }
                        :
                        {
                          backgroundColor: Colors.E5ECFF
                        }
                    ]}>
                      <Image
                        source={cont5_icon}
                        style={[
                          styles.SelectIcon,
                          this.state.active4 ?
                            {
                              tintColor: Colors.white
                            }
                            :
                            {
                              tintColor: Colors["5E79BF"]
                            },
                          {
                            width: ws(34.95),
                            height: ws(26.72),
                          }
                        ]} />
                    </View>
                    <Text style={[
                      styles.SelectIconText,
                      this.state.active4 ?
                        {
                          color: Colors.blue
                        }
                        :
                        {
                          color: Colors["96ABE1"]
                        }
                    ]}>
                      Hạ công rỗng
                    </Text>
                  </View>
                </TouchableOpacity>
              </View>
            </View>
          </View>
          {/*
            ---------------------------------------------------- 
           */}
          {/* <Button
            value={'Tiếp tục'}
            onPress={
              () => {
                NavigationService.navigate(mainStack.detail1, {})
              }
            }
          /> */}
        </View>
      </ScrollView>
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
    marginLeft: ws(29),
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