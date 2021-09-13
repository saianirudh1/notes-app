package com.example.notes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 2)
public abstract class NoteDatabase extends RoomDatabase {

    public static NoteDatabase instance;

    public abstract NoteDAO noteDAO();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDAO noteDAO;
        private PopulateDBAsyncTask(NoteDatabase noteDatabase){
            this.noteDAO = noteDatabase.noteDAO();
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(new Note("Title1","Description1"));
            noteDAO.insert(new Note("Title2","Description2"));
            noteDAO.insert(new Note("Title3","Description3"));
            return null;
        }
    }
}
