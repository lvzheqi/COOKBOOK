package de.hs.inform.lyuz.cookbook.help;

import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import java.math.BigInteger;

public class FormatHelper {

    // Menge der Zutat
    public static String formatQTY(Float qty) {
        String qtyString = "";
        int nenner = 0;
        double eps = 1.0e-6;
        int zaehler = (int) qty.floatValue();
        boolean isrational = false;

        while (!isrational && (nenner++ < 9)) {
            zaehler = (int) ((qty) * nenner);
            isrational = (qty - ((float) zaehler / (float) nenner)) < eps;
        }

        if ((zaehler / nenner > 0) && (nenner > 4)) {
            isrational = false;
        }

        if (isrational) {
            if (nenner == 1) {
                qtyString = (new Integer(zaehler)).toString();
            } else {
                if (zaehler / nenner > 0) {
                    qtyString = (new Integer(zaehler / nenner)).toString() + " ";
                    zaehler = zaehler % nenner;
                }
                qtyString += (new Integer(zaehler)).toString() + "/" + (new Integer(nenner)).toString();

            }
        } else {
            qtyString = qty.toString();
        }

        return qtyString;
    }

    // Mengeneinheiten
    public static String formatUnit(String unit) {
        String unitForm = unit;
        if (unit == null) {
            return unit;
        }
        switch (unit) {
            case "tb":
            case "T":
                unitForm = "EL";
                break;
            case "t":
            case "ts":
                unitForm = "TL";
                break;
            case "fl":
                unitForm = "fl.oz.";
                break;
            case "c":
                unitForm = "Tasse(n)";
                break;
            case "sm":
                unitForm = "klein.";
                break;
            case "pt":
                unitForm = "Pint";
                break;
            case "md":
                unitForm = "mittl.";
                break;
            case "qt":
                unitForm = "l";
                break;
            case "lg":
                unitForm = "groß.";
                break;
            case "ga":
                unitForm = "Gallon.";
                break;
            case "cn":
                unitForm = "Dose(n)";
                break;
            case "pk":
                unitForm = "Packung";
                break;
            case "lb":
                unitForm = "U.S.Pound";
                break;
            case "pn":
                unitForm = "Prise";
                break;
            case "ccm":
                unitForm = "ml";
                break;
            case "dr":
                unitForm = "Tropfen";
                break;
            case "ds":
                unitForm = "Spur";
                break;
            case "ct":
                unitForm = "Karton";
                break;
            case "bn":
                unitForm = "Bund";
                break;
            case "sl":
                unitForm = "Scheibe(n)";
                break;
            case "ea":
                unitForm = "pro Portion";
                break;
            default:
                break;
        }

        return unitForm;
    }

    public static Ingredient formatIngredient(String[] words) {
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
            qty = checkNumber(fto[0]);
            if (fto.length > 1) {
                qty2 = checkNumber(fto[1]);
            }

            if (words.length > 1 && isDigitwithSymbol(words[1])) {
                String[] fto2 = words[1].split("-");
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
            wordsstart++;

            indakt.setQty(qty);
            if (qty2 != null) {
                item = "(bis " + formatQTY(qty2) + " ) ";
            }
        }

        if (words[wordsstart].toUpperCase().equals("TBSP.")
                || (words[wordsstart].length() <= 3 && !words[wordsstart].toUpperCase().equals("EI"))) {
            if (words[wordsstart].equals("Pf.")) {
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
                    && word.charAt(i) != '/' && word.charAt(i) != '-') {
                return false;
            } else if (word.charAt(i) == '-') {
                num++;
            }
        }
        return num < 2;
    }

    private static Float checkNumber(String n) {
        String[] numbers = n.split("/");
        for (int i = 0; i < numbers.length; i++) {
            String[] num = numbers[i].split(",");
            if (num.length > 1) {
                numbers[i] = num[0] + "." + num[1];
            }
        }
        if (numbers.length == 2) {
            return Float.parseFloat(numbers[0]) / Float.parseFloat(numbers[1]);
        } else {
            return Float.parseFloat(numbers[0]);
        }

    }

    public static BigInteger setCookTime(String line) {
        if (line != null) {
            String time = null;
            int index = 0;
            for (String t : line.trim().split(" ")) {
                try {
                    Float.parseFloat(t);
                    index++;
                    time = t;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            if (index == 1) {
                return new BigInteger(time);
            }
        }
        return null;
    }

}
