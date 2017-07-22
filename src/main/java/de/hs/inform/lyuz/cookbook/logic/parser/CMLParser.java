package de.hs.inform.lyuz.cookbook.logic.parser;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class CMLParser {

    private Cookml cookml;
    final String EoL = System.getProperty("line.separator");

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public CMLParser(File f) throws ParserErrorException {
        JAXBContext jc;
        try {
            InputStream inputStream = checkCML(f);
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");

            //ignore DTD check
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(inputStream, FilesUtils.ENCODING);

            Unmarshaller u = jc.createUnmarshaller();
            cookml = (Cookml) u.unmarshal(xsr);
            formatCml();
        } catch (Exception ex) {
            Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserErrorException("Fehler Beim CML Parser", ex.getClass().getName());
        }
    }

    public Cookml getCookml() {
        
        return cookml;
    }

    private void formatCml() {
        cookml.getRecipe().forEach((recakt)-> {
            recakt.getPreparation().forEach((p) -> {
                p.setText(FormatHelper.setText(p.getText()));
            });
        });
    }

    private InputStream checkCML(File file) throws ParserErrorException, UnsupportedEncodingException {
//        String owText = "";
        StringBuilder owText = new StringBuilder();
        int index = 0;
        LineIterator it = null;
        try {
//            String cmlText = FileUtils.readFileToString(file, FilesUtils.ENCODING);
//            Scanner scanner = new Scanner(cmlText);

            it = FileUtils.lineIterator(file, FilesUtils.ENCODING);

            while (it.hasNext()) {
                String line = it.nextLine();
                index++;
                line = checkValue(line, "timeallqty", 0, index);
                line = checkValue(line, "timeprepqty", 0, index);
                line = checkValue(line, "timecookqty", 0, index);
                line = checkValue(line, "quality", 0, index);
                line = checkValue(line, "difficulty", 0, index);
                line = checkValue(line, "qty", 2, index);
                line = checkValue(line, "shop", 1, index);
                line = checkValue(line, "gram", 0, index);
                owText.append(line).append(EoL);
            }
        } catch (IOException ex) {
            Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserErrorException("Fehler beim CML File Read", ex.getClass().getName());
        } finally {
            LineIterator.closeQuietly(it);
        }

        return new ByteArrayInputStream(owText.toString().getBytes(FilesUtils.ENCODING));
    }

    private String checkValue(String line, String name, int typ, int index) {
        String[] strings = line.replace(" ", "").split(name);
        String value = line;
        String start = "=\"";
        if (strings.length == 2 && strings[1].startsWith(start)) {
            String[] values = strings[1].substring(1).split("\"");
            switch (typ) {
                case 0:
                    try {
                        Integer.parseInt(values[1]);
                    } catch (Exception e) {
                        System.err.println("Prüfen CML: Zeile:" + index + ", type: " + name + ", value = " + values[1]);
                        errorMessage += "Prüfen CML: Zeile:" + index + ", type: " + name + ",value = \"" + values[1] + "\"";
                        value = correctLine(line, name);
                    }
                    break;
                case 1:
                    try {
                        Boolean.parseBoolean(values[1]);
                        try {
                            Float.parseFloat(values[1]);
                            System.err.println("Prüfen CML: Zeile:" + index + ", type: " + name + ", value = " + values[1]);
                            errorMessage += "Prüfen CML: Zeile:" + index + ", type: " + name + ", value = \"" + values[1] + "\"";
                            value = correctLine(line, name);
                        } catch (Exception e) {
                            break;
                        }
                    } catch (Exception e) {
                        System.err.println("Prüfen CML: Zeile:" + index + ", type: " + name + ",value = " + values[1]);
                        errorMessage += "Prüfen CML: Zeile:" + index + ", type: " + name + ", value = \"" + values[1] + "\"";
                        value = correctLine(line, name);
                    }
                    break;
                case 2:
                    try {
                        String s = FormatHelper.deNum2En(values[1]);
                        if (s.equals(values[1])) {
                            Float.parseFloat(FormatHelper.deNum2En(values[1]));
                        } else {
                            Float.parseFloat(s);
                            value = line.replace(values[1], s);
                        }
                    } catch (Exception e) {
                        System.err.println("Prüfen CML: Zeile:" + index + ", type: " + name + ",value = " + values[1]);
                        errorMessage += "Prüfen CML: Zeile:" + index + ", type: " + name + ", value = \"" + values[1] + "\"";
                        value = correctLine(line, name);
                    }
                    break;
            }
        }
        return value;
    }

    private String correctLine(String line, String name) {
        String[] texts = line.split(name);
        int i = 0;
        while (texts[1].charAt(i) == ' ') {
            i++;
        }
        if (texts[1].charAt(i) == '=') {
            i++;
            while (texts[1].charAt(i) == ' ') {
                i++;
            }
            if (texts[1].charAt(i) == '"') {
                i++;
                while (texts[1].charAt(i) != '"') {
                    i++;
                }
                return texts[0] + texts[1].substring(++i);
            }
        }
        return line;
    }
}
