import * as Actions from './constants';

export const login = (params) => ({
  type: Actions.LOGIN,
  loginname: params.loginname,
  pwd: params.pwd,
})

export const loginSupplier = (params) => ({
  type: Actions.LOGIN_SUPPLIER,
  loginname: params.loginname,
  pwd: params.pwd,
})

export const setUserInfo = (data) => ({
  type: Actions.SET_USER_INFO,
  data,
})

export const fetchUserInfo = (onSuccess, onError) => ({
  type: Actions.FETCH_USER_INFO,
  onSuccess,
  onError
})

export const home = (params, gohome) => ({
  type: Actions.GO_HOME,
  params,
  gohome,
})

export const signOut = () => ({
  type: Actions.LOGOUT
})

