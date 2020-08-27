import * as Actions from './constants';

export const increaseNoti = () => ({
  type: Actions.INCREASENOTI,
})

export const decreaseNoti = () => ({
    type: Actions.DECREASENOTI,
})

export const setNoti = (number) => ({
    type: Actions.SETNOTI,
    number: number,
})
