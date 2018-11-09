package shadefoundry.grimoire;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    PlayerObject player = new PlayerObject(1,40,0,0,"All set, boss!");
    DBHandler dbHandler= new DBHandler(this,null,null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_life);
        //lock navigation drawer since we're not using it right now.
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //storeData(player);
    }

    /*public void retrieveData(MenuItem item) {
        //get data from sqlite db and populate view
        PlayerObject tempPlayer = dbHandler.generatePlayerObject();
        populateLifeCounterWithData(tempPlayer);
    }

    private void storeData(PlayerObject playerObject){
        //write values from local object to sqlite db
        //should overwrite first value every time
        dbHandler.addPlayer(playerObject);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());

        return true;
    }

    //fragment methods
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_life:
                fragment = new GameTools();
                break;
            /*case R.id.nav_dice:
                fragment = new Dice();
                break;
            /*case R.id.nav_notes:
                fragment = new Notes();
                break;*/
            /*case R.id.nav_cardsearch:
                fragment = new CardSearch();
                break;*/
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void lifeSub1(View view) {
        changeLifeTotal(-1);
        logChanges("- Lost 1 life");
    }
    public void lifeAdd1(View view) {
        changeLifeTotal(1);
        logChanges("+ Gained 1 life");
    }

    public void lifeAdd5(View view) {
        changeLifeTotal(5);
        logChanges("+ Gained 5 life");
    }
    public void lifeSub5(View view) {
        changeLifeTotal(-5);
        logChanges("- Lost 5 life");
    }

    public void addPoison(View view) {
        handlePoison(1);
        logChanges("- Got a Poison Counter");
    }
    public void subPoison(View view) {
        handlePoison(-1);
        logChanges("+ Lost a Poison Counter");
    }

    public void addEnergy(View view) {
        handleEnergy(1);
        logChanges("+ Got an Energy Counter");
    }
    public void subEnergy(View view) {
        handleEnergy(-1);
        logChanges("- Spent an Energy Counter");
    }

    public void gainMana(View view) {
        //gain a mana of the chosen color
        handleMana(view,1);
    }
    public void spendMana(View view) {
        //spend a mana of the chosen color
        handleMana(view,-1);
    }

    private void handlePoison(int i) {
        TextView poisonCounters = (TextView) findViewById(R.id.txt_poison);
        player.handlePoison(i);
        poisonCounters.setText(Integer.toString(player.poison));
        //storeData(player);
    }

    private void handleEnergy(int i) {
        TextView energy=findViewById(R.id.txt_energy);

        player.handleEnergy(i);
        energy.setText(Integer.toString(player.getEnergy()));
        //storeData(player);
    }

    private void changeLifeTotal(int lifeChange) {
        TextView lifeTotal = (TextView) findViewById(R.id.txt_lifeTotal);
        if(player.getLife()+lifeChange<=999 & player.getLife()+lifeChange>=0) {
            player.handleLife(lifeChange);
        }
        lifeTotal.setText(Integer.toString(player.life));
        //storeData(player);
    }

    private void handleMana(View view, int manaChange) {
        /*switch(view.getId()){
            //very long case that handles mana increase/decrease
            //white
            case R.id.txt_w:
                //TextView lose_w = findViewById(R.id.txt_w);
                if(player.getMana(0)+manaChange>=0){
                    player.handleMana(-1,0);
                    logChanges("- Spent W from Mana Pool");
                }
                //lose_w.setText(Integer.toString(player.getMana(0)));

                break;
            case R.id.btn_w:
                //TextView gain_w = findViewById(R.id.txt_w);
                if(player.getMana(0)+manaChange>=0&player.getMana(0)+manaChange<100){
                    player.handleMana(1,0);
                    logChanges("+ Added W to Mana Pool");
                }
                //gain_w.setText(Integer.toString(player.getMana(0)));

                break;
            //blue
            case R.id.txt_u:
                //TextView lose_u = findViewById(R.id.txt_u);
                if(player.getMana(1)+manaChange>=0){
                player.handleMana(-1,1);
                logChanges("- Spent U from Mana Pool");}
                //lose_u.setText(Integer.toString(player.getMana(1)));

                break;
            case R.id.btn_u:
                //TextView gain_u = findViewById(R.id.txt_u);
                if(player.getMana(1)+manaChange>=0&player.getMana(1)+manaChange<100){
                    player.handleMana(1,1);
                    logChanges("+ Added U to Mana Pool");
                }
                //gain_u.setText(Integer.toString(player.getMana(1)));

                break;
            //black
            case R.id.txt_b:
                //TextView lose_b = findViewById(R.id.txt_b);
                if(player.getMana(2)+manaChange>=0){
                    player.handleMana(-1,2);
                    logChanges("- Spent B from Mana Pool");
                }
                //lose_b.setText(Integer.toString(player.getMana(2)));

                break;
            case R.id.btn_b:
                //TextView gain_b = findViewById(R.id.txt_b);
                if(player.getMana(2)+manaChange>=0&player.getMana(2)+manaChange<100){
                    player.handleMana(1,2);
                    logChanges("+ Added B to Mana Pool");
                }
                //gain_b.setText(Integer.toString(player.getMana(2)));

                break;
            //red
            case R.id.txt_r:
                //TextView lose_r = findViewById(R.id.txt_r);
                if(player.getMana(3)+manaChange>=0){
                    player.handleMana(-1,3);
                    logChanges("- Spent R from Mana Pool");
                }
                //lose_r.setText(Integer.toString(player.getMana(3)));

                break;
            case R.id.btn_r:
                //TextView gain_r = findViewById(R.id.txt_r);
                if(player.getMana(3)+manaChange>=0&player.getMana(3)+manaChange<100){
                    player.handleMana(1,3);
                    logChanges("+ Added R to Mana Pool");
                }
                //gain_r.setText(Integer.toString(player.getMana(3)));

                break;
            //green
            case R.id.txt_g:
                //TextView lose_g = findViewById(R.id.txt_g);
                if(player.getMana(4)+manaChange>=0){
                    player.handleMana(-1,4);
                    logChanges("- Spent G from Mana Pool");
                }
                //lose_g.setText(Integer.toString(player.getMana(4)));

                break;
            case R.id.btn_g:
                //TextView gain_g = findViewById(R.id.txt_g);
                if(player.getMana(4)+manaChange>=0&player.getMana(4)+manaChange<100){
                    player.handleMana(1,4);
                    logChanges("+ Added G to Mana Pool");
                }
                //gain_g.setText(Integer.toString(player.getMana(4)));

                break;
            //colorless
            case R.id.txt_c:
                //TextView lose_c = findViewById(R.id.txt_c);
                if(player.getMana(5)+manaChange>=0){
                    player.handleMana(-1,5);
                    logChanges("- Spent 1 from Mana Pool");
                }
                //lose_c.setText(Integer.toString(player.getMana(5)));

                break;
            case R.id.btn_c:
                //TextView gain_c = findViewById(R.id.txt_c);
                if(player.getMana(5)+manaChange>=0&player.getMana(5)+manaChange<100){
                    player.handleMana(1,5);
                    logChanges("+ Added 1 to Mana Pool");}
                //gain_c.setText(Integer.toString(player.getMana(5)));

                break;
        }*/
        //storeData(player);
    }

    private void logChanges(String message){
        //TextView log = findViewById(R.id.txt_changeLog);
        //log.setMovementMethod(new ScrollingMovementMethod());
        player.addLog(message+"\n");
        //log.setText(player.log);
        //storeData(player);
    }

    public void resetAll(MenuItem item) {
        //access everything
        TextView lifeCounter = findViewById(R.id.txt_lifeTotal);
        TextView poisonCounter = findViewById(R.id.txt_poison);
        TextView energy = findViewById(R.id.txt_energy);
        /*TextView txt_w = findViewById(R.id.txt_w);
        TextView txt_u = findViewById(R.id.txt_u);
        TextView txt_b = findViewById(R.id.txt_b);
        TextView txt_r = findViewById(R.id.txt_r);
        TextView txt_g = findViewById(R.id.txt_g);
        TextView txt_c = findViewById(R.id.txt_c);*/
        //TextView txt_log = findViewById(R.id.txt_changeLog);

        //eventually this'll get a shared preference or something to set to whatever the user wants
        player.life = 40;
        lifeCounter.setText(Integer.toString(player.getLife()));

        player.setPoison(0);
        poisonCounter.setText(Integer.toString(player.getPoison()));

        player.setEnergy(0);
        energy.setText(Integer.toString(player.getEnergy()));

        player.setLog("All set, Boss!\n");
        //txt_log.setText(player.getLog());
        //txt_log.scrollTo(0,0);

        /*TextView[] mana = new TextView[6];
        mana[0] = txt_w;
        mana[1] = txt_u;
        mana[2] = txt_b;
        mana[3] = txt_r;
        mana[4] = txt_g;
        mana[5] = txt_c;*/
        for(int i=0;i<6;i++){
            player.setMana(i,0);
            //mana[i].setText(Integer.toString(player.getMana(i)));
        }
        //finally store the data back in the db
        //storeData(player);
    }

    public void openManaCounter(MenuItem item) {

    }

    public void viewLog(MenuItem item) {
    }
}
