import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";
import { commonStyles, Colors, Fonts } from '@/commons'
export default class TextInline extends Component {
  render() {
    const { title, data, money } = this.props
    return (
      <View style={commonStyles.mt8}>
        <Text style={styles.title}>{title}</Text>
        <View>
          {
            money ?
              <Text style={styles.dataFeild}>{data.toFixed().replace(/\d(?=(\d{3})+(?!\d))/g, '$&,')}</Text> :
              <Text style={styles.dataFeild}>{data}</Text>
          }
        </View>
      </View>

    );
  }
}

const styles = StyleSheet.create({
  dataFeild: {
    fontSize: 15,
    color: Colors.textColor,
    fontWeight: 'normal',
    top: 8,
    fontFamily: Fonts.SairaSemiCondensedRegular
  },
  title: {
    fontSize: 14,
    fontWeight: 'normal',
    color: Colors.titleColor,
    fontFamily: Fonts.SairaSemiCondensedRegular
  }
});