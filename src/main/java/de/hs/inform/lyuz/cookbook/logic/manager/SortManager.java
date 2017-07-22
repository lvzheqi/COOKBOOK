package de.hs.inform.lyuz.cookbook.logic.manager;

import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import java.util.*;

public class SortManager {

    private MyBook myBook;

    public SortManager(MyBook myBook) {
        this.myBook = myBook;
    }

    public MyBook sortBook() {
        sortCategory();
        refactorCookml();
        return myBook;
    }

    private void refactorCookml() {
        Cookml cook = new Cookml();
        myBook.getSortCmlMap().keySet().forEach((catString) -> {
            myBook.getSortCmlMap().get(catString).getRecipe().forEach((recipe) -> {
                cook.getContent().add(recipe);
            });
        });
        myBook.setCookml(cook);
    }

    private void sortCategory() {
        LinkedHashMap<String, Cookml> sortCmlMap = new LinkedHashMap<>();
        myBook.getCookml().getRecipe().forEach((Recipe recakt) -> {
            String catakt = recakt.getHead().getCat().get(0);
            if (!sortCmlMap.containsKey(catakt)) {
                Cookml cml = new Cookml();
                cml.getContent().add(recakt);
                sortCmlMap.put(catakt, cml);
            } else {
                Cookml cml = sortCmlMap.get(catakt);
                cml.getContent().add(recakt);
                sortCmlMap.replace(catakt, cml);
            }
//                    forEach((catakt) -> {
//                if (!sortCmlMap.containsKey(catakt)) {
//                    Cookml cml = new Cookml();
//                    cml.getContent().add(recakt);
//                    sortCmlMap.put(catakt, cml);
//                } else {
//                    Cookml cml = sortCmlMap.get(catakt);
//                    cml.getContent().add(recakt);
//                    sortCmlMap.replace(catakt, cml);
//                }
//            });
            });

            LinkedHashMap<String, Cookml> sort = new LinkedHashMap<>();
            myBook.getCatTemplate().forEach((sortString) -> {
                if (sortCmlMap.containsKey(sortString)) {
                    sort.put(sortString, sortRez(sortCmlMap.get(sortString)));
                }
            });
            myBook.setSortCmlMap(sort);
        }

    private Cookml sortRez(Cookml cook) {
        Cookml cookout = new Cookml();
        List<String> titlist = new ArrayList<>();
        for (Recipe recakt : cook.getRecipe()) {
            String titakt = "";
            titakt = recakt.getHead().getTitle();
            List<String> cats = new ArrayList<>();
            recakt.getHead().getCat().forEach((cat) -> {
                if (cat.length() > 1) {
                    String lowString = cat.substring(1).toLowerCase();
                    cats.add(cat.charAt(0) + lowString);
                } else {
                    cats.add(cat);
                }
            });
            recakt.getHead().setCat(cats);

            int index = 0;
            for (String titcomp : titlist) {
                if (titakt.compareTo(titcomp) > 0) {
                    index++;
                } else {
                    break;
                }
            }

            titlist.add(index, titakt);
            cookout.getContent().add(index, recakt);
        }
        return cookout;
    }

}
