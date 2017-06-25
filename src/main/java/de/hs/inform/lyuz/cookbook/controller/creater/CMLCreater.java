package de.hs.inform.lyuz.cookbook.controller.creater;

import de.hs.inform.lyuz.cookbook.controller.convert.BsToCml;
import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import de.hs.inform.lyuz.cookbook.model.cookml.Remark;
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

    private int serialID = new Random().nextInt();

    public CMLCreater(MyBook myBook) {
        this.myBook = myBook;
        this.cookml = formatWithExportInfo(myBook.getCookml());
    }

    private Cookml formatWithExportInfo(Cookml cookml) {
        ExportInfo exportInfo = myBook.getExportInfo();
        cookml.setName(exportInfo.getTitle());
        cookml.setVersion("1.1.2");
        cookml.setProg("CgoesE");
        cookml.setProgver("1.0.0");

        for (Recipe recipe : cookml.getRecipe()) {
            recipe.setLang("DE");

            for (Object objakt : recipe.getHeadAndCustomAndPart()) {
                switch (objakt.getClass().getCanonicalName()) {
                    case "de.hs.inform.lyuz.cookbook.model.cookml.Head":
                        Head headakt = (Head) objakt;
                        headakt.setChangeuser(exportInfo.getFirstName() + " " + exportInfo.getLastName());
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
                            System.err.println("Fehler beim Konvertierung auf BS-Datum");
                        }

                        if (headakt.getServingqty() == null || headakt.getServingqty().trim().equals("")) {
                            headakt.setServingqty("1");
                        }
                        if (headakt.getServingqty() == null || headakt.getServingtype().trim().equals("")) {
                            headakt.setServingtype("Protion");
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
                        if (!exportInfo.isHasDiffculty()) {
                            headakt.setWwpoints(null);
                        }
                        if (!exportInfo.isHasSource()) {
                            headakt.setSourceline(null);
                        }

                        break;

                    case "de.hs.inform.lyuz.cookbook.model.cookml.Remark":
                        Remark reamakt = (Remark) objakt;

                        if (!exportInfo.isHasRemark()) {
                            reamakt.setLine(null);
                        }
                        break;
                    default:
                        break;

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
            throw new ConvertErrorException("Fehler beim Export CML");
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
