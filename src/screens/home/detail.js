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
} from '@/commons';
import { getDetailContItem } from '@/mock';
import {HeaderMain} from '@/components/header';
import {left_arrow, righticon} from '@/assets/icons';
import ListItem from './detail-item';
import navigation from '@/utils/navigation';
import { SCANNER_QR } from '@/modules/home/constants';

const next = require('@/assets/icons/icon_next.png')
//NavigationService.navigate(mainStack.qr_code, { item: item })
//getDetailContItem[0].Data[0].Master.size
//getDetailContItem[0].Data[0].Master.contNumber

export default class DetailScreen extends Component {

  renderLeft =  () => {
    return (
        <Image
          source = {left_arrow}
          style = {styles.backArrow}
          resizeMode = 'contain'
        />
    )
  }

  render() {
    let title = this.props.navigation.getParam('item')
    let master = getDetailContItem[0].Data[0].Master;
    let detail = getDetailContItem[0].Data[0].Detail;
    return (
      <View style={commonStyles.containerClass}>
        <HeaderMain
          title = {`Mã lô: ${title}`}
          backgroundColor = {Colors.blue}
          left = {this.renderLeft()}
          onPress= {() => this.props.navigation.goBack()}
        />
        <ScrollView 
          style = {styles.scroll}
          showsVerticalScrollIndicator = {false}
        >
          <TouchableOpacity 
            style = {styles.commonCont}
            onPress = {() => this.props.navigation.navigate(mainStack.qr_code, { item: master })}
          >
            <View style = {styles.quantityContainer}>
              <Text style = {styles.quantityText}>Số lượng</Text>
              <Text style = {styles.quantityNumber}>{master.contCount}</Text>
            </View>
            <View style = {styles.commonDetailContainer}>
              <View style = {styles.commonContRow}>
                <Text style = {styles.commonContTitle}>Size:</Text>
                <Text style = { styles.commonCOntText}>{master.size}</Text>
              </View>
              <View style = {styles.commonContRow}>
                <Text style = {styles.commonContTitle}>Type:</Text>
                  <Text style = { styles.commonCOntText}>{master.type}</Text>
              </View>
            </View>
            <Image
              source = {righticon}
              style = {styles.commonConIcon}
              resizeMode = 'contain'
            />
          </TouchableOpacity>
          <View style = {styles.listArea}>
            <Text style = {styles.listTitle}>Cont chỉ định</Text>
            <View style = {styles.list}>
              {detail.map((item, index) => (
                <ListItem
                data = {item}
                onPress = {() => this.props.navigation.navigate(mainStack.qr_code, { item: item })}
                />
              ))}
            </View>
          </View>
        </ScrollView>
      </View >
    )
  }
}
const styles = StyleSheet.create({
  containerClass: {
    flex: 1,
  },
  commonCont: {
    backgroundColor: '#FFFFFF',
    height: sizeHeight(14),
    width: sizeWidth(94),
    shadowColor: "#000",
    shadowOffset: {
        width: 0,
        height: 1,
    },
    shadowOpacity: 0.22,
    shadowRadius: 2.22,
    elevation: 6,
    marginHorizontal: sizeWidth(3),
    marginVertical: sizeHeight(2),
    borderRadius : 10,
    flexDirection: 'row',
    alignItems: 'center',
    padding: sizeHeight(1),
  },
  quantityContainer: {
    height: sizeHeight(12),
    width: sizeHeight(15),
    borderRadius: 10,
    backgroundColor: '#F3B03F',
    justifyContent: 'center',
    alignItems: 'center',
  },
  quantityText: {
    fontSize: 13,
    lineHeight: 15,
    color: '#FFFFFF',
  },
  quantityNumber: {
    fontSize: 30,
    lineHeight: 35,
    color: '#FFFFFF',
    fontWeight: 'bold',
  },
  backArrow: {
    height: sizeHeight(3),
    width: sizeWidth(5),
  },
  commonDetailContainer: {
    marginLeft: sizeWidth(10),
    justifyContent: 'center',
    alignItems: 'center',
    width: sizeWidth(45),
    marginRight: sizeWidth(6),
  },
  commonContRow: {
    flexDirection: 'row',
    width: '70%',
    alignItems: 'center',
  },
  commonContTitle: {
    fontSize: 20,
    lineHeight: 23,
    color: '#86889E',
    fontWeight: 'bold',
    marginVertical: sizeHeight(1),
    marginRight: sizeWidth(5),
    width: sizeWidth(13)
  },
  commonCOntText: {
    fontSize: 22,
    lineHeight: 26,
    color: Colors.black,
    fontWeight: 'bold'
  },
  commonConIcon: {
    height: sizeHeight(4),
    width: sizeWidth(5),
    position: 'absolute',
    right: '2%',
  },
  listArea: {
    paddingBottom: sizeHeight(3)
  },
  listTitle: {
    fontSize: 17,
    lineHeight: 21,
    color: Colors.black,
    fontWeight: 'bold',
    marginTop: sizeHeight(1),
    marginLeft: sizeWidth(2),
    marginBottom: sizeHeight(2),
  },
  list: {
    marginHorizontal: sizeWidth(3),
    marginBottom: sizeHeight(1),
  },
  scroll: { 
    height: sizeHeight(87),
  }
})