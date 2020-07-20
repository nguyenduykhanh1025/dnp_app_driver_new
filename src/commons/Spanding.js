import { Dimensions } from "react-native";

const { width, height } = Dimensions.get("window");

const vh = height / 100;
const vw = width / 100;

export const sizeWidth = size => {
    return size * vw;
};

export const sizeHeight = size => {
    return size * vh;
};

export const sizes = {
    base: sizeWidth(4.97),
    h1: sizeWidth(6.5),
    h2: sizeWidth(5.93),
    h3: sizeWidth(4.97),
    h4: sizeWidth(3.7),
    h5: sizeWidth(2.8),
    h6: sizeWidth(2.13),
}