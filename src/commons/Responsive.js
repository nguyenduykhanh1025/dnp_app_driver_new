import { Dimensions, PixelRatio, Platform, StatusBar } from 'react-native';
// Retrieve initial screen's width
let screenWidth = Dimensions.get('window').width;
// Retrieve initial screen's height
let screenHeight = Dimensions.get('window').height;

// wBaseDesign, hBaseDesign lấy từ kích cỡ full screen desgin
const wBaseDesign = 375;
const hBaseDesign = 812;

const standardLength = screenWidth > screenHeight ? screenWidth : screenHeight;
const offset =
    screenWidth > screenHeight ? 0 : Platform.OS === "ios" ? 78 : StatusBar.currentHeight;

const deviceHeight =
    // isIphoneX() || Platform.OS === "android" // if app support iphoneX
    Platform.OS === "android"
        ? standardLength - offset
        : standardLength;

const fontSizePercentage = fsPercentage => {
    const heightPercent = (percent * deviceHeight) / 100;
    return Math.round(heightPercent);
}

// guideline height for standard 5" device screen is 680
// fontSize lấy size của font trong design
const fontSizeValue = (fontSize) => {
    const standardScreenHeight = wBaseDesign > hBaseDesign ? wBaseDesign : hBaseDesign;
    const heightPercent = (fontSize * deviceHeight) / standardScreenHeight;
    return Math.round(heightPercent);
}

/**
* Converts provided width percentage to independent pixel (dp).
* @param {string} widthPercent The percentage of screen's width that UI element should cover
* along with the percentage symbol (%).
* @return {number} The calculated dp depending on current device's screen width.
*/

const widthPercentageToDP = widthPercent => {
    // Parse string percentage input and convert it to number.
    // widthPercent được lấy từ width ở design
    // width là % được lấy từ width ở design
    const width = (widthPercent / wBaseDesign) * 100;
    const elemWidth = typeof widthPercent === "number" ? width : parseFloat(width);
    // Use PixelRatio.roundToNearestPixel method in order to round the layout
    // size (dp) to the nearest one that correspons to an integer number of pixels.
    return PixelRatio.roundToNearestPixel(screenWidth * elemWidth / 100);
};
/**
* Converts provided height percentage to independent pixel (dp).
* @param {string} heightPercent The percentage of screen's height that UI element should cover
* along with the percentage symbol (%).
* @return {number} The calculated dp depending on current device's screen height.
*/
const heightPercentageToDP = heightPercent => {
    // Parse string percentage input and convert it to number.
    // heightPercent được lấy từ height ở design
    // height là % được lấy từ height ở design
    const height = (heightPercent / hBaseDesign) * 100;
    const elemHeight = typeof heightPercent === "number" ? height : parseFloat(height);
    // Use PixelRatio.roundToNearestPixel method in order to round the layout
    // size (dp) to the nearest one that correspons to an integer number of pixels.
    return PixelRatio.roundToNearestPixel(screenHeight * elemHeight / 100);
};
/**
* Event listener function that detects orientation change (every time it occurs) and triggers
* screen rerendering. It does that, by changing the state of the screen where the function is
* called. State changing occurs for a new state variable with the name 'orientation' that will
* always hold the current value of the orientation after the 1st orientation change.
* Invoke it inside the screen's constructor or in componentDidMount lifecycle method.
* @param {object} that Screen's class component this variable. The function needs it to
* invoke setState method and trigger screen rerender (this.setState()).
*/
const listenOrientationChange = that => {
    Dimensions.addEventListener('change', newDimensions => {
        // Retrieve and save new dimensions
        screenWidth = newDimensions.window.width;
        screenHeight = newDimensions.window.height;
        // Trigger screen's rerender with a state update of the orientation variable
        that.setState({
            orientation: screenWidth < screenHeight ? 'portrait' : 'landscape'
        });
    });
};
/**
* Wrapper function that removes orientation change listener and should be invoked in
* componentWillUnmount lifecycle method of every class component (UI screen) that
* listenOrientationChange function has been invoked. This should be done in order to
* avoid adding new listeners every time the same component is re-mounted.
*/
const removeOrientationListener = () => {
    Dimensions.removeEventListener('change', () => { });
};
export {
    widthPercentageToDP,
    heightPercentageToDP,
    fontSizePercentage,
    fontSizeValue,
    listenOrientationChange,
    removeOrientationListener
};