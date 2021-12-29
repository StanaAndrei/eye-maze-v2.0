import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import "https://cdn.jsdelivr.net/npm/p5@1.4.0/lib/p5.js"
import "https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
import { getDial } from "./geometry.js";
import GameState from "./GameState.js";

let stompClient;
let cells;

const DELTA_TIME = 1e3;
let currDial, lastDial = -1, currTime, lastTime = 0;
function handleInp(p5context) {
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

const initP5 = p5context => {
    p5context.preload = () => {
        let socket = new SockJS('/our-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
            //console.log(frame);
            stompClient.subscribe('/user/topic/move-message', message => {
                if (!message) {
                    return;
                }

                const state = JSON.parse(JSON.parse(message.body).buffer);
                GameState.setState(state);
            })

            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': ''
            }));//*/
        });
        
    }

    p5context.setup = () => {
        const DELTA_WIDTH = sessionStorage.wMouse ? 0 : 340;
        let canvas = p5context.createCanvas(window.innerWidth - DELTA_WIDTH, window.innerHeight);
        canvas.position(DELTA_WIDTH, 0);
        //console.warn(document.querySelector('meta[name=loginUUID]').content);
    }

    p5context.draw = () => {
        p5context.background('black');
        GameState.draw(p5context);

        const inp = handleInp(p5context);
        if (inp) {
            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': inp
            }));//*/
        }
    }
}

new p5(initP5, $('#arena')[0]);//*/