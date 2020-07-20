import * as types from './constants';
// import appState from '../contants/initialState';

const carrier = ({
  appointmentId: '',
  status:'',
  status_id:"",
  supplier:'',
  time_end:'',
  time_start:'',
  warehouse:'',
  dock:'',
})

const carrierReducers = (state = carrier, action) => {
  switch (action.type) {
    case types.SEN_DATA:
      const item = action.param
      return { ...state, ...{ 
        appointmentId: item.appointment_id,
        status: item.status,
        status_id: item.status_id,
        supplier: item.supplier,
        time_end: item.time_end,
        time_start: item.time_start,
        warehouse: item.warehouse,
        dock: item.dock,
        map_image: item.img
      } }
    default:
      return state
  }
}

export default carrierReducers