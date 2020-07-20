import React, { Component } from 'react'
import { Text, View, Modal, Dimensions, TouchableWithoutFeedback } from 'react-native'
import SeachBox from './searchBox'
import Noidungsearch from '../../containers/muahang/nhucauvattu/noidungsearch'

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;
export default class searchPopup extends Component {
    constructor(props){
        super(props);
        this.state = { visible: this.props.visible }
    }
    _onModalClose = () => {
        this.setState({
            visible:false
        })
    }
    componentWillReceiveProps = (nextProps) => {
        if(nextProps.visible != this.state.visible){
            this.setState({ visible: nextProps.visible})
        }
    }
    render() {
        const { visible } = this.state;
        const { headers, content } = this.props;
        return (
            <Modal
                animationType='slide'
                transparent={true}
                visible={visible}
                onRequestClose={() => {
                    this._onModalClose();
                }}>
            <TouchableWithoutFeedback onPress={this._onModalClose}>
              <View style={{ width:windowWidth, height:'100%'}}>  
              <TouchableWithoutFeedback>
               <View style={{
                    backgroundColor: '#fff',
                    width: (windowWidth*1)- 92,
                    height: windowHeight - 24.3,
                    alignSelf: 'center',
                    borderRadius: windowHeight*0.01,
                    borderTopRightRadius:0,
                    borderBottomRightRadius:0,
                    right:0,
                    alignItems: 'center',
                    position:'absolute',
                    shadowColor: "#000",
                    shadowOffset: {
                        width: 0,
                        height: 9,
                    },
                    shadowOpacity: 0.48,
                    shadowRadius: 11.95,

                    elevation: 18,
                    }}>
                <View>
                {headers}
                <SeachBox/>
                {content}
                </View>
                </View>
                </TouchableWithoutFeedback>
                </View>
                </TouchableWithoutFeedback>
            </Modal>
        )
    }
}
