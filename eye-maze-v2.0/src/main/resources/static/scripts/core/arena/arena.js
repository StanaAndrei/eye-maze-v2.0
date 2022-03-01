import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import "https://cdn.jsdelivr.net/npm/p5@1.4.0/lib/p5.js"
import "https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
import 'https://cdnjs.cloudflare.com/ajax/libs/p5.js/1.4.1/addons/p5.sound.min.js'
import handleInp from "./handleInp.js"
import GameState from "./GameState.js";

const myProfilePicB64 = document.querySelector('meta[name=myProfilePicB64]').content;
const otherProfilePicB64 = document.querySelector('meta[name=otherProfilePicB64]').content;
let myProfilePic, otherProfilePic;

let stompClient;
let mazeMusic;

const initP5 = p5context => {
    p5context.preload = () => {
        //load
        myProfilePic = p5context.loadImage(myProfilePicB64);
        otherProfilePic = p5context.loadImage(otherProfilePicB64);
        //ws
        let socket = new SockJS('/our-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
            stompClient.subscribe('/user/topic/move-message', message => {
                if (!message) {
                    return;
                }
                const res = JSON.parse(message.body).buffer;
                if (res.startsWith('OVER!')) {
                    setTimeout(() => window.location.assign(`/past-game/${res.substr(6)}`), 1);
                } else {
                    const state = JSON.parse(res);
                    GameState.setState(state);
                }
            })

            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': 'UP'
            }));//*/

        }, () => {
            mazeMusic.pause();
            alert('LOST CONNECTION!!!');
            window.location.assign('/play');
        });

        mazeMusic = p5context.loadSound('./../../../assets/sound/maze-music.mp3');
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

        mazeMusic.loop();        
    }

    let fi = false;
    p5context.draw = () => {
        if (!document.hasFocus()) {/*
            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': 'afk'
            }));//*/
        }

        p5context.background('black');
        GameState.draw(p5context, myProfilePic, otherProfilePic);

        const inp = handleInp(p5context);
        if (inp) {
            stompClient.send('/ws/move-message', {}, JSON.stringify({
                'buffer': inp
            }));//*/
        }
    }

    p5context.keyPressed = () => {
        //press 'M'
        if (p5context.keyCode === 77) {
            if (mazeMusic.isPlaying()) {
                mazeMusic.pause();
            } else {
                mazeMusic.loop();
            }
            return;
        }
    }
}

new p5(initP5, $('#arena')[0]);//*/