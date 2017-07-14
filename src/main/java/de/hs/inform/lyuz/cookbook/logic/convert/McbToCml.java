package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import de.hs.inform.lyuz.cookbook.logic.parser.MCBParser;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorExcepetion;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;
import java.io.File;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                Logger.getLogger(McbToCml.class.getName()).log(Level.SEVERE, null, ex);
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
            if (!l.getValue().equals("")) {
                partakt.getIngredient().add(FormatHelper.formatIngredient(l.getValue().trim()));
            }
        });
        recakt.getHeadAndCustomAndPart().add(partakt);

        // main content
        Preparation prepakt = new Preparation();
        if (recipe.getRecipetext() != null && recipe.getRecipetext().getContent() != null) {
            recipe.getRecipetext().getContent().forEach((l) -> {
                if (!l.getValue().equals("")) {
                    prepakt.getText().add(l.getValue());
                }
            });
        }
        if (recipe.getDescription() != null && recipe.getDescription().getContent() != null) {
            recipe.getDescription().getContent().forEach((l) -> {
                if (!l.getValue().equals("")) {
                    prepakt.getText().add(l.getValue());
                }
            });
        }

        if (recipe.getQuantity() != null) {
            String[] tmp = FormatHelper.reformatLine(recipe.getQuantity()).split(" ");
            switch (tmp.length) {
                case 2:
                    headakt.setServingqty(tmp[0]);
                    headakt.setServingtype(tmp[1]);
                    break;
                case 1:
                    try{
                        int n = Integer.parseInt(tmp[0]);
                        headakt.setServingqty(tmp[0]);
                        if(n>1){
                            headakt.setServingtype("Protionen");
                        }else {
                            headakt.setServingtype("Protion");
                        }
                    } catch(Exception e){
                        headakt.setServingtype(recipe.getQuantity().trim());
                    }   break;
                default:
                    headakt.setServingtype(recipe.getQuantity().trim());
                    break;
            }
        }

        BigInteger time = FormatHelper.setCookTime(recipe.getPreptime());
        if (time != null) {
            headakt.setTimeprepqty(time);
        } else if (recipe.getPreptime() != null && !recipe.getPreptime().trim().equals("")) {
            prepakt.getText().add("\nVorbereitungszeit: " + recipe.getPreptime());
        }

        time = FormatHelper.setCookTime(recipe.getCooktime());
        if (time != null) {
            headakt.setTimecookqty(time);
        } else if (recipe.getCooktime() != null && !recipe.getCooktime().trim().equals("")) {
            prepakt.getText().add("\nZubereitungszeit: " + recipe.getCooktime());

        }

        time = FormatHelper.setCookTime(recipe.getTotaltime());
        if (time != null) {
            headakt.setTimeallqty(time);
        } else if (recipe.getTotaltime() != null && !recipe.getTotaltime().trim().equals("")) {
            prepakt.getText().add("\nArbeitzeit: " + recipe.getTotaltime());

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
            if (!nutrition.equals("")) {
                remakt.getLine().add(nutrition.substring(0, nutrition.length() - 2));
            }
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
