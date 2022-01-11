export default async function createRoom(mazeParams = null, param = '') {
    const res = await fetch(`/api/create-room?${param}`, {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(mazeParams)
    });
    const data = await res.json();
    if (data.status > 300) {
        alert('smth went wrong!');
        return;
    }
    window.location.assign(`/room/${data}`);//*/
}