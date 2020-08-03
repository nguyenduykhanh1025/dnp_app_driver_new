import AsyncStorage from '@react-native-community/async-storage';
import base64 from 'react-native-base64';



const FCM_TOKEN = 'fcm_token';
const KEY_PROFILE = 'profile';
const KEY_TOKEN = 'access_token';

const KEY_ACCOUNT = 'account';
const KEY_PASSWORD = 'pwd';
const KEY_USERID = 'id';
const KEY_NAME = 'name';
const KEY_GPS_ENABLE = 'KEY_GPS_ENABLE';
const KEY_TRUCK = 'KEY_TRUCK';
const KEY_CHASSIS = 'KEY_CHASSIS';
const KEY_UP = 'KEY_UP';
const KEY_DOWN = 'KEY_DOWN';


export const saveToken = (token) => {
    AsyncStorage.setItem(KEY_TOKEN, token);
}

export const getToken = () => {
    return AsyncStorage.getItem(KEY_TOKEN);
}

export const saveAccount = (account) => {
    AsyncStorage.setItem(KEY_ACCOUNT, account);
};


export const savePassword = (pwd) => {
    AsyncStorage.setItem(KEY_PASSWORD, pwd)
}

export const getAccount = () => {
    return AsyncStorage.getItem(KEY_ACCOUNT);
};

export const getPassword = () => {
    return AsyncStorage.getItem(KEY_PASSWORD);
}

export const saveUserID = (id) => {
    AsyncStorage.setItem(KEY_USERID, id.toString());
}
export const saveName = (name) => {
    AsyncStorage.setItem(KEY_NAME, name);
}

export const saveGPSEnable = (gps) => {
    AsyncStorage.setItem(KEY_GPS_ENABLE, gps);
}

export const getGPSEnable = () => {
    return AsyncStorage.getItem(KEY_GPS_ENABLE);
}

export const saveTruck = (item) => {
    AsyncStorage.setItem(KEY_TRUCK, item);
}

export const getTruck = () => {
    return AsyncStorage.getItem(KEY_TRUCK);
}

export const saveChassis = (item) => {
    AsyncStorage.setItem(KEY_CHASSIS, item);
}

export const getChassis = () => {
    return AsyncStorage.getItem(KEY_CHASSIS);
}

export const saveUpEnable = (item) => {
    AsyncStorage.setItem(KEY_UP, item);
}

export const getUpEnable = () => {
    return AsyncStorage.getItem(KEY_UP);
}

export const saveDownEnable = (item) => {
    AsyncStorage.setItem(KEY_DOWN, item);
}

export const getDownEnable = () => {
    return AsyncStorage.getItem(KEY_DOWN);
}

export const getUserID = () => {
    return AsyncStorage.getItem(KEY_USERID);
};
export const getName = () => {
    return AsyncStorage.getItem(KEY_NAME);
};

export const logOut = () => {
    AsyncStorage.clear()
}; 