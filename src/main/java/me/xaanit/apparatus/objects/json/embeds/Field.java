package me.xaanit.apparatus.objects.json.embeds;

/**
 * Created by Jacob on 5/15/2017.
 */
public class Field {
    private String fieldTitle = "";
    private String fieldValue = "";
    private boolean inline = false;

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
}
