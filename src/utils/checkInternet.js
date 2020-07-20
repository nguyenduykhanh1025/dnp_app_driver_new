import NetInfo from '@react-native-community/netinfo';

import { ErrorMessage } from '@/components';

const CheckInternetEvery = () => {
    NetInfo.addEventListener(state => {
        if (!state.isConnected) {
            ErrorMessage('Mất kết nối đường truyền ... !');
        }
    });
}

const CheckInternetFetch = () => {
    const isConnected = true;
    NetInfo.fetch().then(state => {
        if (!state.isConnected) {
            isConnected = state.isConnected;
        }
    });
    return isConnected;
}

export {
    CheckInternetEvery,
    CheckInternetFetch
}