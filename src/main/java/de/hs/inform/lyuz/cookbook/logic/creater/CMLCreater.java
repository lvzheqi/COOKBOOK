package de.hs.inform.lyuz.cookbook.logic.creater;

import de.hs.inform.lyuz.cookbook.logic.convert.BsToCml;
import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import java.io.File;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class CMLCreater {

    private MyBook myBook;
    private Cookml cookml;
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    private int serialID = new Random().nextInt();

    public CMLCreater(MyBook myBook) {
        this.myBook = myBook;
        cookml = formatWithExportInfo(myBook.getCookml());
    }

    private Cookml formatWithExportInfo(Cookml cookml) {
        ExportInfo exportInfo = myBook.getExportInfo();
        cookml.setName(exportInfo.getTitle());
        cookml.setVersion("1.1.1");
        cookml.setProg("CgoesE");
        cookml.setProgver("1.1.1");

//        for (Recipe recipe : cookml.getRecipe()) {
        for (Object object : cookml.getContent()) {
            if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                Recipe recipe = (Recipe) object;
                recipe.setLang("DE");

                Head headakt = (Head) recipe.getHead();
                headakt.setRid(setRidID());

                try {
                    XMLGregorianCalendar d = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                    Calendar c = Calendar.getInstance();
                    d.setYear(c.get(Calendar.YEAR));
                    d.setMonth(c.get(Calendar.MONTH));
                    d.setDay(c.get(Calendar.DATE));
                    headakt.setChangedate(d);
                } catch (DatatypeConfigurationException ex) {
                    Logger.getLogger(BsToCml.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Zugrifffehler auf Datum beim CML Erstellen");
                    errorMessage += "Zugrifffehler auf Datum beim CML Erstellen\n";

                }

                if (headakt.getServingqty() == null) {
                    headakt.setServingqty("1");
                }
                if (headakt.getServingtype() == null || headakt.getServingtype().trim().equals("")) {
                    headakt.setServingtype("Portion");
                }

                if (!exportInfo.isHasTime()) {
                    headakt.setTimeallqty(null);
                    headakt.setTimecookqty(null);
                    headakt.setTimeprepqty(null);
                }
                if (!exportInfo.isHasPic()) {
                    headakt.setPicbin(null);
                    headakt.setPicture(null);
                }
                if (!exportInfo.isHasQuality()) {
                    headakt.setQuality(null);
                }
                if (!exportInfo.isHasSource()) {
                    headakt.setSourceline(null);
                }

                if (!exportInfo.isHasRemark()) {
                    recipe.setRemark(null);
//                reamakt.setLine(null);

                }

            }
        }
        return cookml;
    }

    public void write() throws ConvertErrorException {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "cookml.xsd");
            m.marshal(cookml, new File(myBook.getExportInfo().getFilepath() + myBook.getExportInfo().getBookname() + ".cml"));

        } catch (Exception ex) {
            Logger.getLogger(CMLCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export CML", ex.getClass().getName());
        }

    }

    private String setRidID() {

        Calendar c = Calendar.getInstance();
        int generatorID = 0;
        String createuser = myBook.getExportInfo().getFirstName() + " "
                + myBook.getExportInfo().getLastName();
        for (int i = 0; i < createuser.length(); i++) {
            generatorID += createuser.charAt(i) * (createuser.length() - i);
        }
        generatorID = (generatorID % 4048) | (serialID++ << 12);
        int date = c.get(Calendar.MINUTE) << 24 | c.get(Calendar.HOUR_OF_DAY) << 19
                | (c.get(Calendar.DATE) << 15) | (c.get(Calendar.MONTH) << 11)
                | (c.get(Calendar.YEAR) - 1980);
        return date + "," + generatorID;
    }

}
