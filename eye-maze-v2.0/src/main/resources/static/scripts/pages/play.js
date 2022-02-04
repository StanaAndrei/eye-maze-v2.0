import joinRoom from "../api/room/join.js";
import "https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
import createRoom from "../api/room/create.js";
const mmkRoute = '/user/topic/mmk';
let subscription;

const mmkCallback = message => {
    stompClient.disconnect(async () => { 
        const { body: roomUUID } = message;

        if (!roomUUID.length) {
            await createRoom();
        } else {
            await joinRoom(roomUUID);
        }
    });
}


$('#create-btn').click(event => {
    event.preventDefault();
    window.location.assign('/mkroom');
})

$('#join-btn').click(async event => {
    event.preventDefault();
    const uuid = prompt(`Room's code:`);
    if (!uuid) {
        return;
    }
    await joinRoom(uuid);
})

let stompClient;
$('#matchmaking').click(async event => {
    event.preventDefault();

    if ($('#matchmaking').text() === 'cancel') {
        await stompClient.send('/ws/mmk');//*/
        await subscription.unsubscribe();
        subscription = undefined;
        $('#mmk-state').text('');
        $('#matchmaking').text('matchmaking');
    } else {
        if (!subscription) {
            subscription = stompClient.subscribe(mmkRoute, mmkCallback)
        }
        await stompClient.send('/ws/mmk');//*/
        $('#mmk-state').text('searching...');
        $('#matchmaking').text('cancel');
    }
})

window.onload = () => {
    let socket = new SockJS('/our-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('afed');
        $('#matchmaking')[0].disabled = false;
    });
}