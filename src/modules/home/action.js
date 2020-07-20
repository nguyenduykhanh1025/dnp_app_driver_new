import * as Actions from './constants';

export const go_detail = (param) => ({
  type: Actions.DETAIL,
  param
})
export const go_scannerQR = () => ({
  type: Actions.SCANNER_QR,
})
