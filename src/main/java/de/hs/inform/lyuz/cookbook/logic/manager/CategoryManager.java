package de.hs.inform.lyuz.cookbook.logic.manager;

import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;

public class CategoryManager {

    public void renameCategory(MyBook myBook, String oldName, String newName) {
//        myBook.getCookml().getRecipe().forEach((recakt) -> {
        for (Object object : myBook.getCookml().getContent()) {
            if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                Recipe recakt = (Recipe) object;
                Set<String> newcatList = new HashSet<>();
                recakt.getHead().getCat().forEach((catakt) -> {
                    if (catakt.equals(oldName)) {
                        newcatList.add(newName);
                    } else {
                        newcatList.add(catakt);
                    }
                });
                recakt.getHead().setCat(new ArrayList<>(newcatList));

            }
        }
        for (int i = 0; i < myBook.getCatTemplate().size(); i++) {
            if (myBook.getCatTemplate().get(i).equals(oldName)) {
                myBook.getCatTemplate().set(i, newName);
            }
        }
    }

    public void allocateCatList(MyBook myBook, String cat1, String cat2) {
//        myBook.getCookml().getRecipe().forEach((recakt) -> {
        for (Object object : myBook.getCookml().getContent()) {
            if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                Recipe recakt = (Recipe) object;
                Set<String> newcatList = new HashSet<>();
                recakt.getHead().getCat().forEach((catakt) -> {
                    if (catakt.equals(cat1)) {
                        newcatList.add(cat2);
                    } else {
                        newcatList.add(catakt);
                    }
                });
                recakt.getHead().setCat(new ArrayList<>(newcatList));

            }
        }

        myBook.getCatExtra().remove(cat1);
    }

    public void removeCategory(MyBook myBook, String cat, boolean b) {
        if (!b) {
//            myBook.getCookml().getRecipe().forEach((recakt) -> {
            for (Object object : myBook.getCookml().getContent()) {
                if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                    Recipe recakt = (Recipe) object;
                    Set<String> newcatList = new HashSet<>();
                    recakt.getHead().getCat().forEach((catakt) -> {
                        if (catakt.equals(cat)) {
                            newcatList.add("ANDERE");
                        } else {
                            newcatList.add(catakt);
                        }
                    });
                    recakt.getHead().setCat(new ArrayList<>(newcatList));
                }
            }
            if (!myBook.getCatTemplate().contains("ANDERE")) {
                myBook.getCatTemplate().add("ANDERE");
            }
        }
        myBook.getCatTemplate().remove(cat);
    }

    public void leftCategory(MyBook myBook, String catakt) {

        myBook.getCatTemplate().add(catakt);
        myBook.getCatExtra().remove(catakt);
    }

    public void upDownCategory(MyBook myBook, DefaultListModel catTempListModel) {

        ArrayList<String> catList = new ArrayList<>();
        for (int i = 0; i < catTempListModel.size(); i++) {
            catList.add((String) catTempListModel.get(i));
        }
        myBook.setCatTemplate(catList);
    }
}
