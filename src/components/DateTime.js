    
export const today = new Date(), date = today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();
export const time = today.getHours() + ":" + today.getMinutes() + ":00";
// export const addZero = (i) => {
//         if (i < 10) {
//             i = "0" + i;
//         }
//         return i;
//     }