package de.hs.inform.lyuz.cookbook.help;


public class ExportInfo {
    public static String firstName = "Rossmann";
    public static String lastName = "Tester";
    public static String title = "CookBook";
    
    public static boolean hasPic = true;
    public static boolean isColor = true;
    
    public static boolean hasCat = true;
    public static boolean hasIndex = false;
    public static boolean hasSource = false;
    public static boolean hasRemark = false;
    public static boolean hasTime = false;
    public static boolean hasDiffculty = false;
    
    public static boolean hasCover = false;
    public static String coverPath = null;
    public static String exportTyp = "";
    
    public static String bookname = "cookbook";
    public static String filepath = "";
    
    public static void updateBookInfo(String firstName, String lastName, String title, boolean hasPic, boolean isColor,
            boolean hasCat, boolean hasIndex, boolean hasSource, boolean hasRemark, boolean hasTime, 
            boolean hasDiffculty, boolean hasCover, String coverPath) {
        ExportInfo.firstName = firstName;
        ExportInfo.lastName = lastName;
        ExportInfo.title = title;
        
        ExportInfo.hasPic = hasPic;
        ExportInfo.isColor = isColor;
        
        ExportInfo.hasCat = hasCat;
        ExportInfo.hasIndex = hasIndex;
        ExportInfo.hasSource = hasSource;
        ExportInfo.hasRemark = hasRemark;
        ExportInfo.hasTime = hasTime;
        ExportInfo.hasDiffculty = hasDiffculty;
        
        ExportInfo.hasCover = hasCover;
        ExportInfo.coverPath = coverPath;
    }
    
    public static void updateExportInfo(String bookname, String filepath) {
        ExportInfo.bookname = bookname;
        ExportInfo.filepath = filepath;
    }    
    
}
