package de.hs.inform.lyuz.cookbook.utils;

import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ConfUtils {

    public static List<String> getCatTemplate() throws IOException, JDOMException {
        Document document = readConfXML(File.separator + "conf" + File.separator + "category.xml",
                FilesUtils.CATEGORY_XML);
        ArrayList<String> catList = new ArrayList<>();
        document.getRootElement().getChildren().forEach((e) -> {
            catList.add(e.getText());
        });
        return catList;
    }

    public static void updateCatTemplate(List<String> catList) throws IOException {
        Document document = new Document();
        Element root = new Element("category");
        catList.stream().map((cat) -> {
            Element e = new Element("name");
            e.setText(cat);
            return e;
        }).forEachOrdered((e) -> {
            root.addContent(e);
        });
        document.setRootElement(root);
        writeConfXML(document, File.separator + "conf" + File.separator + "category.xml");
    }

    public static ExportInfo getExportInfo() throws IOException, JDOMException {
        ExportInfo exportInfo = new ExportInfo();
        Document document = readConfXML(File.separator + "conf" + File.separator + "properties.xml",
                FilesUtils.PROPERTIES_XML);
        document.getRootElement().getChildren().forEach((e) -> {
            switch (e.getName()) {
                case "firstname":
                    exportInfo.setFirstName(e.getText());
                    break;
                case "lastname":
                    exportInfo.setLastName(e.getText());
                    break;
                case "title":
                    exportInfo.setTitle(e.getText());
                    break;
                case "bookname":
                    exportInfo.setBookname(e.getText());
                    break;
                case "type":
                    exportInfo.setExportType(e.getText());
                    break;
                case "filepath":
                    String path = System.getProperty("user.dir");
                    exportInfo.setFilepath(path + File.separator);
                    break;
                case "pic":
                    exportInfo.setHasPic(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "color":
                    exportInfo.setIsColor(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "cat":
                    exportInfo.setHasCat(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "source":
                    exportInfo.setHasSource(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "remark":
                    exportInfo.setHasRemark(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "time":
                    exportInfo.setHasTime(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "difficulty":
                    exportInfo.setHasDiffculty(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "cover":
                    exportInfo.setHasCover(Boolean.valueOf(e.getAttributeValue("value")));
                    break;
                case "coverpath":
                    exportInfo.setCoverPath(e.getText());
                    break;
            }
        });
        return exportInfo;
    }

    public static void updateExportInfo(ExportInfo exportInfo) throws IOException {
        Document document = new Document();
        Element root = new Element("export");

        Element firstname = new Element("firstname");
        firstname.setText(exportInfo.getFirstName());
        root.addContent(firstname);

        Element lastname = new Element("lastname");
        lastname.setText(exportInfo.getLastName());
        root.addContent(lastname);

        Element title = new Element("title");
        title.setText(exportInfo.getTitle());
        root.addContent(title);

        Element bookname = new Element("bookname");
        bookname.setText(exportInfo.getBookname());
        root.addContent(bookname);

        Element type = new Element("type");
        type.setText(exportInfo.getExportType());
        root.addContent(type);

        Element filepath = new Element("filepath");
        filepath.setText(exportInfo.getFilepath());
        root.addContent(filepath);

        Element pic = new Element("pic");
        pic.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasPic())));
        root.addContent(pic);

        Element color = new Element("color");
        color.setAttribute(new Attribute("value", String.valueOf(exportInfo.isIsColor())));
        root.addContent(color);

        Element cat = new Element("cat");
        cat.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasCat())));
        root.addContent(cat);

        Element source = new Element("source");
        source.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasSource())));
        root.addContent(source);

        Element remark = new Element("remark");
        remark.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasRemark())));
        root.addContent(remark);

        Element time = new Element("time");
        time.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasTime())));
        root.addContent(time);

        Element difficulty = new Element("difficulty");
        difficulty.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasDiffculty())));
        root.addContent(difficulty);

        Element cover = new Element("cover");
        cover.setAttribute(new Attribute("value", String.valueOf(exportInfo.isHasCover())));
        root.addContent(cover);

        Element coverpath = new Element("coverpath");
        coverpath.setText(exportInfo.getCoverPath());
        root.addContent(coverpath);

        document.setRootElement(root);
        writeConfXML(document, File.separator + "conf" + File.separator + "properties.xml");
    }

    private static void writeConfXML(Document document, String path1) throws IOException {
        String path = System.getProperty("user.dir");
        File file = new File(path + path1);
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        FilesUtils.writeDOMXML(document, new FileOutputStream(file));
    }

    private static Document readConfXML(String path1, String path2) throws IOException, JDOMException {
        SAXBuilder sax = new SAXBuilder();
        String path = System.getProperty("user.dir");
        File file = new File(path + path1);

        Document document = null;

        if (file.exists()) {
            document = sax.build(file);
        } else {
            InputStream in = ConfUtils.class.getClassLoader().getResourceAsStream(path2);
            document = sax.build(in);
        }
        return document;
    }

}
