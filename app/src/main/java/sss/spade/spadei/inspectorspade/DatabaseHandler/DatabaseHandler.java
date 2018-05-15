package sss.spade.spadei.inspectorspade.DatabaseHandler;

/**
 * Created by hp on 9/21/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import sss.spade.spadei.inspectorspade.DatabaseHandler.Beans.GetProfile;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NotificationManager";

    // Contacts table name
    private static final String TABLE_NOTIFICATION = "notification";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOTIFICATION = "notificationnew";
    private static final String KEY_NOTIFICATION_DESCRIPTION = "notificationdescription";
    private static final String KEY_NOTIFICATION_DATE = "notidate";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTIFICATION + " TEXT,"
                +KEY_NOTIFICATION_DESCRIPTION+" TEXT,"+KEY_NOTIFICATION_DATE+" TEXT);";
        db.execSQL(CREATE_CONTACTS_TABLE);


/*
        String TABLE= "DESCRIBE "+TABLE_NOTIFICATION;
         String query=TABLE;
        Cursor cursor=db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            String result_0=cursor.getString(0);
            String result_1=cursor.getString(1);

            Log.e("Table :","This is data"+result_0+"  "+result_1);

            //and so on
        }
        cursor.close();
*/
      //  db.close();

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addNotification(GetProfile getnoti){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
    values.put(KEY_NOTIFICATION,getnoti.getNotification());
    values.put(KEY_NOTIFICATION_DESCRIPTION,getnoti.getNoticationdescription());
    values.put(KEY_NOTIFICATION_DATE,getnoti.getNotidate());

        // Inserting Row
        db.insert(TABLE_NOTIFICATION, null, values);

/*
        String TABLE= "DESCRIBE "+TABLE_NOTIFICATION;



        String query=TABLE;
        Cursor cursor=db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            String result_0=cursor.getString(0);
            String result_1=cursor.getString(1);

            Log.e("Table :","This is data"+result_0+"  "+result_1);

            //and so on
        }
        cursor.close(); //2nd argument is String containing nullColumnHack
*/
        db.close(); // Closing database connection
    }

    // Getting single contact
    GetProfile getNotification(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATION, new String[] { KEY_ID,
                        KEY_NOTIFICATION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        GetProfile notification = new GetProfile(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getString(2),cursor.getString(3));
        // return contact
        return notification;
    }

    // Getting All Contacts
    public List<GetProfile> getAllNotifications() {
        List<GetProfile> notificationlist = new ArrayList<GetProfile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GetProfile notification = new GetProfile();
                notification.set_id(Integer.parseInt(cursor.getString(0)));
                notification.setNotification(cursor.getString(1));
                notification.setNoticationdescription(cursor.getString(2));
                notification.setNotidate(cursor.getString(3));
                // Adding contact to list
                notificationlist.add(notification);
            } while (cursor.moveToNext());
        }

        // return contact list
        return notificationlist;
    }


    // Updating single contact
    public int updateContact(GetProfile contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFICATION, contact.getNotification());

        // updating row
        return db.update(TABLE_NOTIFICATION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }

    // Deleting single contact
    public void deleteContact(GetProfile contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getPosition()) });
        db.close();
    }

    public void deleteOldNotifications(String formattedString) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, KEY_NOTIFICATION_DATE + " < ?", new String[] { String.valueOf(formattedString.trim()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}