import { getDial } from "./geometry.js";

const DELTA_TIME = 1e3;
let currDial, lastDial = -1, currTime, lastTime = 0;
export default function handleInp(p5context) {
    const { mouseX, mouseY } = p5context;
    currDial = getDial(mouseX, mouseY, p5context);

    if (currDial !== lastDial) {
        lastTime = currTime = p5context.millis();
        lastDial = currDial;
    } else {
        currTime = p5context.millis();
        if (currTime - lastTime >= DELTA_TIME) {
            lastDial = -1;
            return currDial;
        }
    }
    return '';
}