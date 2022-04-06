let map;
let bombes;
let height=11;
let width=13;
let P1x=1;
let P1y=1;
let P2x=height-2;
let P2y=width-2;

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

function drawImage(nom, x, y) {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    var img = document.getElementById(nom);
    ctx.drawImage(img, x, y);
}

function drawText(text, x, y){
var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");
ctx.font = "30px Arial";
ctx.fillText(text, x, y);
}

function randomMap(x){
    map = new Array(height);
    bombes = new Array(height);
    for(var i=0; i<height; i++){
        map[i] = new Array(width);
        bombes[i] = new Array(width);
        for(var j=0; j<width; j++){
            bombes[i][j] = false;
            if(j==0 || j==width-1 || i==0 || i==height-1){
                map[i][j] = 'X';
            } else if((i==1 && (j==1||j==2||j==width-3||j==width-2))
            ||(i==2 && (j==1||j==width-2))
            ||(i==height-2 && (j==1||j==2||j==width-3||j==width-2))
            ||(i==height-3 && (j==1||j==width-2))){
                map[i][j] = ' ';

            }else{
                if(i%2==0 && j%2==0){ 
                    map[i][j] = 'X';
                } else {
                    var rand = Math.floor(Math.random() * 100);
                    if(rand<x){
                        map[i][j] = 'O';
                    }else{
                        map[i][j] = ' ';
                    }
                }   
            }
        }
        
    }
    return map;
}

async function drawPlayer(map){
    var i = 0;
    while(true){
        drawMap(map);
        i++;
        drawImage("player1_"+((i%14)+1), 0, 0);
        await sleep(300);
    }
}

function drawMap(){
    
    for(var i= 0; i < map.length; i++) {
        for(var j= 0; j < map[i].length; j++) {
            if(map[i][j]=='X'){
               drawImage("block_incassable", i*43, j*38);
            } else if(map[i][j]=='O'){
                drawImage("block_cassable", i*43, j*38);
            } else if(map[i][j]==' '){
                drawImage("case", i*43, j*38);
            }
            if(bombes[i][j]==1){
                drawImage("bombe1", i*43, j*38);
            } else if(bombes[i][j]==2){
                drawImage("bombe2", i*43, j*38);
            } else if(bombes[i][j]==3){
                drawImage("bombe3", i*43, j*38);
            }
            if(P1x==i&& P1y==j){
                drawImage("player1_1", i*43, j*38);
            }
            if(P2x==i && P2y==j){
                drawImage("player2", i*43, j*38);
            }
        }
    }
    //drawImage("player2", 9*43, 11*38);
}

async function putbombe(x, y){
    bombes[x][y]=1;
    await sleep(1000);
    bombes[x][y]=2;
    await sleep(1000);
    bombes[x][y]=3;
    await sleep(1000);
    bombes[x][y]=0;

}

function checkKey(e) {
    e = e || window.event;

    if (e.keyCode == '38') {
        // up arrow
        if(P1y>1 && map[P1x][P1y-1]==' '){
            P1y=P1y-1;
        }
    }
    else if (e.keyCode == '40') {
        // down arrow
        if(P1y<width-2 && map[P1x][P1y+1]==' '){
            P1y=P1y+1;
        }
    }
    else if (e.keyCode == '37') {
        // left arrow
        if(P1x>1 && map[P1x-1][P1y]==' '){
            P1x=P1x-1;
        }
    }
    else if (e.keyCode == '39') {
        // right arrow
        if(P1x<height-2 && map[P1x+1][P1y]==' '){
            P1x=P1x+1;
        }
    }else if (e.keyCode == '96') {
        // 0 numpad
        putbombe(P2x, P2y);
    }else if (e.keyCode == '90') {
        // Z
        if(P2y>1 && map[P2x][P2y-1]==' '){
            P2y=P2y-1;
        }
    }else if (e.keyCode == '83') {
        // S
        if(P2y<width-2 && map[P2x][P2y+1]==' '){
            P2y=P2y+1;
        }
    }else if (e.keyCode == '81') {
        // Q
        if(P2x>1 && map[P2x-1][P2y]==' '){
            P2x=P2x-1;
        }
    }else if (e.keyCode == '68') {
        // D
        if(P2x<height-2 && map[P2x+1][P2y]==' '){
            P2x=P2x+1;
        }
    }else if (e.keyCode == '32') {
        // space
        putbombe(P2x, P2y);
    }
    drawMap();
}


window.addEventListener("load", function (event) {
    document.onkeydown = checkKey;
    randomMap(20);
    drawMap();
    //drawPlayer(map)
});