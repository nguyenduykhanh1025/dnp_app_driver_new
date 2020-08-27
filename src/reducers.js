import { combineReducers } from 'redux';

import loginReducers from './modules/auth/reducer';
import notifyReducers from './modules/notifications/reducer';

const rootReducers = combineReducers({
  loginReducers,
  notifyReducers,
});

export default rootReducers