package de.hs.inform.lyuz.cookbook.controller.convert;


import de.hs.inform.lyuz.cookbook.help.FormatHelper;
import de.hs.inform.lyuz.cookbook.controller.parser.MCBParser;
import de.hs.inform.lyuz.cookbook.help.Utils;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;
import java.io.File;
import java.math.BigInteger;

public class McbToCml {

    private final Cookml cookml;
    private final MCBParser mcbParser;

    public McbToCml(File f) {

        // TODO: throw
        cookml = new Cookml();
        mcbParser = new MCBParser(f);

        Cookbook mcb = mcbParser.getMcb();

        cookml.setProg("MYCOOKBOOK");
        cookml.setVersion(mcb.getVersion());
        cookml.setProgver("0.91");

        mcb.getRecipe().forEach((recipe) -> {
            setRecipe(recipe);
        });
    }

    private void setRecipe(Cookbook.Recipe recipe) {
        Recipe recakt = new Recipe();
        Head headakt = new Head();

        headakt.setTitle(recipe.getTitle());
        if (recipe.getCategory() != null) {
            headakt.getCat().add(recipe.getCategory());
        } else {
            headakt.getCat().add("Sonstige");
        }

        if (recipe.getRating() != null) {
            headakt.setWwpoints(Float.parseFloat(recipe.getRating()));
        }

        // ingredient
        Recipe.Part partakt = new Recipe.Part();
        recipe.getIngredient().getContent().forEach((l) -> {
            partakt.getIngredient().add(FormatHelper.formatIngredient(l.getValue().trim().split(" ")));
        });
        recakt.getHeadAndCustomAndPart().add(partakt);

        // main content
        Preparation prepakt = new Preparation();
        if (recipe.getRecipetext() != null && recipe.getRecipetext().getContent() != null) {

            recipe.getRecipetext().getContent().forEach((l) -> {
                prepakt.getText().add(l.getValue());
            });
        }
        if (recipe.getDescription() != null && recipe.getDescription().getContent() != null) {
            recipe.getDescription().getContent().forEach((l) -> {
                prepakt.getText().add(l.getValue());
            });
        }

        if (recipe.getQuantity() != null) {
            String[] tmp = recipe.getQuantity().trim().split(" ");
            try {
                Float.parseFloat(tmp[0]);
                headakt.setServingqty(tmp[0]);
                if (tmp.length == 1) {
                    headakt.setServingtype("Protionen");
                } else if (tmp.length == 2) {
                    headakt.setServingtype(tmp[1]);
                }
            } catch (NumberFormatException e) {
                prepakt.getText().add(recipe.getQuantity());
            }
        }

        BigInteger time = FormatHelper.setCookTime(recipe.getPreptime());
        if (time != null) {
            headakt.setTimeprepqty(time);
        } else if (recipe.getPreptime() != null) {
            prepakt.getText().add("Vorbereitungszeit: " + recipe.getPreptime());
        }

        time = FormatHelper.setCookTime(recipe.getCooktime());
        if (time != null) {
            headakt.setTimecookqty(time);
        } else if (recipe.getCooktime() != null) {
            prepakt.getText().add("Zubereitungszeit: " + recipe.getCooktime());
        }

        time = FormatHelper.setCookTime(recipe.getTotaltime());
        if (time != null) {
            headakt.setTimeallqty(time);
        } else if (recipe.getTotaltime() != null) {
            prepakt.getText().add("Arbeitzeit: " + recipe.getTotaltime());
        }
        recakt.getHeadAndCustomAndPart().add(prepakt);

        // remark
        Remark remakt = new Remark();
        if (recipe.getComments() != null && recipe.getComments().getContent() != null) {
            recipe.getComments().getContent().forEach((l) -> {
                remakt.getLine().add(l.getValue());
            });
        }

        // source
        if (recipe.getUrl() != null) {
            headakt.getSourceline().add(recipe.getUrl());
        }
        if (recipe.getSource() != null && recipe.getSource().getContent() != null) {

            recipe.getSource().getContent().forEach((l) -> {
                headakt.getSourceline().add(l.getValue());
            });
        }

        // nutrition
        String nutrition = "";
        if (recipe.getNutrition() != null && recipe.getNutrition().getContent() != null) {
            nutrition = recipe.getNutrition().getContent().stream().map((l) -> l.getValue() + ", ")
                    .reduce(nutrition, String::concat);
            remakt.getLine().add(nutrition.substring(0, nutrition.length() - 2));
        }

        // image;
        if (recipe.getImagepath() != null) {
            String[] picpath = recipe.getImagepath().split("/");
            for (File f : mcbParser.getFiles()) {
                if (f.getName().equals(picpath[picpath.length - 1])) {
                    Picbin picbin = new Picbin();
                    picbin.setFormat("JPG");
                    picbin.setValue(Utils.file2Byte(f));
                    headakt.getPicbin().add(picbin);
                }
            }
        }

        if (!remakt.getLine().isEmpty()) {
            recakt.getHeadAndCustomAndPart().add(remakt);
        }
        recakt.getHeadAndCustomAndPart().add(headakt);
        cookml.getRecipe().add(recakt);
    }

    public Cookml getCookml() {
        return cookml;
    }

}
