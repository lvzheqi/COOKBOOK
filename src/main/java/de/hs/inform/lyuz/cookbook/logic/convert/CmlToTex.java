package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import de.hs.inform.lyuz.cookbook.model.cookml.Picbin;
import de.hs.inform.lyuz.cookbook.model.cookml.Preparation;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
import de.hs.inform.lyuz.cookbook.model.cookml.Step;
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

public class CmlToTex {

    private LinkedHashMap<String, Cookml> cmlSort;
    private ExportInfo exportInfo;

    private String filepath;

    private String tex = "";

    private int anzpics = 0;

    private String errorMessage ="";

    public String getErrorMessage() {
        return errorMessage;
    }

    
    public String getTex() {
        String vorspann = "\\begin{document}\n\n";
        if (exportInfo.isHasCover()) {
            File f = new File(filepath + "cover.pdf");
            if (f.exists()) {
                vorspann += "\\includepdf[fitpaper]{cover.pdf}\n\n";
            }
        }
        String content = vorspann + "\\author{" + exportInfo.getFirstName() + " " + exportInfo.getLastName() + "}\n\\title{" + exportInfo.getBookname()
                + "}\n\\maketitle\n";
        if (exportInfo.isHasCat()) {
            content += "\\tableofcontents\\pagebreak \n \n";
        }
        tex = content + tex;
        return tex;
    }

    public CmlToTex(MyBook myBook, String filepath) {
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
        String parttex = "\n\\begin{multicols}{2}";		// Zutaten
        String preptex = "\n" + "\\" + "end{multicols}\n\n";		// Zubereitung
        String remarktex = "";								// Bemerkungen
        String sourcetex = "";								// Quellenangaben
        String timetex = "";								// Zubereitungszeit
        String servetex = "";								// Portionen
        String conttex = "";									// pro Portion
        String pictex = "";									// Bilder
        String pointtex = "";

        //head
        Head headakt = recipe.getHead();
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
        if (exportInfo.isHasQuality()) {
            pointtex = point2tex(headakt);
        }

        //remark
        if (exportInfo.isHasRemark()) {
            for (Remark remark : recipe.getRemark()) {
                remarktex += remark2tex(remark);
            }
        }

        //Preparation
        for (Preparation prep : recipe.getPreparation()) {
            preptex += prep2tex(prep);
        }

        //Part
        for (Recipe.Part part : recipe.getPart()) {
            String title = part.getTitle();
            parttex += "\n\n";
            if (title != null) {
                parttex += "\\" + "textit{" + title + "}\n\n";
            }
            parttex = part.getIngredient().stream().map((ingit)
                    -> ing2tex(ingit)).map((ingtex) -> "\n\n" + ingtex).reduce(parttex, String::concat);

        }
        rectex += titletex + texString(indextex + "\n }{" + pointtex + parttex + pictex + preptex
                + remarktex + servetex + conttex + timetex + sourcetex);
        rectex += "} \n\n";

        return rectex;
    }

    // Zubereitungszeit
    private String time2tex(Head head) {
        String timetex = "";

        BigInteger time = head.getTimeallqty();
        if (time != null) {
            timetex += "Gesamtzeit " + FormatHelper.reformatTime(time.toString());
        }

        time = head.getTimecookqty();
        if (time != null) {
            if (!timetex.equals("")) {
                timetex += ", ";
            }
            timetex += "Kochzeit " + FormatHelper.reformatTime(time.toString());
        }

        time = head.getTimeprepqty();
        if (time != null) {
            if (!timetex.equals("")) {
                timetex += ", ";
            }
            timetex += "Vorbereitungszeit: " + FormatHelper.reformatTime(time.toString());
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

        String contex = FormatHelper.outputContent(head);
        if (!contex.equals("")) {
            contex = "{\\" + "bfseries Inhalt pro Portion:} " + contex + "\n\n";
        }

        return contex;
    }

    // Portionen
    private String serve2tex(Head head) {
        String serve = "";
        if (head.getServingqty() != null && !head.getServingqty().equals("")) {
            serve += head.getServingqty() + " ";
        }
        if (head.getServingtype() != null && !head.getServingtype().equals("")) {
            serve += head.getServingtype();
        }
        if (!serve.equals("")) {
            serve = "{\\" + "bfseries Menge:} " + serve + "\n\n";
        } else {
            serve = "{\\" + "bfseries Menge:} 1 Portion \n\n";
        }
        return serve;
    }

    // Schritte der Zubereitung
    private String prep2tex(Preparation prep) {
        String preptex = "";
        for (Step step : prep.getStep()) {
            preptex += step.getText() + "\n";
        }
        preptex += prep.getText() + "\n\n";

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
                    + "." + pic.getFormat().toLowerCase(Locale.getDefault());
            String path = filepath + "images" + File.separator + picName;
            try {
                File file = new File(path);
                InputStream inputStream = new ByteArrayInputStream(pic.getValue());
                if (!exportInfo.isIsColor()) {
                    FilesUtils.changeImgeColor2BW(inputStream, file, pic.getFormat().toLowerCase(Locale.getDefault()));
                } else {
                    FileUtils.copyInputStreamToFile(inputStream, file);
                }
                pictex += "\\bild{images/" + picName + "}\n\n";

            } catch (Exception ex) {
                Logger.getLogger(CmlToTex.class.getName()).log(Level.SEVERE, null, ex);
                errorMessage += "Fehler beim Schreiben CML Bild beim Rezept "+ head.getTitle()+"\n";
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

    private String point2tex(Head headakt) {
        String pointtex = "";
        if (headakt.getQuality() != null) {
            int p;
                p = headakt.getQuality();
                pointtex = "\\icons{images/star.png}"
                        + "{images/star_board.png}{" + p + "}";
        }
        return pointtex;
    }

}
