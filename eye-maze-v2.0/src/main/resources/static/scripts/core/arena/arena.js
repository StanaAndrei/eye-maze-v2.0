import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import "https://cdn.jsdelivr.net/npm/p5@1.4.0/lib/p5.js"
import "https://cdnjs.cloudflare.com/ajax/libs/p5.js/1.4.0/addons/p5.sound.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
import handleInp from "./handleInp.js"
import GameState from "./GameState.js";

let stompClient;

const initP5 = p5context => {
    p5context.preload = () => {
        let socket = new SockJS('/our-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
            stompClient.subscribe('/user/topic/move-message', message => {
                if (!message) {
                    return;
                }

                const res = JSON.parse(message.body).buffer;
/*
                if (res === 'OVER') {
                    alert('game over!');
                    p5context.noLoop();
                    stompClient.disconnect();
                    return;
                }//*/

                try {
                    const state = JSON.parse(res);
                    GameState.setState(state);        
                } catch (err) {
                    setTimeout(() => window.location.assign(`/past-game/${res.substr(6)}`), 1000);
                }

            })

            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': 'UP'
            }));//*/
        });
        
    }

    p5context.setup = () => {
        const DELTA_WIDTH = sessionStorage.wMouse ? 0 : 340;
        let canvas = p5context.createCanvas(window.innerWidth - DELTA_WIDTH, window.innerHeight);
        canvas.position(DELTA_WIDTH, 0);
        if (!document.hasFocus()) {/*
            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': 'afk'
            }));//*/
        }
    }

    p5context.draw = () => {
        if (!document.hasFocus()) {/*
            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': 'afk'
            }));//*/
        }

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