package de.hs.inform.lyuz.cookbook.controller.manager;

import de.hs.inform.lyuz.cookbook.help.Utils;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CategoryManager {
    

    public static List<String> readCatTemplate() {
        List<String> catList = new ArrayList<>();
        String path = CategoryManager.class.getClassLoader().getResource("META-INF/category.txt").getPath();
        System.out.println(path);
        String text = Utils.readFile(CategoryManager.class.getClassLoader().getResourceAsStream("META-INF/category.txt"));
        
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.equals("")) {
                catList.add(line);
            }
        }
        return catList;
    }
    
    public static void writeCatTemplate(List<String> catList) {
        String text = "";
        text = catList.stream().map((cat) -> cat+"\n").reduce(text, String::concat);
        Utils.writeTexttoFile(text, "category.txt");
    }

    public MyBook renameCatList(MyBook myBook, String oldName, String newName) {
        myBook.getCookml().getRecipe().forEach((recakt) -> {
            recakt.getHeadAndCustomAndPart().stream().filter((objakt)
                    -> (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")))
                    .forEachOrdered((objakt) -> {
                Set<String> newcatList = new HashSet<>();
                ((Head) objakt).getCat().forEach((catakt) -> {
                    if (catakt.equals(oldName)) {
                        newcatList.add(newName);
                    } else {
                        newcatList.add(catakt);
                    }
                });
                ((Head) objakt).setCat(new ArrayList<>(newcatList));
            });
        });
        myBook.getCatTemplate().add(newName);
        myBook.getCatTemplate().remove(oldName);
        myBook.getCatAll().add(newName);
        myBook.getCatAll().remove(oldName);
        
        return myBook;
    }

    public MyBook allocateCatList(MyBook myBook, String cat1, String cat2) {
        myBook.getCookml().getRecipe().forEach((recakt) -> {
            recakt.getHeadAndCustomAndPart().stream().filter((objakt)
                    -> (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")))
                    .forEachOrdered((objakt) -> {
                Set<String> newcatList = new HashSet<>();
                ((Head) objakt).getCat().forEach((catakt) -> {
                    if (catakt.equals(cat1)) {
                        newcatList.add(cat2);
                    } else {
                        newcatList.add(catakt);
                    }
                });
                ((Head) objakt).setCat(new ArrayList<>(newcatList));
            });
        });
        myBook.getCatExtral().remove(cat1);
        myBook.getCatAll().remove(cat1);
        
        return myBook;
    }

    public MyBook removeCat4AllList(MyBook myBook, String cat, boolean b) {
        if (b) {
            //归到sonstige
            myBook.getCookml().getRecipe().forEach((recakt) -> {
                recakt.getHeadAndCustomAndPart().stream().filter((objakt)
                        -> (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")))
                        .forEachOrdered((objakt) -> {
                    Set<String> newcatList = new HashSet<>();
                    ((Head) objakt).getCat().forEach((catakt) -> {
                        if (catakt.equals(cat)) {
                            newcatList.add("SONSTIGE");
                        } else {
                            newcatList.add(catakt);
                        }
                    });
                    ((Head) objakt).setCat(new ArrayList<>(newcatList));
                });
            });
        }
        myBook.getCatAll().remove(cat);
        myBook.getCatTemplate().remove(cat);
    
        return myBook;
    }

    public MyBook addCatAllList(MyBook myBook, String catakt) {
        myBook.getCatTemplate().add(catakt);
        myBook.getCatExtral().remove(catakt);
        
        return myBook;
    }
}
