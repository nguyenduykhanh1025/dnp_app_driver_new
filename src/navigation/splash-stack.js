import { createStackNavigator, TransitionPresets } from 'react-navigation-stack';
import { SplashScreen } from '@/screens/splash';

export default createStackNavigator(
    {
        SplashScreen: SplashScreen,
    },
    {
        defaultNavigationOptions: {
            headerShown: false
        },
    }
);