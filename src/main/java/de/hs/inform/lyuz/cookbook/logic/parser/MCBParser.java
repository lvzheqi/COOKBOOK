package de.hs.inform.lyuz.cookbook.logic.parser;

import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import de.hs.inform.lyuz.cookbook.model.mycookbook.Cookbook;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.io.FileUtils;



public class MCBParser {

    private Cookbook mcb = null;
    private File[] files = new File[0];
    private File xml = null;
//    private File xml = new File("test.xml");

    private String path = "";

    public MCBParser(File zipFile) throws ParserErrorException {

        try {
            path = System.getProperty("user.dir") + File.separator;
            creatMcbFiles(0);
            uncompress(zipFile, path);
        } catch (Exception ex) {
            try {
                FileUtils.deleteDirectory(new File(path));
            } catch (IOException ex1) {
                Logger.getLogger(MCBParser.class.getName()).log(Level.SEVERE, null, ex1);
                System.err.println("Fehler beim Löschen Dateien");
            }
            Logger.getLogger(MCBParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserErrorException("Fehler beim MCB Parser",ex.getClass().getName());
        }

        if (xml != null) {
            JAXBContext jc;
            try {
                jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.mycookbook");

                //ignore DTD check
                XMLInputFactory xif = XMLInputFactory.newFactory();
                xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
                XMLStreamReader xsr = xif.createXMLStreamReader(new FileInputStream(xml));

                Unmarshaller u = jc.createUnmarshaller();
                mcb = (Cookbook) u.unmarshal(xsr);
            } catch (FileNotFoundException | IllegalArgumentException | JAXBException | XMLStreamException ex) {
                Logger.getLogger(MCBParser.class.getName()).log(Level.SEVERE, null, ex);
                throw new ParserErrorException("Fehler beim MCB Parser", ex.getClass().getName());
            }
        } else {
            throw new ParserErrorException("Fehler beim MCB Parser", "keine XML");
        }
    }

    private void creatMcbFiles(int i) throws IOException {
        File file;
        if (i == 0) {
            file = new File(path + "MCB");
        } else {
            file = new File(path + "MCB(" + i + ")");
        }

        if (file.exists() || file.isDirectory()) {
            creatMcbFiles(++i);
        } else {
            file.mkdir();
            path = file.getAbsolutePath() + File.separator;
            file = new File(path + "images");
            file.mkdir();
        }
    }

    private void uncompress(File zipFile, String descDir) throws Exception {

        FilesUtils.uncompress(zipFile, descDir);
        readAllFile(descDir);
    }

    private void readAllFile(String descDir) {
        files = (new File(descDir)).listFiles();
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
    
    public String getFilePath() {
        return path;
    }
}
