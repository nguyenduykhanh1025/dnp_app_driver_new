import { put, call, all, fork, takeLatest, takeEvery } from 'redux-saga/effects';
import * as types from './constants';
import {
    loginAuth,
    logOutAuth,
    loginS
} from './service';

import { authStack, mainStack, scanQRStack, rootSwitch, carrierStack, supplierStack } from '../../config/navigator';
import NavigationService from '@/utils/navigation';
import { saveAccount, savePassword, saveUserID, saveName } from '@/stores';
import { ErrorMessage } from '@/components';

function* login(action) {
    try {
        //yield call(NavigationService.navigate, mainStack.home);
        // yield put({
        //     type: types.LOGIN_LOADING,
        //     isLoading: true,
        // });
        // const response = yield call(loginAuth, action.params); //Gọi API Login ở đây.
        // yield call(doLoginSuccess, response, action.params);
    } catch (err) {
        ErrorMessage(err.toString());
        yield put({
            type: types.LOGIN_LOADING,
            isLoading: false,
        });
    }
}
function* loginSupplier(action) {
    // try {
    //     yield put({
    //         type: types.LOGIN_LOADING,
    //         isLoading: true,
    //     });
    //     const response = yield call(loginS, action.params); //Gọi API Login ở đây.
    //     yield call(doLoginSuccess, response, action.params);
    // } catch (err) {
    //     ErrorMessage(err.toString());
    //     yield put({
    //         type: types.LOGIN_LOADING,
    //         isLoading: false,
    //     });
    // }
}

function* doLoginSuccess(result, account) {
    try {
        if (typeof (result) == 'object')
            if (result.code == 1) {
                saveAccount(account.loginname);
                savePassword(account.pwd);
                saveUserID(result.id);
                saveName(result.name)
                switch (result.role_id) {
                    case '6':
                        yield call(NavigationService.navigate, supplierStack.supplier);
                        // yield put({
                        //     type: types.LOGIN_SUCCESS,
                        //     msg: 'Đăng nhập thành công !',
                        //     type_msg: 'success',
                        //     userId: result.role_id,
                        //     name: result.name,
                        //     isLoading: false,
                        // });
                        break;
                    default:
                        yield put({
                            type: types.LOGIN_FAIL,
                            msg: 'Đăng nhập thất bại. Tài khoản không có quyền đăng nhập !',
                            type_msg: 'danger',
                            isLoading: false,
                        });
                        break;
                }

            }
            else {
                yield put({
                    type: types.LOGIN_FAIL,
                    msg: 'Đăng nhập thất bại !',
                    type_msg: 'danger',
                    isLoading: false,
                });
            }
        else if (Array.isArray()) {
            ErrorMessage('Server trả về không đúng kiểu dữ liệu !');
            yield put({
                type: types.LOGIN_LOADING,
                isLoading: false,
            });
        }
        else {
            ErrorMessage('Mất kết nối đường truyền ... !');
            yield put({
                type: types.LOGIN_LOADING,
                isLoading: false,
            });
        }
    } catch (err) {
        ErrorMessage(err.toString());
        yield put({
            type: types.LOGIN_LOADING,
            isLoading: false,
        });
    }
}

function* logOut() {
    try {
        yield call(logOutAuth);
        yield call(NavigationService.navigate, rootSwitch.auth);
    } catch (err) {
        ErrorMessage(err.toString());
    }
}



export default function* authSaga() {
    yield takeEvery(types.LOGIN, login);
    yield takeEvery(types.LOGIN_SUPPLIER, loginSupplier);
    yield takeEvery(types.LOGOUT, logOut);
}