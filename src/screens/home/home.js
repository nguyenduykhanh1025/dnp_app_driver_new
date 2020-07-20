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
  TouchableOpacity
} from 'react-native';
import NavigationService from '@/utils/navigation';
import { mainStack, authStack } from '@/config/navigator';
import { Colors, sizes, sizeWidth, sizeHeight } from '@/commons'
import { getListInfoHome } from '@/mock/index';
import Item from './item';
import {
  down_arrow, 
  cont2_icon, 
  cont3_icon,
  cont4_icon,
  cont5_icon
} from '@/assets/icons'
import HomeButton from './home_button'
import {getRequestAPI} from '@/requests'
import {getToken} from '@/stores';


export default class HomeScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      Data: [
        {
            batchCode: "HH001",
            billNumber: "BN001",
            size: "A12",
            type: "AB23",
            contCount: "10",
            note: 'ok 1234'
        },
        {
            batchCode: "HH002",
            billNumber: "BN002",
            size: "A12",
            type: "AB23",
            contCount: "20",
            note: 'ok 1234'
        },
        {
            batchCode: "HH003",
            billNumber: "BN003",
            size: "A12",
            type: "AB23",
            contCount: "12",
            note: 'ok 1234'
        },
        {
            batchCode: "HH004",
            billNumber: "BN004",
            size: "A12",
            type: "AB23",
            contCount: "9",
            note: 'ok 1234'
        },
        {
            batchCode: "HH005",
            billNumber: "BN005",
            size: "A12",
            type: "AB23",
            contCount: "3",
            note: 'ok 1234'
        },
      ],
      carCode: '73A1 - 042.32',
      ContCode: '73A1 - 042.32',
      date: '10 Jun 2020',
      boc_focused: true,
      ha_focused: false,
      boc_rong_focused: false,
      ha_rong_focused: false,
      userData: [],
    }
  };

  componentDidMount = async () => {
    var { data } = this.state;
    this.setState({
      data: getListInfoHome[0].Data
    })
    this.getUserInfo()

  };

  getUserInfo = async () => {
    const token = await getToken();
    const param = {
      url: 'user/info',
      token: token,
    }
    const result = await getRequestAPI(param);
    this.setState({userData: result.data})
  }

  onLoadProduct  = () => {
    this.setState({
      boc_focused: true,
      ha_focused: false,
      boc_rong_focused: false,
      ha_rong_focused: false,
    })
  }

  onUnLoadProduct = () => {
    this.setState({
      boc_focused: false,
      ha_focused: true,
      boc_rong_focused: false,
      ha_rong_focused: false,
    })
  }

  onLoadEmptyProduct = () => {
    this.setState({
      boc_focused: false,
      ha_focused: false,
      boc_rong_focused: true,
      ha_rong_focused: false,
    })
  }

  onUnLoadEmptyProduct = () => {
    this.setState({
      boc_focused: false,
      ha_focused: false,
      boc_rong_focused: false,
      ha_rong_focused: true,
    })
  }

  onChangeCarCode = () => {

  }

  onChangeContCode = () => {
    
  }

  render() {
    var { Data, carCode, ContCode, date, userData } = this.state;
    return (
      <View style={styles.container}>
        <View
          showsVerticalScrollIndicator = {false}
        >
          <ImageBackground
            source = {require('@/assets/images/home_bg.png')}
            style = {styles.bg}
          >
            <View style = {styles.bgArea}> 
              <View style = {styles.detail}>
                <View style = {{flexDirection: 'row', alignItems: 'center'}}>
                  <Image
                    source = {require('@/assets/images/profile_2.jpg')}
                    style = {styles.avatar}
                  />
                  <View>  
                    <Text style = {styles.bgText}>{date}</Text>
                    <Text style = {styles.bgBoldText}>{'DANG BIN'}</Text>
                  </View>
                </View>
                <View>
                    <Text style = {styles.bgSubText}>Xe đầu kéo</Text>
                    <TouchableOpacity 
                      style = {styles.detail_2}
                      onPress = {this.onChangeCarCode}
                    >
                      <Text style = {styles.bgBoldText}>{carCode}</Text>
                      <Image
                        source = {down_arrow}
                        style = {styles.bgIcon}
                        resizeMode = 'contain'
                      />
                    </TouchableOpacity>
                    <Text style = {[styles.bgSubText, {marginTop: sizeHeight(1)}]}>Xe Rơ-mooc</Text>
                    <TouchableOpacity 
                      style = {styles.detail_2}
                      onPress = {this.onChangeContCode}
                    >
                      <Text style = {styles.bgBoldText}>{ContCode}</Text>
                      <Image
                        source = {down_arrow}
                        style = {styles.bgIcon}
                        resizeMode = 'contain'
                      />
                    </TouchableOpacity>
                </View>
              </View>
            </View>
          </ImageBackground>

            <ScrollView 
              style = {styles.listArea}
              showsVerticalScrollIndicator = {false}
            >
                <View style = {styles.listContainer}>
                    <Text style = {styles.listTitle}>Danh sách</Text>
                    {Data.map((item, index) => (
                      <Item 
                        data = {item} 
                        key = {index}
                        onPress = {() => this.props.navigation.navigate(mainStack.detail, {item: item.batchCode})}
                      />
                    ))}
                </View>   
            </ScrollView>

          <View style = {styles.buttonArea}>
            <View style = {[styles.buttonContainer,{borderBottomWidth: 1}]}>
              <HomeButton 
                title = 'Bốc công hàng từ Cảng'
                icon = {cont2_icon}
                active = {this.state.boc_focused}
                onPress = {this.onLoadProduct} 
              />
              <HomeButton 
                icon = {cont3_icon}
                title = 'Hạ công hàng từ Cảng'
                active = {this.state.ha_focused}
                onPress = {this.onUnLoadProduct}
              />
            </View>
            <View style = {styles.buttonContainer}>
              <HomeButton 
                title = 'Bốc công rỗng từ Cảng'
                icon = {cont4_icon}
                active = {this.state.boc_rong_focused}
                onPress = {this.onLoadEmptyProduct}
              />
              <HomeButton 
                title = 'Hạ công rỗng từ Cảng'
                icon = {cont5_icon}
                active = {this.state.ha_rong_focused}
                onPress = {this.onUnLoadEmptyProduct}
              />
            </View>
            <View style = {styles.spacing}></View>
          </View>
        </View>
      </View>

    )
  }
}
const styles = StyleSheet.create({
  container: {
    height: sizeHeight(100),
    width: sizeWidth(100),
    backgroundColor: Colors.white
  },
  title: {
    fontSize: sizes.h3,
    paddingLeft: sizeWidth(2),
    color: Colors.blueColor
  },
  bg: {
    height: sizeHeight(30),
    width: sizeWidth(100),
  },
  bgArea: {
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    height: sizeHeight(30),
    width: sizeWidth(100),
  },
  listArea: {
    height: sizeHeight(80),
    width: sizeWidth(100),
    backgroundColor: '#FFFFFF',
  },
  buttonArea: {
    position: 'absolute',
    height: sizeHeight(25),
    width: sizeWidth(92),
    backgroundColor: '#FFFFFF',
    top: sizeHeight(17.5),
    bottom: sizeHeight(57.5),
    left: sizeWidth(4),
    shadowColor: "#000",
    shadowOffset: {
        width: 0,
        height: 1,
    },
    shadowOpacity: 0.22,
    shadowRadius: 2.22,
    elevation: 6,
    borderRadius: 10,
    padding: sizeHeight(1),
  },
  detail: {
    flexDirection: 'row',
    marginVertical: sizeHeight(5),
    marginHorizontal: sizeWidth(5),
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  bgIcon: {
    height: sizeHeight(2),
    width: sizeWidth(3),
    marginLeft: sizeWidth(2),
  },
  bgText: {
    fontSize: 12,
    lineHeight: 14,
    color: '#FFFFFF',
  },
  bgBoldText: {
    fontSize: 16,
    lineHeight: 19,
    color: '#FFFFFF',
    fontWeight: 'bold',
  },
  detail_2: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  listContainer: {
    width: sizeWidth(100),
    marginTop: sizeHeight(15),
    marginBottom: sizeHeight(20),
    paddingHorizontal: sizeWidth(8),
    paddingVertical: sizeHeight(1),
  },
  listTitle: {
    fontSize: 17,
    lineHeight: 21,
    color: '#000000',
    fontWeight: 'bold',
  },
  buttonContainer: {
    flexDirection: 'row',
    borderColor: '#EFF1F5',
  },
  spacing: {
    height: '100%',
    width: 1.5,
    backgroundColor: '#EFF1F5',
    position: 'absolute',
    alignSelf: 'center',
  },
  avatar: {
    height: sizeHeight(6),
    width: sizeHeight(6),
    marginRight: sizeHeight(2),
    borderRadius: 25,
  },
  bgSubText: {
    color: 'rgba(255, 255, 255, 0.7)',
    fontSize: 11,
    lineHeight: 13,
  }
})