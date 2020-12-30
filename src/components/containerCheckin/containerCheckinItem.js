import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import {
	Colors,
	sizeWidth,
	widthPercentageToDP as ws,
	heightPercentageToDP as hs,
	fontSizeValue as fs,
} from '@/commons';
import moment from 'moment';
import Icon from 'react-native-vector-icons/FontAwesome';

class ContainerCheckinItem extends Component {
	render() {
		var { data, onPress, onSelectCont } = this.props;
		var date = moment(data.gateInDate).format('DD-MM-YYYY');

		return (
			<TouchableOpacity onPress={() => onSelectCont(data)}>
				<View>
					<View style={styles.Container}>
						<View style={styles.ItemContainer}>
							<View style={[styles.checkbox, data.selected ? styles.checkboxActive : '']}>
								{data.selected ?
									<Icon name="check" size={15} color='#fff' />
									: null
								}
							</View>
							<View style={styles.ItemRight}>
								<View style={styles.ItemRightContainer}>
									<View style={styles.ItemRightText}>
										<View style={styles.ItemLineText}>
											<Text style={styles.ItemLineLabel}>
												Container
                      </Text>
											<Text style={styles.ItemLineValue}>
												{data.containerNo}
											</Text>
										</View>
										<View style={[styles.ItemLineText, { marginTop: hs(10) }]}>
											<Text style={styles.ItemLineLabel}>
												Kích thước
                        </Text>
											<Text style={styles.ItemLineDate}>
												{date}
											</Text>
										</View>
									</View>
								</View>
							</View>
						</View>
					</View>
				</View>
			</TouchableOpacity>
		)
	}
}

const styles = StyleSheet.create({
	Container: {
		width: ws(375),
		justifyContent: 'center',
		alignItems: 'center',
		marginBottom: hs(15),
	},
	ItemContainer: {
		width: ws(345),
		height: hs(75),
		backgroundColor: Colors.white,
		flexDirection: 'row',
		alignItems: 'center',
		paddingLeft: 15,
		borderBottomColor: Colors['(0, 224, 150, 0.1)'],
		borderBottomWidth: 1
	},
	ItemLeft: {
		width: ws(75),
		height: hs(75),
		backgroundColor: Colors.subColor,
		borderRadius: 10,
		justifyContent: 'center',
		alignItems: 'center',
	},
	ItemRight: {
		flexDirection: 'row',
		justifyContent: 'space-between',
		width: ws(270),
		height: hs(75),
		alignItems: 'center',
	},
	ItemRightContainer: {
		marginLeft: ws(21),
		height: hs(55),
	},
	ItemRightText: {

	},
	ItemLineText: {
		flexDirection: 'row',
	},
	ItemLineLabel: {
		fontStyle: 'normal',
		fontWeight: '500',
		fontSize: fs(14),
		color: Colors.tinyTextGrey,
		marginRight: ws(7)
	},
	ItemLineValue: {
		fontSize: fs(16),
		fontWeight: 'bold',
		color: Colors.blue,
	},
	ItemLineDate: {
		fontSize: fs(14),
		color: Colors.black,
		fontWeight: 'bold',
		fontStyle: 'normal',
	},
	righticon: {
		width: ws(10.29),
		height: ws(18),
		marginRight: ws(17.57)
	},
	IconLeft: {
		width: ws(34),
		height: ws(34),
		tintColor: Colors.white,
	},
	checkbox: {
		width: 20,
		height: 20,
		borderColor: Colors.blue,
		borderWidth: 1,
		alignItems: 'center',
		justifyContent: 'center',
		borderRadius: 4,
		backgroundColor: Colors.white,
		marginRight: sizeWidth(3)
	},
	checkboxActive: {
		backgroundColor: Colors.blue,
		borderColor: Colors.blue,
	}
})

export default ContainerCheckinItem;