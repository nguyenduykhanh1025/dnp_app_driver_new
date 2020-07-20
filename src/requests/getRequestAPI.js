import { URL } from '../services/config';
import { CheckInternetFetch } from '@/utils';

export default getRequestAPI = (param) => {
  if (CheckInternetFetch()) {
    var url  = URL + param.url
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", param.token)
    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
    }
    return  fetch(url, requestOptions)
      .then(response => response.json())
      .then(result => result )
      .catch(err => err)

  }
}
