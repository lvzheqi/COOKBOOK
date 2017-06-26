package de.hs.inform.lyuz.cookbook.controller.parser;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.exception.ParserErrorExcepetion;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.io.FileUtils;

public class CMLParser {

    private Cookml cookml;

    public CMLParser(File f) throws ParserErrorExcepetion {
        JAXBContext jc;
        try {
            InputStream inputStream = checkCML(f);
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");

            //ignore DTD check
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(inputStream,"UTF-8");

            Unmarshaller u = jc.createUnmarshaller();
            cookml = (Cookml) u.unmarshal(xsr);
        } catch (Exception ex) {
            Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserErrorExcepetion("Fehler Beim CML Parser");
        }
    }

    public Cookml getCookml() {
        return cookml;
    }

    private InputStream checkCML(File file) throws ParserErrorExcepetion, UnsupportedEncodingException {
        String owText = "";
        try {
            String cmlText = FileUtils.readFileToString(file, "UTF-8");
            Scanner scanner = new Scanner(cmlText);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                line = checkValue(line, "timeallqty", 0);
                line = checkValue(line, "timeprepqty", 0);
                line = checkValue(line, "timecookqty", 0);
                line = checkValue(line, "wwwpoints", 2);
                line = checkValue(line, "qty", 2);
                line = checkValue(line, "shop", 1);
                line = checkValue(line, "gram", 0);

                owText += line;
            }
        } catch (IOException ex) {
            Logger.getLogger(CMLParser.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParserErrorExcepetion("Fehler beim CML File Read");
        }

        return new ByteArrayInputStream(owText.getBytes("UTF-8"));
    }

    private String checkValue(String line, String name, int typ) {
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
                        System.err.println("Prüfen CML: type: " + name + ",value = " + values[1]);
                        value = correctLine(line, name);
                    }
                    break;
                case 1:
                    try {
                        Boolean.parseBoolean(values[1]);
                        try {
                            Float.parseFloat(values[1]);
                            System.err.println("Prüfen CML: type: " + name + ",value = " + values[1]);
                            value = correctLine(line, name);
                        } catch (Exception e) {
                            break;
                        }
                    } catch (Exception e) {
                        System.err.println("Prüfen CML: type: " + name + ",value = " + values[1]);
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
                        System.err.println("Prüfen CML: type: " + name + ",value = " + values[1]);
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
