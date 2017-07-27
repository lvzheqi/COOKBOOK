package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.logic.manager.ExportManager;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import de.hs.inform.lyuz.cookbook.logic.parser.MCBParser;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Picbin;
import de.hs.inform.lyuz.cookbook.model.cookml.Preparation;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
import de.hs.inform.lyuz.cookbook.model.cookml.Step;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorException;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


public class McbToCml {

    private Cookml cookml;
    private MCBParser mcbParser;

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public McbToCml(File f) throws ParserErrorException, ConvertErrorException {

        cookml = new Cookml();
        mcbParser = new MCBParser(f);

        Cookbook mcb = mcbParser.getMcb();

//        cookml.setProg("MYCOOKBOOK");
//        cookml.setProgver(mcb.getVersion());
        for (Cookbook.Recipe recipe : mcb.getRecipe()) {
            try {
                setRecipe(recipe);
            } catch (Exception ex) {
                Logger.getLogger(McbToCml.class.getName()).log(Level.SEVERE, null, ex);
                throw new ConvertErrorException("Fehler beim Konvertierung von MCB", ex.getClass().getName());
            }
        }
        
         try {
            FileUtils.deleteDirectory(new File(mcbParser.getFilePath()));
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Löschen MCB Hilfeatei ");
            errorMessage += "Fehler beim Löschen MCB Hilfeatei: " + mcbParser.getFilePath()+ "\n";
        }
    }

    private void setRecipe(Cookbook.Recipe recipe) {
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
            try {
                Short ranking = Short.parseShort(recipe.getRating());
                if (ranking > 0 && ranking < 6) {
                    headakt.setQuality(ranking);
                }
            } catch (Exception e) {
                Logger.getLogger(McbToCml.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("Fehler beim Konvertierung der Berwertung --Mcb");
                errorMessage += "Fehler beim Konvertierung der Berwertung " + recipe.getTitle() + "\n";

            }
        }

        // ingredient
        Recipe.Part partakt = new Recipe.Part();
        recipe.getIngredient().getContent().forEach((l) -> {
            if (!l.getValue().equals("")) {
                partakt.getIngredient().add(FormatHelper.formatIngredient(l.getValue().trim()));
            }
        });
        recakt.getPart().add(partakt);

        // main content
        Preparation prepakt = new Preparation();
        prepakt.setText("");
        if (recipe.getRecipetext() != null && recipe.getRecipetext().getContent() != null) {
            recipe.getRecipetext().getContent().forEach((l) -> {
                if (!l.getValue().equals("")) {
                    Step step = new Step();
                    step.setText(l.getValue());
                    prepakt.getStep().add(step);

                }
            });
        }
        if (recipe.getDescription() != null && recipe.getDescription().getContent() != null) {
            recipe.getDescription().getContent().forEach((l) -> {
                if (!l.getValue().equals("")) {
                    prepakt.setText(prepakt.getText() + "\n" + l.getValue());
                }
            });
        }

        if (recipe.getQuantity() != null) {
            String[] tmp = FormatHelper.setServing(FormatHelper.reformatLine(recipe.getQuantity()));
            headakt.setServingqty(tmp[0]);
            headakt.setServingtype(tmp[1]);
        }

        BigInteger time = FormatHelper.setCookTime(recipe.getPreptime());
        if (time != null) {
            headakt.setTimeprepqty(time);
        } else if (recipe.getPreptime() != null && !recipe.getPreptime().trim().equals("")) {
            prepakt.setText(prepakt.getText() + "\n" + "Vorbereitungszeit: " + recipe.getPreptime());
        }

        time = FormatHelper.setCookTime(recipe.getCooktime());
        if (time != null) {
            headakt.setTimecookqty(time);
        } else if (recipe.getCooktime() != null && !recipe.getCooktime().trim().equals("")) {
            prepakt.setText(prepakt.getText() + "\n" + "Zubereitungszeit: " + recipe.getCooktime());

        }

        time = FormatHelper.setCookTime(recipe.getTotaltime());
        if (time != null) {
            headakt.setTimeallqty(time);
        } else if (recipe.getTotaltime() != null && !recipe.getTotaltime().trim().equals("")) {
            prepakt.setText(prepakt.getText() + "\n" + "Arbeitzeit: " + recipe.getTotaltime());

        }
        if (prepakt.getText().equals("")) {
            prepakt.setText(null);
        }
        recakt.getPreparation().add(prepakt);

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
//                    picbin.setFormat("JPG");
                    picbin.setFormat(f.getName().substring(f.getName().lastIndexOf(".") + 1));
                    try {
                        picbin.setValue(FileUtils.readFileToByteArray(f));
                    } catch (IOException ex) {
                        Logger.getLogger(McbToCml.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("Fehler beim Schreiben Bilder --Mcb");
                        errorMessage += "Fehler beim Schreiben CML Bild beim Rezept " + recipe.getTitle() + "\n";

                    }
                    headakt.getPicbin().add(picbin);
                    break;
                }
            }
        }

        if (!remakt.getLine().isEmpty()) {
            recakt.getRemark().add(remakt);
        }
        recakt.getPreparation().forEach((p) -> {
            if (p.getText() != null) {
                p.setText(FormatHelper.setText(p.getText()));
            }
        });

        recakt.setHead(headakt);
        cookml.getContent().add(recakt);
    }

    public Cookml getCookml() {
        return cookml;
    }

}
