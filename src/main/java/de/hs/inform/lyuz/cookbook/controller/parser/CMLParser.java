package de.hs.inform.lyuz.cookbook.controller.parser;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;

public class CMLParser {

    private Cookml cookml;

    public CMLParser(File f) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");
            Unmarshaller u = jc.createUnmarshaller();
            cookml = (Cookml) u.unmarshal(f);
        } catch (JAXBException ex) {
            Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Cookml getCookml() {
        return cookml;
    }
    
}
