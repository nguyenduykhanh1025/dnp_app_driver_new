import { all, fork } from 'redux-saga/effects';

import authSaga from './modules/auth/saga';
import homeSaga from './modules/home/saga';

export default function* rootSagas() {
  yield all([
    fork(authSaga),
    fork(homeSaga),
  ]);
}
