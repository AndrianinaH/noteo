package com.creation.apps.mada.noteo.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.creation.apps.mada.noteo.Model.Note;
import com.creation.apps.mada.noteo.Persistance.NoteSQLiteHelper;
import com.creation.apps.mada.noteo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrianina_pc on 28/08/2018.
 */

public class NoteService {
    // Champs de la base de données
    private SQLiteDatabase database;
    private NoteSQLiteHelper dbHelper;
    private String[] allColumns = {
            NoteSQLiteHelper.COLUMN_ID,
            NoteSQLiteHelper.COLUMN_NAME,
            NoteSQLiteHelper.COLUMN_CONTENT,
            NoteSQLiteHelper.COLUMN_THEME
    };

    public NoteService(Context context){
        dbHelper = new NoteSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private Note cursorToObject(Cursor cursor) {
        Note note = new Note(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        return note;
    }

    //------------- add
    public void addNoteSQLlite(String namenote,String contentnote,String themenote){
        //----------- persistence
        ContentValues values = new ContentValues();
        values.put(NoteSQLiteHelper.COLUMN_NAME, namenote);
        values.put(NoteSQLiteHelper.COLUMN_CONTENT, contentnote);
        values.put(NoteSQLiteHelper.COLUMN_THEME, themenote);
        long insertId = database.insert(NoteSQLiteHelper.TABLE_NAME, null,
                values);
    }

    //------------- update
    public void updateNoteSQLlite(Note changeNote){
        //----------- persistence
        ContentValues values = new ContentValues();
        values.put(NoteSQLiteHelper.COLUMN_NAME, changeNote.getNamenote());
        values.put(NoteSQLiteHelper.COLUMN_CONTENT, changeNote.getContentnote());
        values.put(NoteSQLiteHelper.COLUMN_THEME, changeNote.getThemenote());
        long updateValue = database.update(NoteSQLiteHelper.TABLE_NAME,values,
                NoteSQLiteHelper.COLUMN_ID+ " = " + changeNote.getIdnote(), null);
    }

    //------------- get by id
    public Note getNoteById(long id) {
        Cursor cursor = database.query(NoteSQLiteHelper.TABLE_NAME,
                allColumns, NoteSQLiteHelper.COLUMN_ID+"="+String.valueOf(id), null, null, null, null);
        cursor.moveToFirst();
        Note note = cursorToObject(cursor);
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return note;
    }

    //------------- get all
    public List<Note> getAllNote() {
        List<Note> notes = new ArrayList<Note>();

        Cursor cursor = database.query(NoteSQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = cursorToObject(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return notes;
    }

    //-------------- delete
    public void deleteNote(long id){
        database.delete(NoteSQLiteHelper.TABLE_NAME, NoteSQLiteHelper.COLUMN_ID + " = " + id, null);
    }


    //----------- métier oneNote
    public List<Note> demarrageOneNote(){
        List<Note> allNote = getAllNote();
        if(allNote.size() == 0){
            addNoteSQLlite("Principal note","First Note","Teal");
            allNote = getAllNote();
            return allNote;
        }else{
            return allNote;
        }
    }

}
