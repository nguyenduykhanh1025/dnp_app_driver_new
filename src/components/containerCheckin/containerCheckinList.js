import React, { Component } from 'react';
import { Text, View, TouchableOpacity, StyleSheet, StatusBar, ScrollView, ActivityIndicator, FlatList, Alert, Image } from 'react-native';
import {
	Colors, widthPercentageToDP as ws,
	heightPercentageToDP as hs,
	fontSizeValue as fs,
	sizeWidth
} from '@/commons';
import Icon from 'react-native-vector-icons/AntDesign';
import { getToken } from '@/stores';
import { ContainerCheckinItem } from '@/components/containerCheckin';
import { callApi } from '@/requests';
import Toast from 'react-native-tiny-toast';
export default class ContainerCheckinList extends Component {

	constructor(props) {
		super(props);
		this.state = {
			containerList: [],
			refreshing: false,
			checkedAbleLenght: {},
			checkedJobOrderNo: {},
			contStateMap: {}
		}
	}

	componentDidMount = async () => {
		this.token = await getToken();
		this.getContainerlist();
	};

	//--------> On load data from server
	getContainerlist = async () => {

		const params = {
			api: 'checkin/cont-available',
			param: '',
			token: this.token,
			method: 'GET'
		}
		var payload = undefined;

		payload = await callApi(params);
		if (payload.code == 0) {
			const { listContAvailable } = payload;
			const checkedAbleLenght = this.getLenJobOrderNo(listContAvailable);
			this.setState({
				containerList: listContAvailable,
				checkedAbleLenght
			});

			// contStateMap giup nhan dien disable hay ko?
			let payoadContStateMap = {};
			listContAvailable.forEach(cont => {
				payoadContStateMap[`${cont.block}${cont.bay}${cont.row}${cont.tier}`] = true;
			});
			this.setState({
				contStateMap: payoadContStateMap
			})
			this.getContTierUnderNotChecked(null);
		} else {
			Alert.alert('Thông báo!', payload.msg);
			this.props.onClose();
		}

	}
	//---------> end onload 

	//---------> render
	renderLeft = () => {
		return (
			<View
				style={{
					marginLeft: ws(29.54)
				}}
			>
				<Icon name={'close'} size={20} />
			</View>
		)
	}

	renderItem = (item, index) => {
		const indexJob = this.state.containerList.findIndex(data => data.jobOrderNo === item.item.jobOrderNo);
		if (indexJob === item.index) {
			return (
				<View>
					<View style={[styles.ItemLineText, { marginTop: hs(10) }]}>
						<Text style={styles.ItemLineLabel}>
							Số tham chiếu
					</Text>
						<Text style={styles.ItemLineDate}>
							{item.item.jobOrderNo}
						</Text>
					</View>
					<ContainerCheckinItem
						data={item.item}
						onSelectCont={(value) => this.onSelectCont(value)}
						isDisable={!this.state.contStateMap[`${item.item.block}${item.item.bay}${item.item.row}${item.item.tier}`]}
					/>
				</View>
			)
		} else {
			return (
				<ContainerCheckinItem
					data={item.item}
					onSelectCont={(value) => this.onSelectCont(value)}
					isDisable={!this.state.contStateMap[`${item.item.block}${item.item.bay}${item.item.row}${item.item.tier}`]}
				/>
			)
		}

	}
	//---------> end render

	onBack = () => {
		onClose();
	};

	onRefresh = () => {
		this.getContainerlist();
	}

	onSelectCont = (value) => {
		this.setState({
			containerList: this.state.containerList.map(item => {
				if (item.shipmentDetailId === value.shipmentDetailId) {
					item.checked = !item.checked;
				}
				return item;
			})
		})
		this.getContTierUnderNotChecked(value);
	}

	onClickOK = async () => {

		//validate không check đủ số lượng ban đầu từ server
		const clickAbleLenghtCurrent = this.getLenJobOrderNo(this.state.containerList);
		for (let i = 0; i < clickAbleLenghtCurrent.length; ++i) {
			const { key: keyCurrent, len: lenCurrent } = clickAbleLenghtCurrent[i];
			const { key, len } = this.state.checkedAbleLenght[i];

			if (keyCurrent === key && lenCurrent != len) {
				Alert.alert('Thông báo!', 'Bạn không chọn đúng số lượng container cần bốc.');
				return;
			}
		}

		// validate không cho bốc container đang bị đè
		let value = null;
		for (let i = 0; i < this.state.containerList.length; ++i) {
			const containerItem = this.state.containerList[i];
			if (value && value.block === containerItem.block && value.bay === containerItem.bay && value.row === containerItem.row) {
				if (value.tier < this.state.containerList[i].tier && this.state.containerList[i].checked === false) {
					Alert.alert('Thông báo!', `Container ${value.containerNo} không được phép bốc do có container ${this.state.containerList[i].containerNo} đang nằm trên.`);
					return;
				}
			}

			if (containerItem.checked === true) {
				value = containerItem;
			}
		}

		// success
		const containerListChecked = this.state.containerList.filter(item => item.checked === true);
		const pickupHistories = containerListChecked.map(item => {
			return {
				id: item.pickupHistoryId,
				shipmentDetailId: item.shipmentDetailId,
				containerNo: item.containerNo,
				bay: item.bay,
				block: item.block,
				line: item.row,
				tier: item.tier
			}
		});
		this.props.onSuccess(pickupHistories);
	}

	getLenJobOrderNo = (listContAvailable) => {
		let result = [];
		if (listContAvailable && listContAvailable.length) {
			let jobOrderNoOld = listContAvailable[0].jobOrderNo;
			let count = 0;
			for (let i = 0; i < listContAvailable.length; ++i) {
				if (jobOrderNoOld != listContAvailable[i].jobOrderNo) {
					result = [
						...result,
						{
							key: jobOrderNoOld,
							len: count,
							array: this.state.containerList.filter(item => item.jobOrderNo === jobOrderNoOld)
						}
					]
					jobOrderNoOld = listContAvailable[i].jobOrderNo;
					count = 0;
					if (listContAvailable[i].checked) {
						count++;
					}
				} else {
					if (listContAvailable[i].checked) {
						count++;
					}
				}
			}
			result = [
				...result,
				{
					key: jobOrderNoOld,
					len: count,
					array: this.state.containerList.filter(item => item.jobOrderNo === jobOrderNoOld)
				}
			]
		}
		return result;
	}

	getContAbove(block, bay, row, tier) {
		return this.state.containerList.filter(item => {
			if (item.block === block && item.bay === bay && item.row === row && item.tier === tier) {
				return item;
			}
		})[0];
	}

	isDisableFollowContAbrove(contStateMap, cont) {
		const { block, bay, row, tier } = cont;

		let contAbroveFloorTwo = contStateMap[`${block}${bay}${row}${tier + 1}`];
		let contAbroveFloorThree = contStateMap[`${block}${bay}${row}${tier + 2}`];
		let contAbroveFloorFour = contStateMap[`${block}${bay}${row}${tier + 3}`];
		let contAbroveFloorFive = contStateMap[`${block}${bay}${row}${tier + 4}`];

		let flag = false;

		if (contAbroveFloorTwo != null && !this.getContAbove(block, bay, row, tier + 1).checked) {
			flag = true;
		} else if (contAbroveFloorThree != null && !this.getContAbove(block, bay, row, tier + 2).checked) {
			flag = true;
		} else if (contAbroveFloorFour != null && !this.getContAbove(block, bay, row, tier + 3).checked) {
			flag = true;
		} else if (contAbroveFloorFive != null && !this.getContAbove(block, bay, row, tier + 4).checked) {
			flag = true;
		}

		if (flag && !cont.checked) {
			return true;
		}
		return false;
	}

	getContTierUnderNotChecked(value) {
		const { contStateMap, containerList } = this.state;
		let contActiveList = {};

		// disable nhung cont neu tren dau no chua duoc check va ban than no chua duoc check
		containerList.forEach((cont, index) => {
			const { block, bay, row, tier } = cont;
			let contBelow = contStateMap[`${cont.block}${cont.bay}${cont.row}${cont.tier + 1}`];
			if (this.isDisableFollowContAbrove(contStateMap, cont)) {
				contActiveList[`${cont.block}${cont.bay}${cont.row}${cont.tier}`] = false;
			}
			else {
				contActiveList[`${cont.block}${cont.bay}${cont.row}${cont.tier}`] = true;
			}
		});

		//disable nhung cont neu chua du so luong cont trong cung 1 so tham chieu (so luong ban dau do server tra ve)
		this.state.checkedAbleLenght.forEach(checkedAbleItem => {
			// Get current checked containers
			let contCounter = containerList.filter(item => checkedAbleItem.key === item.jobOrderNo && (item.checked === true)).length;
			// disable all cont because enough
			if (contCounter >= checkedAbleItem.len) {
				containerList.forEach(cont => {
					if (!cont.checked) {
						// Disable
						contActiveList[`${cont.block}${cont.bay}${cont.row}${cont.tier}`] = false;
					}
				});
			}
		});

		// disable neu tren dau no van chua duoc check
		containerList.forEach(cont => {
			let contBelow = contStateMap[`${cont.block}${cont.bay}${cont.row}${cont.tier + 1}`];
			if (contBelow != null && value && cont.shipmentDetailId === value.shipmentDetailId) {
				// disable contBelow
				contActiveList[`${cont.block}${cont.bay}${cont.row}${cont.tier}`] = true;
			}
		});

		this.setState({
			contStateMap: contActiveList
		})

		// xoa check cua cont phia duoi, neu cont phia tren no vua bo checked
		this.removeCheckedIfAboveNotChecked(contActiveList, containerList);
	}

	isRemoveFollowContAbrove(contStateMap, cont) {
		const { block, bay, row, tier } = cont;

		let contAbroveFloorTwo = contStateMap[`${block}${bay}${row}${tier + 1}`];
		let contAbroveFloorThree = contStateMap[`${block}${bay}${row}${tier + 2}`];
		let contAbroveFloorFour = contStateMap[`${block}${bay}${row}${tier + 3}`];
		let contAbroveFloorFive = contStateMap[`${block}${bay}${row}${tier + 4}`];

		let flag = false;

		if (contAbroveFloorTwo != null && !this.getContAbove(block, bay, row, tier + 1).checked) {
			flag = true;
		} else if (contAbroveFloorThree != null && !this.getContAbove(block, bay, row, tier + 2).checked) {
			flag = true;
		} else if (contAbroveFloorFour != null && !this.getContAbove(block, bay, row, tier + 3).checked) {
			flag = true;
		} else if (contAbroveFloorFive != null && !this.getContAbove(block, bay, row, tier + 4).checked) {
			flag = true;
		}

		if (flag) {
			return true;
		}

		return false;
	}

	removeCheckedIfAboveNotChecked(contStateMap, containerList) {
		let arrChecked = [];
		containerList.forEach((cont, index) => {
			const { block, bay, row, tier } = cont;
			if (this.isRemoveFollowContAbrove(contStateMap, cont)) {
				arrChecked.push(false);
				contStateMap[`${block}${bay}${row}${tier}`] = false;
			}
			else {
				arrChecked.push(cont.checked);
			}
		});

		this.setState({
			containerList: containerList.map((item, index) => {
				item.checked = arrChecked[index];
				return item;
			}),
		})
	}

	render() {
		var {
			onClose, onSuccess
		} = this.props;
		return (
			<View style={styles.container}>
				<StatusBar
					translucent
					barStyle='dark-content'
				/>
				<View style={styles.header}>
					<TouchableOpacity style={styles.iconClose} onPress={onClose}>
						<View
							style={{
								marginLeft: ws(16),
							}}
						>
							<Icon name={'close'} size={20} />
						</View>
					</TouchableOpacity>
					<View style={styles.titleContainer}>
						<Text style={styles.titleText}>Container có thể bốc</Text>
					</View>
				</View>
				<View style={styles.Body}>
					<ScrollView >
						<View>
							{
								this.state.containerList.length == 0 ?
									<View style={{ height: hs(425), width: '100%', justifyContent: 'center', alignItems: 'center' }}>
										<ActivityIndicator size='large' color={Colors.mainColor} />
									</View>
									:
									<FlatList
										data={this.state.containerList}
										refreshing={this.state.refreshing}
										onRefresh={() => {
											this.onRefresh()
										}}
										renderItem={(item, index) => this.renderItem(item, index)}
									/>
							}
						</View>
					</ScrollView>
				</View>
				<View
					style={styles.btnWrapper}
				>
					<View style={[styles.btnContainer]}>
						<TouchableOpacity onPress={onClose}>
							<View style={styles.btnContainerCancel}>
								<Text style={[styles.btnButtonTextCancel]}>
									{'Hủy'}
								</Text>
							</View>
						</TouchableOpacity>
					</View>

					<View style={[styles.btnContainer]}>
						<TouchableOpacity onPress={() => this.onClickOK()}>
							<View style={styles.btnButtonContainer}>
								<Text style={[styles.btnButtonText]}>
									{'OK'}
								</Text>
							</View>
						</TouchableOpacity>
					</View>
				</View>
			</View>
		)
	}
}

const styles = StyleSheet.create({
	container: {
		height: '100%'
	},
	header: {
		// flexDirection : "row", 
		justifyContent: "center",
		alignItems: 'center',
		height: ws(60)
	},
	iconClose: {
		alignSelf: 'flex-start',
		top: ws(10)
	},
	titleContainer: {
		alignSelf: 'center',
		bottom: ws(10),
	},
	titleText: {
		fontSize: fs(18),
		fontWeight: 'bold',
		color: Colors.black,
	},
	Body: {
		width: ws(375),
		height: hs(629),
	},
	contentView: {
		backgroundColor: Colors.white,
		height: hs(712),
	},
	TitleHistory: {
		marginLeft: ws(16),
		marginBottom: hs(14),
	},
	TitleHistoryText: {
		fontSize: fs(18),
		fontWeight: 'bold',
		color: Colors.black,
	},
	btnWrapper: {
		width: '100%',
		paddingLeft: 12,
		paddingRight: 12,
		position: 'absolute',
		bottom: 15,
		left: 0,
		flex: 1,
		flexDirection: 'row',
		justifyContent: 'space-between',
	},

	btnContainer: {
		width: ws(170),
		justifyContent: 'center',
		alignItems: 'center',

	},
	btnButtonContainer: {
		width: ws(170),
		height: hs(55),
		backgroundColor: Colors.subColor,
		borderRadius: 8,
		justifyContent: 'center',
		alignItems: 'center',
	},
	btnContainerCancel: {
		width: ws(170),
		height: hs(55),
		backgroundColor: Colors.blue,
		borderRadius: 8,
		justifyContent: 'center',
		alignItems: 'center',
	},
	btnButtonText: {
		fontSize: fs(16),
		color: Colors.white,
		fontWeight: 'bold',
	},
	btnButtonTextCancel: {
		fontSize: fs(16),
		color: Colors.white,
		fontWeight: 'bold',
	},
	ItemLineLabel: {
		fontStyle: 'normal',
		fontWeight: '500',
		fontSize: fs(14),
		color: Colors.tinyTextGrey,
		marginRight: sizeWidth(3)
	},
	ItemLineDate: {
		fontSize: fs(14),
		color: Colors.black,
		fontWeight: 'bold',
		fontStyle: 'normal',
	},
	ItemLineText: {
		flexDirection: 'row',
		marginLeft: ws(21)
	},
})