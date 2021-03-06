package de.hs.inform.lyuz.cookbook.model;

import java.io.Serializable;

public class ExportInfo implements Serializable{

    private String firstName;
    private String lastName;
    private String title;

    public static boolean hasPic;
    private boolean isColor;

    public static boolean hasCat;
    public static boolean hasIndex;
    public static boolean hasSource;
    public static boolean hasRemark;
    public static boolean hasTime;
    public static boolean hasQuality;

    private boolean hasCover;
    private String coverPath;
    
    private String bookname;
    private String exportType;
    private String filepath;

    public ExportInfo() {
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public boolean isIsColor() {
        return isColor;
    }

    public void setIsColor(boolean isColor) {
        this.isColor = isColor;
    }

    public boolean isHasCat() {
        return hasCat;
    }

    public void setHasCat(boolean hasCat) {
        this.hasCat = hasCat;
    }

    public boolean isHasIndex() {
        return hasIndex;
    }

    public void setHasIndex(boolean hasIndex) {
        this.hasIndex = hasIndex;
    }

    public boolean isHasSource() {
        return hasSource;
    }

    public void setHasSource(boolean hasSource) {
        this.hasSource = hasSource;
    }

    public boolean isHasRemark() {
        return hasRemark;
    }

    public void setHasRemark(boolean hasRemark) {
        this.hasRemark = hasRemark;
    }

    public boolean isHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public boolean isHasQuality() {
        return hasQuality;
    }

    public void setHasQuality(boolean hasQuality) {
        this.hasQuality = hasQuality;
    }

    public boolean isHasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    

    public void updateBookInfo(String firstName, String lastName, String title, boolean hasPic, boolean isColor,
            boolean hasCat, boolean hasIndex, boolean hasSource, boolean hasRemark, boolean hasTime,
            boolean hasQuality, boolean hasCover, String coverPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;

        this.hasPic = hasPic;
        this.isColor = isColor;

        this.hasCat = hasCat;
        this.hasIndex = hasIndex;
        this.hasSource = hasSource;
        this.hasRemark = hasRemark;
        this.hasTime = hasTime;
        this.hasQuality = hasQuality;

        this.hasCover = hasCover;
        this.coverPath = coverPath;
    }

    public void updateExportInfo(String bookname, String filepath, String type) {
        this.bookname = bookname;
        this.filepath = filepath;
        this.exportType = type;
    }

}
