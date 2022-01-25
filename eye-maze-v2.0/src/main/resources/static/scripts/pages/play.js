import joinRoom from "../api/room/join.js";

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