import React, { Component } from "react";
import { View, Text, StyleSheet } from "react-native";
import { Colors, commonStyles, Fonts } from '@/commons'

export default class TextDuo extends Component {
    render() {
        const { title, data, money, colors } = this.props
        return (
            <View style={styles.container}>
                <View style={commonStyles.f1}>
                    <Text style={[styles.title,{color: colors?colors:Colors.titleColor}]}>{title}</Text>
                </View>
                <View>
                    {
                        money ?
                            <Text style={styles.dataField}>{data.toFixed().replace(/\d(?=(\d{3})+(?!\d))/g, '$&,')}</Text> :
                            <Text style={styles.dataField}>{data}</Text>
                    }
                </View>
            </View>

        );
    }
}

const styles = StyleSheet.create({
    dataField: {
        fontSize: 15,
        color: Colors.textColor,
        fontWeight: 'normal',
        right: 10,
        fontFamily: Fonts.SairaSemiCondensedRegular

    },
    title: {
        fontSize: 14,
        fontWeight: 'normal',
        color: Colors.titleColor,
        fontFamily: Fonts.SairaSemiCondensedRegular

    },
    container: {
        flexDirection: 'row',
        marginTop: 8
    }
});