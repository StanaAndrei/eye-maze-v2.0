import "https://cdn.jsdelivr.net/npm/p5@1.4.0/lib/p5.js"
import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
import Cell from "./../core/entities/cell.js";
let cells, start, finish;
let n, m;
let mzform = document.querySelector('meta[name=mzform]').content;
//const exists = document.querySelector('meta[name=exists]').content;

const fidim = () => {
    if (mzform !== "TO_BE_CREATED") {
        mzform = JSON.parse(mzform);
    } else {
        mzform = null;
    }

    if (!mzform) {
        n = Number($('#lines').val()), m = Number($('#cols').val());
        cells = new Array(n).fill().map(() => new Array(m));
        for (let i = 0; i < n; i++) {
            for (let j = 0; j < m; j++) {
                cells[i][j] = new Cell(i, j, new Array(4).fill(true), false);
            }
        }
    } else {
        n = mzform.nrLines, m = mzform.nrCols;
        $('#lines').val(n), $('#cols').val(m);
        cells = new Array(n).fill().map(() => new Array(m));
        
        for (let i = 0; i < mzform.cells.length; i++) {
            mzform.cells[i] = JSON.parse(mzform.cells[i]);
        }

        for (let i = 0, nr = 0; i < n; i++) {
            for (let j = 0; j < m; j++) {
                const cell = mzform.cells[nr++];
                cells[i][j] = new Cell(cell.line, cell.col, cell.walls, cell.hasCoin);
            }
        }
        start = mzform.start;
        finish = mzform.finish;
    }

}

const redim = () => {
    n = Number($('#lines').val()), m = Number($('#cols').val());
    let aux = new Array(n).fill().map(() => new Array(m));
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < m; j++) {
            if (i < cells.length && j < cells[0].length) {
                aux[i][j] = cells[i][j];
            } else {
                aux[i][j] = new Cell(i, j, new Array(4).fill(true), false);
            }
        }
    }
    cells = aux;
}

const procCells = (selected, wallState) => {
    for (let i = 1; i < selected.length; i++) {
        const last = selected[i - 1], curr = selected[i];
        if (curr.line - last.line > 0) {
            cells[curr.line][curr.col].walls[0] = wallState;
            cells[last.line][last.col].walls[2] = wallState;
        } else if (curr.line - last.line < 0) {
            cells[curr.line][curr.col].walls[2] = wallState;
            cells[last.line][last.col].walls[0] = wallState;
        } else if (curr.col - last.col > 0) {
            cells[curr.line][curr.col].walls[3] = wallState;
            cells[last.line][last.col].walls[1] = wallState;
        } else if (curr.col - last.col < 0) {
            cells[curr.line][curr.col].walls[1] = wallState;
            cells[last.line][last.col].walls[3] = wallState;
        }
    }
}

let selected = [], wallAction = 'rem', toPlace = 'coin';
$('.radio').click(e => {
    const formElem = $('#radios')[0];
    const formData = new FormData(formElem);
    wallAction = formData.get('wall_act');
})

$('.radio2').click(e => {
    const formElem = $('#radios2')[0];
    const formData = new FormData(formElem);
    toPlace = formData.get('to_place');
})

$('#submit-maze').click(async e => {
    e.preventDefault();
    const res = await fetch('/api/upload-maze', {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: $('#mzname').val(),
            nrLines: n,
            nrCols: m,
            mazeCells: [].concat(...cells),
            start, finish
        })
    });
    if (res.status === 200) {
        window.location.assign('/my-mazes');//*/
    } else {
        alert('error');
    }
})

$('#ch-dim').click(e => {
    if (Number($('#lines').val()) * Number($('#cols').val()) > 22 * 22) {
        alert('error: maze too large');
        return;
    }
    redim();
})

const initP5 = p5context => {
    p5context.setup = () => {
        if (mzform === '') {
            document.body.style.display = "none";
            alert('The maze does not exists!');
            window.location.assign('/mkmaze');
            return;
        }
        p5context.createCanvas(window.innerWidth * 3 / 4, window.innerHeight);
        fidim();
        start = { line: 0, col: 0 };
        finish = { line: n - 1, col: m - 1 };
    }

    p5context.draw = () => {
        p5context.background('black');

        const { mouseX, mouseY } = p5context;
        Cell.CELL_W = p5context.width / m;
        Cell.CELL_H = p5context.height / n;
        for (let i = 0; i < n; i++) {
            for (let j = 0; j < m; j++) {
                cells[i][j].draw(p5context);
                if (Math.floor(mouseX / Cell.CELL_W) === j && Math.floor(mouseY / Cell.CELL_H) === i) {
                    cells[i][j].highlight(p5context, p5context.color(0, 0, 0, 200));
                }
                if (start && i === start.line && j === start.col) {
                    cells[i][j].highlight(p5context, p5context.color(138, 43, 226, 175));
                }
                if (finish && i === finish.line && j === finish.col) {
                    cells[i][j].highlight(p5context, p5context.color(56, 94, 0, 175));
                }
            }
        }

        if (p5context.mouseIsPressed) {
            const col = Math.floor(mouseX / Cell.CELL_W), line = Math.floor(mouseY / Cell.CELL_H);
            if (line >= n || col >= m || line < 0 || col < 0) {
                return;
            }
            const obj = { line, col }, last = selected.slice(-1)[0];
            if (JSON.stringify(obj) !== JSON.stringify(last)) {
                selected.push(obj);
                if (selected.length > 1) {
                    procCells(selected, Boolean(wallAction === 'add'));
                    selected.shift();
                }
            }
        } else {
            selected.length = 0;
        }
    }

    p5context.doubleClicked = () => {
        const { mouseX, mouseY } = p5context;
        const col = Math.floor(mouseX / Cell.CELL_W), line = Math.floor(mouseY / Cell.CELL_H);
        if (line >= n || col >= m) {
            return;
        }
        if (toPlace === 'coin') {
            cells[line][col].hasCoin ^= true;
        } else if (toPlace === 'start') {
            if (JSON.stringify({ line, col }) !== JSON.stringify(finish)) {
                start = { line, col };
            }
        } else {//end
            if (JSON.stringify({ line, col }) !== JSON.stringify(start)) {
                finish = { line, col };
            }
        }
    }
}

new p5(initP5, $('#maze')[0]);//*/