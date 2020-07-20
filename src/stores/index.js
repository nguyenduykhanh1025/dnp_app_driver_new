import AsyncStorage from '@react-native-community/async-storage';
import base64 from 'react-native-base64';



const FCM_TOKEN = 'fcm_token';
const KEY_PROFILE = 'profile';
const KEY_TOKEN = 'access_token';

const KEY_ACCOUNT = 'account';
const KEY_PASSWORD = 'pwd';
const KEY_USERID = 'id'
const KEY_NAME = 'name'


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
    AsyncStorage.setItem(KEY_NAME,name);
}


export const getUserID = () => {
    return AsyncStorage.getItem(KEY_USERID);
};
export const getName= () => {
    return AsyncStorage.getItem(KEY_NAME);
};

export const logOut = () => {
    AsyncStorage.clear()
}; 