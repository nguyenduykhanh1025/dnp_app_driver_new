import { callAPI } from '@/requests';


export const callApi = (param) => {
    const result = await callAPI(param)
    return result
};
