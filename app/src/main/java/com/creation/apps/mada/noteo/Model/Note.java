package com.creation.apps.mada.noteo.Model;

/**
 * Created by Andrianina_pc on 28/08/2018.
 */

public class Note {

    private long idnote;
    private String namenote;
    private String contentnote;
    private String themenote;

    public Note(long idnote, String namenote, String contentnote, String themenote) {
        this.idnote = idnote;
        this.namenote = namenote;
        this.contentnote = contentnote;
        this.themenote = themenote;
    }

    public Note(){}

    public long getIdnote() {
        return idnote;
    }

    public void setIdnote(long idnote) {
        this.idnote = idnote;
    }

    public String getContentnote() {
        return contentnote;
    }

    public void setContentnote(String contentnote) {
        this.contentnote = contentnote;
    }

    public String getNamenote() {
        return namenote;
    }

    public void setNamenote(String namenote) {
        this.namenote = namenote;
    }

    public String getThemenote() {
        return themenote;
    }

    public void setThemenote(String themenote) {
        this.themenote = themenote;
    }
}
