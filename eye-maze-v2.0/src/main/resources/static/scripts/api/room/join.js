export default async function joinRoom(uuid) {
    const res = await fetch(`/api/join-room/${uuid}`, {
        method: 'PUT',
        cache: 'no-cache',
        credentials: 'same-origin'
    });
    if (res.status === 200) {
        window.location.assign(`/room/${uuid}`);
    } else if (res.status === 409) {
        alert('lobby is full!');
        window.location.reload();
    } else {
        alert(`lobby doesn't exist!`);
        window.location.reload();
    }
}