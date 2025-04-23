const canvas = document.getElementById('game');
const ctx = canvas.getContext('2d');
//each square 60x60
function drawBorder() {
    ctx.fillStyle = 'blue';
    ctx.strokeStyle = 'white';
    ctx.lineWidth = 2;
    for (let i = 0; i < 15; i++) {
        drawBorderedRect(60 * i, 0);
    }
    for (let i = 1; i < 15; i++) {
        drawBorderedRect(0, 60 * i);
    }
    for (let i = 0; i < 15; i++) {
        drawBorderedRect(60 + 60 * i, 840);
    }
    for (let i = 0; i < 15; i++) {
        drawBorderedRect(840, 60 + 60 * i);
    }
}
function drawInnerSquares(){
    ctx.fillStyle = 'blue';
    ctx.strokeStyle = 'white';
    ctx.lineWidth = 2;
    for(let i=0;i<6;i++){
        for(let j=0;j<6;j++){
            drawBorderedRect(120+120*j,120+120*i);
        }
    }
}
function drawBorderedRect(x, y) {
    ctx.fillRect(x, y, 60, 60);
    ctx.strokeRect(x, y, 60, 60);
}
function drawInitialBoard(){
    ctx.clearRect(0,0, canvas.width, canvas.height);
    drawBorder()
    drawInnerSquares()
}
drawInitialBoard()