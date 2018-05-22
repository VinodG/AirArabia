package com.winit.airarabia.dataaccess;

import java.util.ArrayList;
import java.util.Vector;

import com.winit.airarabia.AirArabiaApp;
import com.winit.airarabia.database.DatabaseHelper;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.objects.OriginsDO;
import com.winit.airarabia.utils.LogUtils;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class AirportDA {
	private Context context;

	public AirportDA(Context context) {
		this.context = context;
	}

	public void insertAllAirport(Vector<AirportsDO> list) {
		{
			// ArrayList<AirportsDO> list = new ArrayList<AirportsDO>(vlist);
			SQLiteDatabase db = null;
			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				SQLiteStatement stmtInst = db.compileStatement(
						"INSERT INTO tblAirport(code,en,ar,fr,service_type1,name,countryname,countryid,language,currency) VALUES(?,?,?,?,?,?,?,?,?,?)");
				for (AirportsDO obj : list) {
					stmtInst.bindString(1, obj.code);
					stmtInst.bindString(2, obj.en);
					stmtInst.bindString(3, obj.ar);
					stmtInst.bindString(4, obj.fr);
					stmtInst.bindString(5, obj.service_type1);
					stmtInst.bindString(6, obj.name);
					stmtInst.bindString(7, obj.countryname);
					stmtInst.bindString(8, obj.countryid);
					stmtInst.bindString(9, obj.language);
					stmtInst.bindString(10, obj.currency);
					stmtInst.execute();
				}
				if (stmtInst != null)
					stmtInst.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}

	}

	public Vector<AirportsDO> getAllAirport() {
		Vector<AirportsDO> list = new Vector<AirportsDO>();
		AirportsDO obj = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		String query = "select * from tblAirport";
		{

			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst()) {
					do {
						obj = new AirportsDO();
						obj.code = cursor.getString(cursor.getColumnIndex("code"));
						obj.en = cursor.getString(cursor.getColumnIndex("en"));
						obj.ar = cursor.getString(cursor.getColumnIndex("ar"));
						obj.fr = cursor.getString(cursor.getColumnIndex("fr"));
						obj.service_type1 = cursor.getString(cursor.getColumnIndex("service_type1"));
						obj.name = cursor.getString(cursor.getColumnIndex("name"));
						obj.countryname = cursor.getString(cursor.getColumnIndex("countryname"));
						obj.countryid = cursor.getString(cursor.getColumnIndex("countryid"));
						obj.language = cursor.getString(cursor.getColumnIndex("language"));
						obj.currency = cursor.getString(cursor.getColumnIndex("currency"));
						list.add(obj);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {

				LogUtils.errorLog("DB Trace", e.getMessage() + "");
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
		return list;
	}

	public void deleteAllAirport() {
		SQLiteDatabase sqLiteDatabase = null;
		String query = "";

		{
			try {
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				sqLiteDatabase = DatabaseHelper.openDataBase();

				query = "DELETE  FROM tblAirport";
				// Cursor cursor = sqLiteDatabase .rawQuery(query, null);
				int x = sqLiteDatabase.delete("tblAirport", null, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ALL AllAirport" + e.getMessage());
			} finally {
				if (sqLiteDatabase != null)
					sqLiteDatabase.close();
				DatabaseHelper.closedatabase();
			}
		}
	}

	// ******************************************Airport
	// name************************************************
	public void insertAllAirportName(Vector<AirportNamesDO> list) {
		{
			// ArrayList<AirportsDO> list = new ArrayList<AirportsDO>(vlist);
			SQLiteDatabase db = null;
			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				SQLiteStatement stmtInst = db
						.compileStatement("INSERT INTO tblAllAirportName(code,en,ar,fr,name) VALUES(?,?,?,?,?)");
				for (AirportNamesDO obj : list) {
					stmtInst.bindString(1, obj.code);
					stmtInst.bindString(2, obj.en);
					stmtInst.bindString(3, obj.ar);
					stmtInst.bindString(4, obj.fr);
					stmtInst.bindString(5, obj.name);
					stmtInst.execute();
				}
				if (stmtInst != null)
					stmtInst.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}

	}

	public Vector<AirportNamesDO> getAllAirportName() {
		Vector<AirportNamesDO> list = new Vector<AirportNamesDO>();
		AirportNamesDO obj = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		String query = "select * from tblAllAirportName";
		{

			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst()) {
					do {
						obj = new AirportNamesDO();
						obj.code = cursor.getString(cursor.getColumnIndex("code"));
						obj.en = cursor.getString(cursor.getColumnIndex("en"));
						obj.ar = cursor.getString(cursor.getColumnIndex("ar"));
						obj.fr = cursor.getString(cursor.getColumnIndex("fr"));
						obj.name = cursor.getString(cursor.getColumnIndex("name"));
						list.add(obj);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {

				LogUtils.errorLog("DB Trace", e.getMessage() + "");
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
		return list;
	}

	public void deleteAllAirportName() {
		SQLiteDatabase sqLiteDatabase = null;
		String query = "";

		{
			try {
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				sqLiteDatabase = DatabaseHelper.openDataBase();

				query = "DELETE  FROM tblAllAirportName";
				// Cursor cursor = sqLiteDatabase .rawQuery(query, null);
				int x = sqLiteDatabase.delete("tblAllAirportName", null, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ALL tblAllAirportName" + e.getMessage());
			} finally {
				if (sqLiteDatabase != null)
					sqLiteDatabase.close();
				DatabaseHelper.closedatabase();
			}
		}
	}

	// ********************************currency
	// data*****************************************
	public void insertCurrency(ArrayList<CurrencyDo> list) {
		{
			// ArrayList<AirportsDO> list = new ArrayList<AirportsDO>(vlist);
			SQLiteDatabase db = null;
			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				SQLiteStatement stmtInst = db.compileStatement(
						"INSERT INTO tblCurrency(code,en,ar,fr,ru,es,it,tr,zh) VALUES(?,?,?,?,?,?,?,?,?)");
				for (CurrencyDo obj : list) {
					stmtInst.bindString(1, obj.code);
					stmtInst.bindString(2, obj.en);
					stmtInst.bindString(3, obj.ar);
					stmtInst.bindString(4, obj.fr);
					stmtInst.bindString(5, obj.ru);
					stmtInst.bindString(6, obj.es);
					stmtInst.bindString(7, obj.it);
					stmtInst.bindString(8, obj.tr);
					stmtInst.bindString(9, obj.zh);
					stmtInst.execute();
				}
				if (stmtInst != null)
					stmtInst.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
	}

	public ArrayList<CurrencyDo> getCurrency() {
		ArrayList<CurrencyDo> list = new ArrayList<CurrencyDo>();
		CurrencyDo obj = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		String query = "select * from tblCurrency";
		{

			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst()) {
					do {
						obj = new CurrencyDo();
						obj.code = cursor.getString(cursor.getColumnIndex("code"));
						obj.en = cursor.getString(cursor.getColumnIndex("en"));
						obj.ar = cursor.getString(cursor.getColumnIndex("ar"));
						obj.fr = cursor.getString(cursor.getColumnIndex("fr"));
						obj.ru = cursor.getString(cursor.getColumnIndex("ru"));
						obj.es = cursor.getString(cursor.getColumnIndex("es"));
						obj.it = cursor.getString(cursor.getColumnIndex("it"));
						obj.tr = cursor.getString(cursor.getColumnIndex("tr"));
						obj.zh = cursor.getString(cursor.getColumnIndex("zh"));
						list.add(obj);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {

				LogUtils.errorLog("DB Trace", e.getMessage() + "");
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
		return list;
	}

	public void deleteCurrency() {
		SQLiteDatabase sqLiteDatabase = null;
		String query = "";

		{
			try {
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				sqLiteDatabase = DatabaseHelper.openDataBase();

				query = "DELETE  FROM tblCurrency";
				// Cursor cursor = sqLiteDatabase .rawQuery(query, null);
				int x = sqLiteDatabase.delete("tblCurrency", null, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ALL tblCurrency" + e.getMessage());
			} finally {
				if (sqLiteDatabase != null)
					sqLiteDatabase.close();
				DatabaseHelper.closedatabase();
			}
		}
	}

	// **************************origins***************************************
	public void insertOrigins(ArrayList<OriginsDO> list) {
		{
			// ArrayList<AirportsDO> list = new ArrayList<AirportsDO>(vlist);
			SQLiteDatabase db = null;
			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				SQLiteStatement stmtInst = db.compileStatement(
						"INSERT INTO tblOrigins(airport_num,flight_type1,currency_num,flight_type2,noOfStops,airport_name) VALUES(?,?,?,?,?,?)");
				for (OriginsDO obj : list) {
					stmtInst.bindLong(1, obj.airport_num);
					stmtInst.bindString(2, obj.flight_type1);
					stmtInst.bindLong(3, obj.currency_num);
					stmtInst.bindString(4, obj.flight_type2);
					stmtInst.bindLong(5, obj.noOfStops);
					stmtInst.bindString(6, obj.airport_name);

					stmtInst.execute();
				}
				if (stmtInst != null)
					stmtInst.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
	}

	public ArrayList<OriginsDO> getOrigins() {
		ArrayList<OriginsDO> list = new ArrayList<OriginsDO>();
		OriginsDO obj = null;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		String query = "select * from tblOrigins";
		{

			try {
				DatabaseHelper helper = new DatabaseHelper(context);
				db = helper.openDataBase();
				cursor = db.rawQuery(query, null);

				if (cursor != null && cursor.moveToFirst()) {
					do {
						obj = new OriginsDO();
						obj.airport_num = cursor.getInt(cursor.getColumnIndex("airport_num"));
						obj.flight_type1 = cursor.getString(cursor.getColumnIndex("flight_type1"));
						obj.currency_num = cursor.getInt(cursor.getColumnIndex("currency_num"));
						obj.flight_type2 = cursor.getString(cursor.getColumnIndex("flight_type2"));
						obj.noOfStops = cursor.getInt(cursor.getColumnIndex("noOfStops"));
						obj.airport_name = cursor.getString(cursor.getColumnIndex("airport_name"));
						list.add(obj);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {

				LogUtils.errorLog("DB Trace", e.getMessage() + "");
			} finally {
				if (cursor != null)
					cursor.close();
				if (db != null)
					db.close();
				DatabaseHelper.closedatabase();
			}
		}
		return list;
	}

	public void deleteOrigins() {
		SQLiteDatabase sqLiteDatabase = null;
		String query = "";

		{
			try {
				DatabaseHelper dbHelper = new DatabaseHelper(context);
				sqLiteDatabase = DatabaseHelper.openDataBase();

				query = "DELETE  FROM tblCurrency";
				// Cursor cursor = sqLiteDatabase .rawQuery(query, null);
				int x = sqLiteDatabase.delete("tblOrigins", null, null);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ALL tblOrigins" + e.getMessage());
			} finally {
				if (sqLiteDatabase != null)
					sqLiteDatabase.close();
				DatabaseHelper.closedatabase();
			}
		}
	}
}
