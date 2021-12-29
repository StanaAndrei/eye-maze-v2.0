export default class Cell {
    static CELL_W;
    static CELL_H;

    constructor(i, j, walls, hasCoin) {
        this.i = i;
        this.j = j;
        this.walls = walls;
        this.hasCoin = hasCoin;
    }

    highlight(p5context, color) {
        const startX = this.j * Cell.CELL_W;
        const startY = this.i * Cell.CELL_H;
        p5context.noStroke();
        p5context.fill(color);
        p5context.rect(startX, startY, Cell.CELL_W - 3, Cell.CELL_H - 5);
    }

    draw(p5context) {
        const startX = this.j * Cell.CELL_W;
        const startY = this.i * Cell.CELL_H;
        p5context.stroke(255, 255, 255, 200);
        p5context.strokeWeight(4);
        //walls
        if (this.walls[0] === true) {
            p5context.line(startX, startY, startX + Cell.CELL_W, startY);
        }
        if (this.walls[1] === true) {
            p5context.line(startX + Cell.CELL_W, startY, startX + Cell.CELL_W,
                 startY + Cell.CELL_H);//*/
        }
        if (this.walls[2] === true) {
            p5context.line(startX + Cell.CELL_W, startY + Cell.CELL_H, startX,
                 startY + Cell.CELL_H);
        }
        if (this.walls[3] === true) {
            p5context.line(startX, startY + Cell.CELL_H, startX, startY);
        } 
        //cell
            p5context.noStroke();
            p5context.fill(0, 0, 255, 200);//blue
            p5context.rect(startX, startY, Cell.CELL_W, Cell.CELL_H);
        //coin
        if (this.hasCoin) {
            p5context.push();
            p5context.fill(255, 251, 0, 200);
            p5context.strokeWeight(1);
            p5context.circle(
                startX + Cell.CELL_W / 2,
                startY + Cell.CELL_H / 2,
                Cell.CELL_W / 8
            );
            p5context.pop();
        }
    }
}