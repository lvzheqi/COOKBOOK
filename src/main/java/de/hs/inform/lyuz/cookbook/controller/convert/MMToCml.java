package de.hs.inform.lyuz.cookbook.controller.convert;


import de.hs.inform.lyuz.cookbook.help.FormatHelper;
import de.hs.inform.lyuz.cookbook.help.Utils;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import de.hs.inform.lyuz.cookbook.model.cookml.*;

public class MMToCml {

    private final Cookml cookml;
    private Recipe recakt;
    private Head headakt;

    private Scanner scanner;
    private String nextLine = "";

    public MMToCml(File f) {
        String text = Utils.readFile(f);

        cookml = new Cookml();
        cookml.setName(f.getName());
        cookml.setProg("MM2Cook");
        cookml.setVersion("1.0.2");
        cookml.setProgver("0.91");

        for (String rez : text.split("MMMMM----- ")) {
            if (rez.trim().endsWith("MMMMM")) {
                setRecipe(formatMM(rez));
            }
        }
    }

    private void setRecipe(String rez_akttext) {

        scanner = new Scanner(rez_akttext);

        recakt = new Recipe();
        headakt = new Head();
        Preparation prepakt = new Preparation();
        Remark remakt = new Remark();

        scanner.nextLine();

        recakt.setLang("DE");
        headakt.setTitle(setHeadAndQty(getNextLine().trim()).trim());

        headakt.getCat().addAll(Arrays.asList(setCat(getNextLine().trim())));

        String yield[] = setHeadAndQty(getNextLine().trim()).trim().split(" ");
        headakt.setServingqty(yield[0]);
        headakt.setServingtype(yield[1]);

        String line;

        do {
            line = getNextLine();
            if (line.startsWith("MMMMM-", 0)) {
                switch (cutMMMM(line)) {
                    case "QUELLE":
                        setSource("QUELLE");
                        break;
                    default:
                        if (nextLine.trim().startsWith("--")) {
                            headakt.setCreateuser(getNextLine().trim().substring(3));
                            headakt.setChangeuser(getNextLine().trim().substring(3));
                        } else {
                            setPart(line);
                        }
                        break;
                }
            } else if (line.startsWith(" ", 0)) {
                setPart(line);
            } else {
                break;
            }
        } while (!nextLine.trim().equals("MMMMM"));

        do {
            if (line.startsWith(":", 0) || line.startsWith("*", 0)) {
                String[] tmp = line.split(":");
                String lineCut = tmp[tmp.length - 1];
                lineCut = lineCut.trim();
                switch ((line.split(" ")[1].toUpperCase())) {
                    case "QUELLE:":
                    case "QUELLE":
                        setSource(lineCut);
                        break;
                    case "DATUM:":
                    case "DATUM":
                        if (setDate(lineCut)) {
                            prepakt.getText().add(line.substring(2));
                        }
                        break;
                    case "VORBEREITUNGSZEIT:":
                    case "VORBEREITUNGSZEIT":
                        if (!setTime(lineCut, 0)) {
                            prepakt.getText().add(line.substring(2));
                        }
                        break;
                    case "ZUBEREITUNGSZEIT:":
                    case "ZUBEREITUNGSZEIT":
                        if (!setTime(lineCut, 1)) {
                            prepakt.getText().add(line.substring(2));
                        }
                        break;
                    case "STICHWORTE":
                    case "STICHWORTE:":
                        setKeyword(lineCut);
                        break;
                    case "ABS":
                    case "ABS:":
                        headakt.setCreateemail(lineCut);
                        headakt.setChangeemail(lineCut);
                        break;
                    case "EDA":
                    case "EDA:":
                        headakt.setRid(lineCut);
                        break;
                    case "ENERGIE":
                    case "ENERGIE:":
                        if (!setContent(lineCut)) {
                            prepakt.getText().add(line.substring(2));
                        }
                        break;
                    case "FINGERPRINT":
                    case "FINGERPRINT:":
                        break;
                    case "ERFASSER:":
                    case "ERFASSER":
                        headakt.setCreateuser(lineCut);
                        headakt.setChangeuser(lineCut);
                        break;
                    default:
                        if (line.split(" ")[1].toUpperCase().equals("PRO")
                                && (line.split(" ")[2].toUpperCase().equals("PORTION")
                                || line.split(" ")[2].toUpperCase().equals("PERSON"))) {

                            remakt.getLine().add(line);
                        } else {
                            prepakt.getText().add(line.substring(2));

                        }
                        break;
                }
            } else {
                prepakt.getText().add(line);

            }
            line = getNextLine();
        } while (!line.trim().equals("MMMMM"));

        recakt.getHeadAndCustomAndPart().add(remakt);
        recakt.getHeadAndCustomAndPart().add(prepakt);
        recakt.getHeadAndCustomAndPart().add(headakt);
        cookml.getRecipe().add(recakt);
    }

    private String cutMMMM(String line) {
        line = line.substring(5);
        while (!Character.isLetter(line.charAt(0))) {
            line = line.substring(1);
        }
        while (!Character.isLetter(line.charAt(line.length() - 1))) {
            int length = line.length() - 1;
            line = line.substring(0, length);
        }
        return line;
    }

    private boolean setContent(String line) {
        String[] con = line.split(" ");
        if (con.length == 2) {
            Head.Content contakt = new Head.Content();
            contakt.setType(con[1]);
            contakt.setValue(con[0]);
            headakt.getContent().add(contakt);
            return true;
        }
        return false;
    }

    private boolean setTime(String line, int type) {
        String[] tmp = line.split(" ");
        String time = "";
        BigInteger min = null;
        if (tmp.length == 3) {
            for (String t : tmp) {
                if (!t.contains("ca.")) {
                    time += t + " ";
                }
            }
        }
        if (time.trim().split(" ").length == 2) {
            int isDigit = 0;
            for (String t : time.trim().split(" ")) {
                int index = 0;
                for (int i = 0; i < t.length(); i++) {
                    if (Character.isDigit(t.charAt(i))) {
                        index++;
                    }
                }
                if (index == t.length()) {
                    isDigit++;
                    min = BigInteger.valueOf(Long.parseLong(t));
                }
            }
            if (isDigit == 1) {
                switch (type) {
                    case 0:
                        headakt.setTimeprepqty(min);
                        break;
                    case 1:
                        headakt.setTimecookqty(min);
                        break;
                    case 2:
                        headakt.setTimeallqty(min);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    private void setKeyword(String line) {
        String[] kw = line.split(",");
        for (String t : kw) {
            headakt.getHint().add(t.trim());
        }
    }

    private boolean setDate(String line) {
        String[] date = line.split(".");
        XMLGregorianCalendar d = null;
        try {
            d = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(BSToCml.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (date.length == 3) {
            int year = Integer.parseInt(date[2]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[0]);

            if (year > 2090) {
                year = 1998;
            }
            d.setYear(year);
            d.setMonth(month);
            d.setDay(day);

            headakt.setCreatedate(d);
            headakt.setChangedate(d);
            return true;
        }
        return false;
    }

    private void setPart(String line) {

        Recipe.Part partakt = new Recipe.Part();
        if (line.startsWith("MMMMM-", 0)) {
            partakt.setTitle(cutMMMM(line));
        } else {
            partakt.getIngredient().add(setIngredient(line.trim()));
        }

        while (!nextLine.replace(" ", "").equals("") && nextLine.startsWith(" ")) {
            partakt.getIngredient().add(setIngredient(getNextLine().trim()));
        }
        recakt.getHeadAndCustomAndPart().add(partakt);
    }

    private Ingredient setIngredient(String line) {
        line = reformatLine(line).trim();
        Ingredient ingredient = FormatHelper.formatIngredient(line.split(" "));

        return ingredient;
    }

    private void setSource(String line) {
        if (line.equals("QUELLE")) {
            line = getNextLine().trim();
            if (line.startsWith("--", 0)) {
                headakt.getSourceline().add(line.substring(3));
            } else if (line.startsWith("-", 0)) {
                headakt.getSourceline().add(line.substring(2));
            }
        } else {
            headakt.getSourceline().add(line);
        }
    }

    private String setHeadAndQty(String line) {
        String[] text = line.trim().split(":");
        return text[1];
    }

    private String[] setCat(String line) {
        String[] tmp = line.trim().split("Categories:");

        String text[];
        if (tmp.length == 0) {
            text = new String[1];
            text[0] = "Sonstige";
        } else {
            text = tmp[1].split(",");
            for (int i = 0; i < text.length; i++) {
                text[i] = text[i].trim();
            }
        }
        return text;
    }

    private String getNextLine() {
        String line = nextLine;
        while (line.replace(" ", "").equals("") && scanner.hasNextLine()) {
            line = scanner.nextLine();
        }
        if (line.startsWith(":") || nextLine.startsWith("*", 0)) {
            line = reformatLine(line).trim();
        }

        while (scanner.hasNextLine()) {
            nextLine = scanner.nextLine();
            if (nextLine.trim().startsWith("--", 0) && !line.startsWith("MMMMM", 0)) {
                line += " " + nextLine.trim().substring(2);
            } else if (nextLine.trim().startsWith("-", 0) && !line.startsWith("MMMMM", 0)) {
                line += " " + nextLine.trim().substring(1);
            } else if (!nextLine.startsWith(" ", 0) && !nextLine.replace(" ", "").equals("")
                    && !nextLine.startsWith("Categories", 0) && !nextLine.startsWith("MMMMM", 0)
                    && !nextLine.startsWith(":", 0)) {
                line += " " + nextLine;
            } else if (nextLine.startsWith(":", 0) || nextLine.startsWith("*", 0)) {
                nextLine = reformatLine(nextLine).trim();
                switch (nextLine.split(" ")[1].toUpperCase()) {
                    case "PRO":
                    case "QUELLE":
                    case "DATUM":
                    case "VORBEREITUNGSZEIT":
                    case "ZUBEREITUNGSZEIT":
                    case "STICHWORTE":
                    case "ABS":
                    case "EDA":
                    case "ENERGIE":
                    case "FINGERPRINT":
                    case "PRO:":
                    case "QUELLE:":
                    case "DATUM:":
                    case "VORBEREITUNGSZEIT:":
                    case "ZUBEREITUNGSZEIT:":
                    case "STICHWORTE:":
                    case "ABS:":
                    case "EDA:":
                    case "ENERGIE:":
                    case "FINGERPRINT:":
                    case "ERFASSER:":
                    case "ERFASSER":
                        return line;
                    case ":":
                        if (nextLine.length() > 3) {
                            line += " " + nextLine.substring(4);
                        }
                        break;
                    case "*":
                        if (nextLine.length() > 3) {
                            nextLine = nextLine.substring(2);
                        }
                        return line;
                    default:
                        if (nextLine.split(" ").length > 4 && nextLine.split(" ")[3].equals(":")) {
                            return line;
                        }

                        if (nextLine.split(" ").length > 3 && nextLine.split(" ")[2].equals(":")) {
                            return line;
                        }

                        if (nextLine.length() > 2) {
                            line += "\n" + nextLine.substring(2);
                        }
                        break;
                }
            } else {
                break;
            }
        }
        return line;
    }

    private String reformatLine(String line) {
        List<String> words = new ArrayList<>();
        for (String l : line.split(" ")) {
            if (!l.equals("")) {
                words.add(l);
            }
        }
        line = "";
        return words.stream().map((w) -> w + " ").reduce(line, String::concat);
    }

    private String formatMM(String recipe) {

        String text = "";
        for (String r : recipe.split(":")) {
            text += r + ": ";
        }
        int length = text.length();
        recipe = text.substring(0, length - 2);
        recipe = sortRec(recipe, "pro Portion", ":");
        recipe = sortRec(recipe, "Pro Portion", ":");
        recipe = sortRec(recipe, "Pro Person", ":");
        recipe = sortRec(recipe, "Vorbereitungszeit", ":");
        recipe = sortRec(recipe, "Zubereitungszeit", ":");
        recipe = sortRec(recipe, "Quelle", "*");
        recipe = sortRec(recipe, "Datum", ":");
        recipe = sortRec(recipe, "Stichworte", ":");
        recipe = sortRec(recipe, "Abs", ":");
        recipe = sortRec(recipe, "Eda", ":");
        recipe = sortRec(recipe, "Erfasser", ":");

        return recipe;
    }

    public String sortRec(String recipe, String type, String prefix) {
        String part[] = recipe.split(type);
        if (part.length > 1) {
            recipe = part[0];
            for (int i = 1; i < part.length; i++) {
                if (!part[i - 1].trim().endsWith(":")) {
                    recipe += "\n" + prefix + " " + type;
                } else {
                    recipe += type;
                }
                if (!part[i].trim().startsWith(":")) {
                    recipe += " : " + part[i];
                } else {
                    recipe += part[i];
                }
            }
        }
        return recipe;
    }

    public Cookml getCookml() {
        return this.cookml;
    }
}
