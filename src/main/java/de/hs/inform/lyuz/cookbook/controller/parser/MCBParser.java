package de.hs.inform.lyuz.cookbook.controller.parser;

import de.hs.inform.lyuz.cookbook.help.Utils;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;

public class MCBParser {

    private Cookbook mcb = null;
    private File[] files = null;
    private File xml = null;
    // TODO:路径
    private String path = "/Users/xuer/test/";
    

    public MCBParser(File f) {
        init(f);

        // TODO: throw
        if (xml != null) {
            JAXBContext jc;
            try {
                jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.mycookbook");
                Unmarshaller u = jc.createUnmarshaller();
                mcb = (Cookbook) u.unmarshal(xml);
            } catch (JAXBException ex) {
                Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Kein Datei gefundens");
        }
    }

    private void init(File f) {

        Utils.uncompress(f, path);
        readAllFile(path);
    }

    public void readAllFile(String path) {
        files = (new File(path)).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                readAllFile(file.getAbsolutePath());
            } else {
                if (file.getName().endsWith("xml")) {
                    xml = file;
                }
            }
        }
    }

    public File[] getFiles() {
        return files;
    }

    public Cookbook getMcb() {
        return mcb;
    }
}
