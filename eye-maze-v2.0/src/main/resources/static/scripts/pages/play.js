import joinRoom from "../api/room/join.js";

$('#join-curr').click(event => {
    event.preventDefault();
    window.location.assign(`/room/${roomUUID}`);
})

$('#create-btn').click(async event => {
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

const roomUUID = document.querySelector('meta[name=currRoomUUID]').content;
if (roomUUID && roomUUID !== 'null') {
    $('#join-curr').css('display', 'block');
}