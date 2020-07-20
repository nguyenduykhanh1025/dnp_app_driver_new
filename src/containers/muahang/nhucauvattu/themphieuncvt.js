import React, { Component } from 'react'
import { Text, View, TextInput } from 'react-native';
import { Colors, commonStyles} from '@/commons';
import { InputAnimation, TextItem, InputDateTime } from '@/components';
import { go_popup_view } from '@/modules/purchase/domesticorders/action';
const popup = require('@/assets/icons/lookupIcon.png')
const down = require('@/assets/icons/down-arrow.png')

export default class Themphieuncvt extends Component {
    state = { nguoi_yc:'', ma_kh:'', bo_phan:'', ma_gd:'', muc_do:'', ngay_lap:date, ty_gia:'',trang_thai:'1.Chờ duyệt'}
    go_popup_view = (params) => {
        const navigation = this.props.navigation;
        navigation.dispatch(go_popup_view(params))
    }
    render() {
        const { nguoi_yc, ma_kh, bo_phan, ma_gd, muc_do, ngay_lap, ty_gia, trang_thai } = this.state
        const { editable, navigation } = this.props
        return (
            <View style={commonStyles.component}>
                <InputAnimation 
                    editable={editable}
                    placeholder='Người yêu cầu' 
                    onChangeText={(text)=> this.setState({nguoi_yc:text})} 
                    value={nguoi_yc} />      

                <InputAnimation 
                    editable={editable}
                    placeholder='Bộ phận' 
                    onChangeText={(text)=> this.setState({bo_phan:text})} 
                    value={bo_phan} 
                    img={popup} 
                    onFocus={() => this.go_popup_view({title:'Danh sách bộ phận'})}
                    onPress={() => this.go_popup_view({title:'Danh sách bộ phận'})}
                    />            
                <InputAnimation 
                    editable={editable}
                    placeholder='Mã khách' 
                    onChangeText={(text)=> this.setState({ma_kh:text})} 
                    value={ma_kh} 
                    img={popup}
                    onFocus={() => this.go_popup_view({title:'Danh sách mã khách'})}
                    onPress={() => this.go_popup_view({title:'Danh sách mã khách'})} /> 

                <View style={commonStyles.row}>
                    <View style={commonStyles.formGroupL}>
                        <InputAnimation 
                            editable={editable}
                            placeholder='Mã giao dịch' 
                            onChangeText={(text)=> this.setState({ma_gd:text})} 
                            value={ma_gd} />                
                    </View>
                    <View style={commonStyles.formGroupR}>
                        <InputAnimation 
                            editable={editable}
                            placeholder='Mức độ' 
                            onChangeText={(text)=> this.setState({muc_do:text})} 
                            value={muc_do} 
                            img={popup}
                            onFocus={() => this.go_popup_view({title:'Danh sách mức độ'})}
                            onPress={() => this.go_popup_view({title:'Danh sách mức độ'})} />                
                    </View>
                </View>   
                <View style={commonStyles.row}>
                    <View style={[commonStyles.formGroupL,{top:4}]}>
                        <InputDateTime 
                            placeholder='Ngày lập' 
                            onDateChange={(text)=> this.setState({ngay_lap:text})} 
                            value={ngay_lap}
                            format="DD/MM/YYYY" />                
                    </View>
                    <View style={commonStyles.formGroupR}>
                        <InputAnimation 
                            editable={editable}
                            placeholder='Tỷ giá' 
                            onChangeText={(text)=> this.setState({ty_gia:text})} 
                            value={ty_gia} 
                            img={popup} 
                            onFocus={() => this.go_popup_view({title:'Danh sách tỷ giá'})}
                            onPress={() => this.go_popup_view({title:'Danh sách tỷ giá'})}
                            />                
                    </View>
                </View>   
                <InputAnimation  
                    editable={editable}
                    placeholder='Chờ duyệt' 
                    onChangeText={(text)=> this.setState({trang_thai:text})} 
                    value={trang_thai} 
                    img={down} />                
            </View>
        )
    }
}
