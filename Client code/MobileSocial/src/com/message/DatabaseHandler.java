package com.message;

import java.util.ArrayList;

import com.configs.ConstantValues;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper
{
	private static final String DEBUG_TAG = "______DatabaseHandler";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ChatApplication";
	private static final String TABLE_NAME = "message_table";
	
	public static final String KEY_ID = "id";
	public static final String KEY_FROM_USER_ID = "from_user_id";
	public static final String KEY_TO_USER_ID = "to_user_id";
	public static final String KEY_MESSAGE_CONTENT = "message_content";
	public static final String KEY_MESSAGE_TYPE = "message_type";
	public static final String KEY_DATE = "date";
	public static final String KEY_READ_FLAG = "read_flag";


	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.i(DEBUG_TAG, "onCreate...");
	    
		String CREATE_DIALOG_TABLE = 
				String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
				" %s INTEGER, %s INTEGER, %s BLOB, %s INTEGER, %s TEXT, %s INTEGER);", 
				TABLE_NAME, KEY_ID,
				KEY_FROM_USER_ID,KEY_TO_USER_ID,KEY_MESSAGE_CONTENT,KEY_MESSAGE_TYPE,KEY_DATE,KEY_READ_FLAG);
	    db.execSQL(CREATE_DIALOG_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.i(DEBUG_TAG, "onUpgrade..."); 
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i(DEBUG_TAG, "Move version from " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
        onCreate(db);
	}
	
	/*		Create, Read, Update and Delete		*/
	public void insertMessage(AbstractMessage message)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();

		message.setID(getNextID(db));
		//values.put(KEY_ID, message.getID());
		values.put(KEY_FROM_USER_ID, message.getFromUserID());
		values.put(KEY_TO_USER_ID, message.getToUserID());
		values.put(KEY_MESSAGE_CONTENT, message.getContent());
		values.put(KEY_MESSAGE_TYPE, message.getMessageType());
		values.put(KEY_DATE, message.getDate());
		values.put(KEY_READ_FLAG, message.hasBeenRead() ? 1 : 0);
		
		db.insert(TABLE_NAME, null, values);
		
		db.close();
	}
	
	private int getNextID(SQLiteDatabase db)
	{
		String query = "select * FROM SQLITE_SEQUENCE";
		Cursor cursor = db.rawQuery(query, null);
		
		int id = 5;
		if (cursor.moveToFirst())
			id = cursor.getInt(1) + 1;
		cursor.close();
		
		return id;
	}
	
	public AbstractMessage getMessage(int messageID)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		AbstractMessage message = null;
		Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID,KEY_FROM_USER_ID,KEY_TO_USER_ID,
				KEY_MESSAGE_CONTENT,KEY_MESSAGE_TYPE,KEY_DATE,KEY_READ_FLAG}, 
				KEY_ID + "=?", new String [] {String.valueOf(messageID)}, null, null, null, null);
		if (cursor != null && cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			int msgID = cursor.getInt(0);
			int fromID = cursor.getInt(1);
			int toID = cursor.getInt(2);
			byte [] content = cursor.getBlob(3);
			int msgType = cursor.getInt(4);
			String date = cursor.getString(5);
			boolean	isRead = (cursor.getInt(6) == 1);
			
			switch (msgType) 
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				message = new AudioMessage(msgID, fromID, toID, content, date, isRead);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				message = new ImageMessage(msgID, fromID, toID, content, date, isRead);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				message = new TextMessage(msgID, fromID, toID, content, date, isRead);
				break;
			default:
				Log.e("______", "error");
				break;
			}
		}
		cursor.close();
		db.close();
		
		
		return message;
	}
	
	public int updateMessage(AbstractMessage message)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_FROM_USER_ID, message.getFromUserID());
		values.put(KEY_TO_USER_ID, message.getToUserID());
		values.put(KEY_MESSAGE_CONTENT, message.getContent());
		values.put(KEY_MESSAGE_TYPE, message.getMessageType());
		values.put(KEY_DATE, message.getDate());
		values.put(KEY_READ_FLAG, message.hasBeenRead() ? 1 : 0);
		
		int status =  db.update(TABLE_NAME, values, KEY_ID + "=?", new String [] {String.valueOf(message.getID())});
		db.close();
		
		return status;
	}
	
	public void deleteMessage(AbstractMessage message)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_NAME, KEY_ID + " =? ", new String [] {String.valueOf(message.getID())});
		db.close();
	}
	
	
	private ArrayList<AbstractMessage> getMessagesBetween(int fromUserID, int toUserID)
	{
		ArrayList<AbstractMessage> messageList = new ArrayList<AbstractMessage>();
		
		String selectQuery = String.format("SELECT * FROM %s WHERE %s = %s AND %s = %s ", 
				TABLE_NAME, KEY_FROM_USER_ID, String.valueOf(fromUserID), KEY_TO_USER_ID, 
				String.valueOf(toUserID));
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst())
		{
			do
			{
				AbstractMessage message = null;
				
				int msgID = cursor.getInt(0);
				int fromID = cursor.getInt(1);
				int toID = cursor.getInt(2);
				byte [] content = cursor.getBlob(3);
				int msgType = cursor.getInt(4);
				String date = cursor.getString(5);
				boolean	isRead = (cursor.getInt(6) == 1);
				
				switch (msgType) 
				{
				case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
					message = new AudioMessage(msgID, fromID, toID, content, date, isRead);
					break;
				case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
					message = new ImageMessage(msgID, fromID, toID, content, date, isRead);
					break;
				case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
					message = new TextMessage(msgID, fromID, toID, content, date, isRead);
					break;
				default:
					Log.e(DEBUG_TAG, "error");
					break;
				}
				
				messageList.add(message);
			}while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		
		return messageList;
	}
	
	public ArrayList<AbstractMessage> getAllMsg(int fromUserID, int toUserID)
	{
		ArrayList<AbstractMessage> messageList = getMessagesBetween(fromUserID, toUserID);
		ArrayList<AbstractMessage> list = getMessagesBetween(toUserID, fromUserID);
		messageList.addAll(list);
		
		return messageList;
	}
	
	public void eraseDatabase()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
}
