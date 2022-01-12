import joinRoom from "../api/room/join.js";
import leaveRoom from "../api/room/leave.js";

$('li').click(async event => {
    event.preventDefault();
    const code = event.target.id;
    await joinRoom(code);
})

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

    function showMessage(from, message) {
        $('#message-history').append(`<p><b>${from}:</b> ${message}</p>`);
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
            'messageContent': message
        }));
        $('#message-cont').val('');
    })

    function connect() {
        let socket = new SockJS('/our-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
            //console.log('connected:' + frame);
            stompClient.subscribe('/user/topic/room-messages', message => {
                const { from, content } = JSON.parse(message.body);
                showMessage(from, content);
            })

            stompClient.subscribe('/user/topic/launch-message', message => {
                if (!message) {
                    return;
                }
                const { who, state } = JSON.parse(message.body);
                const roomUUID = '[[${session.currRoomUUID}]]';
                if (state === 'LAUNCH') {
                    window.onbeforeunload = undefined;
                    stompClient.disconnect(() => {}, {});
                    sessionStorage.wMouse = (new URLSearchParams(window.location.search)).has('wmouse');
                    window.location.assign(`/arena/${roomUUID}`);
                    return;
                }

                $('#are-ready').append(`<h5>${who}</h5>`);
            })
        })
    }

    $('#leave-btn').click(async event => {
        event.preventDefault();
        await leaveRoom();
    })

    $('#launch').click(event => {
        stompClient.send('/ws/launch-message');
    })

    window.onblur = async () => {
        //await leaveRoom();
    }