package itcom.cartographer.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Point;
import android.util.Log;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.ArrayList;

import itcom.cartographer.Utils.CoordinateUtils;

public class Database extends SQLiteOpenHelper {

    private Context _context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationHistory";

    private static final String TABLE_LOCATION_HISTORY = "location_history";
    private static final String LH_ID = "lh_id";
    private static final String LH_TIMESTAMP = "lh_timestamp";
    private static final String LH_LATITUDE_E7 = "lh_latitude_e7";
    private static final String LH_LONGITUDE_E7 = "lh_longitude_e7";
    private static final String LH_ACCURACY = "lh_accuracy";
    private static final String LH_VELOCITY = "lh_velocity";
    private static final String LH_HEADING = "lh_heading";
    private static final String LH_ALTITUDE = "lh_altitude";
    private static final String LH_VERTICAL_ACCURACY = "lh_vertical_accuracy";

    private static final String TABLE_ACTIVITIES = "activities";
    private static final String AC_ID = "ac_id";
    private static final String AC_TIMESTAMP = "ac_timestamp";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_LOCATION_HISTORY + "(" +
                LH_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LH_TIMESTAMP + " BIGINT NOT NULL, " +
                LH_LATITUDE_E7 + " BIGINT NOT NULL, " +
                LH_LONGITUDE_E7 + " BIGINT_NOT NULL, " +
                LH_ACCURACY + " INTEGER NOT NULL, " +
                LH_VELOCITY + " INTEGER, " +
                LH_HEADING + " INTEGER, " +
                LH_ALTITUDE + " INTEGER, " +
                LH_VERTICAL_ACCURACY + " INTEGER" +
                ");";
        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE " + TABLE_ACTIVITIES + "(" +
                AC_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                AC_TIMESTAMP + " BIGINT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_HISTORY);
        onCreate(sqLiteDatabase);
    }

    public int getDatapointCount() {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_LOCATION_HISTORY);
    }

    public String getFavouritePlaces() {
        String latestAddress = "";
        String query = "SELECT " + LH_LATITUDE_E7 + ", " + LH_LONGITUDE_E7 + " FROM " + TABLE_LOCATION_HISTORY + " WHERE " + LH_TIMESTAMP + " BETWEEN 1517296083822 AND 1518790784697";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Point> coordinates = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                int lat = cursor.getInt(cursor.getColumnIndex("lh_latitude_e7"));
                int lon = cursor.getInt(cursor.getColumnIndex("lh_longitude_e7"));
                coordinates.add(new Point(lat, lon));
            }
        } finally {
            cursor.close();
        }

        //this is just a test of the getDistanceBetweenTwoPoints method
        System.out.println(CoordinateUtils.getDistanceBetweenTwoPoints(55.6482684, 12.5526691, 55.6481274, 12.5526561));

        LatLng latestLocation = new LatLng(coordinates.get(0).x / 10000000, coordinates.get(0).y / 10000000);
        System.out.println(latestLocation);
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyC7w2p0ViSu2MRNbc_RlHRR7rScokSxUGE")
                    .build();
            GeocodingResult[] results = GeocodingApi.reverseGeocode(context, latestLocation).await();
            System.out.println("results");
            System.out.println(results[0].formattedAddress);
            latestAddress = results[0].formattedAddress;
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            latestAddress = "Error";
        }


        return latestAddress;
    }


    public int getActivitesCount() {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_ACTIVITIES);
    }

    public void deleteAllLocationHistoryEntries() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LOCATION_HISTORY, null, null);
    }

    public void deleteAllActivityEntries() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ACTIVITIES, null, null);
    }

    public void addLocationHistoryEntry(ArrayList<LocationHistoryObject> lhObjects) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_LOCATION_HISTORY + " VALUES (?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();

        try {
            for (LocationHistoryObject lhObject : lhObjects) {
                statement.clearBindings();
                statement.bindLong(2, Long.parseLong(lhObject.getTimestampMs()));
                statement.bindLong(3, lhObject.getLatitudeE7());
                statement.bindLong(4, lhObject.getLongitudeE7());
                statement.bindLong(5, lhObject.getAccuracy());
                statement.bindLong(6, lhObject.getVelocity());
                statement.bindLong(7, lhObject.getHeading());
                statement.bindLong(8, lhObject.getAltitude());
                statement.bindLong(9, lhObject.getVerticalAccuracy());
                statement.execute();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Database Error", e.getMessage());
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}