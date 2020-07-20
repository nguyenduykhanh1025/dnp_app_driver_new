
import { IP } from '../services/config';
import { CheckInternetFetch } from '@/utils';

export default callApi = (params) => {
  if (CheckInternetFetch()) {
    var URL  = IP + params.url
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var data = JSON.stringify(params.param);
    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: data
    }
    return  fetch(URL, requestOptions)
      .then(response => response.json())
      .then(result => result )
      .catch(err => err)

  }
}

