const getPointSide = (x1, y1, x2, y2, x, y) => {
    if (y1 > y2) {
        [x1, x2] = [x2, x1];
        [y1, y2] = [y2, y1];
    }
    let dx = Math.abs(x2 - x1);
    let dy = Math.abs(y2 - y1);
    let mx = Math.abs(x - x1);
    let my = Math.abs(y - y1);
    let cross = (dx * my - dy * mx);
    if (cross === 0) {
        return 0;
    }
    let below = cross > 0;
    if (dx) {
        if (dy / dx < 0) {
            below = !below;
        }
    }
    return below ? -1 : 1;
}

export const getDial = (i, j, { width, height }) => {
    let sideBesideFi = getPointSide(0, 0, width, height, i, j);
    let sideBesideSe = getPointSide(width, 0, 0, height, i, j);
    if (sideBesideFi === 1) {//over main diag
        if (sideBesideSe === 1) {//over sec diag
            return 'UP';
        } else {
            return 'RIGHT';
        }
    } else {
        if (sideBesideSe === 1) {//over sec diag
            return 'LEFT';
        } else {
            return 'DOWN';
        }
    }
}