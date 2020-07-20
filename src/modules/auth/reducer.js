import { fromJS } from 'immutable';

import * as types from './constants';
// import appState from '../contants/initialState';
import { getAccount, getPassword, getUserID } from '@/stores';
import { FlashMessage } from '@/components';

const login = fromJS({
  loginname: '',
  pwd: '',
  userID: '',
  message: '',
  type_message: '',
  isLoading: false,
})

export default function loginReducers(state = login, action) {

  switch (action.type) {

    case types.LOGIN:
      FlashMessage('Đăng nhập thành công !', 'success')
      return state
        .set('loginname', action.loginname)
        .set('pwd', action.pwd)
    case types.LOGIN_SUPPLIER:
      return state
      .set('loginname', action.loginname)
      .set('pwd', action.pwd)

    case types.LOGIN_LOADING:
      return state
        .set('isLoading', action.isLoading);

    case types.LOGIN_SUCCESS:
      FlashMessage(action.msg, action.type_msg)
      return state
        .set('userID', action.userID)
        .set('isLoading', action.isLoading);

    case types.LOGIN_FAIL:
      FlashMessage(action.msg, action.type_msg)
      return state
        .set('isLoading', action.isLoading)

    case types.LOGOUT:
      FlashMessage('Đăng xuất thành công !', 'success')
      return state
        .set('loginname', '')
        .set('pwd', '')
        .set('userID', '')
        .set('message', '')
        .set('type_message', '')
        .set('isLoading', false)

    default:
      return state;
  }
}
