export default async function createRoom(isPublic, mazeParams = null, mzName = '') {
    const res = await fetch(`/api/create-room?is-public=${isPublic}&mz-name=${mzName}`, {
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
        throw new Error();
    }
    window.location.assign(`/room/${data}`);//*/
}