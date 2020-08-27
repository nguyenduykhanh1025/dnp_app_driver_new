import { fromJS } from 'immutable';
import * as types from './constants';


const notify = {
    unseen: 0,
}

export default function notifyReducers(state = notify, action) {
  switch (action.type) {
    case types.SETNOTI:
      return {...state, unseen: action.number}
    default:
      return state;
  }
}
