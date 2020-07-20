import React, { Component } from 'react';
import { StyleSheet, Text, View, TouchableOpacity, Animated, ScrollView, Dimensions} from 'react-native';
import ScrollableTabView from 'react-native-scrollable-tab-view';
import TabBar from 'react-native-underline-tabbar';
import { Colors, commonStyles} from '@/commons';
import { SearchPopup, HeaderSearch, HeaderList, DanhSach, Icon } from '@/components';


const Page = ({data, _renderItems, iconName1 , icon1, icon2, iconName2, title, isKhac, input, addDetail, ExportDB}) => (
  <View style={{padding:5}}>
      <View style={{flexDirection:'row'}}>
        <View style={{flex: 3,justifyContent:'center', paddingLeft:20}}>
          <Text style={{ fontWeight: 'bold', fontSize:15 }}>{title}</Text>
        </View>
        <TouchableOpacity style={{margin:10, flex:1, alignItems:'flex-end'}} onPress={ExportDB}>
          <Icon name={icon1} color={Colors.mainColor} size={22} style={{marginRight:35}}/>
          <Text style={{color:Colors.mainColor, fontSize:15, position:'absolute'}}>{iconName1}</Text>
        </TouchableOpacity>
        <TouchableOpacity style={{margin:10, flex:1, alignItems:'flex-end'}} onPress={addDetail}>
          <Icon name={icon2} color={Colors.mainColor} size={22} style={{marginRight:45}}/>
          <Text style={{color:Colors.mainColor, fontSize:15, position:'absolute'}}>{iconName2}</Text>
        </TouchableOpacity>
      </View>
      <DanhSach 
        data={data}
        _renderItems={_renderItems}/>
        {input}
  </View>
);

const Tab = ({ tab, page, isTabActive, onPressHandler, onTabLayout, styles }) => {
  const { label } = tab;
  const style = {
    marginHorizontal: 50,
    paddingVertical: 10,
  };
  const containerStyle = {
    paddingHorizontal: 30,
    paddingVertical: 8,
    borderRadius: 20,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: styles.backgroundColor,
    opacity: styles.opacity,
    transform: [{ scale: styles.opacity }],
  };
  const textStyle = {
    color: styles.textColor,
    fontWeight: '600',
  };
  return (
    <TouchableOpacity style={style} onPress={onPressHandler} onLayout={onTabLayout} key={page}>
      <Animated.View style={containerStyle}>
        <Animated.Text style={textStyle}>{label}</Animated.Text>
      </Animated.View>
    </TouchableOpacity>
  );
};

class Index extends Component {
  state ={ nguoi_duyet:'', tt_duyet:'', ma_kd:'' }

  _scrollX = new Animated.Value(0);
  interpolators = Array.from({ length: 6 }, (_, i) => i).map(idx => ({
    scale: this._scrollX.interpolate({
      inputRange: [idx - 1, idx, idx + 1],
      outputRange: [1, 1.2, 1],
      extrapolate: 'clamp',
    }),
    opacity: this._scrollX.interpolate({
      inputRange: [idx - 1, idx, idx + 1],
      outputRange: [0.9, 1, 0.9],
      extrapolate: 'clamp',
    }),
    textColor: this._scrollX.interpolate({
      inputRange: [idx - 1, idx, idx + 1],
      outputRange: ['#000', '#fff', '#000'],
    }),
    backgroundColor: this._scrollX.interpolate({
      inputRange: [idx - 1, idx, idx + 1],
      outputRange: ['rgba(0,0,0,0.1)', 'rgba(15, 181, 255, 1)', 'rgba(0,0,0,0.1)'],
      extrapolate: 'clamp',
    }),
  }));

  render() {
    const { data, _renderItems, icon, icon1, icon2, iconName1, iconName2, title, titleKhac, input, addDetail, ExportDB } = this.props
    const lengths = Object.keys(data[0]).length;


    return (
      <View style={[styles.container, { paddingTop: -20 , height: (data.length + 1) * (lengths > 3 ? lengths * 25 : lengths * 35) }]}>
       {/* <View style={[styles.container, { paddingTop: -20 , height: 500 }]}> */}
          <ScrollableTabView
              renderTabBar={() => (
                <TabBar
                  underlineColor='rgba(15, 181, 255, 1)'
                  tabBarStyle={{ backgroundColor: "#f5f5f5", borderTopColor: '#d2d2d2', borderTopWidth: 1 }}
                  renderTab={(tab, page, isTabActive, onPressHandler, onTabLayout) => (
                    <Tab
                      key={page}
                      tab={tab}
                      page={page}
                      isTabActive={isTabActive}
                      onPressHandler={onPressHandler}
                      onTabLayout={onTabLayout}
                      styles={this.interpolators[page]}
                    />
                  )}
                />
              )}
              onScroll={(x) => this._scrollX.setValue(x)}
            >
              <Page 
                tabLabel={{label: "Chi tiết"}} 
                data={data} _renderItems={_renderItems} 
                icon1={icon1} 
                icon2={icon2} 
                iconName1={iconName1} 
                iconName2={iconName2} 
                title={title} 
                addDetail={addDetail}
                ExportDB={ExportDB}
              />
              <Page tabLabel={{label: "Khác"}}  data={''} _renderItems={''} icon1={''} iconName={''} title={titleKhac} input={input}/>
            </ScrollableTabView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex:1,
    backgroundColor: '#fff',
  },
});

export default Index;