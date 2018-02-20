package com.uptech.SQLite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SQLiteActivity extends Activity {
	OnClickListener listener_add = null;
	OnClickListener listener_update = null;
	OnClickListener listener_delete = null;
	OnClickListener listener_clear = null;
	Button button_add;
	Button button_update;
	Button button_delete;
	Button button_clear;
	DBConnection helper;
	public int id_this;
	public interface UserSchema {
			String TABLE_NAME = "Users";          //Table Name
			String ID = "_id";                    //ID
			String USER_NAME = "user_name";       //User Name
			String ADDRESS = "address";           //Address
			String TELEPHONE = "telephone";       //Phone Number
			String MAIL_ADDRESS = "mail_address"; //Mail Address
		}
	/** Called when the activity is first created. */
    //SQLiteTest主程序
	@Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final EditText mEditText01 = (EditText)findViewById(R.id.EditText01);
		final EditText mEditText02 = (EditText)findViewById(R.id.EditText02);
		final EditText mEditText03 = (EditText)findViewById(R.id.EditText03);
		final EditText mEditText04 = (EditText)findViewById(R.id.EditText04);
		//建立数据库PhoneBookDB和表Table:Users
		helper = new DBConnection(this);
        final SQLiteDatabase db = helper.getWritableDatabase();	
        final String[] FROM = 
		{   
        	UserSchema.ID,
        	UserSchema.USER_NAME,
        	UserSchema.TELEPHONE,
        	UserSchema.ADDRESS, 
        	UserSchema.MAIL_ADDRESS
		};
        //取得所有数据的USER_NAME，放置在list[]上
        Cursor c = db.query(UserSchema.TABLE_NAME, new String[] {UserSchema.USER_NAME}, null, null, null, null, null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];
		for (int i = 0; i < list.length; i++) {
				list[i] = c.getString(0);
				c.moveToNext();
		}
		c.close();
		//显示USER_NAME在Spinner下拉列表-spinner上
		Spinner spinner = (Spinner)findViewById(R.id.Spinner01);
		spinner.setAdapter(new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, list));
		//在Spinner下拉列表-spinner上选定查询的数据，显示所有数据在画面上
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String user_name = ((Spinner)parent).getSelectedItem().toString();
				Cursor c = db.query("Users", FROM , "user_name='" + user_name + "'", null, null, null, null);
				c.moveToFirst();
				id_this = Integer.parseInt(c.getString(0));
				String user_name_this = c.getString(1);
				String telephone_this = c.getString(2);
				String address_this = c.getString(3);
				String mail_address_this = c.getString(4);
				c.close();
				mEditText01.setText(user_name_this);
				mEditText02.setText(telephone_this);
				mEditText03.setText(address_this);
				mEditText04.setText(mail_address_this);
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});    
        //按下[Add]按钮时，新增一行数据
        listener_add = new OnClickListener() {	
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put(UserSchema.USER_NAME, mEditText01.getText().toString());
				values.put(UserSchema.TELEPHONE, mEditText02.getText().toString());
				values.put(UserSchema.ADDRESS, mEditText03.getText().toString());
				values.put(UserSchema.MAIL_ADDRESS, mEditText04.getText().toString());
				SQLiteDatabase db = helper.getWritableDatabase();
				db.insert(UserSchema.TABLE_NAME, null, values);
				db.close();
				onCreate(savedInstanceState);
			}
		};
		//按下[Update]按钮时，更新一行数据
		listener_update = new OnClickListener() {
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put(UserSchema.USER_NAME, mEditText01.getText().toString());
				values.put(UserSchema.TELEPHONE, mEditText02.getText().toString());
				values.put(UserSchema.ADDRESS, mEditText03.getText().toString());
				values.put(UserSchema.MAIL_ADDRESS, mEditText04.getText().toString());
				String where = UserSchema.ID + " = " + id_this;
				SQLiteDatabase db = helper.getWritableDatabase();
				db.update(UserSchema.TABLE_NAME, values, where ,null);
				db.close();
				onCreate(savedInstanceState);
			}
		};
		//按下[Delete]按钮时，刪除一行数据
		listener_delete = new OnClickListener() {
			public void onClick(View v) {
				String where = UserSchema.ID + " = " + id_this;
				SQLiteDatabase db = helper.getWritableDatabase();
				db.delete(UserSchema.TABLE_NAME, where ,null);
				db.close();
				onCreate(savedInstanceState);
			}
		};
		//按下[Clear]按钮时，清空编辑框
		listener_clear = new OnClickListener() {
			public void onClick(View v) {
				mEditText01.setText("");
				mEditText02.setText("");
				mEditText03.setText("");
				mEditText04.setText("");
			}
		};
		//设定BUTTON0i,i=1,2,3,4的OnClickListener
		button_add = (Button)findViewById(R.id.Button01);
		button_add.setOnClickListener(listener_add);
		button_update = (Button)findViewById(R.id.Button02);
		button_update.setOnClickListener(listener_update);
		button_delete = (Button)findViewById(R.id.Button03);
		button_delete.setOnClickListener(listener_delete);	
		button_clear = (Button)findViewById(R.id.Button04);
		button_clear.setOnClickListener(listener_clear);	
    }
    //SQLiteOpenHelper-建立数据库PhoneBookDB和Table:Users
    public static class DBConnection extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "PhoneBookDB";
		private static final int DATABASE_VERSION = 1;
		private DBConnection(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + UserSchema.TABLE_NAME + " (" 
			+ UserSchema.ID  + " INTEGER primary key autoincrement, " 
			+ UserSchema.USER_NAME + " text not null, " 
			+ UserSchema.TELEPHONE + " text not null, " 
			+ UserSchema.ADDRESS + " text not null, "
			+ UserSchema.MAIL_ADDRESS + " text not null "+ ");";
			//Log.i("haiyang:createDB=", sql);
			db.execSQL(sql);	
		}
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub	
		}
	}
}