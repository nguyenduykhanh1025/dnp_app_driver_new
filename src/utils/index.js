import screen from './screen';
import { CheckInternetEvery, CheckInternetFetch } from './checkInternet';

const replaceAll = (strText, strTarget, strSubString) => {
    var intIndexOfMatch = strText.indexOf(strTarget);
    while (intIndexOfMatch != -1) {
        strText = strText.replace(strTarget, strSubString);
        intIndexOfMatch = strText.indexOf(strTarget);
    }
    return (strText);
}

export {
    screen,
    CheckInternetEvery,
    CheckInternetFetch,
    replaceAll,
}