package shadefoundry.grimoire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Player player = Player.getInstance();

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
        //toggle.setDrawerIndicatorEnabled(false);//disable drawer icon
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_life);
        //lock navigation drawer since we're not using it right now.
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

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
            case R.id.nav_dice:
                fragment = new Dice();
                break;
            case R.id.nav_notes:
                fragment = new Notes();
                break;
            case R.id.nav_cardsearch:
                fragment = new CardSearch();
                break;
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

    public void addExp(View view) {
        handleExperience(1);
        logChanges("+ Got an Experience Counter");
    }

    public void subExp(View view) {
        handleExperience(-1);
        logChanges("- Lost an Experience Counter");
    }

    public void gainMana(View view) {
        //gain a mana of the chosen color
        handleMana(view,1);
    }
    public void spendMana(View view) {
        //spend a mana of the chosen color
        handleMana(view,-1);
    }

    private void handleExperience(int i){
        TextView experienceCounters = (TextView) findViewById(R.id.txt_experience);
        player.handleExperience(i);
        experienceCounters.setText(Integer.toString(player.experience));
    }

    private void handlePoison(int i) {
        TextView poisonCounters = (TextView) findViewById(R.id.txt_poison);
        player.handlePoison(i);
        poisonCounters.setText(Integer.toString(player.poison));
    }

    private void handleEnergy(int i) {
        TextView energy=findViewById(R.id.txt_energy);

        player.handleEnergy(i);
        energy.setText(Integer.toString(player.getEnergy()));
    }

    private void changeLifeTotal(int lifeChange) {
        TextView lifeTotal = (TextView) findViewById(R.id.txt_lifeTotal);
        if(player.getLife()+lifeChange<=999 & player.getLife()+lifeChange>=0) {
            player.handleLife(lifeChange);
        }
        lifeTotal.setText(Integer.toString(player.life));
    }

    private void handleMana(View view, int manaChange) {
        switch(view.getId()){
            //very long case that handles mana increase/decrease
            //white
            case R.id.txt_w:
                TextView lose_w = findViewById(R.id.txt_w);
                if(player.getMana(0)+manaChange>=0){
                    player.handleMana(-1,0);
                    logChanges("- Spent W from Mana Pool");
                }
                lose_w.setText(Integer.toString(player.getMana(0)));

                break;
            case R.id.btn_w:
                TextView gain_w = findViewById(R.id.txt_w);
                if(player.getMana(0)+manaChange>=0&player.getMana(0)+manaChange<100){
                    player.handleMana(1,0);
                    logChanges("+ Added W to Mana Pool");
                }
                gain_w.setText(Integer.toString(player.getMana(0)));

                break;
            //blue
            case R.id.txt_u:
                TextView lose_u = findViewById(R.id.txt_u);
                if(player.getMana(1)+manaChange>=0){
                player.handleMana(-1,1);
                logChanges("- Spent U from Mana Pool");}
                lose_u.setText(Integer.toString(player.getMana(1)));

                break;
            case R.id.btn_u:
                TextView gain_u = findViewById(R.id.txt_u);
                if(player.getMana(1)+manaChange>=0&player.getMana(1)+manaChange<100){
                    player.handleMana(1,1);
                    logChanges("+ Added U to Mana Pool");
                }
                gain_u.setText(Integer.toString(player.getMana(1)));

                break;
            //black
            case R.id.txt_b:
                TextView lose_b = findViewById(R.id.txt_b);
                if(player.getMana(2)+manaChange>=0){
                    player.handleMana(-1,2);
                    logChanges("- Spent B from Mana Pool");
                }
                lose_b.setText(Integer.toString(player.getMana(2)));

                break;
            case R.id.btn_b:
                TextView gain_b = findViewById(R.id.txt_b);
                if(player.getMana(2)+manaChange>=0&player.getMana(2)+manaChange<100){
                    player.handleMana(1,2);
                    logChanges("+ Added B to Mana Pool");
                }
                gain_b.setText(Integer.toString(player.getMana(2)));

                break;
            //red
            case R.id.txt_r:
                TextView lose_r = findViewById(R.id.txt_r);
                if(player.getMana(3)+manaChange>=0){
                    player.handleMana(-1,3);
                    logChanges("- Spent R from Mana Pool");
                }
                lose_r.setText(Integer.toString(player.getMana(3)));

                break;
            case R.id.btn_r:
                TextView gain_r = findViewById(R.id.txt_r);
                if(player.getMana(3)+manaChange>=0&player.getMana(3)+manaChange<100){
                    player.handleMana(1,3);
                    logChanges("+ Added R to Mana Pool");
                }
                gain_r.setText(Integer.toString(player.getMana(3)));

                break;
            //green
            case R.id.txt_g:
                TextView lose_g = findViewById(R.id.txt_g);
                if(player.getMana(4)+manaChange>=0){
                    player.handleMana(-1,4);
                    logChanges("- Spent G from Mana Pool");
                }
                lose_g.setText(Integer.toString(player.getMana(4)));

                break;
            case R.id.btn_g:
                TextView gain_g = findViewById(R.id.txt_g);
                if(player.getMana(4)+manaChange>=0&player.getMana(4)+manaChange<100){
                    player.handleMana(1,4);
                    logChanges("+ Added G to Mana Pool");
                }
                gain_g.setText(Integer.toString(player.getMana(4)));

                break;
            //colorless
            case R.id.txt_c:
                TextView lose_c = findViewById(R.id.txt_c);
                if(player.getMana(5)+manaChange>=0){
                    player.handleMana(-1,5);
                    logChanges("- Spent 1 from Mana Pool");
                }
                lose_c.setText(Integer.toString(player.getMana(5)));

                break;
            case R.id.btn_c:
                TextView gain_c = findViewById(R.id.txt_c);
                if(player.getMana(5)+manaChange>=0&player.getMana(5)+manaChange<100){
                    player.handleMana(1,5);
                    logChanges("+ Added 1 to Mana Pool");}
                gain_c.setText(Integer.toString(player.getMana(5)));

                break;
        }
    }

    private void logChanges(String message){
        player.addLog(message+"\n");
    }

    public void resetAll(MenuItem item) {
        //access everything
        TextView lifeCounter = findViewById(R.id.txt_lifeTotal);
        TextView poisonCounter = findViewById(R.id.txt_poison);
        TextView energy = findViewById(R.id.txt_energy);
        TextView experience = findViewById(R.id.txt_experience);

        emptyManaPool();

        //eventually this'll get a shared preference or something to set to whatever the user wants
        player.life = 40;
        lifeCounter.setText(Integer.toString(player.getLife()));

        player.setPoison(0);
        poisonCounter.setText(Integer.toString(player.getPoison()));

        player.setEnergy(0);
        energy.setText(Integer.toString(player.getEnergy()));

        player.setExperience(0);
        experience.setText(Integer.toString(player.getExperience()));

        player.setLog("All set, boss!");
    }

    public void toggleMana(MenuItem item) {
        ConstraintLayout manaLayout = findViewById(R.id.manaLayout);
        if(manaLayout.getVisibility()==View.GONE){
            manaLayout.setVisibility(View.VISIBLE);
        }
        else{manaLayout.setVisibility(View.GONE);}

    }

    public void viewLog(MenuItem item) {
        displayLog();
    }

    private void displayLog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(player.getLog());
                alertDialogBuilder.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //Toast.makeText(MainActivity.this,"Log dismissed",Toast.LENGTH_SHORT).show();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void toggleCounters(MenuItem item) {
        ConstraintLayout countersLayout = findViewById(R.id.countersLayout);
        //if counters are hidden display them
        if(countersLayout.getVisibility()==View.GONE){
            countersLayout.setVisibility(View.VISIBLE);
        }
        //otherwise we hide them
        else{
            countersLayout.setVisibility(View.GONE);
        }
    }

    public void displayMonarchText(View view) {
        CheckBox monarch = findViewById(R.id.chk_monarch);
        TextView monarchReminderText = findViewById(R.id.txt_monarchReminder);
        //if monarch is checked, display reminder text, otherwise remove it
        if(monarch.isChecked()){
            logChanges("+ Became the Monarch");
            monarchReminderText.setVisibility(View.VISIBLE);
        }
        else{
            monarchReminderText.setVisibility(View.GONE);
            logChanges("- No longer the Monarch");}
    }

    public void resetMana(MenuItem item) {
        emptyManaPool();
    }

    private void emptyManaPool() {
        TextView txt_w = findViewById(R.id.txt_w);
        TextView txt_u = findViewById(R.id.txt_u);
        TextView txt_b = findViewById(R.id.txt_b);
        TextView txt_r = findViewById(R.id.txt_r);
        TextView txt_g = findViewById(R.id.txt_g);
        TextView txt_c = findViewById(R.id.txt_c);
        TextView[] mana = new TextView[6];
        mana[0] = txt_w;
        mana[1] = txt_u;
        mana[2] = txt_b;
        mana[3] = txt_r;
        mana[4] = txt_g;
        mana[5] = txt_c;
        for(int i=0;i<6;i++){
            player.setMana(i,0);
            mana[i].setText(Integer.toString(player.getMana(i)));
        }
        logChanges("- Emptied Mana Pool");
    }

    public void toggleDesignations(MenuItem item) {
        ConstraintLayout designationsLayout = findViewById(R.id.designationLayout);
        //if player designations are hidden display them
        if(designationsLayout.getVisibility()==View.GONE){
            designationsLayout.setVisibility(View.VISIBLE);
        }
        //otherwise we hide them
        else{
            designationsLayout.setVisibility(View.GONE);
        }
    }

    public void ascend(View view) {
        CheckBox citysBlessing = findViewById(R.id.chk_cityBlessing);
        if(citysBlessing.isChecked()){
            logChanges("+ Got the City's Blessing");
        }
    }
}
