import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import createRoom from "../api/room/create.js";

$('#def').click(async event => {
    event.preventDefault();
    await createRoom();
})

$('form').submit(async event => {
    event.preventDefault();
    const mazeParams = {
        nrLines: Number($('form > #lines').val()),
        nrCols: Number($('form > #cols').val()),
    };
    await createRoom(mazeParams);
})

$('#imp').click(async event => {
    event.preventDefault();
    const mzName = prompt('name of the maze you want to import:');
    if (!mzName) {
        return;
    }
    try {
        await createRoom(null, `mz-name=${mzName}`);
    } catch (e) {
        alert('smth went wrong! check if the name of maze is correct!');
    }
})