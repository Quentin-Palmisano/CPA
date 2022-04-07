class Player {
    constructor(x, y) {
      this.x = x;
      this.y = y;
      this.dir = 0;
      this.lives = 3;
      this.nb_bombe = 1;
      this.nb_bombe_pose = 0;
      this.puissance = 1;
    }
}

let map;
let bombes;
let explosion;
let powerup;
let percent = 60;
let height=11;
let width=13;
let tile_height=38;
let tile_width=38;
const player1 = new Player(1,1);
const player2 = new Player(height-2, width-2);
let canvas = document.getElementById("myCanvas");
let context = canvas.getContext("2d");
let start = null;

function step() {
    drawMap();
    requestAnimationFrame(step);
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

function drawImage(nom, x, y) {
    var img = document.getElementById(nom);
    context.drawImage(img, y*tile_height, x*tile_width);
}

function drawText(text, x, y){
context.font = "30px Arial";
context.fillText(text, x, y);
}

function randomMap(){
    map = new Array(height);
    bombes = new Array(height);
    explosion = new Array(height);
    powerup = new Array(height);
    for(var i=0; i<height; i++){
        map[i] = new Array(width);
        bombes[i] = new Array(width);
        explosion[i] = new Array(width);
        powerup[i] = new Array(width);
        for(var j=0; j<width; j++){
            bombes[i][j] = 0;
            explosion[i][j] = 0;
            powerup[i][j] = 0;
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
                    if(rand<percent){
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

function drawMap(){
    p1 = document.getElementById("player1");
    p1.innerHTML = "player 1 : "+player1.lives+" (vie)";
    p2 = document.getElementById("player2");
    p2.innerHTML = "player 2 : "+player2.lives+" (vie)";
    nbb1 = document.getElementById("nb_bombe1");
    nbb1.innerHTML = "NB bombes : "+player1.nb_bombe;
    nbb2 = document.getElementById("nb_bombe2");
    nbb2.innerHTML = "NB bombes : "+player2.nb_bombe;
    nbbp1 = document.getElementById("nb_bombe1p");
    nbbp1.innerHTML = "NB bombes posées : "+player1.nb_bombe_pose;
    nbbp2 = document.getElementById("nb_bombe2p");
    nbbp2.innerHTML = "NB bombes posées : "+player2.nb_bombe_pose;
    pow1 = document.getElementById("puissance1");
    pow1.innerHTML = "puissance : "+player1.puissance;
    pow2 = document.getElementById("puissance2");
    pow2.innerHTML = "puissance : "+player2.puissance;
    
    for(var i= 0; i < map.length; i++) {
        for(var j= 0; j < map[i].length; j++) {
            if(map[i][j]=='X'){
               drawImage("block_incassable", i, j);
            } else if(map[i][j]=='O'){
                drawImage("block_cassable", i, j);
            } else if(map[i][j]==' '){
                drawImage("case", i, j);
            }
            if(explosion[i][j]==1){
                drawImage("fire4_center", i, j);
            } else if(explosion[i][j]==2){
                drawImage("fire4_left_middle", i, j);
            } else if(explosion[i][j]==3){
                drawImage("fire4_left_left", i, j);
            } else if(explosion[i][j]==4){
                drawImage("fire4_right_middle", i, j);
            } else if(explosion[i][j]==5){
                drawImage("fire4_right_right", i, j);
            } else if(explosion[i][j]==6){
                drawImage("fire4_up_middle", i, j);
            } else if(explosion[i][j]==7){
                drawImage("fire4_up_top", i, j);
            } else if(explosion[i][j]==8){
                drawImage("fire4_down_middle", i, j);
            } else if(explosion[i][j]==9){
                drawImage("fire4_down_bottom", i, j);
            }
            if(bombes[i][j]==1){
                drawImage("bombe1", i, j);
            } else if(bombes[i][j]==2){
                drawImage("bombe2", i, j);
            } else if(bombes[i][j]==3){
                drawImage("bombe3", i, j);
            }
            if(powerup[i][j]==1){
                drawImage("bombe+", i, j);
            } else if(powerup[i][j]==2){
                drawImage("fire+", i, j);
            }
            if(player1.x==i&& player1.y==j){
                drawImage("player1_1", i, j);
            }
            if(player2.x==i && player2.y==j){
                drawImage("player2_1", i, j);
            }
        }
    }
}

function randompowerup(px, py){
    var rand = Math.floor(Math.random() * 100);
    map[px][py] = ' ';
    if(rand<25){
        powerup[px][py] = 1;
    }else if(rand<50){
        powerup[px][py] = 2;
    }
}

function explose_bis(x,y,dx,dy,pow, middle, end, b){
    for(var i=1; i<=pow; i++){
        let px = x+i*dx;
        let py = y+i*dy;
        if(!b && explosion[px][py]==0)break;
        if(map[px][py]==' '){
            if(b){
                explosion[px][py] = (pow==i ? end : middle);
                if(player1.x==px && player1.y==py) player1.lives=player1.lives-1;
                if(player2.x==px && player2.y==py) player2.lives=player2.lives-1;
                powerup[px][py]=0;
            }else{
                explosion[px][py] = 0;
            }
        } else if(map[px][py] == "O") {
            if(b){
                randompowerup(px, py);
                explosion[px][py] = end;
            }else{
                explosion[px][py] = 0;
            }
            break;
        } else {
            break;
        }
    }
}

async function explose(x, y, pow){
    if(map[x][y]==' '){
        explosion[x][y]=1;
    }
    
    if(player1.x==x && player1.y==y) player1.lives=player1.lives-1;
    if(player2.x==x && player2.y==y) player2.lives=player2.lives-1;
    explose_bis(x,y,0,-1,pow,2,3,true);
    explose_bis(x,y,0,1,pow,4,5,true);
    explose_bis(x,y,-1,0,pow,6,7,true);
    explose_bis(x,y,1,0,pow,8,9,true);

    await sleep(500);
    explosion[x][y]=0;

    explose_bis(x,y,0,-1,pow,2,3,false);
    explose_bis(x,y,0,1,pow,4,5,false);
    explose_bis(x,y,-1,0,pow,6,7,false);
    explose_bis(x,y,1,0,pow,8,9,false);
}

async function putbombe(x, y, pow, player){
    if(player.nb_bombe_pose<player.nb_bombe && bombes[x][y]==0){
        player.nb_bombe_pose=player.nb_bombe_pose+1;
        bombes[x][y]=1;
        await sleep(500);
        bombes[x][y]=2;
        await sleep(500);
        bombes[x][y]=3;
        await sleep(500);
        bombes[x][y]=1;
        await sleep(500);
        bombes[x][y]=2;
        await sleep(500);
        bombes[x][y]=3;
        await sleep(500);
        bombes[x][y]=0;
        player.nb_bombe_pose=player.nb_bombe_pose-1;
        explose(x, y, pow);
    }
}

function checkpowerup(player){
    if(powerup[player.x][player.y]==1)player.nb_bombe=player.nb_bombe+1;
    if(powerup[player.x][player.y]==2)player.puissance=player.puissance+1;
    powerup[player.x][player.y]=0;
}

function checkKey(e) {
    e = e || window.event;

    if (e.keyCode == '38') {
        // up arrow
        if(player1.x>1 && map[player1.x-1][player1.y]==' ' && bombes[player1.x-1][player1.y]==0){
            player1.x=player1.x-1;
            checkpowerup(player1);
        }
    }
    else if (e.keyCode == '40') {
        // down arrow
        if(player1.x<height-2 && map[player1.x+1][player1.y]==' ' && bombes[player1.x+1][player1.y]==0){
            player1.x=player1.x+1;
            checkpowerup(player1);
        }
    }
    else if (e.keyCode == '37') {
        // left arrow
        if(player1.y>1 && map[player1.x][player1.y-1]==' ' && bombes[player1.x][player1.y-1]==0){
            player1.y=player1.y-1;
            checkpowerup(player1);
        }
    }
    else if (e.keyCode == '39') {
        // right arrow
        if(player1.y<width-2 && map[player1.x][player1.y+1]==' ' && bombes[player1.x][player1.y+1]==0){
            player1.y=player1.y+1;
            checkpowerup(player1);
        }
    }else if (e.keyCode == '96') {
        // 0 numpad
        putbombe(player1.x, player1.y, player1.puissance, player1);
    }else if (e.keyCode == '90') {
        // Z
        if(player2.x>1 && map[player2.x-1][player2.y]==' ' && bombes[player2.x-1][player2.y]==0){
            player2.x=player2.x-1;
            checkpowerup(player2);
        }
    }else if (e.keyCode == '83') {
        // S
        if(player2.x<height-2 && map[player2.x+1][player2.y]==' ' && bombes[player2.x+1][player2.y]==0){
            player2.x=player2.x+1;
            checkpowerup(player2);
        }
    }else if (e.keyCode == '81') {
        // Q
        if(player2.y>1 && map[player2.x][player2.y-1]==' ' && bombes[player2.x][player2.y-1]==0){
            player2.y=player2.y-1;
            checkpowerup(player2);
        }
    }else if (e.keyCode == '68') {
        // D
        if(player2.y<width-2 && map[player2.x][player2.y+1]==' ' && bombes[player2.x][player2.y+1]==0){
            player2.y=player2.y+1;
            checkpowerup(player2);
        }
    }else if (e.keyCode == '32') {
        // space
        putbombe(player2.x, player2.y, player2.puissance, player2);
    }
}


window.addEventListener("load", function (event) {
    canvas.width=width*tile_width;
    canvas.height=height*tile_height;
    document.onkeydown = checkKey;
    randomMap();
    requestAnimationFrame(step);
});