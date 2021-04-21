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
    down_arrow,
} from '@/assets/icons'
import {
    Container,
    Header,
    Content,
    Icon,
    Picker,
    Form
} from "native-base";
import {
    Colors,
    widthPercentageToDP as ws,
    heightPercentageToDP as hs,
    fontSizeValue as fs,
} from '@/commons';


export default class DropDown extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selected: ''
        };
    }
    onValueChange = async (value) => {
        await this.setState({
            selected: value
        });
        await this.props.onSelect(value)
    }

    render() {
        var {
            title,
            style,
            TitleViewStyle,
            TitleTextStyle,
            PickerStyle,
            onSelect,
            itemTextStyle,
            data,
            line,
            mode,
            textStyle
        } = this.props;
        return (
            <View style={[styles.Container, style]}>
                <View style={[styles.TitleView, TitleViewStyle]}>
                    <Text style={[styles.TitleText, TitleTextStyle]}>
                        {title}
                    </Text>
                </View>
                <Picker
                    mode={mode ? mode : "dropdown"}
                    placeholder=""
                    placeholderStyle={{ color: "#bfc6ea" }}
                    placeholderIconColor="#007aff"
                    style={[styles.Picker, PickerStyle]}
                    itemTextStyle={[styles.DropdownItemText, itemTextStyle]}
                    selectedValue={this.state.selected}
                    onValueChange={this.onValueChange.bind(this)}
                >
                    {
                        data.map((item, index) => (
                            <Picker.Item label={item} value={item} />
                        ))
                    }

                </Picker>
                {line ? null : <View style={styles.Line} />}
            </View>
        )
    }
}

const styles = StyleSheet.create({
    Container: {
        width: ws(375),
        justifyContent: 'center',
        alignItems: 'center',
    },
    Picker: {
        width: ws(315),
    },
    TitleView: {
        width: ws(375),
    },
    TitleText: {
        marginLeft: ws(29),
        fontSize: fs(15),
        fontWeight: 'bold',
        color: '#BEC2CE',
    },
    DropdownItemText: {
        fontSize: fs(16),
    },
    Line: {
        borderTopColor: "#EFF1F5",
        borderTopWidth: 1.5,
        borderStyle: 'solid',
        width: ws(315),
        marginTop: -12.71,
    }
})