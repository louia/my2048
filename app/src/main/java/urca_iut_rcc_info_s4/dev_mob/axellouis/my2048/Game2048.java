package urca_iut_rcc_info_s4.dev_mob.axellouis.my2048;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by undr0002 on 28/01/19.
 */

public class Game2048 {

    public static class SaveBundle{
        public String board;
        public String lastP;
        public int score;

        public SaveBundle(){
            this.board = "";
            this.lastP = "";
            this.score = 0;
        }

        public SaveBundle(String board, String lastP, int score){
            this.board = board;
            this.lastP = lastP;
            this.score = score;
        }
    }
    //********************************************
    public static class Tile{
        private int flag;
        private int r;
        private static int[] pow2 = {0,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072};


        public Tile(){
            this.flag =0;
            this.r = 0;
        }

        public Tile(int exp){
            this.flag = -1;
            this.r = exp;
        }

        public int getRank(){
            return this.r;
        }

        public int value(int p){
            return this.pow2[p];
        }

        public boolean isNew(){
            return this.flag ==-1;
        }

        public boolean isFusion(){
            return this.flag == 1;
        }

        @Override
        public String toString(){
            if(this.r ==0){
                return "";
            }
            else{
                return ""+this.value(r);
            }
        }

        private void set(int rk, int fl){
            this.r = rk;
            this.flag = fl;
        }

        public String Log(){
            String s = "";
            if(this.flag == -1){
                s+="+";
            }
            else if(this.flag == 1){
                s+="^";
            }
            else{
                s+=" ";
            }
            if(this.getRank() <10 && this.getRank() >0){
                s+= this.getRank();
            }
            else if(this.getRank() >=10){
                int i = 55 + this.getRank();
                char c = (char) i;
                s+= c;
            }
            else{
                s+=" ";
            }
            return s;
        }

        public void setFromLog(String log, int i){
            String nb = " 123456789ABCDEFGH";
            String flag = "+ ^";
            int rank = nb.indexOf(log.charAt(i+1));
            if(log.charAt(i) == flag.charAt(0)){
                this.set(rank,-1);
            }
            else if(log.charAt(i) == flag.charAt(1)){
                this.set(rank,0);
            }
            else{
                this.set(rank,1);
            }
        }
    }




    //********************************************
    private int nbv;
    private Tile[][] board;
    private int score = 0;
    private int bestR = 0;
    private String lastP = "";
    private Random rand;
    private boolean goal;

    public String getLastP() {
        return lastP;
    }

    public void setLastP(String lastP) {
        this.lastP = lastP;
    }

    public int getBestR() {
        return bestR;
    }

    public void setBestR(int bestR) {
        this.bestR = bestR;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public Game2048(){
        this.rand = new Random();
        this.board = new Tile[4][4];
        for (int x = 0; x<4;x++){
            for (int y = 0; y<4;y++){
                this.board[x][y] =  new Tile();
            }
        }
    }

    public Tile getTile(int l, int c){
        return this.board[l][c];
    }

    public void init(){
        for (int x = 0; x<4;x++){
            for (int y = 0; y<4;y++){
                this.board[x][y].r = 0;
                this.board[x][y].flag = 0;
            }
        }
        this.score = 0;
        this.bestR = 0;
        this.lastP = "";
        this.nbv = 16;
        this.addTile();
        this.addTile();
        this.goal=false;
    }

    private void addTile(){
        int i = this.rand.nextInt(this.nbv);
        int compt = 0;
        for (int x = 0; x < 4 ; x++) {
            for (int y = 0; y < 4; y++) {
                if (this.board[x][y].getRank()==0){

                    if (compt == i) {
                        if(this.rand.nextInt(100)<10) {
                            this.board[x][y].set(2, -1);
                        }
                        else{
                            this.board[x][y].set(1, -1);
                        }
                        this.nbv--;
                        if(this.board[x][y].getRank()>this.bestR){
                            this.bestR = this.board[x][y].getRank();
                        }
                    }
                    compt++;
                }

            }
        }
    }

    public void initTest(){
        for (int x = 0; x<4;x++){
            for (int y = 0; y<4;y++){
                this.board[x][y].r = 0;
                this.board[x][y].flag = 0;
            }
        }

        this.score = 0;
        this.bestR = 0;
        this.lastP = "";
        this.nbv = 16;
        this.board[0][0].set(1, 0);
        this.board[0][1].set(3, 0);
        this.board[0][3].set(1, 0);
        this.board[1][1].set(1, 0);
        this.board[1][2].set(1, 0);
        this.board[2][0].set(1, 0);
        this.board[2][1].set(1, 0);
        this.board[2][2].set(1, 0);
        this.board[2][3].set(1, 0);
        this.board[3][0].set(1, 0);
        this.board[3][1].set(1, 0);
        this.board[3][2].set(2, 0);
        this.board[3][3].set(2, 0);
    }

    public boolean move(boolean croiss, boolean vert){
        String info = "";
        boolean modif = false;
        ArrayList<Integer> pile = new ArrayList<Integer>(4);
        this.nbv = 0;
        for (int lc = 0; lc < 4; lc++) {
            for (int i = 0; i <4 ; i++) {
                if(this.getTile(lc,i,croiss,vert).getRank() != 0) {
                    pile.add(this.getTile(lc, i, croiss, vert).getRank());
                    if(i>=pile.size()){
                        modif= true;
                    }
                }
            }
            int i =0;
            for (int ip = 0; ip <pile.size() ; ip++, i++) {
                //info+= ""+pile.get(ip);
                if(ip < pile.size()-1 && pile.get(ip)==pile.get(ip+1)){
                        getTile(lc, i, croiss, vert).set(1 + pile.get(ip), 1);
                        modif = true;
                        this.score+= getTile(lc, i, croiss, vert).value(getTile(lc,i,croiss,vert).getRank());
                        if(getTile(lc,i,croiss,vert).getRank()>this.bestR){
                            this.bestR = getTile(lc,i,croiss,vert).getRank();
                        }
                        if(info == ""){
                           info+=getTile(lc, i, croiss, vert).value(getTile(lc,i,croiss,vert).getRank());
                        }
                        else{
                            info+= "+" + getTile(lc, i, croiss, vert).value(getTile(lc,i,croiss,vert).getRank());
                        }
                        ip++;

                }
                else{
                    getTile(lc,i,croiss,vert).set(pile.get(ip),0);
                }


            }
            for ( ; i <4 ; i++) {
                getTile(lc,i,croiss,vert).set(0,0);
                this.nbv++;
            }
            //info+= '\n';
            pile.clear();
        }
        if(modif == true){
            this.lastP = info;
            this.addTile();
        }
        return modif;
    }

    private Tile getTile(int lc, int i, boolean croiss, boolean vert){
        if(croiss == true && vert == true){
            return board[i][lc];
        }
        else if(croiss == true && vert == false){
            return board[lc][i];
        }
        else if(croiss == false && vert == false){
            return board[lc][3-i];
        }
        else if(croiss == false && vert == true){
            return board[3-i][lc];
        }
        return null;
    }
    public boolean hasJustWon() {
        if (this.bestR == 11 && goal == false) {
            this.goal = true;
            return true;
        }
        return false;
    }

    public boolean isOver(){
        if(this.nbv > 0){
            return false;
        } else if (this.getTile(0, 0).getRank() == this.getTile(0, 1).getRank()|| this.getTile(0, 0).getRank() == this.getTile(1, 0).getRank()) {
            return false;
        }
        for (int i = 1; i < 4 ; i++) {
            for (int j = 1; j < 4 ; j++) {
               if(this.getTile(i,j).getRank() == this.getTile(i-1,j).getRank()|| this.getTile(i,j).getRank() == this.getTile(i,j-1).getRank()){
                   return false;
               }

            }
        }
        return true;
    }

    private String logBoard(){
        String s = "";
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j <4 ; j++) {
                s+=this.getTile(i,j).Log();
            }
        }
        return s;
    }
    public SaveBundle getSaveBundle(){
        return new SaveBundle(this.logBoard(),this.lastP,this.score);
    }

    public void restoreFromSaveBundle(SaveBundle sb){
        this.lastP = sb.lastP;
        this.score = sb.score;
        this.bestR = 0;
        this.nbv = 0;
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 4 ; j++) {
                this.getTile(i,j).setFromLog(sb.board,(i*4+j)*2);
                if(this.getTile(i,j).getRank()> bestR){
                    this.bestR=this.getTile(i,j).getRank();
                }
                if(this.getTile(i,j).isNew() == false && this.getTile(i,j).isFusion() == false){
                    this.nbv++;
                }
            }
        }
        this.hasJustWon();

    }

}

