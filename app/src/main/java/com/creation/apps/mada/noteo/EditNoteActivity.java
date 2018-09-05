package com.creation.apps.mada.noteo;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.creation.apps.mada.noteo.Model.Note;
import com.creation.apps.mada.noteo.Service.NoteService;
import com.creation.apps.mada.noteo.Service.UtilService;

public class EditNoteActivity extends AppCompatActivity {

    private EditText nameText;
    private Button saveNoteBtn;
    private RadioGroup radioGroup;
    private NoteService noteService;
    private Note myNote;
    private  String[] allColor = {
            "Teal",
            "Red",
            "Amber",
            "Indigo",
            "Cyan",
            "Lime",
            "Purple",
            "Grey"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit note");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        noteService = new NoteService(getApplicationContext());
        noteService.open();
        nameText = (EditText) findViewById(R.id.nameText);

        //----------- setNote and parameters
        Bundle extras = getIntent().getExtras();
        long noteId = extras.getLong("noteId");
        myNote = noteService.getNoteById(noteId);
        nameText.setText(myNote.getNamenote());
        //----- set radio button group
        buildRadioGroup();
        //----- add new note
        addNewNote();
    }

    //--------- build radio group
    private void buildRadioGroup(){
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioGroup.LayoutParams radioParams;
        for (int i = 0; i < allColor.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(allColor[i]);
            radioButton.setId(i);
            int colorTheme = getResources().getIdentifier("color"+allColor[i],"color",getPackageName());

            radioButton.setLinkTextColor(ContextCompat.getColor(this,colorTheme));
            radioButton.setTextColor(ContextCompat.getColor(this,colorTheme));
            if(myNote.getThemenote().compareTo(allColor[i])==0){
                radioButton.setChecked(true);
            }
            UtilService.setRadioButtonTint(radioButton,ContextCompat.getColor(this,colorTheme));
            radioParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton,radioParams);
        }
    }


    //--------- add new note
    private void addNewNote(){
        UtilService.setCursorDrawableColor(nameText, R.color.colorTealDark);
        saveNoteBtn = (Button) findViewById(R.id.saveNoteBtn);
        saveNoteBtn.setOnClickListener(saveNoteListener);
    }

    private View.OnClickListener saveNoteListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String nameString = nameText.getText().toString();
            int radioId = radioGroup.getCheckedRadioButtonId();

            if(!nameString.isEmpty()){
                myNote.setNamenote(nameString);
                myNote.setThemenote(allColor[radioId]);
                noteService.updateNoteSQLlite(myNote);
                Intent intent = new Intent(EditNoteActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
