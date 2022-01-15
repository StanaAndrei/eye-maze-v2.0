const messagesAsStr = document.querySelector('meta[name=messages]').content;
const loginUUID = document.querySelector('meta[name=loginUUID]').content;
const chat = document.querySelector('#chat');

const procTimeStp = timestp => {
    timestp = timestp?.replaceAll('T', ' ');
    return timestp?.substr(0, timestp.indexOf('.'));
}

const addMessage = message => {
    const container = document.createElement('div');
    container.classList.add('container');

    const img = document.createElement('img');
    img.src = message.profilePicB64;
    img.alt = 'profile pic';

    const paragraph = document.createElement('p');

    const span = document.createElement('span');
    span.innerText = procTimeStp(message.timestp);

    const bold = document.createElement('b');

    if (message.senderId === loginUUID) {
        container.classList.add('darker');
        span.classList.add('time-left');
        img.classList.add('right');
        bold.innerText = 'you';
    } else {
        span.classList.add('time-right');
        bold.innerText = message.username;
    }
    paragraph.append(bold);
    paragraph.append(': ' + message.content);
    container.append(img);
    container.append(paragraph);
    container.append(span);
    chat.append(container);
}

window.onload = () => {
    if (messagesAsStr === 'null') {
        return;
    }
    const messages = JSON.parse(messagesAsStr);
    for (const message of messages) {
        addMessage(message);
    }
}

let socket = new SockJS('/our-websocket');
let stompClient;
stompClient = Stomp.over(socket);
stompClient.connect({}, frame => {
    stompClient.subscribe('/topic/global-messages', message => {
        if (!message) {
            return;
        }
        const res = JSON.parse(message.body);
        addMessage(res)
    })
});

document.querySelector('#send').addEventListener('click', async e => {
    e.preventDefault();
    const {value} = document.querySelector('input');
    if (!value) {
        alert(`message cann't be empty!`);
        return;
    }
    stompClient.send('/ws/global-messages', {}, JSON.stringify({
        'buffer': value
    }));
    document.querySelector('input').value = '';
})