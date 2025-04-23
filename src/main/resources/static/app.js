const canvas = document.getElementById('game');
const ctx = canvas.getContext('2d');

function drawInitialBoard(){
    ctx.clearReact(0,0, canvas.width, canvas.height);
    ctx.fillStyle = 'blue';
    ctx.fillRect(50,50,40,40);
}