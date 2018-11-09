package shadefoundry.grimoire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    //does all the shit we care about with the database
    //remember to update this number whenever you update database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grimoire.db";
    public static final String TABLE_PRODUCTS = "lifeCounter";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LIFE = "life";
    public static final String COLUMN_POISON = "poison";
    public static final String COLUMN_ENERGY = "energy";
    public static final String COLUMN_LOG = "log";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the table
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LIFE + " INTEGER, " +
                COLUMN_POISON+" INTEGER, " +
                COLUMN_ENERGY+" INTEGER, "+
                COLUMN_LOG+" TEXT "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //kill table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addPlayer(PlayerObject playerObject){
        //this will probably go unused since we're planning to overwrite every time
        ContentValues values = new ContentValues();
        //put current data into values
        values.put(COLUMN_ID,playerObject.getId());
        values.put(COLUMN_LIFE,playerObject.getLife());
        values.put(COLUMN_POISON,playerObject.getPoison());
        values.put(COLUMN_ENERGY,playerObject.getEnergy());
        values.put(COLUMN_LOG,playerObject.getLog());

        //get database and add the player to it
        SQLiteDatabase db = getWritableDatabase();

        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }

    public void deletePlayer(String playerId){
        //another method that will likely go unused, but is nice to have regardless
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_PRODUCTS+" WHERE "+COLUMN_ID+" = \""+playerId+"\";");
    }

    public PlayerObject generatePlayerObject(){
        List<PlayerObject> playerObjectList = new ArrayList<PlayerObject>();
        SQLiteDatabase db = getReadableDatabase();
        //set up a cursor to pull from required fields
        String[] field = {COLUMN_ID,COLUMN_LIFE,COLUMN_POISON,COLUMN_ENERGY,COLUMN_LOG};
        Cursor c = db.query(TABLE_PRODUCTS,field,null,null,null,null,null);

        int idIndex = c.getColumnIndex(COLUMN_ID);
        int lifeIndex = c.getColumnIndex(COLUMN_LIFE);
        int poisonIndex = c.getColumnIndex(COLUMN_POISON);
        int energyIndex = c.getColumnIndex(COLUMN_ENERGY);
        int logIndex = c.getColumnIndex(COLUMN_LOG);

        //iterate through rows and store values in object.
        /*technically we only will ever have one object but it
        * being in a list could be useful if we expand the scope*/
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            int id = c.getInt(idIndex);
            int life = c.getInt(lifeIndex);
            int poison = c.getInt(poisonIndex);
            int energy = c.getInt(energyIndex);
            String log = c.getString(logIndex);
            playerObjectList.add(new PlayerObject(id,life,poison,energy,log));
        }
        //return most recent object (there should only ever be one object in the db)
        return playerObjectList.get(playerObjectList.size()-1);
    }
}
