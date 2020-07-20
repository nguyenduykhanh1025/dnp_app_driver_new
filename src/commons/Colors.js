const tintColor = '#2f95dc';

export default {

  white: '#ffffff',
  grey1: '#f4f4f4',
  grey2: '#e9ecef',
  grey3: '#dee2e6',
  grey4: '#adb5bd',
  grey5: '#999999',
  grey6: '#777777',
  grey7: '#383838',
  grey8: '#1e1e1e',
  grey9: '#2C2C2C',
  black: '#121212',

  maincolor: '#1B5198',
  subColor: '#F3B03F',
  red: '#e60023',
  orange: '#F2711C',
  yellow: '#FBBD08',
  olive: '#B5CC18',
  green: '#51d96c',
  teal: '#A7FCFD',
  blue: '#15307A',
  violet: '#6435C9',
  purple: '#A333C8',
  pink: '#FFC0CB',
  brown: '#A5673F',
  transparent: 'transparent',

  headerColor:'#15307A',

  tintColor,
  tabIconDefault: '#ccc',
  tabIconSelected: tintColor,
  tabBar: '#fefefe',
  errorBackground: 'red',
  warningBackground: '#EAEB5E',
  warningText: '#666804',
  noticeBackground: tintColor,
  greyColor: '#999',
  successColor: '#4caf50',
  pinkColor: '#ff4081',
  mainColor: '#D87245',
  lightMainColor: '#ebf3f5',
  bgLoginScreen: '#8CE0FF',
  colorUnenable: '#c7c7c7',
  colorBorder: '#BBBBBB',
  textColor: '#556299',
  titleColor: '#AABBCC',
  blueColor: '#0F8FFF',
  badgeColor: '#FF6060',
};

const percentToHex = (p) => {
  const intValue = ~~(p / 100 * 255); // map percent to nearest integer (0 - 255)
  const hexValue = intValue.toString(16); // get hexadecimal representation
  return hexValue.padStart(2, '0').toUpperCase(); // format with leading 0 and upper case characters
}

export const colorOpacityMaker = (color, opacity) => `${color}${percentToHex(opacity)}`

