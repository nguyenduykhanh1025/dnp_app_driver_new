import React, { Component } from 'react'
import { Text, View } from 'react-native'
import { Colors, commonStyles} from '@/commons';
import { InputAnimation } from '@/components';
const popup = require('@/assets/icons/lookupIcon.png')
const down = require('@/assets/icons/down-arrow.png')

export default class Noidungsearch extends Component {
    state = { nguoi_yc:'', ma_kh:'', bo_phan:'', ma_gd:'', muc_do:'', ngay_lap:'', ty_gia:'',trang_thai:'1.Chờ duyệt'}
    render() {
        const { nguoi_yc, ma_kh, bo_phan, ma_gd, muc_do, ngay_lap, ty_gia, trang_thai } = this.state
        return (
            <View style={{padding:10}}>
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
                <InputAnimation placeholder='test' value={nguoi_yc} onChangeText={(text) => this.setState({ nguoi_yc: text })} img={popup} />
            </View>
        )
    }
}
