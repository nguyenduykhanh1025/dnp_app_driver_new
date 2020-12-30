import React from 'react';
import { createStackNavigator, TransitionPresets } from 'react-navigation-stack'
import { mainStack } from '../config/navigator';

import {
    Home,
    Detail,
    QRCode,
    Result,
    Detail1,
    Detail11,
    Detail2,
    ResultReturn
} from '@/screens/home';
import ListNotification from '@/screens/notification/list';
import ListHistory from '@/screens/history/list';
import Profile from '@/screens/profile/profile';
import HomeTab from './home_tab';

import { Tabbar } from '@/components'


export default createStackNavigator(
    {
        [mainStack.home_tab]: HomeTab,
        [mainStack.detail]: Detail,
        [mainStack.detail1]: Detail1,
        [mainStack.detail11]: Detail11,
        [mainStack.detail2]: Detail2,
        [mainStack.qr_code]: QRCode,
        [mainStack.result]: Result,
        [mainStack.resultReturn]: ResultReturn,

        [mainStack.history]: ListHistory,

        [mainStack.notification]: ListNotification,

        [mainStack.profile]: Profile,
    },
    {
        defaultNavigationOptions: {
            header: null,
            ...TransitionPresets.SlideFromRightIOS,
        },
    }
);
