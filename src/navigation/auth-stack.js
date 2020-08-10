import { createStackNavigator } from 'react-navigation-stack';
import LoginContainer from '../screens/auth/login';
import SignUpScreen from '@/screens/auth/signup';
import ResetScreen from '@/screens/auth/reset';

import { authStack } from '../config/navigator';

export default createStackNavigator(
    {
        [authStack.login]: LoginContainer,
        [authStack.signup]: SignUpScreen,
        [authStack.reset]: ResetScreen,
    },
    {
        defaultNavigationOptions: {
            headerShown: false
        },
    }
);