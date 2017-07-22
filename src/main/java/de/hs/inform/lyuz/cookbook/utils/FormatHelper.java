package de.hs.inform.lyuz.cookbook.utils;

import de.hs.inform.lyuz.cookbook.logic.creater.CMLCreater;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatHelper {

    // Menge der Zutat
    public static String formatQTY(Float qty) {
//        String qtyString = "";
//        int nenner = 0;
//        double eps = 1.0e-6;
//        int zaehler = (int) qty.floatValue();
//        boolean isrational = false;

//        while (!isrational && (nenner++ < 9)) {
//            zaehler = (int) ((qty) * nenner);
//            isrational = (qty - ((float) zaehler / (float) nenner)) < eps;
//        }
//
//        if ((zaehler / nenner > 0) && (nenner > 4)) {
//            isrational = false;
//        }
//
//        if (isrational) {
//            if (nenner == 1) {
//                qtyString = (new Integer(zaehler)).toString();
//            } else {
//                if (zaehler / nenner > 0) {
//                    qtyString = (new Integer(zaehler / nenner)).toString() + " ";
//                    zaehler = zaehler % nenner;
//                }
//                qtyString += (new Integer(zaehler)).toString() + "/" + (new Integer(nenner)).toString();
//
//            }
//        } else {
//            qtyString = qty.toString();
//        }
        int count = 0;
        int base = 10;
        float num = (float) (Math.round((qty - qty.intValue()) * 100) / 100.0);
        if (num == 0) {
            return qty.intValue() + "";
        }
        while (num != Math.floor(num)) {
            num *= base;
            count++;
        }

        base = (int) Math.pow(base, count);
        int nor = (int) num;
        int gcd = findGCD(nor, base);
        if ((nor / gcd) < 10 && (base / gcd) < 10) {
            if (qty.intValue() > 0) {
                return qty.intValue() + " " + String.valueOf(nor / gcd) + "/" + String.valueOf(base / gcd);
            }
            return String.valueOf(nor / gcd) + "/" + String.valueOf(base / gcd);
        }
        return qty.toString();
    }

    private static int findGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return findGCD(b % a, a);
    }

    // Mengeneinheiten
    public static String formatUnitDE(String unit) {
        String unitForm = unit;
        if (unit == null) {
            return unit;
        }
        switch (unit) {
            case "Be":
                unitForm = "Becher";
                break;
            case "Bd":
            case "b":
            case "bn":
                unitForm = "Bund";
                break;
            case "cn":
            case "Do":
                unitForm = "Dose(n)";
                break;
            case "tb":
            case "T":
            case "tbl.":
            case "tbs.":
            case "tbsp.":
            case "EL":
                unitForm = "El";
                break;
            case "fl":
            case "oz.fl.":
                unitForm = "fl.oz.";
                break;
            case "gal":
            case "gal.":
                unitForm = "Gallon";
                break;
            case "Gl":
                unitForm = "Glas";
                break;
            case "lg":
            case "gr":
                unitForm = "groß.";
                break;
            case "ct":
            case "Kt":
                unitForm = "Karton";
                break;
            case "sm":
            case "kl":
                unitForm = "klein.";
                break;
            case "pn":
            case "pr":
            case "Ms":
            case "Msp":
                unitForm = "Prise";
                break;
            case "ccm":
                unitForm = "ml";
                break;
            case "md":
            case "mi":
            case "mt":
                unitForm = "mittl.";
                break;
            case "pk":
            case "Pk":
            case "Pck":
                unitForm = "Packung";
                break;
            case "p":
            case "pt.":
            case "pt":
                unitForm = "Pint";
                break;
            case "ea":
            case "pP":
                unitForm = "pro Portion";
                break;
            case "q":
            case "qt":
            case "qt.":
                unitForm = "l";
                break;
            case "lb":
            case "lb.":
            case "#":
            case "Pf.":
            case "pfd":
                unitForm = "U.S.Pound";
                break;
            case "sl":
            case "Sc.":
            case "Sc":
            case "Sch":
                unitForm = "Scheibe(n)";
                break;
            case "ds":
            case "Sp":
            case "Spr":
                unitForm = "Spritzer";
                break;
            case "St":
                unitForm = "Stange";
                break;
            case "Sk":
            case "stck":
            case "stk":
            case "pc":
                unitForm = "Stu ̈ck";
                break;
            case "c":
            case "Ta":
            case "Tas":
                unitForm = "Tasse(n)";
                break;
            case "t":
            case "ts":
            case "tsp.":
            case "BL":
            case "TL":
            case "Teel":
                unitForm = "Tl";
                break;
            case "tc.":
                unitForm = "Teetasse";
                break;
            case "dr":
            case "Tr":
                unitForm = "Tropfen";
                break;
            default:
                break;
        }
        return unitForm;
    }

    public static String outputContent(Head head) {
        int qty = 1;
        if (head.getServingqty() != null) {
            try {
                qty = Integer.valueOf(head.getServingqty());
            } catch (NumberFormatException e) {
                Logger.getLogger(CMLCreater.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("Fehler beim Convert Servingqty zu Integer");
            }
        }

        HashMap<String, String> content = new HashMap<>();
        head.getContent().forEach((cont) -> {
            content.put(cont.getType(), cont.getValue());
        });

        return outputContent(content, qty);
    }

    public static String outputContent(HashMap<String, String> content, int qty) {
        String context = "";
        float kalorien = -1.0f;

        for (String unit : content.keySet()) {
            String value = content.get(unit);
            String contunit = "";
            float unitvalue = Float.parseFloat(deNum2En(value)) / qty;
            if (unit.equals("ZE")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Eiweiß, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + "mg Eiweiß, ";
                }
            } else if (unit.equals("ZF")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Fett, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg Fett, ";
                }
            } else if (unit.equals("ZK")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Kohlenhydrate, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg Kohlenhydrate, ";
                }
            } else if (unit.equals("ZB")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Ballaststoffe, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg Ballaststoffe, ";
                }
            } else if (unit.equals("ZA")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Alkohol, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg Alkohol, ";
                }
            } else if (unit.equals("FC")) {
                if (unitvalue / 1000f > 1.0f) {

                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g Cholesterin, ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg Cholesterin, ";
                }
            } else if (unit.startsWith("Z") || unit.startsWith("E") || unit.startsWith("F")) {
                if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " g " + unit + ", ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " mg " + unit + ", ";
                }
            } else if (unit.startsWith("V")) {
                if (unitvalue / 1000000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000000.0)) + " g " + unit + ", ";
                } else if (unitvalue / 1000f > 1.0f) {
                    contunit = String.valueOf((int) (unitvalue / 1000.0)) + " mg " + unit + ", ";
                } else {
                    contunit = String.valueOf((int) (unitvalue)) + " μg " + unit + ", ";
                }
            } else if (unit.equals("GKB")) {
                contunit = String.valueOf((int) (unitvalue * 100 / 100)) + " BE, ";
            } else if (unit.equals("GJ")) {
                contunit = String.valueOf((int) (unitvalue)) + " kJ, ";
            } else {
                contunit = String.valueOf((int) (unitvalue)) + " " + unit + ", ";
            }

            if (unit.equals("GCAL")) {
                kalorien = unitvalue;
            }

            context += contunit;
        }
        if (kalorien >= 0) {
            context += String.valueOf((int) kalorien) + " kcal";
        } else if (!context.equals("")) {
            context = context.substring(0, context.length() - 2);
        }
        return context;
    }

    public static String outputCategories(String cat) {
        String output = cat;
        if (cat.equals("SALAT")) {
            output = "Salate";
        } else if (cat.equals("SUPPE")) {
            output = "Suppen und Eintöpfe";
        } else if (cat.equals("AUFLAUF")) {
            output = "Aufläufe und Gratins";
        } else if (cat.equals("FISCH")) {
            output = "Fischgerichte";
        } else if (cat.equals("FLEISCH")) {
            output = "Fleischgerichte";
        } else if (cat.equals("GEFLÜGEL")) {
            output = "Geflügelgerichte";
        } else if (cat.equals("Gemüse")) {
            output = "Kartoffel- und Gemüsegerichte";
        } else if (cat.equals("VEGETARISCH")) {
            output = "Vegetarisches";
        } else if (cat.equals("SAUCE")) {
            output = "Saucen, Marinaden und Dips";
        } else if (cat.equals("NACHTISCH")) {
            output = "Süßes";
        } else if (cat.equals("BACKEN")) {
            output = "Kuchen und Gebäck";
        } else if (cat.startsWith("DRINK")) {
            output = "Getränke";
        } else if (cat.startsWith("PASTA")) {
            output = "Pasteten und Terrinen";
        }
        return output;
    }

    public static String deNum2En(String n) {
        String[] num = reformatLine(n).split(",");
        if (num.length == 2) {
            return num[0] + "." + num[1];
        } else if (num.length == 1) {
            if (n.contains(",")) {
                return num[0];
            }
        }
        return n;
    }

    public static BigInteger setCookTime(String line) {

        if (line != null) {
            String t[] = reformatLine(line).split(" ");
            String time = "";
            if (t.length == 1) {
                for (int i = 0; i < t[0].length(); i++) {
                    if (Character.isDigit(t[0].charAt(i))) {
                        time += t[0].charAt(i);
                    } else {
                        break;
                    }
                }
                if (!time.equals("")) {
                    t[0] = time;
                }
            }
            if (t.length == 1 || t.length == 2) {
                try {
                    Float.parseFloat(t[0]);
                    return new BigInteger(t[0]);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Ingredient formatIngredient(String line) {
        String[] words = reformatLine(line).split(" ");
//        String[] words = new String[]{};
        Ingredient indakt = new Ingredient();
        int wordsstart = 0;
        String item = "";
        Float qty = null;
        Float qty2 = null;

        if (isDigitwithSymbol(words[0]) && words.length == 1) {
            indakt.setItem(words[0]);
            return indakt;
        }

        if (isDigitwithSymbol(words[0])) {
            String[] fto = words[0].split("-");
            if (fto != null && fto.length > 0) {
                qty = checkNumber(fto[0]);
                if (fto.length > 1) {
                    qty2 = checkNumber(fto[1]);
                }

                if (words.length > 1 && isDigitwithSymbol(words[1])) {
                    String[] fto2 = words[1].trim().split("-");
                    if (fto2 != null && fto2.length > 0) {
                        if (fto.length == 1) {
                            qty += checkNumber(fto2[0]);
                        } else {
                            qty2 += checkNumber(fto2[0]);
                        }
                        if (fto2.length > 1) {
                            if (fto.length == 1) {
                                qty2 = checkNumber(fto2[1]);
                            }
                        }

                        if (words.length > 2 && isDigitwithSymbol(words[2])) {
                            qty2 += checkNumber(words[2]);
                            wordsstart++;
                        }
                        wordsstart++;
                    }
                }
                wordsstart++;

                indakt.setQty(qty);
                if (qty2 != null) {
                    item = "(bis " + formatQTY(qty2) + " ) ";
                }
            }
        }

        if (words[wordsstart].toUpperCase().equals("TBSP.")
                || (words[wordsstart].length() <= 3 && !words[wordsstart].toUpperCase().equals("EI"))) {
            if (words[wordsstart].equals("Pf.") || words[wordsstart].equals("lb")
                    || words[wordsstart].equals("lb.") || words[wordsstart].equals("pf")
                    || words[wordsstart].equals("pfd")) {
                if (qty >= 2) {
                    qty = qty / 2.0f;
                    indakt.setUnit("kg");
                } else {
                    qty = qty * 500.0f;
                    indakt.setUnit("g");
                }
                indakt.setQty(qty);
            } else {
                indakt.setUnit(words[wordsstart]);
            }
            wordsstart++;
        }

        for (int i = wordsstart; i < words.length; i++) {
            item += words[i] + " ";
        }
        indakt.setItem(item);
        return indakt;

    }

    private static boolean isDigitwithSymbol(String word) {
        int num = 0;
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isDigit(word.charAt(i))
                    && word.charAt(i) != '/' && word.charAt(i) != '-' && word.charAt(i) != ',') {
                return false;
            } else if (word.charAt(i) == '-') {
                num++;
            }
        }
        return num < 2;
    }

    private static Float checkNumber(String n) {
        if (n != null && !n.trim().equals("")) {
            if (n.contains("/")) {
                String[] numbers = n.split("/");
                for (int i = 0; i < numbers.length; i++) {
                    numbers[i] = deNum2En(numbers[i]);
                }
                if (numbers.length == 2) {
                    return Float.parseFloat(numbers[0]) / Float.parseFloat(numbers[1]);
                } else {
                    return Float.parseFloat(numbers[0]);
                }
            } else {
                return Float.parseFloat(deNum2En(n));
            }
        } else {
            return Float.parseFloat("0");
        }
    }

    public static String reformatLine(String line) {
        List<String> words = new ArrayList<>();
        for (String l : line.split(" ")) {
            if (!l.equals("")) {
                words.add(l);
            }
        }
        line = "";
        return words.stream().map((w) -> w + " ").reduce(line, String::concat);
    }

    
        public static String setText(String text){
        String output ="";
        Scanner scanner = new Scanner(text);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim(); 
            if(!line.equals("")){
                output +=line+"\n";
            }       
        }
        return output;
    }
}
