import React, { Component } from 'react';
import { Text, View, Modal, TouchableOpacity, Image, StyleSheet, Dimensions, TouchableWithoutFeedback, StatusBar, FlatList, ScrollView } from 'react-native';
import Toast from 'react-native-tiny-toast';
import {
  Colors, sizeWidth, sizeHeight, sizes,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import { HeaderMain, Button } from '@/components';
const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;
import Icon from 'react-native-vector-icons/AntDesign';

const ic_close = require('@/assets/icons/ic_back_menu.png')

export default class QRresult extends Component {
  constructor(props) {
    super(props);
    this.state = {
      visible: this.props.visible,
      data: this.props.data,
    }
  }

  onModalClose = () => {
    this.setState({
      visible: false
    })
  }

  componentDidMount() {
    this.setState({
      data: this.state.data.Data
    })
  }



  componentWillReceiveProps = async (nextProps) => {
    if (nextProps.visible != this.state.visible) {
      await this.setState({ visible: nextProps.visible })
    }
    if (nextProps.msg != this.state.msg) {
      await this.setState({ msg: nextProps.msg })
    }
  }
  renderItems = (item) => {
    return (
      <View style={{
        backgroundColor: '#E2EEFB',
        borderRadius: hs(10),
        marginVertical: hs(5)
      }}>
        <View style={{ backgroundColor: Colors.blue, borderRadius: hs(10), padding: hs(15) }}>
          <Text style={{ color: Colors.white, fontSize: fs(16), marginVertical: hs(3) }}>{item.item.name}</Text>
          <Text style={{ color: Colors.grey4, fontSize: fs(16), marginVertical: hs(3) }}>Số container:  <Text style={{ color: Colors.white }}>{item.item.containerNo}</Text></Text>
        </View>
        <Text style={{ color: Colors.grey6, fontSize: fs(18), margin: hs(15) }}>Tọa độ:    <Text style={{ color: Colors.grey7, fontSize: fs(20) }}>{item.item.coordinates}</Text></Text>
      </View>
    )
  }
  render() {
    var { data } = this.state;
    var item = this.props.data;
    return (
      <Modal
        animationType="fade"
        transparent={true}
        visible={this.state.visible}
        onRequestClose={() => {
          this.props.onClose()
        }}
      >
        <ScrollView style={styles.wrapper}>
          <View style={{ paddingTop: hs(20), padding: hs(10) }}>
            <TouchableOpacity style={{ width: ws(50), justifyContent: 'center', alignItems: 'center' }} onPress={() => this.props.onClose()}>
              <Icon
                name={'arrowleft'}
                size={fs(25)}
              />
            </TouchableOpacity>
          </View>
          <View style={{ alignItems: 'center', padding: hs(15), paddingTop: 0 }}>
            <View style={{ justifyContent: 'center', alignItems: 'center', width: hs(50), height: hs(50), backgroundColor: item.code == 1 ? '#CDFFE0' : '#F39292', borderRadius: hs(25), borderWidth: item.code == 1 ? 0 : 1, borderColor: 'red' }}>
              {item.code == 1 ? <Icon name='check' size={fs(40)} color='#15F21D' /> :
                <Icon name='close' size={fs(40)} color='#fff' />}
            </View>
            <Text style={{ textAlign: 'center', color: item.code == 1 ? Colors.blue : "red", fontWeight: 'bold', fontSize: fs(20) }}>{item.code == 1 ? 'Check in thành công' : ' Check in thất bại'}</Text>
            {item.code == 1 ? <Text style={{ fontSize: fs(20), color: Colors.blue }}>XIN MỜI XE VÀO CỔNG</Text> : null}
          </View>
          {item.code == 1 ?
            <View style={{ padding: ws(10), paddingTop: 0 }}>
              <Text style={{ color: Colors.grey6, fontSize: fs(18)}}>Thông tin tọa độ</Text>
              <FlatList
                renderItem={(item) => this.renderItems(item)}
                data={this.state.data}
              />
            </View>
            :
            <View style={{ padding: ws(10), paddingTop: 0 }}>
            <Text style={{ color: Colors.grey6, fontSize: fs(18) }}>Thông báo</Text>
            <Text style={{ color: 'red', fontSize: fs(20)}}>{item.msg}</Text>

            </View>
          }
        </ScrollView>
          <View style={{ position: 'absolute', bottom: hs(30) }}>
            <Button value="OK" 
              onPress={() => this.props.onClose()}
            />
          </View>

      </Modal>
    )
  }
}

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    backgroundColor: '#fff',
  },
  container: {
    backgroundColor: 'yellow',
    width: sizeWidth(70),
    height: sizeHeight(50),
    borderRadius: 7,

  },
  contentView: {
    paddingLeft: sizeWidth(5)
  },
  ic_close: {
    width: windowWidth * 0.03,
    height: windowWidth * 0.03,
  },
  close: {
    alignItems: 'flex-end',
    padding: windowWidth * 0.03,
  },
  text: {
    paddingVertical: sizeWidth(1)
  }
})
