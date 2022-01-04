import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"

$('#def').click(async event => {
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

$('form').submit(async event => {
    event.preventDefault();
    const mazeParams = {
        nrLines: Number($('form > #lines').val()),
        nrCols: Number($('form > #cols').val()),
    };
    const res = await fetch("/api/create-room", {
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
})