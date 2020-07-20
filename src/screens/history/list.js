import React, { Component } from 'react'
import { Text, View, StyleSheet, Button, ScrollView, FlatList } from 'react-native';
import NavigationService from '@/utils/navigation';
import { mainStack } from '@/config/navigator';
import { Colors, sizes, sizeWidth, sizeHeight } from '@/commons'
import { getListHistory, SearchQRCode } from '@/mock/index';
import Item from './item';
import { ModalQRResult } from '@/components/modal'
import { Right } from 'native-base';
import { HeaderMain } from '@/components/header';

export default class ListHistory extends Component {
	constructor(props) {
		super(props);
		this.state = {
			data: [],
			visible: false,
		}
	};

	componentDidMount = async () => {
		var { data } = this.state;
		this.setState({
			data: getListHistory[0].Data
		})
	};

	onClose = () => {
		this.setState({
			visible: false,
		})
	};

	onOpen = () => {
		this.setState({
			visible: true,
		})
	}

	render() {
		var { data } = this.state;
		return (
			<View>
				<HeaderMain
					backgroundColor={Colors.blue}
					title='History'
				/>
				<ScrollView
					style={styles.container}
					showsVerticalScrollIndicator={false}
				>
					<View style={styles.list}>
						{data.map((item, index) => (
							<Item
								data={item}
								key={index}
								onPress={() => {
									NavigationService.navigate(mainStack.result, { item: item })
								}}
							/>
						))}
					</View>
				</ScrollView>
			</View>
		)
	}
}
const styles = StyleSheet.create({
	container: {
		backgroundColor: Colors.white,
		height: sizeHeight(97),
	},
	list: {
		marginTop: sizeHeight(3),
		marginHorizontal: sizeWidth(3),
		marginBottom: sizeHeight(20)
	}
})