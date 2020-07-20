import React, { Component } from 'react';
import { Text, View, Modal, TouchableOpacity, Image, StyleSheet, Dimensions, TouchableWithoutFeedback,  } from 'react-native';
import Toast from 'react-native-tiny-toast';
import { Colors, sizeWidth, sizeHeight, sizes } from '@/commons';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

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
      data: this.state.data[0].Data
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

  render() {
    var { data } = this.state;
    console.log(data)
    return (
      <Modal
        animationType="fade"
        transparent={true}
        visible={this.state.visible}
        onRequestClose={() => {
          Toast.show(
            "Vui lòng điền, chọn và thoát ra đúng cách.!"
          );
        }}
      >
        <TouchableWithoutFeedback
          onPress={this.onModalClose}
        >
          <View style={styles.wrapper}>
            <View style={styles.container}>
              <TouchableWithoutFeedback onPress={this.onModalClose}>
                <View>
                  <View style={styles.close}>
                    <Image source={ic_close} style={styles.ic_close} />
                  </View>
                  <View style={styles.contentView}>
                    <Text style={styles.text}>Cont: {data[0].contCode}</Text>
                    <Text style={styles.text}>Size: {data[0].contSize}</Text>
                    <Text style={styles.text}>Type: {data[0].contType}</Text>
                    <Text style={styles.text}>Yard Position: {data[0].yardPosition}</Text>
                    <Text style={styles.text}>Gate Pass: {data[0].gatePass}</Text>
                    <Text style={styles.text}>Notes: {data[0].notes}</Text>
                  </View>
                </View>
              </TouchableWithoutFeedback>
            </View>
          </View>
        </TouchableWithoutFeedback>
      </Modal>
    )
  }
}

const styles = StyleSheet.create({
  wrapper: {
    height: sizeHeight(100),
    width: sizeWidth(100),
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)'
  },
  container: {
    backgroundColor: '#fff',
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
