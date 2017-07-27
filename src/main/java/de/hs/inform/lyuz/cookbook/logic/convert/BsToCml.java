package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Picbin;
import de.hs.inform.lyuz.cookbook.model.cookml.Preparation;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe.Part;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
import de.hs.inform.lyuz.cookbook.model.cookml.Step;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


public class BsToCml {

    private Cookml cookml;
    private Recipe recakt;
    private Head headakt;
    private Scanner scanner;
    final String EoL = System.getProperty("line.separator");
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public BsToCml(File f) throws ConvertErrorException {
        LineIterator it = null;

        try {
            it = FileUtils.lineIterator(f, FilesUtils.ENCODING);
            cookml = new Cookml();
//            cookml.setProg("B&S");
//            cookml.setProgver("0.91");

            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                String line = it.nextLine();

                if (line.trim().startsWith("#T1")) {
                    setRecipe(sb.substring(0));
                    sb = new StringBuilder();
                    sb.append(line).append(EoL);
                } else {
                    sb.append(line).append(EoL);
                }

            }
            setRecipe(sb.substring(0));
        } catch (Exception ex) {
            Logger.getLogger(BsToCml.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Lesen auf B&S", ex.getClass().getName());
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    private void setRecipe(String bs_rez) {

        scanner = new Scanner(bs_rez);
        String remark = "";

        if (scanner.hasNextLine()) {

            recakt = new Recipe();
            headakt = new Head();

            String line = scanner.nextLine();
            while (scanner.hasNextLine()) {

                if (line.replace(" ", "").equals("")) {
                    line = scanner.nextLine();
                    continue;
                }
                if (!line.startsWith("#", 0)) {
                    line = setPart(line);
                    continue;
                }
                if (line.length() >= 4 && line.substring(0, 3).equals("#T1")) {
                    headakt.setTitle(line.substring(4));
                    line = scanner.nextLine();
                    continue;
                }

                if (line.trim().length() == 2 && line.substring(0, 2).equals("#B")) {
                    line = setPreparation();
                    continue;
                }

                if (line.length() >= 3 && line.substring(0, 3).equals("#SC")) {
                    line = setPart(line);
                } else {
                    setBS(line, remark);
                    line = scanner.nextLine();
                }
            }

            if (!remark.equals("")) {
                Remark remakt = new Remark();
                remakt.getLine().add(remark);
                recakt.getRemark().add(remakt);
            }

//            recakt.getPreparation().forEach((p) -> {
//                if (p.getText() != null) {
//                    p.setText(FormatHelper.setText(p.getText()));
//                }
//            });
            recakt.setHead(headakt);
            cookml.getContent().add(recakt);
        }
    }

    private void setBS(String line, String remark) {
        if (line.length() > 4) {
            try {
                switch (line.substring(0, 3).trim()) {
                    case "#RU":
                        headakt.getCat().add(line.substring(4));
                        break;
                    case "#AT":
                        String createuser = line.substring(4);
                        headakt.setCreateuser(createuser);
                        break;
                    case "#MA":
                        headakt.setServingqty(line.substring(4));
                        break;
                    case "#MB":
                        headakt.setServingtype(line.substring(4));
                        break;
                    case "#D":
                    case "#AD":
                        XMLGregorianCalendar d = null;
                         {
                            try {
                                d = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                                int year = Integer.parseInt(line.substring(10, 14));
                                if (year > 2090) {
                                    year = 1998;
                                }
                                int month = Integer.parseInt(line.substring(7, 9));
                                int day = Integer.parseInt(line.substring(4, 6));
                                d.setYear(year);
                                d.setMonth(month);
                                d.setDay(day);
                                if (line.startsWith("#D ", 0)) {
                                    headakt.setCreatedate(d);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(BsToCml.class.getName()).log(Level.SEVERE, null, ex);
                                errorMessage += "Fehler beim Konvertierung auf BS-Datum: \n "
                                        + "Rezept: " + headakt.getTitle() + ", line: " + line + "\n";
                                System.err.println("Fehler beim Konvertierung auf BS-Datum");
                            }
                        }
                        break;
                    case "#IM":
                        //TODO: path datei?
                        File f = new File(line.substring(4));
                        if (f.exists()) {
                            Picbin picbin = new Picbin();
                            picbin.setFormat("JPG");
                            try {
                                picbin.setValue(FileUtils.readFileToByteArray(f));
                            } catch (Exception ex) {
                                Logger.getLogger(BsToCml.class.getName()).log(Level.SEVERE, null, ex);
                                errorMessage += "Fehler beim BS Bilder Lesem: \n"
                                        + "Rezept: " + headakt.getTitle() + ",line: " + line + "\n";
                                System.err.println("Fehler beim Bilder-Lesen");
                            }
                            headakt.getPicbin().add(picbin);
                        }
                        break;
                    case "#NI":
                        break;
                    case "#NO":
                        if (line.length() > 12 && line.substring(4, 10).equals("Quelle: ")) {
                            headakt.getSourceline().add(line.substring(11));
                        }
                        headakt.getSourceline().add(line.substring(4));
                        break;
                    case "#ST":
                        headakt.getHint().add(line.substring(4));
                        break;
                    default:
                        setRemark(line, remark);
                        break;
                }

            } catch (Exception e) {
                System.err.println("Fehler beim BS--setBS('#')");
                setRemark(line, remark);

            }
        }
    }

    private void setRemark(String line, String remark) {

        if (!remark.equals("")) {
            remark += ", ";
        }
        if (line.length() > 4) {
            try {
                switch (line.substring(0, 3)) {
                    case "#EW":
                        remark += "Eiweiß: " + line.substring(4) + " g";
                        break;
                    case "#FT":
                        remark += "Fett: " + line.substring(4) + " g";
                        break;
                    case "#KH":
                        remark += "Kohlenhydrate: " + line.substring(4) + " g";
                        break;
                    case "#KJ":
                        remark += "kJ: " + line.substring(4);
                        break;
                    case "#KC": {
                        remark += "kcal: " + line.substring(4);
                        Head.Content contakt = new Head.Content();
                        contakt.setType("GCAL");
                        contakt.setValue(line.substring(4));
                        headakt.getContent().add(contakt);
                        break;
                    }
                    case "#BE": {
                        remark += "BE: " + line.substring(4);
                        Head.Content contakt = new Head.Content();
                        contakt.setType("GKB");
                        contakt.setValue(line.substring(4));
                        headakt.getContent().add(contakt);
                        break;
                    }
                    default:
                        remark += line.substring(1, 3) + ": " + line.substring(4);
                        break;
                }
            } catch (Exception e) {
                System.err.println("Fehler beim BS--setRemark('#')");
                remark += line;
            }
        }
    }

    private String setPart(String lineTitle) {
        Part partakt = new Part();

        if (lineTitle.startsWith("#", 0)) {
            if (lineTitle.length() > 5) {
                partakt.setTitle(lineTitle.substring(4));
            }
        }

        String line = null;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0)) {
            partakt.getIngredient().add(FormatHelper.formatIngredient(line));
        }
        recakt.getPart().add(partakt);
        return line;
    }

    private String setPreparation() {

        Preparation prepakt = new Preparation();
        String line = null;
        String rectext = "";
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0)) {
            if ((line.length() > 2 && line.startsWith("  "))
                    || (line.trim().equals("") && scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0))) {
                if (!rectext.equals("")) {
                    Step step = new Step();
                    step.setText(rectext);
                    prepakt.getStep().add(step);
                }
                rectext = line.trim();
            } else {
                rectext += line.trim();
            }
        }
        if (!rectext.equals("")) {
            Step step = new Step();
            step.setText(rectext);
            prepakt.getStep().add(step);
        }

//        while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0)) {
//            if (line.length() > 2 && line.startsWith("  ")) {
//                rectext += "\n" + line.trim();
//            } else {
//                rectext += line.trim();
//            }
//        }
//        if (!rectext.equals("")) {
//            prepakt.setText(rectext);
//        }
        recakt.getPreparation().add(prepakt);
        return line;
    }

    public Cookml getCookml() {
        return this.cookml;
    }
}
