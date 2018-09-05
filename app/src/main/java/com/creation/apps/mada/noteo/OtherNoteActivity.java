package com.creation.apps.mada.noteo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.creation.apps.mada.noteo.Model.Note;
import com.creation.apps.mada.noteo.Service.NoteService;
import com.creation.apps.mada.noteo.Service.UtilService;

public class OtherNoteActivity extends AppCompatActivity {

    private EditText oneNote;
    private Note myNote;
    private NoteService noteService;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        noteService = new NoteService(getApplicationContext());
        noteService.open();

        //----------- setNote and parameters
        Bundle extras = getIntent().getExtras();
        long noteId = extras.getLong("noteId");
        myNote = noteService.getNoteById(noteId);
        setTitle(myNote.getNamenote());

        //----------- set theme color
        int noteTheme = getResources().getIdentifier("AppTheme"+myNote.getThemenote(),"style",getPackageName());
        setTheme(noteTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_other_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        oneNote = (EditText) findViewById(R.id.oneNote);
        setAllColor();

        //----------- traitement oneNote
        gestionOneNote();

    }

    //---------------- Gestion OneNote
    private TextWatcher oneNoteWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String oneNoteText = charSequence.toString();
            myNote.setContentnote(oneNoteText);
            noteService.updateNoteSQLlite(myNote);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };

    private void setAllColor(){
        int noteColor = getResources().getIdentifier("color"+myNote.getThemenote(),"color",getPackageName());
        int noteDarkColor = getResources().getIdentifier("color"+myNote.getThemenote()+"Dark","color",getPackageName());
        int noteAccentColor = getResources().getIdentifier("color"+myNote.getThemenote()+"Accent","color",getPackageName());

        mainLayout.setBackgroundColor(ContextCompat.getColor(this,noteAccentColor));
        oneNote.setTextColor(ContextCompat.getColor(this,noteDarkColor));
        oneNote.setHintTextColor(ContextCompat.getColor(this,noteColor));
        oneNote.setLinkTextColor(ContextCompat.getColor(this,noteColor));
        UtilService.setCursorDrawableColor(oneNote,ContextCompat.getColor(this,noteDarkColor));
    }

    private void gestionOneNote(){
        //-------- au début
        oneNote.setText(myNote.getContentnote());
        //-------- a chaque changement
        oneNote.addTextChangedListener(oneNoteWatcher);
    }

    //--------- option nav
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_note, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_note) {
            Intent intent = new Intent(OtherNoteActivity.this, EditNoteActivity.class);
            intent.putExtra("noteId",myNote.getIdnote());
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_delete_note) {
            noteService.deleteNote(myNote.getIdnote());
            Intent intent = new Intent(OtherNoteActivity.this, PrincipalActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
