package me.xaanit.apparatus.internal.json.embeds;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Field {
    private String fieldTitle = "";
    private String fieldValue = "";
    private boolean inline = false;

    public Field() {
    }

    public Field(String title, String value, boolean inline) {
        this.fieldTitle = title;
        this.fieldValue = value;
        this.inline = inline;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public String toString() {
        return "[title=" + fieldTitle + "[value=" + fieldValue + "[inline=" + inline + "]]]";
    }
}
