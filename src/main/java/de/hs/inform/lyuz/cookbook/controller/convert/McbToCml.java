package de.hs.inform.lyuz.cookbook.controller.convert;

import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import de.hs.inform.lyuz.cookbook.controller.parser.MCBParser;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorExcepetion;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;
import java.io.File;
import java.math.BigInteger;
import org.apache.commons.io.FileUtils;

// TODO: when more than a categroy fehler
/**
 * imageurl, video ignore
 */
public class McbToCml {

    private Cookml cookml;
    private MCBParser mcbParser;

    public McbToCml(File f) throws ParserErrorExcepetion, ConvertErrorException {

        cookml = new Cookml();
        mcbParser = new MCBParser(f);

        Cookbook mcb = mcbParser.getMcb();

        cookml.setProg("MYCOOKBOOK");
        cookml.setProgver(mcb.getVersion());

        for (Cookbook.Recipe recipe : mcb.getRecipe()) {
            try {
                setRecipe(recipe);
            } catch (Exception ex) {
                throw new ConvertErrorException("Fehler beim Konvertierung von MCB");
            }
        }
    }

    private void setRecipe(Cookbook.Recipe recipe) throws Exception {
        Recipe recakt = new Recipe();
        Head headakt = new Head();

        if (recipe.getTitle() == null && recipe.getTitle().trim().equals("")) {
            headakt.setTitle("Unbekannt");
        } else {
            headakt.setTitle(recipe.getTitle());
        }
        
        // TODO: when more than a categroy fehler
        if (recipe.getCategory() != null) {
            headakt.getCat().add(recipe.getCategory());
        } else {
            headakt.getCat().add("Andere");
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
            if (tmp.length == 2) {
                headakt.setServingqty(tmp[0]);
                headakt.setServingtype(tmp[1]);
            } else {
                headakt.setServingtype(recipe.getQuantity().trim());
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
                    picbin.setValue(FileUtils.readFileToByteArray(f));
                    headakt.getPicbin().add(picbin);
                    break;
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
