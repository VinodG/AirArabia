package com.winit.airarabia.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.winit.airarabia.common.AppConstants;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static SQLiteDatabase database;
	private final Context myContext;
	public static String apstorphe = "'";
	public static String sep = ",";

	public DatabaseHelper(Context context) {
		super(context, AppConstants.DATABASE_NAME, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();

		if (!dbExist) {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(AppConstants.DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public static SQLiteDatabase openDataBase() throws SQLException {
		try {
			// Open the database
			if (database == null) {
				database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null,
						SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
			} else if (!database.isOpen()) {
				database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null,
						SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			return database;
		} catch (Exception e) {
			Log.e("DatabaseHelper", "openDataBase() - " + e.toString());
			return database;
		}
	}

	public static void closedatabase() {
		if (database != null)
			database.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public static DictionaryEntry[][] get(String query_str) {
		DictionaryEntry dir = null;
		String[] columns;
		int index;
		int rowIndex = 0;
		DictionaryEntry[] row_obj = null; // An array of columns and their
											// values
		DictionaryEntry[][] data_arr = null;
		Cursor c;

		if (database != null) {
			try {
				openDataBase();
				c = database.rawQuery(query_str, null);
				if (c.moveToFirst()) {
					rowIndex = 0;
					data_arr = new DictionaryEntry[c.getCount()][];
					do {
						columns = c.getColumnNames();
						row_obj = new DictionaryEntry[columns.length]; // (columns.length);
						for (int i = 0; i < columns.length; i++) {
							dir = new DictionaryEntry();
							dir.key = columns[i];
							index = c.getColumnIndex(dir.key);

							if (dir.key.equals("CardImage") || dir.key.equals("ImagePath")) {
								Log.i("dir.key", "" + dir.key);
								dir.value = c.getBlob(index);
							} else
								dir.value = c.getString(index);
							row_obj[i] = dir;
						}
						data_arr[rowIndex] = row_obj;
						rowIndex++;
					} while (c.moveToNext());
				}
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data_arr;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

}
