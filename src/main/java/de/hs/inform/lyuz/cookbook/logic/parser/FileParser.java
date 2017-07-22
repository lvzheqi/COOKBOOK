package de.hs.inform.lyuz.cookbook.logic.parser;

import java.io.File;
import java.util.*;
import de.hs.inform.lyuz.cookbook.logic.convert.BsToCml;
import de.hs.inform.lyuz.cookbook.logic.convert.McbToCml;
import de.hs.inform.lyuz.cookbook.logic.convert.MmToCml;
import de.hs.inform.lyuz.cookbook.utils.ConfUtils;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.SystemErrorException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileParser {

    private MyBook myBook;

    public MyBook getMyBook() {
        return myBook;
    }

    public FileParser() throws SystemErrorException {
        myBook = new MyBook();
        try {
            myBook.setExportInfo(ConfUtils.getExportInfo());
        } catch (Exception ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information", ex.getClass().getName());
        }
    }

    public void reloadFiles(List<File> files) throws ParserErrorException, ConvertErrorException {
        myBook = new MyBook();
        myBook.setFiles(files);
        fileParser();
    }

    private void fileParser() throws ParserErrorException, ConvertErrorException {
        for (File f : myBook.getFiles()) {
            Cookml cookml = null;
            if (f.getName().endsWith("cml")) {
                CMLParser cmlParser = new CMLParser(f);
                cookml = cmlParser.getCookml();
                myBook.setErrorMessage(myBook.getErrorMessage() + cmlParser.getErrorMessage());
            } else if (f.getName().endsWith("mcb")) {
                McbToCml mcbToCml = new McbToCml(f);
                cookml = mcbToCml.getCookml();
                myBook.setErrorMessage(myBook.getErrorMessage() + mcbToCml.getErrorMessage());

            } else if (f.getName().endsWith("mm")) {
                MmToCml mmToCML = new MmToCml(f);
                cookml = mmToCML.getCookml();
                myBook.setErrorMessage(myBook.getErrorMessage() + mmToCML.getErrorMessage());

            } else if (f.getName().endsWith("bs")) {
                BsToCml bsTocml = new BsToCml(f);
                cookml = bsTocml.getCookml();
                myBook.setErrorMessage(myBook.getErrorMessage() + bsTocml.getErrorMessage());
            }

            sortCatList(cookml);
            add2cooml(cookml);
        }
    }

    private void add2cooml(Cookml cml) {
        if (myBook.getCookml() == null) {
            myBook.setCookml(cml);
        } else {
            cml.getRecipe().forEach((recipe) -> {
                myBook.getCookml().getContent().add(recipe);
            });
        }
    }

    private String normCat(String catakt) {
        String catNorm;

        if (catakt.toUpperCase().contains("SALAT") || catakt.toUpperCase().contains("SALAD")) {
            catNorm = "SALAT";

        } else if (catakt.toUpperCase().contains("SUPPE")) {
            catNorm = "SUPPE";
        } else if (catakt.toUpperCase().contains("PASTA")) {
            catNorm = "PASTA";
        } else if (catakt.toUpperCase().contains("FISCH") || catakt.toUpperCase().contains("FISH")) {
            catNorm = "FISCH";
        } else if (catakt.toUpperCase().contains("FLEISCH") || catakt.toUpperCase().contains("MEAT")) {
            catNorm = "FLEISCH";
        } else if (catakt.toUpperCase().contains("GEFLUEGEL") || catakt.toUpperCase().contains("GEFLÜGEL")
                || catakt.toUpperCase().contains("HUHN")
                || catakt.toUpperCase().contains("ENTE") || catakt.toUpperCase().contains("GANS")) {
            catNorm = "GEFLÜGEL";
        } else if (catakt.toUpperCase().contains("GEMUESE") || catakt.toUpperCase().contains("GEMÜSE")
                || catakt.toUpperCase().contains("VAGETABLE")) {
            catNorm = "GEMÜSE";
        } else if (catakt.toUpperCase().contains("VEGETARISCH") || catakt.toUpperCase().contains("VEGETARIAN")) {
            catNorm = "VEGETARISCH";
        } else if (catakt.toUpperCase().contains("BEILAGE")) {
            catNorm = "BEILAGE";
        } else if (catakt.toUpperCase().contains("SAUCE") || catakt.toUpperCase().contains("MARINADE")
                || catakt.toUpperCase().contains("DIP")) {
            catNorm = "SAUCE";
        } else if (catakt.toUpperCase().contains("NACHTISCH") || catakt.toUpperCase().contains("DESSERT")) {
            catNorm = "NACHTISCH";
        } else if (catakt.toUpperCase().contains("BACKEN") || catakt.toUpperCase().contains("KUCHEN")
                || catakt.toUpperCase().contains("CAKE")) {
            catNorm = "BACKEN";
        } else if (catakt.toUpperCase().contains("DRINK")
                || catakt.toUpperCase().contains("GETRAENKE") || catakt.toUpperCase().contains("GETRANKE")) {
            catNorm = "DRINK";
        } else if (catakt.toUpperCase().contains("SONSTIGES")
                || catakt.toUpperCase().contains("SONSTIGE")) {
            catNorm = "SONSTIGE";
        } else {
            catNorm = catakt.toUpperCase();
        }
        return catNorm;
    }

    private void sortCatList(Cookml cookml) {
        List<String> catTemp = new ArrayList<>();
        try {
            catTemp = ConfUtils.getCatTemplate();
        } catch (Exception ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
//            throw new SystemErrorException("Fehler beim Lesen Kategorien");
            System.err.println("Fehler beim Lesen Kategorien Muster");
            myBook.setErrorMessage(myBook.getErrorMessage() + "Fehler beim Lesen Kategorien Muster");
        }

//        for (Recipe recakt : cookml.getRecipe()) {
        for (Object object : cookml.getContent()) {
            if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                Recipe recakt = (Recipe) object;
                ArrayList<String> newcatList = new ArrayList<>();

                for (String catakt : recakt.getHead().getCat()) {
                    String catNorm = normCat(catakt);
                    newcatList.add(catNorm);
                }
                if (newcatList.isEmpty()) {
                    newcatList.add("ANDERE");
                }

                String catakt = newcatList.get(0);
                for (String ct : catTemp) {
                    if (catakt.equals(ct) && !myBook.getCatTemplate().contains(catakt)) {
                        myBook.getCatTemplate().add(catakt);
                    }
                }
                if (!myBook.getCatTemplate().contains(catakt) && !myBook.getCatExtra().contains(catakt)) {
                    myBook.getCatExtra().add(catakt);
                }

                recakt.getHead().setCat(new ArrayList<>(newcatList));

            }
        }
    }
}
