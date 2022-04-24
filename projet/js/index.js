//pourcentage de block cassable sur la map
let percent = 60;
//hauteur de la map
let height = 11;
//largeur de la map
let width = 13;
//hauteur d'une image
let tile_height = 50;
//largeur d'une image
let tile_width = 50;
//nombre maximum de powerup par joueur
let max_bombe = 10;
let max_puissance = 10;
let max_speed = 5;
//nombre de vie des joueurs
let nb_lives = 3;
//barre de chargement
let chargement = true;

let gray = "#1d1d1d";

let canvas = document.getElementById("myCanvas");
let context = canvas.getContext("2d");

const menu = document.getElementById("menu");
const game = document.getElementById("game");
const load = document.getElementById("load");
const loadbar = document.getElementById("loadbar");
const play = document.getElementById("play");
const won = document.getElementById("won");
const parambutton = document.getElementById("parambutton");
const param = document.getElementById("param");
const valider = document.getElementById("valider");

let start = null;

class Player {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.px = tile_height * x;
        this.py = tile_width * y;
        this.dx = 0;
        this.dy = 0;
        this.is_moving = false;
        this.reset_anim = 0;
        this.lives = nb_lives;
        this.dead = false;
        this.won = false;
        this.nb_bombe = 1;
        this.nb_bombe_pose = 0;
        this.puissance = 1;
        this.speed = 1;
        this.up = false;
        this.down = false;
        this.right = false;
        this.left = false;
    }
}
//tableau des elements mur, block cassable, sol
let map;
//tableau des bombes en train d'explosées
let bombes;
//tableau des flammes de l'explosion
let explosion;
//tableau des power up
let powerup;

let player1 = new Player(1, 1);
let player2 = new Player(height - 2, width - 2);

let kill = false;

async function loading(b){
    load.style.display = "block";
    if(b){
        for(var i=1; i<=100; i++){
            var rand = Math.floor(Math.random() * 100);
            loadbar.value = i;
            if(rand<10) await sleep(100*rand);
            if(rand>90) i = i+100-rand;
            await sleep(50);
        }
    }

    load.style.display = "none";
    menu.style.display = "none";
    game.style.display = "block";
}

play.onclick = function () {
    launch();
    loading(chargement);
};

parambutton.onclick = function () {
    menu.style.display = "none";
    param.style.display = "block";
};

valider.onclick = function () {
    menu.style.display = "block";
    param.style.display = "none";
    var form = document.getElementById("parametre");
    var formData = new FormData(form);
    if(formData.get("lives")!=null && formData.get("lives")>0) nb_lives = formData.get("lives");
    if(formData.get("height")!=null && formData.get("height")>4) height = formData.get("height");
    if(formData.get("width")!=null && formData.get("width")>4) width = formData.get("width");
    if(formData.get("max_speed")!=null && formData.get("max_speed")>0) max_speed = formData.get("max_speed");
    if(formData.get("max_bombe")!=null && formData.get("max_bombe")>0) max_bombe = formData.get("max_bombe");
    if(formData.get("max_fire")!=null && formData.get("max_fire")>0) max_puissance = formData.get("max_fire");
    if(formData.get("percent")!=null && formData.get("percent")>0) percent = formData.get("percent");
    if(formData.get("load")==null){
        chargement = true;  
    } else{
        chargement = false;
    }
};

async function step() {
    if(player1.won || player2.won){
        if(player1.won && player2.won){
            drawWon("match nul");
        }else{
            drawWon(player1.won ? "player 1" : "player 2");
        }
        await sleep(5000);
        won.style.display = "none";
        game.style.display = "none";
        menu.style.display = "block";
        return;
    }
    drawMap();
    requestAnimationFrame(step);
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function restart(){
    await sleep(3000);
    kill = true;
    await sleep(3000);
    kill = false;
    randomMap();
    resetPlayer();
    changeAnime(300, player1);
    changeAnime(300, player2);
}

function initialPosition(){
    player1.x=1;
    player1.y=1;
    player1.px=tile_height;
    player1.py=tile_width;
    player2.x=height-2;
    player2.y=width-2;
    player2.px=tile_height*player2.x;
    player2.py=tile_width*player2.y;
}

function resetPlayer(){
    var p1 = player1.lives;
    var p2 = player2.lives;
    player1 = new Player(1, 1);
    player2 = new Player(height - 2, width - 2);
    player1.lives=p1;
    player2.lives=p2;
}

async function changeAnime(ms, player) {
    if(player.dead){
        player.reset_anim = 0;
        await sleep(200);
        player.reset_anim = 1;
        await sleep(200);
        changeAnime(ms, player);
    }else{
        player.reset_anim = 1;
        await sleep(ms - player.speed * 10);
        player.reset_anim = 2;
        await sleep(ms - player.speed * 10);
        player.reset_anim = 0;
        await sleep(ms - player.speed * 10);
        changeAnime(ms, player);
    }
    
}

function drawWon(player){
    drawColor(gray);
    won.style.display = "block";
    won.innerHTML = player + " won !";
}

function drawColor(color){
    context.beginPath();
    context.rect(0, 0, width*tile_width, height*tile_height);
    context.fillStyle = color;
    context.fill();
}

function drawInGrid(nom, x, y) {
    var img = document.getElementById(nom);
    context.drawImage(img, y * tile_height, x * tile_width, tile_height, tile_width);
}
function drawByPixel(nom, x, y) {
    var img = document.getElementById(nom);
    context.drawImage(img, y, x, tile_height, tile_width);
}

function drawAnime(nom, x, y, player, k) {
    if(player.dead){
        if (player.reset_anim == 0) {
            drawByPixel("player_dead", x, y);
        } else if (player.reset_anim == 1) {
            drawByPixel(nom + 2, x, y);
        }
    }else{
        var i = player.reset_anim + k;
        if (player.reset_anim == 0) {
            drawByPixel(nom + i, x, y);
        } else if (player.reset_anim == 1) {
            drawByPixel(nom + i, x, y);
        } else if (player.reset_anim == 2) {
            drawByPixel(nom + i, x, y);
        }
    }
    
}

function drawPlayer(nom, x, y, player) {
    if(player.dead){
        drawAnime(nom, x, y, player, -1);
    }else{
        if (player.dx == -1) {
            drawAnime(nom, x, y, player, 7);
        } else if (player.dx == 1) {
            drawAnime(nom, x, y, player, 1);
        } else if (player.dy == -1) {
            drawAnime(nom, x, y, player, 4);
        } else if (player.dy == 1) {
            drawAnime(nom, x, y, player, 12);
        } else {
            drawByPixel(nom + "2", x, y);
        }
    }
    
}

function drawText(text, x, y) {
    context.font = "30px Arial";
    context.fillText(text, x, y);
}

function randomMap() {
    map = new Array(height);
    bombes = new Array(height);
    explosion = new Array(height);
    powerup = new Array(height);
    for (var i = 0; i < height; i++) {
        map[i] = new Array(width);
        bombes[i] = new Array(width);
        explosion[i] = new Array(width);
        powerup[i] = new Array(width);
        for (var j = 0; j < width; j++) {
            bombes[i][j] = 0;
            explosion[i][j] = 0;
            powerup[i][j] = 0;
            if (j == 0 || j == width - 1 || i == 0 || i == height - 1) {
                map[i][j] = 'X';
            } else if ((i == 1 && (j == 1 || j == 2 || j == width - 3 || j == width - 2))
                || (i == 2 && (j == 1 || j == width - 2))
                || (i == height - 2 && (j == 1 || j == 2 || j == width - 3 || j == width - 2))
                || (i == height - 3 && (j == 1 || j == width - 2))) {
                map[i][j] = ' ';

            } else {
                if (i % 2 == 0 && j % 2 == 0) {
                    map[i][j] = 'X';
                } else {
                    var rand = Math.floor(Math.random() * 100);
                    if (rand < percent) {
                        map[i][j] = 'O';
                    } else {
                        map[i][j] = ' ';
                    }
                }
            }
        }

    }
    return map;
}

function drawMap() {

    if(kill){
        drawColor(gray);
        return;
    }

    //AFFICHAGE DES DONNEES DES JOUEURS
    p1 = document.getElementById("vie1");
    p1.innerHTML = "vies : " + player1.lives;
    p2 = document.getElementById("vie2");
    p2.innerHTML = "vies : " + player2.lives;
    nbb1 = document.getElementById("nb_bombe1");
    nbb1.innerHTML = "NB bombes : " + player1.nb_bombe;
    nbb2 = document.getElementById("nb_bombe2");
    nbb2.innerHTML = "NB bombes : " + player2.nb_bombe;
    nbbp1 = document.getElementById("nb_bombe1p");
    nbbp1.innerHTML = "NB bombes posées : " + player1.nb_bombe_pose;
    nbbp2 = document.getElementById("nb_bombe2p");
    nbbp2.innerHTML = "NB bombes posées : " + player2.nb_bombe_pose;
    pow1 = document.getElementById("puissance1");
    pow1.innerHTML = "puissance : " + player1.puissance;
    pow2 = document.getElementById("puissance2");
    pow2.innerHTML = "puissance : " + player2.puissance;
    pow2 = document.getElementById("vitesse1");
    pow2.innerHTML = "vitesse : " + player1.speed;
    pow2 = document.getElementById("vitesse2");
    pow2.innerHTML = "vitesse : " + player2.speed;

    for (var i = 0; i < map.length; i++) {
        for (var j = 0; j < map[i].length; j++) {
            if (map[i][j] == 'X') {
                drawInGrid("block_incassable", i, j);
            } else if (map[i][j] == 'O') {
                drawInGrid("block_cassable", i, j);
            } else if (map[i][j] == ' ') {
                drawInGrid("case", i, j);
            }
            if (explosion[i][j] == 1) {
                drawInGrid("fire4_center", i, j);
            } else if (explosion[i][j] == 2) {
                drawInGrid("fire4_left_middle", i, j);
            } else if (explosion[i][j] == 3) {
                drawInGrid("fire4_left_left", i, j);
            } else if (explosion[i][j] == 4) {
                drawInGrid("fire4_right_middle", i, j);
            } else if (explosion[i][j] == 5) {
                drawInGrid("fire4_right_right", i, j);
            } else if (explosion[i][j] == 6) {
                drawInGrid("fire4_up_middle", i, j);
            } else if (explosion[i][j] == 7) {
                drawInGrid("fire4_up_top", i, j);
            } else if (explosion[i][j] == 8) {
                drawInGrid("fire4_down_middle", i, j);
            } else if (explosion[i][j] == 9) {
                drawInGrid("fire4_down_bottom", i, j);
            }
            if (bombes[i][j] == 1) {
                drawInGrid("bombe1", i, j);
            } else if (bombes[i][j] == 2) {
                drawInGrid("bombe2", i, j);
            } else if (bombes[i][j] == 3) {
                drawInGrid("bombe3", i, j);
            }
            if (powerup[i][j] == 1) {
                drawInGrid("bombe+", i, j);
            } else if (powerup[i][j] == 2) {
                drawInGrid("fire+", i, j);
            } else if (powerup[i][j] == 3) {
                drawInGrid("speed+", i, j);
            }

        }
    }

    drawPlayer("player1_", player1.px, player1.py, player1);
    drawPlayer("player2_", player2.px, player2.py, player2);
}

function randompowerup(px, py) {
    var rand = Math.floor(Math.random() * 100);
    map[px][py] = ' ';
    if (rand < 20) {
        powerup[px][py] = 1;
    } else if (rand < 40) {
        powerup[px][py] = 2;
    } else if (rand < 60) {
        powerup[px][py] = 3;
    }
}

function explose_bis(x, y, dx, dy, pow, middle, end, b) {
    for (var i = 1; i <= pow; i++) {
        let px = x + i * dx;
        let py = y + i * dy;
        if (!b && explosion[px][py] == 0) break;
        if (map[px][py] == ' ') {
            if (b) {
                explosion[px][py] = (pow == i ? end : middle);
                var r = false;
                if (player2.x == px && player2.y == py && player1.x == px && player1.y == py){
                    player2.lives = player2.lives - 1;
                    player1.lives = player1.lives - 1;
                    if(player2.lives==0) player1.won = true;
                    if(player1.lives==0) player2.won = true;
                    player2.dead = true;
                    player1.dead = true;
                    r=true;
                } else if (player1.x == px && player1.y == py){
                    player1.lives = player1.lives - 1;
                    if(player1.lives==0) player2.won = true;
                    player1.dead = true;
                    r=true;
                } else if (player1.x == px && player2.y == py){
                    player2.lives = player2.lives - 1;
                    if(player2.lives==0) player1.won = true;
                    player2.dead = true;
                    r=true;
                }
                if(r)restart(); 
                powerup[px][py] = 0;
            } else {
                explosion[px][py] = 0;
            }
        } else if (map[px][py] == "O") {
            if (b) {
                randompowerup(px, py);
                explosion[px][py] = end;
            } else {
                explosion[px][py] = 0;
            }
            break;
        } else {
            break;
        }
    }
}

async function explose(x, y, pow) {
    if (map[x][y] == ' ') {
        explosion[x][y] = 1;
    }

    if (player1.x == x && player1.y == y) player1.lives = player1.lives - 1;
    if (player2.x == x && player2.y == y) player2.lives = player2.lives - 1;
    explose_bis(x, y, 0, -1, pow, 2, 3, true);
    explose_bis(x, y, 0, 1, pow, 4, 5, true);
    explose_bis(x, y, -1, 0, pow, 6, 7, true);
    explose_bis(x, y, 1, 0, pow, 8, 9, true);

    await sleep(500);
    explosion[x][y] = 0;

    explose_bis(x, y, 0, -1, pow, 2, 3, false);
    explose_bis(x, y, 0, 1, pow, 4, 5, false);
    explose_bis(x, y, -1, 0, pow, 6, 7, false);
    explose_bis(x, y, 1, 0, pow, 8, 9, false);
}

async function putbombe(x, y, pow, player) {
    if (player.nb_bombe_pose < player.nb_bombe && bombes[x][y] == 0) {
        player.nb_bombe_pose = player.nb_bombe_pose + 1;
        for (var i = 0; i < 12; i++) {
            bombes[x][y] = 1 + i % 3;
            await sleep((13 - i) * 40)
        }
        bombes[x][y] = 0;
        player.nb_bombe_pose = player.nb_bombe_pose - 1;
        explose(x, y, pow);
    }
}

function checkpowerup(player) {
    if (powerup[getmx(player.px)][getmy(player.py)] == 1 && player.nb_bombe < max_bombe) {
        player.nb_bombe = player.nb_bombe + 1;
    } else if (powerup[getmx(player.px)][getmy(player.py)] == 2 && player.puissance < max_puissance) {
        player.puissance = player.puissance + 1;
    } else if (powerup[getmx(player.px)][getmy(player.py)] == 3 && player.speed < max_speed) {
        player.speed = player.speed + 1;
    }
    powerup[getmx(player.px)][getmy(player.py)] = 0;
}

function isEmpty(x, y) {
    return map[x][y] == ' ' && bombes[x][y] == 0;
}

function getx(px) {
    return Math.floor(px / tile_height);
}

function gety(py) {
    return Math.floor(py / tile_width);
}

function getmx(px) {
    return Math.floor(px / tile_height + 0.5);
}

function getmy(py) {
    return Math.floor(py / tile_width + 0.5);
}

function willCollide(player, dx, dy) {
    let npx = player.px + dx * player.speed;
    let npy = player.py + dy * player.speed;
    let rx = getx(npx);
    let ry = gety(npy);

    if (dx > 0) {
        rx++;
    }

    if (dy > 0) {
        ry++;
    }

    if (dy != 0 && npx % tile_height != 0) return true;
    if (dx != 0 && npy % tile_width != 0) return true;

    return !isEmpty(rx, ry);

}

function turn(player, dx, dy) {

    let nx = player.x + dx;
    let ny = player.y + dy;


    if (willCollide(player, dx, dy)) {
        return false;
    }

    if (player.dx == dx && player.dy == dy) {
        return false;
    }

    player.dx = dx;
    player.dy = dy;

    return true;

}

const clamp = (num, min, max) => Math.min(Math.max(num, min), max);

function move(player) {

    let nx = player.x + player.dx;
    let ny = player.y + player.dy;
    let npx = player.px + player.dx * player.speed;
    let npy = player.py + player.dy * player.speed;
    let rx = getx(npx);
    let ry = gety(npy);

    if (willCollide(player, player.dx, player.dy)) {

        if (player.dy != 0) {
            ry = player.y;
            npy = clamp(npy, (ry) * tile_width, (ry + 1) * tile_width);
        }

        if (player.dx != 0) {
            rx = player.x;
            npx = clamp(npx, (rx) * tile_height, (rx + 1) * tile_height);
        }

    }

    if (player.dx == -1 && npx < tile_height * (player.x - 1)) npx = tile_height * (player.x - 1);
    if (player.dx == 1 && npx > tile_height * (player.x + 1)) npx = tile_height * (player.x + 1);
    if (player.dy == -1 && npy < tile_width * (player.y - 1)) npy = tile_width * (player.y - 1);
    if (player.dy == 1 && npy > tile_width * (player.y + 1)) npy = tile_width * (player.y + 1);

    player.x = rx;
    player.y = ry;
    player.px = npx;
    player.py = npy;

}

async function movePlayerSmooth(player) {
    if (player.is_moving) return;
    player.is_moving = true;
    while (true) {
        var x = player.x;
        var y = player.y;
        if (player.up && turn(player, -1, 0)) {

        } else if (player.down && turn(player, 1, 0)) {

        } else if (player.left && turn(player, 0, -1)) {

        } else if (player.right && turn(player, 0, 1)) {

        } else if (!player.right && !player.left && !player.up && !player.down) {
            player.dx = 0;
            player.dy = 0;
            player.is_moving = false;
            return;
        }

        move(player);

        checkpowerup(player);
        await sleep(15);
    }
}

function checkKey(e, b) {
    e = e || window.event;

    if(player1.dead || player2.dead) return;
        
    

    if (e.keyCode == '38') {
        // up arrow
        player1.up = b;
    }
    else if (e.keyCode == '40') {
        // down arrow
        player1.down = b;
    }
    else if (e.keyCode == '37') {
        // left arrow
        player1.left = b;
    }
    else if (e.keyCode == '39') {
        // right arrow
        player1.right = b;
    } else if (e.keyCode == '96') {
        // 0 numpad
        if (b) putbombe(getmx(player1.px), getmy(player1.py), player1.puissance, player1);
    } else if (e.keyCode == '90') {
        // Z
        player2.up = b;
    } else if (e.keyCode == '83') {
        // S
        player2.down = b;
    } else if (e.keyCode == '81') {
        // Q
        player2.left = b;
    } else if (e.keyCode == '68') {
        // D
        player2.right = b;
    } else if (e.keyCode == '32') {
        // space
        if (b) putbombe(getmx(player2.px), getmy(player2.py), player2.puissance, player2);
    } else if (e.keyCode == '73') {
        // i
        var p = player1;
        if (b) {
            console.log("player 1 :\n" + p.x + "\t" + p.y + "\n" + p.px + "\t" + p.py + "\n");
        } else {
            p = player2;
            console.log("player 2 :\n" + p.x + "\t" + p.y + "\n" + p.px + "\t" + p.py + "\n");
        }
    }
    movePlayerSmooth(player1);
    movePlayerSmooth(player2);
}

function launch(){
    canvas.width = width * tile_width;
    canvas.height = height * tile_height;
    player1 = new Player(1, 1);
    player2 = new Player(height - 2, width - 2);
    document.onkeydown = (e) => { checkKey(e, true); };
    document.onkeyup = (e) => { checkKey(e, false); };
    randomMap();
    changeAnime(300, player1);
    changeAnime(300, player2);
    requestAnimationFrame(step);
}

window.addEventListener("load", function (event) {
    launch();
});