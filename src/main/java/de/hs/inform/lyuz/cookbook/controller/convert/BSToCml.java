package de.hs.inform.lyuz.cookbook.controller.convert;


import de.hs.inform.lyuz.cookbook.help.FormatHelper;
import de.hs.inform.lyuz.cookbook.help.Utils;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe.Part;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class BSToCml {

    private final Cookml cookml;
    private Recipe recakt;
    private Head headakt;
    private Scanner scanner;


    public BSToCml(File f) {
        String text = Utils.file2Text(f);

        cookml = new Cookml();
        cookml.setName(f.getName());
        cookml.setProg("Bs2Cook");
        cookml.setVersion("1.0.2");
        cookml.setProgver("0.91");
        
        
        for (String rez : text.split("#T1 ")) {
            setRecipe(rez);
        }
    }

    private void setRecipe(String rez_akttext) {

        scanner = new Scanner(rez_akttext);
        String remark = "";

        if (scanner.hasNextLine()) {

            recakt = new Recipe();
//    int serialID = new Random().nextInt();
//            serialID++;
//            int generatorID = 0;
//            for (int i = 0; i < createuser.length(); i++) {
//                generatorID += createuser.charAt(i) * (createuser.length() - i);
//            }
//            int date = 12 << 21; // at 12:00
//            generatorID = (generatorID % 4048) | (serialID << 12);
//            date = date | (year - 1980) | (month << 11) | (day << 13);
//            headakt.setRid(String.valueOf(date) + "," + String.valueOf(generatorID));
            //head
            recakt.setLang("DE");
            headakt = new Head();
            headakt.setTitle(scanner.nextLine());

            String line = scanner.nextLine();
            while (scanner.hasNextLine()){
                
                if (line.replace(" ", "").equals("")) {
                    continue;
                }
                if(!line.startsWith("#", 0)) {
                    line = setPart(line);
                    continue;
                }
                if(line.substring(0, 2).equals("#B")) {
                    line = setPreparation();
                    continue;
                }
                switch (line.substring(0, 3)) {
                    case "#SC":
                        line = setPart(line);
                        break;
                    default:
                        setBS(line, remark);
                        line = scanner.nextLine();
                        break;
                } 
            }

            
            if (!remark.equals("")) {
                Remark remakt = new Remark();
                remakt.getLine().add(remark);
                recakt.getHeadAndCustomAndPart().add(remakt);
            }
            recakt.getHeadAndCustomAndPart().add(headakt);

            cookml.getRecipe().add(recakt);
        }
    }
    

    private void setBS(String line, String remark) {
        switch (line.substring(0, 3).trim()) {
            case "#RU":
                headakt.getCat().add(line.substring(4));
                break;
            case "#AT":
                String createuser = line.substring(4);
                headakt.setCreateuser(createuser);
                headakt.setChangeuser(createuser);
                break;
            case "#MA":
                headakt.setServingqty(line.substring(4));
                break;
            case "#MB":
                headakt.setServingtype(line.substring(4));
                break;
            case "#D":
            case "#AD":
                XMLGregorianCalendar d = null;
                try {
                    d = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                } catch (DatatypeConfigurationException ex) {
                    Logger.getLogger(BSToCml.class.getName()).log(Level.SEVERE, null, ex);
                }
                int year = Integer.parseInt(line.substring(10, 14));
                if (year > 2090) {
                    year = 1998;
                }
                int month = Integer.parseInt(line.substring(7, 9));
                int day = Integer.parseInt(line.substring(4, 6));
                d.setYear(year);
                d.setMonth(month);
                d.setDay(day);
                if (line.startsWith("#D ", 0)) {
                    headakt.setCreatedate(d);
                }
                headakt.setChangedate(d);
                break;
            case "#IM":
                // TODO: filepath;
                Picbin picbin = new Picbin();
                picbin.setFormat("JPG");
                picbin.setValue(Utils.file2Byte(new File(line.substring(4))));
                headakt.getPicbin().add(picbin);
                break;
            case "#NI":
                break;
            case "#NO":
                if (line.substring(4, 10).equals("Quelle: ")) {
                    headakt.getSourceline().add(line.substring(11));
                }
                headakt.getSourceline().add(line.substring(4));
                break;
            case "#ST":
                headakt.getHint().add(line.substring(4));
                break;
            default:
                setRemark(line, remark);
                break;
        }
    }

    private void setRemark(String line,String remark) {

        if (!remark.equals("")) {
            remark += ", ";
        }
        switch (line.substring(0, 3)) {
            case "#EW":
                remark += "Eiweiß: " + line.substring(4) + " g";
                break;
            case "#FT":
                remark += "Fett: " + line.substring(4) + " g";
                break;
            case "#KH":
                remark += "Kohlenhydrate: " + line.substring(4) + " g";
                break;
            case "#KJ":
                remark += "kJ: " + line.substring(4);
                break;
            case "#KC": {
                remark += "kcal: " + line.substring(4);
                Head.Content contakt = new Head.Content();
                contakt.setType("GCAL");
                contakt.setValue(line.substring(4));
                headakt.getContent().add(contakt);
                break;
            }
            case "#BE": {
                remark += "BE: " + line.substring(4);
                Head.Content contakt = new Head.Content();
                contakt.setType("GKB");
                contakt.setValue(line.substring(4));
                headakt.getContent().add(contakt);
                break;
            }
            default:
                remark += line.substring(1, 3) + ": " + line.substring(4);
                break;
        }
    }

    private String setPart(String lineTitle) {
        Part partakt = new Part();
        
        if (lineTitle.startsWith("#", 0)) {
            if(lineTitle.length()>5)
                partakt.setTitle(lineTitle.substring(4));
        }
        
        String line = null;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0)) {
            partakt.getIngredient().add(FormatHelper.formatIngredient(line.split(" ")));
        }
        recakt.getHeadAndCustomAndPart().add(partakt);
        return line;
    }

    private String setPreparation() {

        Preparation prepakt = new Preparation();
        String line = null;
        String rectext = "";
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("#", 0)) {
            rectext += line + "\n";
        }
        prepakt.getText().add(rectext);
        recakt.getHeadAndCustomAndPart().add(prepakt);
        return line;
    }
    
    public Cookml getCookml() {
        return this.cookml;
    }
}
