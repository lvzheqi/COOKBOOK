package de.hs.inform.lyuz.cookbook.controller.parser;

import java.io.File;
import java.util.*;
import de.hs.inform.lyuz.cookbook.controller.convert.BSToCml;
import de.hs.inform.lyuz.cookbook.controller.convert.McbToCml;
import de.hs.inform.lyuz.cookbook.controller.convert.MMToCml;
import de.hs.inform.lyuz.cookbook.controller.manager.CategoryManager;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.*;


public class FileParser {

    private MyBook myBook;

    public MyBook getMyBook() {
        return myBook;
    }
    
    public FileParser() {
        myBook = new MyBook();
    }
    
    public void reloadFiles(List<File> files) {
        myBook = new MyBook();
        myBook.setFiles(files);
        fileParser();
    }

    private void fileParser() {
        myBook.getFiles().forEach((File f) -> {
            Cookml cookml = null;
            if (f.getName().endsWith("cml")) {
                CMLParser cmlParser = new CMLParser(f);
                cookml = cmlParser.getCookml();
            } else if (f.getName().endsWith("mcb")) {
                McbToCml mcbToCml = new McbToCml(f);
                cookml = mcbToCml.getCookml();
            } else if (f.getName().endsWith("mm")) {
                MMToCml mmToCML = new MMToCml(f);
                cookml = mmToCML.getCookml();
            } else if (f.getName().endsWith("bs")) {
                BSToCml bsTocml = new BSToCml(f);
                cookml = bsTocml.getCookml();
            }

            sortCatList(cookml);
            add2cooml(cookml);
        });
    }

    
    private void add2cooml(Cookml cml) {
        if (myBook.getCookml() == null) {
            myBook.setCookml(cml);
        } else {
            cml.getRecipe().forEach((recipe) -> {
                myBook.getCookml().getRecipe().add(recipe);
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
            catNorm = "GEFLUEGEL";
        } else if (catakt.toUpperCase().contains("GEMUESE") || catakt.toUpperCase().contains("GEMÜSE")
                || catakt.toUpperCase().contains("VAGETABLE")) {
            catNorm = "GEMUESE";
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
        } else if (catakt.toUpperCase().contains("DRINK")
                || catakt.toUpperCase().contains("SONSTIGE")) {
            catNorm = "SONSTIGE";
        } else {
            catNorm = catakt.toUpperCase();
        }
        return catNorm;
    }

    private void sortCatList(Cookml cookml) {

        if (!myBook.getCatTemplate().contains("SONSTIGE")) {
            myBook.getCatTemplate().add("SONSTIGE");
        }
        cookml.getRecipe().forEach((Recipe recakt) -> {
            for (Object objakt : recakt.getHeadAndCustomAndPart()) {
                if (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")) {
                    Set<String> newcatList = new HashSet<>();
                    ((Head) objakt).getCat().stream().map((catakt) -> normCat(catakt)).map((catNorm) -> {
                        newcatList.add(catNorm);
                        return catNorm;
                    }).map((catNorm) -> {
                        for (String ct : CategoryManager.getCatTemplate()) {
                            if (catNorm.equals(ct) && !myBook.getCatTemplate().contains(catNorm)) {
                                myBook.getCatTemplate().add(catNorm);
                            }
                        }
                        return catNorm;
                    }).filter((catNorm) -> (!myBook.getCatTemplate().contains(catNorm) && !myBook.getCatExtral().contains(catNorm))).forEachOrdered((catNorm) -> {
                        myBook.getCatExtral().add(catNorm);
                    });
                    if (newcatList.isEmpty()) {
                        newcatList.add("SONSTIGE");
                    }
                    ((Head) objakt).setCat(new ArrayList<>(newcatList));

                    newcatList.stream().filter((ct)
                            -> (!myBook.getCatAll().contains(ct))).forEachOrdered((ct) -> {
                        myBook.getCatAll().add(ct);
                    });
                }
            }
        });
    }


}
