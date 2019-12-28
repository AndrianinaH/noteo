package com.creation.apps.mada.noteo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;

import com.creation.apps.mada.noteo.Model.Note;
import com.creation.apps.mada.noteo.Service.NoteService;
import com.creation.apps.mada.noteo.Service.UtilService;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private EditText oneNote;
    private Note myNote;
    private List<Note> allNote;
    private NoteService noteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationListener);

        noteService = new NoteService(getApplicationContext());
        noteService.open();

        //----------- remplir le menu sidenav
        allNote = noteService.demarrageOneNote();
        Menu menu = navigationView.getMenu();
        Menu subMenu = menu.addSubMenu("NOTEO");
        for(Note note : allNote) {
            subMenu.add((int)note.getIdnote(),0,0,note.getNamenote());
        }
        navigationView.invalidate();
        //----------- traitement oneNote
        oneNote = (EditText) findViewById(R.id.oneNote);
        gestionOneNote();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //--------- option nav
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_note) {
            Intent intent = new Intent(PrincipalActivity.this, AddNoteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //--------- sidenav dynamic
    //---------- menu navigation
    public NavigationView.OnNavigationItemSelectedListener navigationListener =
        new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String itemTitle = (String)item.getTitle();
                if ( itemTitle.compareTo("Principal note")!=0) {
                    long id = (long) item.getGroupId();
                    Intent intent = new Intent(PrincipalActivity.this, OtherNoteActivity.class);
                    intent.putExtra("noteId", id);
                    startActivity(intent);

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        };


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


    private void gestionOneNote(){
        UtilService.setCursorDrawableColor(oneNote, R.color.colorTeal);
        //-------- au début
        myNote =allNote.get(0);
        setTitle(myNote.getNamenote());
        if(myNote.getContentnote().compareTo("First Note") != 0) {
            oneNote.setText(myNote.getContentnote());
        }
        //-------- a chaque changement
        oneNote.addTextChangedListener(oneNoteWatcher);
    }
}
