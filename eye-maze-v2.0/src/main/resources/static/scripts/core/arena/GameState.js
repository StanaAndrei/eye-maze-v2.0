import Cell from "../entities/cell.js";

export default class GameState {
    static cells;
    static n;
    static m;
    static players;
    static finish;
    static setState(state) {
        //NR_COLS,,,,,NR_ROWS,,,,CELL_H,,,,CELL_W
        this.n = state.NR_ROWS;
        this.m = state.NR_COLS;
        this.cells = new Array(this.n).fill().map(() => new Array(this.m));
        for (let cell of state.mazeState) {
            const { line, col, hasCoin, walls } = cell;
            this.cells[line][col] = new Cell(line, col, walls, hasCoin);
        }
        this.players = state.players;
        this.finish = state.finish;
    }

    static draw(p5context, myProfilePic, otherProfilePic) {
        Cell.CELL_W = p5context.width / this.m;
        Cell.CELL_H = p5context.height / this.n;
        const loginUUID = document.querySelector('meta[name=loginUUID]').content;
        for (let i = 0; i < this.n; i++) {
            for (let j = 0; j < this.m; j++) {
                this.cells[i][j].draw(p5context);
                for (let player of this.players) {
                    const { line, col, plUUID } = player;
                    if (i === line && j === col) {
                        const texture = plUUID === loginUUID ? myProfilePic : otherProfilePic;
                        const op = plUUID === loginUUID ? 255 : 200;
                        this.cells[i][j].highlight(p5context, texture, op);
                    }
                    if (i === this.finish.line && j === this.finish.col) {
                        this.cells[i][j].highlight(p5context, p5context.color(56, 94, 0));
                    }
                }
            }
        }
    }
}