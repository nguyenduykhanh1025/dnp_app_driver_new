import React from 'react';
import { createStackNavigator } from 'react-navigation-stack';
import { createBottomTabNavigator } from 'react-navigation-tabs';

import { homeTab } from '../config/navigator';

import Home from '@/screens/home/home';
import History from '@/screens/history/list';
import Notification from '@/screens/notification/list';
import Profile from '@/screens/profile/profile';

import { Tabbar } from '@/components'

export default createBottomTabNavigator(
    {
        [homeTab.home]: { screen: Home },
        [homeTab.history]: { screen: History },
        [homeTab.notification]: { screen: Notification },
        [homeTab.profile]: { screen: Profile },

    },
    {
        defaultNavigationOptions: {
            header: null,
        },
        tabBarComponent: props => <Tabbar {...props} />,
    }
);
