package de.hs.inform.lyuz.cookbook.controller.manager;

import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CategoryManager {

    public void renameCategory(MyBook myBook, String oldName, String newName) {
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

//        return myBook;
    }

    public void allocateCatList(MyBook myBook, String cat1, String cat2) {
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

        myBook.getCatExtra().remove(cat1);
//        return myBook;
    }

    public void removeCategory(MyBook myBook, String cat, boolean b) {
        if (!b) {
            myBook.getCookml().getRecipe().forEach((recakt) -> {
                recakt.getHeadAndCustomAndPart().stream().filter((objakt)
                        -> (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")))
                        .forEachOrdered((objakt) -> {
                            Set<String> newcatList = new HashSet<>();
                            ((Head) objakt).getCat().forEach((catakt) -> {
                                if (catakt.equals(cat)) {
                                    newcatList.add("ANDERE");
                                } else {
                                    newcatList.add(catakt);
                                }
                            });
                            ((Head) objakt).setCat(new ArrayList<>(newcatList));
                        });
            });
            if (myBook.getCatTemplate().contains("ANDERE")) {
                myBook.getCatTemplate().add("ANDERE");
            } 
        }
        myBook.getCatTemplate().remove(cat);

//        return myBook;
    }

    public void leftCategory(MyBook myBook, String catakt) {

        myBook.getCatTemplate().add(catakt);
        myBook.getCatExtra().remove(catakt);

//        return myBook;
    }

}
