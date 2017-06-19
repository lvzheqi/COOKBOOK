package de.hs.inform.lyuz.cookbook.controller.manager;

import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import java.util.*;

public class SortManager {

    private final MyBook myBook;

    
    public SortManager(MyBook myBook) {
        this.myBook = myBook;
    }

    public MyBook sortCML(){
        sortCategory();
        refactorCookml();
        return this.myBook;        
    }
    
    private void refactorCookml() {
        Cookml cook = new Cookml();
        myBook.getSortCmlMap().keySet().forEach((catString) -> {
            myBook.getSortCmlMap().get(catString).getRecipe().forEach((recipe) -> {
                cook.getRecipe().add(recipe);
            });
        });
        myBook.setCookml(cook);
    }

    private void sortCategory() {
        LinkedHashMap<String, Cookml> sortCmlMap = new LinkedHashMap<>();
        myBook.getCookml().getRecipe().forEach((Recipe recakt) -> {
            recakt.getHeadAndCustomAndPart().stream().filter((Object objakt)
                    -> (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head"))).forEachOrdered((objakt) -> {
                ((Head) objakt).getCat().forEach((catakt) -> {
                    if (!sortCmlMap.containsKey(catakt)) {
                        Cookml cml = new Cookml();
                        cml.getRecipe().add(recakt);
                        sortCmlMap.put(catakt, cml);
                    } else {
                        Cookml cml = sortCmlMap.get(catakt);
                        cml.getRecipe().add(recakt);
                        sortCmlMap.replace(catakt, cml);
                    }
                });
            });
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
            for (Object objakt : recakt.getHeadAndCustomAndPart()) {
                if (objakt.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Head")) {
                    titakt = ((Head) objakt).getTitle();
                }
            }
            int index = 0;
            for (String titcomp : titlist) {
                if (titakt.compareTo(titcomp) > 0) {
                    index++;
                } else {
                    break;
                }
            }
            titlist.add(index, titakt);
            cookout.getRecipe().add(index, recakt);

        }
        return cookout;
    }

}
