import { loginAPI } from '@/requests';
import { logOut } from '../../stores';


export const loginAuth = async (param) => {
    return await loginAPI(param)
};

export const logOutAuth = async () => {
    await logOut()
} 