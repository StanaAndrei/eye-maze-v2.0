import leaveRoom from "../api/room/leave.js";

const broadcastCh = new BroadcastChannel('check-dupl');
broadcastCh.postMessage('open');
broadcastCh.onmessage = event => {
    if (event.data === "open") {
        broadcastCh.postMessage(`dupl-found`);
        alert("You can't open a duplicate of the lobby page!");
    }
    if (event.data === `dupl-found`) {
        window.location.assign('/play');
    }
};

let stompClient;

$('#cpy-btn').click(event => {
    event.preventDefault();
    const copyText = $("#room-code")[0];
    copyText.select();
    copyText.setSelectionRange(0, 99999);//for mb
    navigator.clipboard.writeText(copyText.value);
});

function checkRef() {
    if (!document.referrer.startsWith(document.location.origin)) {
        window.location.assign('/play');
    }
}

function showMessage(from, message, isAdmin = false) {
    let style = '';
    if (isAdmin) {
        style = 'style="color: red;"';
    }
    $('#message-history').append(`<p><b ${style} >${from}:</b> ${message}</p>`);
    $('#message-history')[0].scrollTop = $('#message-history')[0].scrollHeight;
}

$('#send').click(event => {
    event.preventDefault();
    const message = $('#message-cont').val().trim();
    if (!message) {
        alert(`message cann't be empty!`);
        return;
    }
    stompClient.send('/ws/room-messages', {}, JSON.stringify({
        'buffer': message
    }));
    $('#message-cont').val('');
})

function connect() {
    let socket = new SockJS('/our-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        //console.log('connected:' + frame);
        stompClient.subscribe('/user/topic/join-leave', resMessage => {
            const { who, state } = JSON.parse(resMessage.body);

            if (state === "JOINED") {
                $('#lobby-players').append(`<h5 class="lobby-pl" id="${who}">${who}</h5>`);
            } else {
                $(`#${who}`).remove();
            }
        })

        stompClient.subscribe('/user/topic/room-messages', message => {
            const { from, content } = JSON.parse(message.body);
            showMessage(from, content);
        })

        stompClient.subscribe('/user/topic/launch-message', message => {
            if (!message) {
                return;
            }
            const { who, state } = JSON.parse(message.body);
            const roomUUID = window.location.pathname.substring(window.location.pathname.lastIndexOf('/') + 1);
            if (state === 'LAUNCH') {
                window.onbeforeunload = undefined;
                stompClient.disconnect(() => { });
                sessionStorage.wMouse = (new URLSearchParams(window.location.search)).has('wmouse');
                window.location.assign(`/arena/${roomUUID}`);
                return;
            }

            $('#are-ready').append(`<h5>${who}</h5>`);
        })
    })
    //sleep(5e3)
    if (!document.querySelectorAll('.lobby-pl').length) {
        //leaveRoom();
    }
}

$('#leave-btn').click(async event => {
    event.preventDefault();
    window.onbeforeunload = undefined;
    await stompClient.disconnect();
    await leaveRoom();
    window.location.assign('/play');
})

$('#launch').click(event => {
    stompClient.send('/ws/launch-message');
})

window.onblur = async () => {
    return;
    const res = await fetch('/api/leave-room', {
        method: 'PUT',
        cache: 'no-cache',
        credentials: 'same-origin'
    });
    if (res.status !== 200) {
        alert('fail to leave lobby');
        return;
    }
    window.location.assign('/play');
}

window.onload = async () => {
    checkRef();
    connect();
}

window.onbeforeunload = () => '';