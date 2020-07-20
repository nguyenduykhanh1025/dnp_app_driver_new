import base64 from 'react-native-base64';
import md5 from 'md5';

import { URL } from '../services/config';
import { CheckInternetFetch } from '@/utils';
const url = URL + 'login'

export default function loginAPI(params) {
  if (CheckInternetFetch()) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var data = JSON.stringify(params);
    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: data,
    };
    return fetch(url, requestOptions)
      .then(response => response.json())
      .then(result => result)
      .catch(error => error);
  }
}
