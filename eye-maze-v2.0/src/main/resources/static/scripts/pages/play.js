import joinRoom from "../api/room/join.js";

$('#join-curr').click(event => {
    event.preventDefault();
    window.location.assign(`/room/${roomUUID}`);
})

$('#create-btn').click(async event => {
    //window.location.assign('/mkroom');
    event.preventDefault();
    const res = await fetch("/api/create-room", {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin'
    });
    const data = await res.json();
    if (data.status > 300) {
        alert('smth went wrong!');
        return;
    }
    window.location.assign(`/room/${data}`);//*/
})

$('#join-btn').click(async event => {
    event.preventDefault();
    const uuid = prompt(`Room's code:`);
    if (!uuid) {
        return;
    }
    await joinRoom(uuid);
})

const roomUUID = document.querySelector('meta[name=currRoomUUID]').content;
if (roomUUID && roomUUID !== 'null') {
    $('#join-curr').css('display', 'block');
}