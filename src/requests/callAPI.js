
import { URL } from '@/services/link';
import { CheckInternetFetch } from '@/utils';

export default callApi = (params) => {
  // console.log('params callapi', params)
  // console.log('params.api', params.api)
  // console.log('params.param', params.param)
  // console.log('params.token', params.token)
  if (CheckInternetFetch()) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", params.token);
    var data = JSON.stringify(params.param);

    if (params.method != 'GET') {
      var requestOptions = {
        method: params.method,
        headers: myHeaders,
        body: data,
      };
    }
    else {
      var requestOptions = {
        method: params.method,
        headers: myHeaders,
      };
    }
    // console.log('requestOptions', requestOptions)

    return fetch(URL + params.api, requestOptions)
      .then(response => response.json())
      .then(result => result)
      .catch(error => error);
  }
}
