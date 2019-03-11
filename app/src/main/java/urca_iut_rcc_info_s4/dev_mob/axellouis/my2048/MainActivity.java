package urca_iut_rcc_info_s4.dev_mob.axellouis.my2048;

import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import static urca_iut_rcc_info_s4.dev_mob.axellouis.my2048.Game2048.*;

public class MainActivity extends AppCompatActivity {

    private TextView[][] box;
    private Game2048 game;
    private int[] colId = {R.color.col00,
            R.color.col01,
            R.color.col02,
            R.color.col03,
            R.color.col04,
            R.color.col05,
            R.color.col06,
            R.color.col07,
            R.color.col08,
            R.color.col09,
            R.color.col10,
            R.color.col11,
            R.color.col12,
            R.color.col13,
            R.color.col14,
            R.color.col15,
            R.color.col16,
            R.color.col17,
            R.color.colNT,
            R.color.colDT,
            R.color.colBT};
    private int[] color = new int[21];
    private TextView score;
    private TextView lastP;
    private RatingBar bestT;
    private TextView dim;
    private ImageButton buttonD;
    private ImageButton buttonU;
    private ImageButton buttonL;
    private ImageButton buttonR;
    private LinearLayout glo;

    private void putSaveToBundle(Game2048.SaveBundle sb , Bundle bdl){
        bdl.putString("board",sb.board);
        bdl.putInt("score",sb.score);
        bdl.putString("lastP",sb.lastP);
    }

    private Game2048.SaveBundle getSaveFromBundle(Bundle bdl){
        return new Game2048.SaveBundle(bdl.getString("board","                                "),bdl.getString("lastP",""),bdl.getInt("score",0));
    }

    private void putSaveToPrefs(Game2048.SaveBundle sb , SharedPreferences sp){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("board",sb.board);
        editor.putInt("score",sb.score);
        editor.putString("lastP",sb.lastP);
        editor.commit();
    }

    private Game2048.SaveBundle getSaveFromPrefs(SharedPreferences sp){
        Log.i("debug2", sp.getString("board", "   _  "));
        return new Game2048.SaveBundle(sp.getString("board","                                "),sp.getString("lastP",""),sp.getInt("score",0));
    }



    public void update(){
        for (int x = 0; x<4;x++){
            for (int y = 0; y<4;y++){
                this.box[x][y].setText(game.getTile(x,y).toString());
                this.box[x][y].setBackgroundColor(color[this.game.getTile(x,y).getRank()]);
                if(this.game.getTile(x,y).getRank()<=3) {
                    this.box[x][y].setTextColor(color[19]);
                }
                else {
                    this.box[x][y].setTextColor(color[20]);
                }
                if(this.game.getTile(x,y).isNew()){
                    this.box[x][y].setTextColor(color[18]);
                }
            }
        }

        this.score.setText(""+this.game.getScore());
        this.bestT.setRating(this.game.getBestR());
        this.lastP.setText(this.game.getLastP());
        if (this.game.hasJustWon()==true){
            Toast.makeText(this, getString(R.string.win), Toast.LENGTH_SHORT).show();
            ( (LayerDrawable) bestT.getProgressDrawable() ).getDrawable(2).setColorFilter(color[11], android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
        if(this.game.isOver()){
            Toast.makeText(this, getString(R.string.gameOver), Toast.LENGTH_SHORT).show();
            this.lastP.setText(this.getString(R.string.gameOver));
            buttonD.setVisibility(View.INVISIBLE);
            buttonL.setVisibility(View.INVISIBLE);
            buttonU.setVisibility(View.INVISIBLE);
            buttonR.setVisibility(View.INVISIBLE);
        }
        else{
            buttonD.setVisibility(View.VISIBLE);
            buttonL.setVisibility(View.VISIBLE);
            buttonU.setVisibility(View.VISIBLE);
            buttonR.setVisibility(View.VISIBLE);
        }
    }
    public void tryMove(int dir){
        if(game.move(dir<2,dir%2 == 1)) {
            update();
        }
        else{
            Toast.makeText(this, getString(R.string.invalidMove), Toast.LENGTH_SHORT).show();
        }
    }

    //*******************************************@OVERRIDE**********************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bestT = (RatingBar) findViewById(R.id.bestTRB);
        this.score = (TextView) findViewById(R.id.scoreTV);
        this.lastP= (TextView) findViewById(R.id.lastPTV);
        this.glo = (LinearLayout) findViewById(R.id.globalLO);

        //Buttton
        this.buttonD = (ImageButton) findViewById(R.id.buttonD);
        this.buttonU = (ImageButton) findViewById(R.id.buttonU);
        this.buttonL = (ImageButton) findViewById(R.id.buttonL);
        this.buttonR = (ImageButton) findViewById(R.id.buttonR);

        glo.setOnTouchListener( new OnSwipeTouchListener(MainActivity.this){
            @Override
            public void onSwipeLeft() {
                tryMove(0);
            }
            @Override
            public void onSwipeTop() {
                tryMove(1);
            }
            @Override
            public void onSwipeRight() {
                tryMove(2);
            }
            @Override
            public void onSwipeBottom() {
                tryMove(3);
            }


        });
        this.buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryMove(3);
            }
        });
        this.buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryMove(0);
            }
        });
        this.buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryMove(2);
            }
        });
        this.buttonU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryMove(1);
            }
        });

        //this.dim = (TextView) findViewById(R.id.dim);
        int[][] boxId = {{R.id.box00, R.id.box01, R.id.box02, R.id.box03},
                {R.id.box10, R.id.box11, R.id.box12, R.id.box13},
                {R.id.box20, R.id.box21, R.id.box22, R.id.box23},
                {R.id.box30, R.id.box31, R.id.box32, R.id.box33}};
        this.box = new TextView[4][4];
        for(int i =0; i<4;i++){
            for(int j =0; j<4;j++){
                this.box[i][j] = (TextView) findViewById(boxId[i][j]);
                //  this.box[i][j].setText("lc =" + i + j);
            }
        }

        for(int i =0; i<21;i++){
            this.color[i]= getResources().getColor(this.colId[i]);
        }
        this.game = new Game2048();
        this.game.init();
        this.game.restoreFromSaveBundle(this.getSaveFromPrefs(this.getPreferences(MODE_PRIVATE)));
        this.update();
    }

    @Override
    protected void onStop() {
        this.putSaveToPrefs(this.game.getSaveBundle(),this.getPreferences(MODE_PRIVATE));
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_new) {
            this.game.init();
            this.update();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);
        View glo = this.findViewById(R.id.globalLO);
        View slo = this.findViewById(R.id.scoresLO);
        View blo = this.findViewById(R.id.boardLO);
        View clo = this.findViewById(R.id.controlLO);
        View rlo = this.findViewById(R.id.rightLO);

        LinearLayout glo2 = (LinearLayout) glo;
        if(glo2.getOrientation() == LinearLayout.VERTICAL) {
            float w = 100 * blo.getWidth() / glo.getHeight();

            if (blo.getHeight() > blo.getWidth()) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) slo.getLayoutParams();
                lp.weight = (float) 0.25 * (100 - w);
                slo.setLayoutParams(lp);
                LinearLayout.LayoutParams mp = (LinearLayout.LayoutParams) blo.getLayoutParams();
                mp.weight = w;
                blo.setLayoutParams(mp);
                lp = (LinearLayout.LayoutParams) clo.getLayoutParams();
                lp.weight = (float) 0.75 * (100 - w);
                clo.setLayoutParams(lp);
            } else {
                glo.setPadding(20, 0, 20, 0);
            }
        }
        else if(glo2.getOrientation() == LinearLayout.HORIZONTAL) {
            float w = 100 * blo.getHeight() / (glo.getWidth()-(2*blo.getLeft())); //100f*h/(D-2*x)
            LinearLayout.LayoutParams mp = (LinearLayout.LayoutParams) blo.getLayoutParams();
            mp.weight = w;
            blo.setLayoutParams(mp);
            LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) rlo.getLayoutParams();
            rp.weight = 100-w;
            rlo.setLayoutParams(rp);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        this.putSaveToBundle(this.game.getSaveBundle(),outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.game.restoreFromSaveBundle(this.getSaveFromBundle(savedInstanceState));
        this.update();
    }


}
