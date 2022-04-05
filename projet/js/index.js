function drawImage(nom) {
    var c = document.getElementById("myCanvas");
    var ctx = c.getContext("2d");
    var img = document.getElementById("img");
    ctx.drawImage(img, 300, 10);
}

function drawText(text, x, y){
var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");
ctx.font = "30px Arial";
ctx.fillText(text, x, y);
}

function randomMap(height, width){
    var map = "";
    var result = new Array(height);

    for(var i=0; i<height; i++){
        result[i] = new Array(width);
        for(var j=0; j<width; j++){
            if(j==0 || j==width-1 || i==0 || i==height-1){
                result[i][j] = 'X';
            } else if((i==1 && (j==1||j==2||j==width-3||j==width-2))
            ||(i==2 && (j==1||j==width-2))
            ||(i==height-2 && (j==1||j==2||j==width-3||j==width-2))
            ||(i==height-3 && (j==1||j==width-2))){
                result[i][j] = ' ';

            }else{
                if(i%2==0 && j%2==0){ 
                    result[i][j] = 'X';
                } else {
                    var rand = Math.floor(Math.random() * 100);
                    if(rand<60){
                        result[i][j] = 'O';
                    }else{
                        result[i][j] = ' ';
                    }
                }   
            }
        }
    }
    return result;
}
function drawMap(tab){
    for(var i= 0; i < tab.length; i++) {
        for(var j= 0; j < tab[i].length; j++) {
            drawText(tab[i][j], i*30, (j+1)*30);
        }
    }

}

window.addEventListener("load", function (event) {
    //addImage("../test.png");
    var map = randomMap(11,13);
    drawMap(map);
});