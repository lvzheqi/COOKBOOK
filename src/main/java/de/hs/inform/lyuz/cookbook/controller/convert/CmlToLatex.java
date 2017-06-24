package de.hs.inform.lyuz.cookbook.controller.convert;

import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import de.hs.inform.lyuz.cookbook.model.cookml.Picbin;
import de.hs.inform.lyuz.cookbook.model.cookml.Preparation;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class CmlToLatex {

    private final LinkedHashMap<String, Cookml> cmlSort;
    private final ExportInfo exportInfo;

    private final String filepath;

    private String tex = "";

    private int anzpics = 0;

    public String getTex() {
        return tex;
    }

    public CmlToLatex(MyBook myBook, String filepath) {
        this.cmlSort = myBook.getSortCmlMap();
        this.exportInfo = myBook.getExportInfo();

        this.filepath = filepath;

        getFormatTex();
    }

    private void getFormatTex() {
        cmlSort.keySet().stream().map((catakt) -> {
            tex += cat2Tex(catakt);
            return catakt;
        }).forEachOrdered((catakt) -> {
            int anzahl = 0;
            for (Recipe recakt : cmlSort.get(catakt).getRecipe()) {
                tex += rec2Tex(recakt, anzahl);
                anzahl++;
            }
        });

        if (exportInfo.isHasIndex()) {
            tex += "\\newpage\n\n \\pdfbookmark{Stichwortindex}{tit}\n "
                    + "\\input {\\jobname.ind} \n\n";
        }
    }

    // Sectionueberschriften sind die Kategorien
    private String cat2Tex(String cat) {
        String cattex = "";
        cattex += "\\pagebreak \n \n";
        cattex += "%----------------------------------------------------\n";
        cattex += "\\nopagebreak{ \n";
        cattex += "\\" + "section{" + FormatHelper.outputCategories(cat) + "} \n\n";
        return cattex;
    }

    // ein Rezept ist eine Subsection
    private String rec2Tex(Recipe recipe, int anzahl) {
        String rectex = "";
        if (anzahl > 0) {
            rectex += "%----------------------\n";
            rectex += "\\nopagebreak{ \n";
        }
        rectex += "\\" + "subsection{";

        String titletex = "";								// Titel
        String indextex = "";								// Indexeintraege
        String parttex = "}{\n\\" + "begin{multicols}{2}";		// Zutaten
        String preptex = "\n" + "\\" + "end{multicols}\n\n";		// Zubereitung
        String remarktex = "";								// Bemerkungen
        String sourcetex = "";								// Quellenangaben
        String timetex = "";								// Zubereitungszeit
        String servetex = "";								// Portionen
        String conttex = "";									// pro Portion
        String pictex = "";									// Bilder

        for (Object objakt : recipe.getHeadAndCustomAndPart()) {
            switch (objakt.getClass().getCanonicalName()) {
                case "de.hs.inform.lyuz.cookbook.model.cookml.Head":
                    Head headakt = (Head) objakt;
                    titletex += texString(headakt.getTitle()) + "}\n\n";
                    if (exportInfo.isHasTime()) {
                        timetex = time2tex(headakt);
                    }
                    if (exportInfo.isHasSource()) {
                        sourcetex = source2tex(headakt);
                    }
                    servetex = serve2tex(headakt);
                    if (exportInfo.isHasIndex()) {
                        indextex = index2tex(headakt);
                    }
                    conttex = content2tex(headakt);
                    if (exportInfo.isHasPic()) {
                        pictex = pic2tex(headakt);
                    }
                    break;
                case "de.hs.inform.lyuz.cookbook.model.cookml.Remark":
                    if (exportInfo.isHasRemark()) {
                        Remark remark = (Remark) objakt;
                        remarktex += remark2tex(remark);
                    }
                    break;
                case "de.hs.inform.lyuz.cookbook.model.cookml.Preparation":
                    Preparation prep = (Preparation) objakt;
                    preptex += prep2tex(prep);
                    break;
                case "de.hs.inform.lyuz.cookbook.model.cookml.Recipe.Part":
                    Recipe.Part part = (Recipe.Part) objakt;
                    String title = part.getTitle();
                    parttex += "\n\n";
                    if (title != null) {
                        parttex += "\\" + "textit{" + title + "}\n\n";
                    }
                    parttex = ((Recipe.Part) objakt).getIngredient().stream().map((ingit)
                            -> ing2tex(ingit)).map((ingtex) -> "\n\n" + ingtex).reduce(parttex, String::concat);
                    break;
                default:
                    break;
            }
        }

        rectex += titletex + texString(indextex + parttex + pictex + preptex
                + remarktex + servetex + conttex + timetex + sourcetex);
        rectex += "} \n\n";

        return rectex;
    }

    // Zubereitungszeit
    private String time2tex(Head head) {
        String timetex = "";

        BigInteger time = head.getTimeallqty();
        if (time != null) {
            timetex += "Gesamtzeit " + time.toString() + " min";
        }

        time = head.getTimecookqty();
        if (time != null) {
            if (!timetex.equals("")) {
                timetex += ", ";
            }
            timetex += "Kochzeit " + time.toString() + " min";
        }

        time = head.getTimeprepqty();
        if (time != null) {
            if (!timetex.equals("")) {
                timetex += ", ";
            }
            timetex += "Vorbereitungszeit: " + time.toString() + " min";
        }

        if (!timetex.equals("")) {
            timetex = "{\\" + "bfseries Zeit:} " + timetex + "\n\n";
        }

        return timetex;
    }

    // Quellenangabe
    private String source2tex(Head head) {
        String sourcetex = "";
        sourcetex = head.getSourceline().stream().map((stit) -> stit + " ").reduce(sourcetex, String::concat);
        if (!sourcetex.equals("")) {
            sourcetex = "{\\" + "bfseries Quelle:} " + sourcetex + "\n\n";
        }
        return sourcetex;
    }

    // Inhalt
    private String content2tex(Head head) {
//        String contex = "";
//        int anz = 1;
//        Integer qty = 1;
//        if (head.getServingqty() != null) {
//            qty = Integer.valueOf(head.getServingqty());
//        }
//
//        Float kalorien = -1.0f;
//        Float be = -1.0f;
//
//        for (Head.Content cont : head.getContent()) {
//            String unit = cont.getType();
//            String[] numbers = cont.getValue().split(",");
//            Float unitvalue;
//            String contunit = "";
//            if (numbers.length == 2) {
//                int nlength = numbers[1].length();
//                unitvalue = Float.parseFloat(numbers[1]) / (float) Math.pow(10.0, nlength);
//                unitvalue = (unitvalue + Float.parseFloat(numbers[0])) / (float) qty;
//            } else {
//                unitvalue = Float.parseFloat(numbers[0]) / (float) qty;
//            }
//            if (unit.equals("GCAL")) {
//                kalorien = unitvalue;
//                anz--;
//            } else {
//                if (unit.equals("GKB")) {
//                    be = unitvalue;
//                } else {
//                    if (unit.equals("GJ")) {
//                        contunit = String.valueOf(unitvalue.intValue()) + "~kJ";
//                    } else {
//                        unitvalue = unitvalue / 1000.0f;
//                        if (unit.equals("ZE")) {
//                            contunit = String.valueOf(unitvalue.intValue()) + "~g Eiweiß";
//                        } else {
//                            if (unit.equals("ZF")) {
//                                contunit = String.valueOf(unitvalue.intValue()) + "~g Fett";
//                            } else {
//                                if (unit.equals("ZK")) {
//                                    contunit = String.valueOf(unitvalue.intValue()) + "~g Kohlenhydrate";
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if ((anz > 1) && !(contunit.equals(""))) {
//                contex += ", ";
//            }
//            anz++;
//            contex += contunit;
//        }
//        if (kalorien > 0) {
//            if (anz > 3) {
//                contex += ", " + String.valueOf(kalorien.intValue()) + "~kcal";
//            }
//        }
//        if (be > 0) {
//            if (anz > 3) {
//                be = be * 100.0f;
//                int intvalue = be.intValue();
//                be = intvalue / 100.0f;
//                contex += ", " + be.toString() + "~BE";
//            }
//        }

        String contex = FormatHelper.outputContent(head);
        if (!contex.equals("")) {
            contex = "{\\" + "bfseries Inhalt pro Portion:} " + contex + "\n\n";
        }

        return contex;
    }

    // Portionen
    private String serve2tex(Head head) {
        String serve = "";
        if (head.getServingqty() != null || !head.getServingqty().equals("")) {
            serve += head.getServingqty() + " ";
        }
        if (head.getServingtype() != null || !head.getServingtype().equals("")) {
            serve += head.getServingtype();
        }
        if (!serve.equals("")) {
            serve = "{\\" + "bfseries Menge:} " + serve + "\n\n";
        }
        return serve;
    }

    // Schritte der Zubereitung
    private String prep2tex(Preparation prep) {
        String preptex = "";
        List<String> steps = prep.getStep();
        List<String> texts = prep.getText();
        for (Iterator<String> stit = steps.iterator(); stit.hasNext();) {
            preptex += stit.next() + "\n\n";
        }
        for (Iterator<String> stit = texts.iterator(); stit.hasNext();) {
            preptex += stit.next() + "\n\n";
        }
        return preptex;
    }

    // TODO check
    // Indexeintraege
    private String index2tex(Head head) {
        String indtex = "";
        String title = head.getTitle();
        String start = "\\" + "index{";
        String middle = "@{";
        String end = "\\" + "/}!" + title + "@{" + title + "\\" + "/}}\n";
        int cat = 0;

        List<String> text = head.getCat();
        for (Iterator<String> stit = text.iterator(); stit.hasNext();) {
            cat++;
            if (cat > 1) {
                String entry = stit.next();
                indtex += start + entry + middle + entry + end;
            } else {
                stit.next();
            }
        }

        indtex = head.getHint().stream().map((entry)
                -> start + entry + middle + entry + end).reduce(indtex, String::concat);

        return indtex;
    }

    private String remark2tex(Remark remark) {
        String remtex = "";
        remtex = remark.getLine().stream().filter((inhalt)
                -> (!(inhalt == null || inhalt.length() == 0))).map((inhalt) -> inhalt + " ").reduce(remtex, String::concat); //!inhalt.isEmpty()

        if (!remtex.equals("")) {
            remtex = "{\\" + "bfseries Bemerkung:} " + remtex + "\n\n";
        }
        return remtex;
    }

    private String pic2tex(Head head) {
        String pictex = "\n\n";
        for (Picbin pic : head.getPicbin()) {
            String picName = exportInfo.getBookname() + "_" + anzpics++
                    + "." + pic.getFormat().toLowerCase(Locale.getDefault());;
            String path = filepath + "images" + File.separator + picName;
            try {
                File file = new File(path);
                InputStream inputStream = new ByteArrayInputStream(pic.getValue());
                if (!exportInfo.isIsColor()) {
                    FilesUtils.changeImgeColor2BW(inputStream, file);
                } else {
                    FileUtils.copyInputStreamToFile(inputStream, file);
                }
                pictex += "\\bild{images/" + picName + "}\n\n";
            } catch (Exception ex) {
                Logger.getLogger(CmlToLatex.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Lesen Bild -- Latex");
            }
        }
        return pictex;
//        pictex = head.getPicture().stream().map((pic) -> pic.getFile()).map((filename)
//                -> "\\bild{" + filename + "}\n\n").reduce(pictex, String::concat);

    }

    // Zutaten 
    private String ing2tex(Ingredient ing) {
        String ingtex = "";
        String text;
        Float qty = ing.getQty();

        if (qty != null) {
            ingtex += FormatHelper.formatQTY(qty) + " ";
        }

        text = ing.getUnit();
        if (text != null) {
            ingtex += FormatHelper.formatUnitDE(text) + " ";
        }

        text = ing.getItem();
        if (text != null) {
            ingtex += text;
        }

        ingtex = ing.getInote().stream().map((stit) -> ", " + stit).reduce(ingtex, String::concat);
        return ingtex;
    }

    private String texString(String line) {
        String texstring = line;
        int setoff = 0;
        String anfang;

        for (int pos = 0; pos < line.length(); pos++) {
            char charat = line.charAt(pos);
            switch (charat) {
                case '%':
                    if (pos == 0) {
                        anfang = "";
                    } else {
                        anfang = texstring.substring(0, pos + setoff - 1);
                    }
                    texstring = anfang + "\\%" + line.substring(pos + 1);
                    setoff++;
                    break;
                case '"':
                    if (pos == 0) {
                        anfang = "";
                    } else {
                        anfang = texstring.substring(0, pos + setoff - 1);
                    }
                    texstring = anfang + "''" + line.substring(pos + 1);
                    setoff++;
                    break;
                case '°':
                    if (pos == 0) {
                        anfang = "";
                    } else {
                        anfang = texstring.substring(0, pos + setoff - 1);
                    }
                    texstring = anfang + "$^\\circ$" + line.substring(pos + 1);
                    setoff += 7;
                    break;
                case '&':
                    if (pos == 0) {
                        anfang = "";
                    } else {
                        anfang = texstring.substring(0, pos + setoff - 1);
                    }
                    texstring = anfang + "\\&" + line.substring(pos + 1);
                    setoff++;
                    break;
                case '#':
                    if (pos == 0) {
                        anfang = "";
                    } else {
                        anfang = texstring.substring(0, pos + setoff - 1);
                    }
                    texstring = anfang + "" + line.substring(pos + 1);
                    setoff--;
                    break;

            }
        }

        return texstring;
    }

}
