import React, { Component } from 'react'
import { Text, View, Modal, Dimensions, TouchableWithoutFeedback } from 'react-native'

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;
export default class index extends Component {
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
                    // backgroundColor: '#FAFAD2',
                    backgroundColor: '#fff',
                    width: (windowWidth*0.98),
                    height: windowHeight*0.4,
                    alignSelf: 'center',
                    // borderRadius: windowHeight*0.05,
                    // borderBottomRightRadius:0,
                    // borderBottomLeftRadius:0,
                    bottom:0,
                    borderColor:'rgba(15, 181, 255, 1)',
                    borderWidth:2,
                    borderBottomWidth:0,
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
