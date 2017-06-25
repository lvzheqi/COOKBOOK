package de.hs.inform.lyuz.cookbook.utils;

import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import java.util.HashMap;



public class XalanHelper {

    private static int title = 1;
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
//        return ExportInfo.hasPic ? 1 : 0;
        return 1;
    }

    public static int isHasSource() {
//        return ExportInfo.hasSource ? 1 : 0;
        return 1;
    }

    public static int isHasRemark() {
//        return ExportInfo.hasRemark ? 1 : 0;
        return 1;
    }

    public static int isHasTime() {
//        return ExportInfo.hasTime ? 1 : 0;
        return 1;
    }

    public static int isHasDiffculty() {
//        return ExportInfo.hasDiffculty ? 1 : 0;
        return 1;
    }

    public static void setContent(String type, String value) {
        content.put(type, value);
    }

    public static String getContent() {
        String text = "";
        int i = 1;

        for (String unit : content.keySet()) {

            String[] numbers = content.get(unit).split(",");
            Float unitvalue;
            String contunit;
            if (numbers.length == 2) {
                int nlength = numbers[1].length();
                unitvalue = Float.parseFloat(numbers[1]) / (float) Math.pow(10.0, nlength);
                unitvalue += Float.parseFloat(numbers[0]);
            } else {
                unitvalue = Float.parseFloat(numbers[0]);
            }

            switch (unit) {
                case "GCAL":
                    contunit = unitvalue.intValue() + "kcal";
                    break;
                case "GKB":
                    contunit = unitvalue + "BE";
                    break;
                case "GJ":
                    contunit = unitvalue.intValue() + "kJ";
                    break;
                case "ZE":
                    unitvalue = unitvalue / 1000.0f;
                    contunit = unitvalue.intValue() + "g Eiweiß";
                    break;
                case "ZF":
                    unitvalue = unitvalue / 1000.0f;
                    contunit = unitvalue.intValue() + "g Fett";
                    break;
                case "ZK":
                    unitvalue = unitvalue / 1000.0f;
                    contunit = unitvalue.intValue() + "g Kohlenhydrate";
                    break;
                default:
                    contunit = unitvalue.intValue() + unit;
                    break;
            }

            if (i < content.size()) {
                text += contunit + ", ";
                i++;
            } else {
                text += contunit;
            }
        }

        content = new HashMap<>();
        return text;
    }

    public static String setQty(String qty) {
        if (qty != null) {
            qty = FormatHelper.formatQTY(Float.valueOf(qty));
        }
        return qty;
    }

}
