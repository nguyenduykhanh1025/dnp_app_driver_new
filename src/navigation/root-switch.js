import React from 'react';
import { createAppContainer, createSwitchNavigator } from 'react-navigation';
import { rootSwitch } from '../config/navigator';

import AuthStack from './auth-stack';
import MainStack from './main_stack';
import SplashStack from './splash-stack';

export const AppContainer = createAppContainer(
    createSwitchNavigator(
        {
            [rootSwitch.splash]: SplashStack,
            [rootSwitch.auth]: AuthStack,
            [rootSwitch.main]: MainStack,
        },
        {
            initialRouteName: rootSwitch.splash,
        }
    )
);
