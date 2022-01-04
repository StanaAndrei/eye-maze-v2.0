export default async function leaveRoom() {
    const res = await fetch('/api/leave-room', {
        method: 'PUT',
        cache: 'no-cache',
        credentials: 'same-origin'
    });
    if (res.status !== 200) {
        alert('fail to leave lobby');
    } else {
        window.location.assign('/play');
    }
}