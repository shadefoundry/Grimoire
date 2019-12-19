package shadefoundry.grimoire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    //TODO: add second table for preferences
    public static final String DATABASE_NAME = "MyDBName.db";

    private static final String PLAYER_TABLE_NAME = "player";
    private static final String PLAYER_TABLE_ID = "id";
    private static final String PLAYER_COLUMN_LIFE = "life";
    private static final String PLAYER_COLUMN_POISON = "poison";
    private static final String PLAYER_COLUMN_ENERGY = "energy";
    private static final String PLAYER_COLUMN_EXPERIENCE = "experience";
    private static final String PLAYER_COLUMN_WHITE_MANA = "white";
    private static final String PLAYER_COLUMN_BLUE_MANA = "blue";
    private static final String PLAYER_COLUMN_BLACK_MANA = "black";
    private static final String PLAYER_COLUMN_RED_MANA = "red";
    private static final String PLAYER_COLUMN_GREEN_MANA = "green";
    private static final String PLAYER_COLUMN_COLORLESS_MANA = "colorless";
    private static final String PLAYER_COLUMN_LOG = "log";

    private HashMap hp;

    public DBHelper(Context c){
        super(c,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //create the db
        db.execSQL("create table player "+
                "(id integer primary key,life integer, poison integer, experience integer," +
                "white integer,blue integer, black integer, red integer, green integer, colorless integer, log text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer,int newVer){
        //delete the db and then recreate it
        db.execSQL("DROP TABLE IF EXISTS player");
        onCreate(db);
    }

    public boolean insertPlayer(int life, int poison, int energy, int experience, int[] mana, String log){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("life",life);
        contentValues.put("poison",poison);
        contentValues.put("energy",energy);
        contentValues.put("experience",experience);
        contentValues.put("white",mana[0]);
        contentValues.put("blue",mana[1]);
        contentValues.put("black",mana[2]);
        contentValues.put("red",mana[3]);
        contentValues.put("green",mana[4]);
        contentValues.put("colorless",mana[5]);
        contentValues.put("log",log);

        db.insert("player",null,contentValues);

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from player where id="+id+"", null );
        return res;
    }

    public int numRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,PLAYER_TABLE_NAME);
        return numRows;
    }

    public boolean updatePlayer(Integer id, int life, int poison, int energy, int experience, int[] mana, String log){
        //updates the player, this is what we should be using by default
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("life",life);
        contentValues.put("poison",poison);
        contentValues.put("energy",energy);
        contentValues.put("experience",experience);
        contentValues.put("white",mana[0]);
        contentValues.put("blue",mana[1]);
        contentValues.put("black",mana[2]);
        contentValues.put("red",mana[3]);
        contentValues.put("green",mana[4]);
        contentValues.put("colorless",mana[5]);
        contentValues.put("log",log);

        db.update("player",contentValues,"id = ? ", new String[]{Integer.toString(id)});

        return true;
    }


    public Integer deletePlayer(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("player","id = ? ",new String[] {Integer.toString(id)});
    }

    public ArrayList<String> getAllPlayers(){
        ArrayList<String> arrayList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from player",null);
        res.moveToFirst();

        while(res.isAfterLast()==false){
            arrayList.add(res.getString(res.getColumnIndex(PLAYER_COLUMN_LIFE)));
            res.moveToNext();
        }
        return arrayList;
    }
}