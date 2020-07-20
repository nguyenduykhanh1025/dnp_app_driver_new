import React, { Component } from 'react'
import { Text, View, TextInput } from 'react-native'
const popup = require('@/assets/icons/lookupIcon.png')
const down = require('@/assets/icons/down-arrow.png')
import { go_popup_view } from '@/modules/purchase/domesticorders/action';
import { Colors, commonStyles } from '@/commons';
import { Input, date, InputDateTime, InputAnimation,  } from '@/components';

export default class Chitietphieuncvt extends Component {
    state = { ten_sp, kho_hang, quy_cach, dvt, ty_gia, trang_thai, gia_tk, so_luong, gia, tien, so_luong_duyet, so_luong_dat, ngay_yc, ghi_chu, dia_chi, nha_cc, vu_viec, thiet_bi}
    go_popup_view = (params) => {
        const navigation = this.props.navigation;
        navigation.dispatch(go_popup_view(params))
    }
    render() {
        const { ten_sp, kho_hang, quy_cach, dvt, ty_gia, trang_thai, gia_tk, so_luong, gia, tien, so_luong_duyet, so_luong_dat, ngay_yc, ghi_chu, dia_chi, nha_cc, vu_viec, thiet_bi} = this.state
        return (
            <View style={commonStyles.component}>
                <InputAnimation 
                    placeholder='Tên sản phẩm' 
                    onChangeText={(text)=> this.setState({ten_sp:text})} 
                    value={ten_sp} 
                    img={popup}
                    onFocus={() => this.go_popup_view({title:'Danh sách sản phẩm'})}
                    onPress={() => this.go_popup_view({title:'Danh sách sản phẩm'})}
                    />      
                <InputAnimation 
                    placeholder='Quy cách' 
                    onChangeText={(text)=> this.setState({quy_cach:text})} 
                    value={quy_cach} />            
                <InputAnimation 
                    placeholder='Kho hàng' 
                    onChangeText={(text)=> this.setState({kho_hang:text})} 
                    value={kho_hang} 
                    img={popup} 
                    onFocus={() => this.go_popup_view({title:'Danh sách kho hàng'})}
                    onPress={() => this.go_popup_view({title:'Danh sách kho hàng'})}
                    /> 
                <InputAnimation 
                    placeholder='Giá tham khảo' 
                    onChangeText={(text)=> this.setState({gia_tk:text})} 
                    value={gia_tk} /> 
                <InputAnimation 
                    placeholder='Giá' 
                    onChangeText={(text)=> this.setState({gia:text})} 
                    value={gia} /> 
                <InputAnimation 
                    placeholder='Tiền' 
                    onChangeText={(text)=> this.setState({tien:text})} 
                    value={tien} /> 
                <View style={commonStyles.row}>
                    <View style={commonStyles.formGroupL}>
                        <InputAnimation 
                            placeholder='Số lượng' 
                            onChangeText={(text)=> this.setState({so_luong:text})} 
                            value={so_luong} />                
                    </View>
                    <View style={commonStyles.formGroupR}>
                        <InputAnimation 
                            placeholder='Đơn vị tính' 
                            onChangeText={(text)=> this.setState({dvt:text})} 
                            value={dvt} />                
                    </View>
                </View>   
                <View style={commonStyles.row}>
                    <View style={commonStyles.formGroupL}>
                        <InputAnimation 
                            placeholder='Số lượng duyệt' 
                            onChangeText={(text)=> this.setState({so_luong:text})} 
                            value={so_luong_duyet} />                
                    </View>
                    <View style={commonStyles.formGroupR}>
                        <InputAnimation 
                            placeholder='Số lượng đặt' 
                            onChangeText={(text)=> this.setState({so_luong_dat:text})} 
                            value={so_luong_dat} />                
                    </View>
                </View>   
                <InputAnimation 
                    placeholder='Trạng thái' 
                    onChangeText={(text)=> this.setState({trang_thai:text})} 
                    value={trang_thai} img={down}/>
                <InputDateTime 
                    placeholder='Ngày yêu cầu' 
                    onDateChange={(text)=> this.setState({ngay_yc:text})} 
                    value={ngay_yc}
                    format="DD/MM/YYYY"
                    width='100%' />                 
                <InputAnimation 
                    placeholder='Ghi chú' 
                    onChangeText={(text)=> this.setState({ghi_chu:text})} 
                    value={ghi_chu} />          
                <InputAnimation 
                    placeholder='Địa chỉ' 
                    onChangeText={(text)=> this.setState({dia_chi:text})} 
                    value={dia_chi} 
                    img={popup}
                    onFocus={() => this.go_popup_view({title:'Danh sách địa chỉ'})}
                    onPress={() => this.go_popup_view({title:'Danh sách địa chỉ'})}
                    />          
                <InputAnimation 
                    placeholder='Mã nhà cc' 
                    onChangeText={(text)=> this.setState({nha_cc:text})} 
                    value={nha_cc} 
                    img={popup}
                    onFocus={() => this.go_popup_view({title:'Danh sách nhà cung cấp'})}
                    onPress={() => this.go_popup_view({title:'Danh sách nhà cung cấp'})}
                    />          
                <InputAnimation 
                    placeholder='Vụ việc' 
                    onChangeText={(text)=> this.setState({vu_viec:text})} 
                    value={vu_viec} 
                    img={popup} 
                    onFocus={() => this.go_popup_view({title:'Danh sách vụ việc'})}
                    onPress={() => this.go_popup_view({title:'Danh sách vụ việc'})}
                    />          
                <InputAnimation 
                    placeholder='Thiết bị' 
                    onChangeText={(text)=> this.setState({thiet_bi:text})} 
                    value={thiet_bi} 
                    img={popup}
                    onFocus={() => this.go_popup_view({title:'Danh sách thiết bị'})}
                    onPress={() => this.go_popup_view({title:'Danh sách thiết bị'})}
                    />          
            </View>
        )
    }
}
