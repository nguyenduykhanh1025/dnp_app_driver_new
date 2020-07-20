import React, { Component } from 'react'
import { Text, View, StyleSheet, ScrollView } from 'react-native';
import NavigationService from '@/utils/navigation';
import { mainStack } from '@/config/navigator';
import { Colors, sizes, sizeWidth, sizeHeight } from '@/commons'
import { getListNotification, SearchQRCode } from '@/mock/index';
import Item from './item';
import { HeaderMain } from '@/components'
import { ModalQRResult } from '@/components/modal'
import { Right } from 'native-base';

export default class ListScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      visible: false,
    }
  };

  componentDidMount = async () => {
    var { data } = this.state;
    this.setState({
      data: getListNotification[0].Data
    })
  };

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

  render() {
    var { data } = this.state;
    return (
      <View>
      <HeaderMain
        backgroundColor={Colors.blue}
        title='Notification'
      />
      <ScrollView 
        style={styles.container}
        showsVerticalScrollIndicator = {false}
      >
        <View style = {styles.list}>
          {data.map((item, index) => (
            <View>
              <View style={styles.itemView}>
                <Item
                  data={item}
                  index={index}
                  onPress={() => {
                    NavigationService.navigate(mainStack.result, { item: item })
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