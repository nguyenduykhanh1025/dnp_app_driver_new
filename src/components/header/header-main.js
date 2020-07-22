import React, { Component } from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  Image,
  StatusBar,
} from 'react-native';
import {
  sizes,
  sizeWidth,
  sizeHeight,
  Colors,
  widthPercentageToDP as ws,
  heightPercentageToDP as hs,
  fontSizeValue as fs,
} from '@/commons';
import StepIndicator from 'react-native-step-indicator';

const hbg = require('@/assets/images/home_bg.png');

export default class HeaderMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      labels: ["Chọn xe", "Chọn lệnh", "Hoàn thành"],
    }
  }

  render() {
    var {
      renderLeft,
      renderRight,
      renderCenter,
      status,
      onPressLeft,
      onPressCenter,
      onPressRight,
    } = this.props;
    return (
      <View style={styles.Container}>
        <StatusBar
          translucent
          barStyle={'light-content'}
          backgroundColor='transparent'
        />
        <Image source={hbg} style={styles.ContainerBg} />
        <View style={[styles.ContainerBg]} />
        <View style={styles.HeaderButton}>
          <TouchableOpacity onPress={onPressLeft ? onPressLeft : null}>
            <View style={styles.HeaderButtonLeft}>
              {renderLeft ? renderLeft : null}
            </View>
          </TouchableOpacity>
          <TouchableOpacity onPress={onPressCenter ? onPressCenter : null}>
            <View style={styles.HeaderButtonCenter}>
              {renderCenter ? renderCenter : null}
            </View>
          </TouchableOpacity>
          <TouchableOpacity onPress={onPressRight ? onPressRight : null}>
            <View style={styles.HeaderButtonRight}>
              {renderRight ? renderRight : null}
            </View>
          </TouchableOpacity>
        </View>
        <View style={styles.HeaderStatus}>
          <StepIndicator
            customStyles={{
              stepIndicatorSize: ws(30),
              currentStepIndicatorSize: ws(30),
              separatorStrokeWidth: 2,
              currentStepStrokeWidth: 0,
              stepStrokeCurrentColor: Colors.subColor,
              stepStrokeWidth: 1,
              stepStrokeFinishedColor: Colors.subColor,
              stepStrokeUnFinishedColor: Colors.grey5,
              separatorFinishedColor: Colors.white,
              separatorUnFinishedColor: 'rgba(255, 255, 255, 0.1)',
              stepIndicatorFinishedColor: Colors.subColor,
              stepIndicatorUnFinishedColor: '#364F8D',
              stepIndicatorCurrentColor: Colors.subColor,
              stepIndicatorLabelFontSize: fs(15),
              currentStepIndicatorLabelFontSize: fs(15),
              stepIndicatorLabelCurrentColor: Colors.white,
              stepIndicatorLabelFinishedColor: Colors.white,
              stepIndicatorLabelUnFinishedColor: Colors.grey4,
              labelColor: Colors.grey5,
              labelSize: fs(10),
              currentStepLabelColor: Colors.white,
            }}
            currentPosition={status}
            labels={this.state.labels}
            stepCount={3}
          />
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  Container: {
    width: ws(375),
    height: hs(183),
    flexDirection: 'column',
  },
  ContainerBg: {
    width: ws(375),
    height: hs(183),
    borderBottomLeftRadius: 30,
    borderBottomRightRadius: 30,
    backgroundColor: "rgba(14, 39, 114, 0.8)",
    position: 'absolute',
  },
  HeaderButton: {
    width: ws(375),
    height: hs(30),
    marginTop: hs(63),
    flexDirection: 'row',
    justifyContent: 'center',
  },
  HeaderStatus: {
    marginTop: hs(18)
  },
  HeaderButtonLeft: {
    width: ws(93.75)
  },
  HeaderButtonCenter: {
    width: ws(187.5)
  },
  HeaderButtonRight: {
    width: ws(93.75)
  }
})