import { NavigationActions, StackActions } from 'react-navigation';

let navigation;

function setTopLevelNavigator(navigatorRef) {
    navigation = navigatorRef;
}

function navigate(routeName, params) {
    if (navigation && routeName) {
        const action = navigation._navigation.navigate({ routeName, params });
        navigation.dispatch(action)
    }
}

function goBack() {
    navigation.dispatch(navigation.back());
}

// add other navigation functions that you need and export them

export default {
    navigate,
    goBack,
    setTopLevelNavigator,
};
