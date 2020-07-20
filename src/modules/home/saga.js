import { put, call, all, fork, takeLatest, takeEvery } from 'redux-saga/effects';
import * as types from './constants';
import { mainStack, carrierStack } from '@/config/navigator';
import NavigationService from '@/utils/navigation';


function* go_detail(action) {
    yield call(NavigationService.navigate, carrierStack.detail);
    yield put({
        type: types.SEN_DATA,
        param: action.param
    });
}

function* go_scannerQR() {
    yield call(NavigationService.navigate, mainStack.scanqr);
}

export default function* homeSaga() {
    yield takeEvery(types.DETAIL, go_detail),
    yield takeEvery(types.SCANNER_QR, go_scannerQR)
}