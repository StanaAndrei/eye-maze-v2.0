import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import "https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.js"
import createRoom from "../api/room/create.js";

let isPublic = false;
const confirmOptions = {
    title: 'LOBBY ACCESS',
    content: 'Make lobby public?',
    buttons: {
        yes: function () {
            isPublic = true;
        },
        no: function () {
            isPublic = false;
        },
    }
};

$('#def').click(async event => {
    event.preventDefault();
    await createRoom(isPublic);
})

$('form').submit(async event => {
    event.preventDefault();
    const mazeParams = {
        nrLines: Number($('form > #lines').val()),
        nrCols: Number($('form > #cols').val()),
    };
    await createRoom(isPublic, mazeParams);
})

$('#imp').click(async event => {
    event.preventDefault();
    const mzName = prompt('name of the maze you want to import:');
    if (!mzName) {
        return;
    }
    try {
        await createRoom(isPublic, null, mzName);
    } catch (e) {
        alert('smth went wrong! check if the name of maze is correct!');
    }
})


window.onload = () => $.confirm(confirmOptions);