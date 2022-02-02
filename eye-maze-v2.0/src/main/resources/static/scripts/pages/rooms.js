import joinRoom from "../api/room/join.js";

console.warn('pressssss');
$('.room').click(async event => {
    event.preventDefault();
    const code = event.target.id;
    await joinRoom(code);
})

function checkRef() {
    if (!document.referrer.startsWith(document.location.origin)) {
        window.location.assign('/play');
    }
}