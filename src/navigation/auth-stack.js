import { createStackNavigator } from 'react-navigation-stack';
import LoginContainer from '../screens/auth/login';
import SignUpScreen from '@/screens/auth/signup';

import { authStack } from '../config/navigator';

export default createStackNavigator(
    {
        [authStack.login]: LoginContainer,
        [authStack.signup]: SignUpScreen,
    },
    {
        defaultNavigationOptions: {
            headerShown: false
        },
    }
);