import { showMessage } from 'react-native-flash-message';

import { commonStyles } from '@/commons';

const ErrorMessage = (message, description) => {
    var msg = message != undefined || message != null ? message : '';
    var tp = 'danger';
    var des = description != undefined || description != null ? description : '';
    des != '' ?
        showMessage({
            message: msg,
            description: des,
            type: tp,
            titleStyle: commonStyles.flashMessageStyle,
            textStyle: commonStyles.flashMessageStyle,
            hideStatusBar:true
        })
        :
        showMessage({
            message: msg,
            description: des,
            type: tp,
            titleStyle: commonStyles.flashMessageStyle,
            textStyle: commonStyles.flashMessageStyle,
            hideStatusBar:true
        })
}

export default ErrorMessage;