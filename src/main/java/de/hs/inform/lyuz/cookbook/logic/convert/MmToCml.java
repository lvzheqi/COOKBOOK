package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import de.hs.inform.lyuz.cookbook.model.cookml.Preparation;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class MmToCml {

    private Cookml cookml;
    private Recipe recakt;
    private Head headakt;

    private Scanner scanner;
    private String nextLine = "";
    final String EoL = System.getProperty("line.separator");
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public MmToCml(File f) throws ConvertErrorException {

//        LineIterator it = null;
        try {
            String mm = FileUtils.readFileToString(f, StandardCharsets.ISO_8859_1);
//            it = FileUtils.lineIterator(f, "ISO-8859-1");
            cookml = new Cookml();
            cookml.setName(f.getName());
//            cookml.setProg("MealMaster");
//            cookml.setProgver("0.91");
//            StringBuilder sb = new StringBuilder();
//            while (it.hasNext()) {
//                String line = it.nextLine();
//                sb.append(line);
//                if (line.trim().equals("MMMMM")) {
//                    setRecipe(formatMM(sb.substring(0)));
//                    sb = new StringBuilder();
//                } else {
//                    sb.append(EoL);
//                }
//            }
            cookml = new Cookml();
            cookml.setName(f.getName());
            cookml.setProg("MealMaster");
            cookml.setProgver("0.91");

            for (String rez : mm.split("MMMMM----- ")) {
                if (rez.trim().endsWith("MMMMM")) {
                    setRecipe(formatMM(rez));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MmToCml.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Lesen mm", ex.getClass().getName());
        } 
//        finally {
//            LineIterator.closeQuietly(it);
//        }
    }

    private void setRecipe(String mm_rez) {

        scanner = new Scanner(mm_rez);

        recakt = new Recipe();
        headakt = new Head();
        Preparation prepakt = new Preparation();
        prepakt.setText("");
        Remark remakt = new Remark();

//        while (scanner.hasNextLine()) {
//            String li = scanner.nextLine();
//            if (li.startsWith("MMMMM-----")) {
//                break;
//            }
//        }
        scanner.nextLine();

        headakt.setTitle(setHeadAndQty(getNextLine().trim()).trim());

        System.out.println(headakt.getTitle());
        headakt.getCat().addAll(Arrays.asList(setCat(getNextLine().trim())));

        String serving = setHeadAndQty(getNextLine().trim());
        String yield[] = FormatHelper.reformatLine(serving).split(" ");
        switch (yield.length) {
            case 2:
                headakt.setServingqty(yield[0]);
                headakt.setServingtype(yield[1]);
                break;
            case 1:
                try {
                    int n = Integer.parseInt(yield[0]);
                    headakt.setServingqty(yield[0]);
                    if (n > 1) {
                        headakt.setServingtype("Portionen");
                    } else {
                        headakt.setServingtype("Portion");
                    }
                } catch (Exception e) {
                    headakt.setServingtype(serving);
                }
                break;
            default:
                headakt.setServingtype(serving);
                break;
        }

        String line;
        do {
            line = getNextLine();
            if (line.startsWith("MMMMM-", 0)) {
                if (cutMMMM(line).equals("QUELLE") || line.contains("QUELLE")) {
                    setSource("QUELLE");
                } else {
                    if (nextLine.trim().startsWith("--")) {
                        headakt.setCreateuser(getNextLine().trim().substring(3));
                        headakt.setChangeuser(getNextLine().trim().substring(3));
                    } else {
                        setPart(line);
                    }
                    break;
                }
            } else if (line.startsWith(" ", 0) && !line.trim().equals("")) {
                setPart(line);
            } else {
                break;
            }
        } while (!nextLine.trim().equals("MMMMM"));

        while (!line.trim().equals("MMMMM") && scanner.hasNextLine()) {
            if ((line.startsWith(":", 0) || line.startsWith("*", 0)) && line.length() > 1) {
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
                            prepakt.setText(prepakt.getText() + line.substring(2) + "\n");
                        }
                        break;
                    case "VORBEREITUNGSZEIT:":
                    case "VORBEREITUNGSZEIT":
                        if (!setTime(lineCut, 0)) {
                            prepakt.setText(prepakt.getText() + line.substring(2) + "\n");
                        }
                        break;
                    case "ZUBEREITUNGSZEIT:":
                    case "ZUBEREITUNGSZEIT":
                        if (!setTime(lineCut, 1)) {
                            prepakt.setText(prepakt.getText() + line.substring(2) + "\n");
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
                            prepakt.setText(prepakt.getText() + line.substring(2) + "\n");
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
                            prepakt.setText(prepakt.getText() + line.substring(2) + "\n");
                        }
                        break;
                }
            } else {
                prepakt.setText(prepakt.getText() + line + "\n");
            }

            line = getNextLine();
        }

        recakt.getRemark().add(remakt);
        if (prepakt.getText().equals("")) {
            prepakt.setText(null);
        }
        recakt.getPreparation().add(prepakt);
        recakt.getPreparation().forEach((p) -> {
            if (p.getText() != null) {
                p.setText(FormatHelper.setText(p.getText()));
            }
        });
        recakt.setHead(headakt);
        cookml.getContent().add(recakt);
    }

    private String cutMMMM(String line) {
        line = line.substring(5);

        while (line.charAt(0) != '-') {
            line = line.substring(1);
        }
        while (line.length() > 1 && line.charAt(line.length() - 1) != '-') {
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
        if (date.length == 3) {
            try {
                d = DatatypeFactory.newInstance().newXMLGregorianCalendar();

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

            } catch (Exception ex) {
                Logger.getLogger(MmToCml.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Konvertierung auf MM-Datum");
                errorMessage += "Fehler beim Konvertierung auf MM-Datum: \n "
                        + "Rezept: " + headakt.getTitle() + ", line: " + line + "\n";
            }
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
        recakt.getPart().add(partakt);
    }

    private Ingredient setIngredient(String line) {
        line = FormatHelper.reformatLine(line).trim();
        Ingredient ingredient = FormatHelper.formatIngredient(line);

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
            text[0] = "Andere";
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
            line = FormatHelper.reformatLine(line).trim();
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
                nextLine = FormatHelper.reformatLine(nextLine).trim();
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
