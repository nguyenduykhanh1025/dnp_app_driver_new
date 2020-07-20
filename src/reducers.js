import { combineReducers } from 'redux';

import loginReducers from './modules/auth/reducer';

const rootReducers = combineReducers({
  loginReducers,
});

export default rootReducers