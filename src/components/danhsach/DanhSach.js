import React, { Component } from 'react';
import { View, StyleSheet, Button, ScrollView } from 'react-native';
import { FlatList } from '@/components';

class DanhSach extends Component {
    render() {
        return (
            <FlatList
                scrollEnabled = {true}
                data={this.props.data}
                onRefresh={this.props._refresh}
                refreshing={this.props.isRefreshing}
                renderItem={(item) => this.props._renderItems(item)}
                keyExtractor={this.props.keyExtractor}
                style={{ backgroundColor: '#fff' }}
                ListHeaderComponent={this.props.renderHeader}
                onEndReached={this.props._loadMore}
                onEndReachedThreshold={this.props.onEndReachedThreshold}
            />
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    buttonLogin: {
        marginTop: 50,
    }
})

export default DanhSach;