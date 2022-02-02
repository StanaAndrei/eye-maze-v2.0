import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"

let pastGames = document.querySelector('meta[name=pastGames]').content;
pastGames = JSON.parse(pastGames);

for (let pastGame of pastGames) {
    const stateToCol = new Map();
    stateToCol['WON'] = 'green';
    stateToCol['LOST'] = 'red';
    stateToCol['DRAW'] = '#e6e632';
    $('#matches').append(`
        <li>
            <a
                style="color: ${stateToCol[pastGame.state]}"
                href="/past-game/${pastGame.code}"
            >${pastGame.code}</a>
        </li>
    `)
}