package de.hs.inform.lyuz.cookbook.utils;

import de.hs.inform.lyuz.cookbook.controller.convert.CmlToEpubObject;
import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XalanHelper {

    private static int title = 1;
    private static int servQty = 1;
    private static HashMap<String, String> content = new HashMap<>();
    public static HashMap<String, String> refTitle = new HashMap<>();

    public static String getRefTitle(String title) {
        for (String refKey : refTitle.keySet()) {
            if (refKey.equals(title)) {
                return refTitle.get(refKey);
            }
        }
        return "";
    }

    public static String getId() {
        return "p" + title;
    }

    public static int getTitle() {
        return title++;
    }

    public static void setTitle(int title) {
        XalanHelper.title = title;
    }

    public static int isHasPic() {
        return ExportInfo.hasPic ? 1 : 0;
//        return 1;
    }

    public static int isHasSource() {
        return ExportInfo.hasSource ? 1 : 0;
//        return 1;
    }

    public static int isHasRemark() {
        return ExportInfo.hasRemark ? 1 : 0;
//        return 1;
    }

    public static int isHasTime() {
        return ExportInfo.hasTime ? 1 : 0;
//        return 1;
    }

    public static int isHasDiffculty() {
        return ExportInfo.hasDiffculty ? 1 : 0;
//        return 1;
    }

    public static void setContent(String type, String value) {
        content.put(type, value);
    }

    public static String getContent() {
        String text = FormatHelper.outputContent(content, servQty);
        content = new HashMap<>();
        return text;
    }

    public static String setQty(String qty) {
        if (qty != null) {
            qty = FormatHelper.formatQTY(Float.valueOf(qty));
        }
        return qty;
    }

    public static void setServQty(String servQty) {
        try {
            int q = Integer.parseInt(servQty);
            XalanHelper.servQty = q;
        } catch (Exception e) {
            XalanHelper.servQty = 1;
            Logger.getLogger(XalanHelper.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Fehler beim Konvert ServQty");
        }
    }
    
    public static String getTime(String cook, String pre, String all){
        String text = "";
        if (cook!=null || pre!=null || all!=null) {
            if(cook!=null && !cook.trim().equals("")){
                text += "Arbeitszeit: "+cook.trim()+" min, ";
            }
            if(pre!=null  && !pre.trim().equals("")){
                text += "Vorbereitungszeit: "+pre.trim()+" min, ";
            }
            if(all!=null  && !all.trim().equals("")){
                text += "Gesamtzeit: "+all.trim()+" min, ";
            }
            if (!text.equals("") && text.length()>2) {
                text = text.substring(0, text.length() - 2)+"\n";
            }
        }
        return text;
    }

}
