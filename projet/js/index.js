let map;
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

    for(var i=0; i<height; i++){
        map[i] = new Array(width);
        for(var j=0; j<width; j++){
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
    map[P1x][P1y]='P';
    map[P2x][P2y]='Q';
    return map;
}

async function drawPlayer(map){
    var i = 0;
    while(true){
        drawMap(map);
        i++;
        drawImage("player1_"+((i%14)+1), 43, 38);
        await sleep(300);
    }
}

async function drawMap(){
    for(var i= 0; i < map.length; i++) {
        for(var j= 0; j < map[i].length; j++) {
            if(map[i][j]=='X'){
               drawImage("block_incassable", i*43, j*38);
            } else if(map[i][j]=='O'){
                drawImage("block_cassable", i*43, j*38);
            } else if(map[i][j]==' '){
                drawImage("case", i*43, j*38);
            } else if(map[i][j]=='P'){
                drawImage("case", i*43, j*38);
                drawImage("player1_1", i*43, j*38);
            } else if(map[i][j]=='Q'){
                drawImage("case", i*43, j*38);
                drawImage("player2", i*43, j*38);
            }
        }
    }
    //drawImage("player2", 9*43, 11*38);
}

function checkKey(e) {
    e = e || window.event;

    if (e.keyCode == '38') {
        // up arrow
        if(P1y>1 && map[P1x][P1y-1]==' '){
            map[P1x][P1y]=' ';
            P1y=P1y-1;
            map[P1x][P1y]='P';
        }
    }
    else if (e.keyCode == '40') {
        // down arrow
        if(P1y<width-2 && map[P1x][P1y+1]==' '){
            map[P1x][P1y]=' ';
            P1y=P1y+1;
            map[P1x][P1y]='P';
        }
    }
    else if (e.keyCode == '37') {
        // left arrow
        if(P1x>1 && map[P1x-1][P1y]==' '){
            map[P1x][P1y]=' ';
            P1x=P1x-1;
            map[P1x][P1y]='P';
        }
    }
    else if (e.keyCode == '39') {
        // right arrow
        if(P1x<height-2 && map[P1x+1][P1y]==' '){
            map[P1x][P1y]=' ';
            P1x=P1x+1;
            map[P1x][P1y]='P';
            
        }
    }

    drawMap();
}


window.addEventListener("load", function (event) {
    document.onkeydown = checkKey;
    randomMap(20);
    drawMap();
    //drawPlayer(map)
});