package me.xaanit.apparatus.internal.json.embeds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 5/15/2017.
 */
public class CustomEmbed {
    private String authorIcon = "";
    private String authorName = "";
    private String authorURL = "";
    private String thumbnail = "";
    private String title = "";
    private String titleURL = "";
    private String desc = "";
    private List<Field> fields = new ArrayList<>();
    private String image = "";
    private String colorHex = "";
    private String footerIcon = "";
    private String footerText = "";
    private boolean includeTimestamp = false;

    public CustomEmbed() {
    }

    @Override
    public String toString() {
        return "CustomEmbed{" +
                "authorIcon='" + authorIcon + '\'' + "\n" +
                ", authorName='" + authorName + '\'' + "\n"+
                ", authorURL='" + authorURL + '\'' + "\n"+
                ", thumbnail='" + thumbnail + '\'' + "\n"+
                ", title='" + title + '\'' + "\n"+
                ", titleURL='" + titleURL + '\'' + "\n"+
                ", desc='" + desc + '\'' + "\n"+
                ", fields=" + fields + "\n"+
                ", image='" + image + '\'' + "\n"+
                ", colorHex='" + colorHex + '\'' + "\n"+
                ", footerIcon='" + footerIcon + '\'' + "\n"+
                ", footerText='" + footerText + '\'' + "\n"+
                ", includeTimestamp=" + includeTimestamp + "\n"+
                '}';
    }

    public CustomEmbed(String authorIcon, String authorName, String authorURL, String thumbnail, String title, String titleURL, String desc, List<Field> fields, String image, String colorHex, String footerIcon, String footerText, boolean includeTimestamp) {
        this.authorIcon = authorIcon;
        this.authorName = authorName;
        this.authorURL = authorURL;
        this.thumbnail = thumbnail;
        this.title = title;
        this.titleURL = titleURL;
        this.desc = desc;
        this.fields.addAll(fields);
        this.image = image;
        this.colorHex = colorHex;
        this.footerIcon = footerIcon;
        this.footerText = footerText;
        this.includeTimestamp = includeTimestamp;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleURL() {
        return titleURL;
    }

    public void setTitleURL(String titleURL) {
        this.titleURL = titleURL;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getFooterIcon() {
        return footerIcon;
    }

    public void setFooterIcon(String footerIcon) {
        this.footerIcon = footerIcon;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public boolean isIncludeTimestamp() {
        return includeTimestamp;
    }

    public void setIncludeTimestamp(boolean includeTimestamp) {
        this.includeTimestamp = includeTimestamp;
    }

    public boolean isEmpty() {
        return authorIcon.isEmpty() && authorName.isEmpty() && authorURL.isEmpty() && thumbnail.isEmpty() && title.isEmpty() && titleURL.isEmpty() && desc.isEmpty() && fields.isEmpty() && image.isEmpty() && colorHex.isEmpty() && footerIcon.isEmpty() && footerText.isEmpty();
    }

}

