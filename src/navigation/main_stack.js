import React from 'react';
import { createStackNavigator, TransitionPresets } from 'react-navigation-stack'
import { mainStack } from '../config/navigator';

import { Home, Detail, QRCode, Result } from '@/screens/home';
import ListNotification from '@/screens/notification/list';
import ListHistory from '@/screens/history/list';
import Profile from '@/screens/profile/profile';
import HomeTab from './home_tab'

import { Tabbar } from '@/components'


export default createStackNavigator(
    {
        [mainStack.home_tab]: HomeTab,
        [mainStack.detail]: Detail,
        [mainStack.qr_code]: QRCode,
        [mainStack.result]: Result,

        [mainStack.history]: ListHistory,

        [mainStack.notification]: ListNotification,

        [mainStack.profile]: Profile,
    },
    {
        defaultNavigationOptions: {
            header: null,
            ...TransitionPresets.SlideFromRightIOS,
        },}
);
