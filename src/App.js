import React, { Component } from 'react';
import { Provider } from 'react-redux';
// import redux from './config/redux';
// import { AppContainer } from './navigation/root-switch';
import AppRouter from './app_router';
import { PersistGate } from 'redux-persist/integration/react';
import configureStore from './config-store';
import firebase from 'react-native-firebase'
import { StatusBar, View, Text } from 'react-native';
import {
  saveGPSEnable,
  getGPSEnable,
} from '@/stores'

const { store, persistor } = configureStore();
console.disableYellowBox = true;

class App extends Component {

  componentDidMount = async () => {
    var gpsEnable = await getGPSEnable()
    // console.log('gpsEnable', gpsEnable)
    if (!gpsEnable) {
      saveGPSEnable("true")
    }
  }

  render() {
    return (
      <Provider store={store}>
        <StatusBar
          translucent
          barStyle={"default"}
          backgroundColor='transparent'
        />
        <PersistGate loading={null} persistor={persistor}>
          <AppRouter />
        </PersistGate>
      </Provider>
    )
  }
}
export default App