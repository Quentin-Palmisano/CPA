//pourcentage de block cassable sur la map
let percent = 60;
//hauteur de la map
let height = 11;
//largeur de la map
let width = 13;
//hauteur d'une image
let tile_height = 38;
//largeur d'une image
let tile_width = 38;

let canvas = document.getElementById("myCanvas");
let context = canvas.getContext("2d");
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
        this.lives = 3;
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

const player1 = new Player(1, 1);
const player2 = new Player(height - 2, width - 2);

//nombre maximum de powerup par joueur
let max_bombe = 10;
let max_puissance = 10;
let max_speed = 10;


function step() {
    drawMap();
    requestAnimationFrame(step);
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function drawInGrid(nom, x, y) {
    var img = document.getElementById(nom);
    context.drawImage(img, y * tile_height, x * tile_width);
}
function drawByPixel(nom, x, y) {
    var img = document.getElementById(nom);
    context.drawImage(img, y, x);
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
    //context.clearRect(0, 0, width, height);

    //AFFICHAGE DES DONNEES DES JOUEURS
    p1 = document.getElementById("player1");
    p1.innerHTML = "player 1 : " + player1.lives + " (vie)";
    p2 = document.getElementById("player2");
    p2.innerHTML = "player 2 : " + player2.lives + " (vie)";
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

    drawByPixel("player1_1", player1.px, player1.py);
    drawByPixel("player2_1", player2.px, player2.py);
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
                if (player1.x == px && player1.y == py) player1.lives = player1.lives - 1;
                if (player2.x == px && player2.y == py) player2.lives = player2.lives - 1;
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
    if (powerup[player.x][player.y] == 1 && player.nb_bombe < max_bombe) {
        player.nb_bombe = player.nb_bombe + 1;
    } else if (powerup[player.x][player.y] == 2 && player.puissance < max_puissance) {
        player.puissance = player.puissance + 1;
    } else if (powerup[player.x][player.y] == 3 && player.speed < max_speed) {
        player.speed = player.speed + 1;
    }
    powerup[player.x][player.y] = 0;
}

function isEmpty(x, y) {
    return map[x][y] == ' ' && bombes[x][y] == 0;
}

function turn(player, dx, dy) {

    let nx = player.x + dx;
    let ny = player.y + dy;

    if(!isEmpty(nx, ny)){
        return false;
    }

    if(player.dx == dx && player.dy == dy){
        return false;
    }

    player.dx = dx;
    player.dy = dy;

    return true;

}

function move(player) {

    let nx = player.x + player.dx;
    let ny = player.y + player.dy;

    if(isEmpty(nx, ny)){
        
        player.y = ny;
        player.py = player.y * tile_width;

        player.x = nx;
        player.px = player.x * tile_height;

    }

}

async function movePlayerSmooth(player) {
    if(player.is_moving) return;
    player.is_moving = true;
    while (true) {
        var x = player.x;
        var y = player.y;
        if (player.up && turn(player, -1, 0)) {

        } else if (player.down && turn(player, 1, 0)) {

        } else if (player.left && turn(player, 0, -1)) {

        } else if (player.right && turn(player, 0, 1)) {

        } else if(!player.right && !player.left && !player.up && !player.down){
            player.dx = 0;
            player.dy = 0;
            player.is_moving = false;
            return;
        }

        move(player);

        checkpowerup(player);
        await sleep(1 + (max_speed + 1 - player.speed) * 100);
    }
}

function checkKey(e, b) {
    e = e || window.event;


    if (e.keyCode == '38') {
        // up arrow
        player1.up = b;
        console.log(player1.up);
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
        if(b) putbombe(player1.x, player1.y, player1.puissance, player1);
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
        if(b) putbombe(player2.x, player2.y, player2.puissance, player2);
    }
    movePlayerSmooth(player1);
    movePlayerSmooth(player2);
}


window.addEventListener("load", function (event) {
    canvas.width = width * tile_width;
    canvas.height = height * tile_height;
    document.onkeydown = (e) => { checkKey(e, true); };
    document.onkeyup = (e) => { checkKey(e, false); };
    randomMap();
    requestAnimationFrame(step);
});