import joinRoom from "../api/join.js";

$('#join-curr').click(event => {
    event.preventDefault();
    window.location.assign(`/room/${roomUUID}`);
})

$('#create-btn').click(async event => {
    console.log('asd');
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