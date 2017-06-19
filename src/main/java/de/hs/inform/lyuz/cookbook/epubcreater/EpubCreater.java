package de.hs.inform.lyuz.cookbook.epubcreater;

import de.hs.inform.lyuz.cookbook.controller.convert.CmlToEpub;
import de.hs.inform.lyuz.cookbook.help.Utils;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import de.hs.inform.lyuz.cookbook.help.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.epub.EpubObjekt;
import java.util.LinkedHashMap;

public class EpubCreater {

    private String filepath;

    public EpubCreater(String filepath) {
        this.filepath = filepath;
        creatEpubFiles(0);
        copyEpubFiles();

    }

    private void creatEpubFiles(int i) {
        File file;
        if (i == 0) {
            file = new File(filepath + "EPUB");
        } else {
            file = new File(filepath + "EPUB(" + i + ")");
        }

        if (file.exists() || file.isDirectory()) {
            creatEpubFiles(++i);
        } else {
            file.mkdir();
            filepath = file.getAbsolutePath() + File.separator;
            creatEpubIndex();
        }
    }

    private void creatEpubIndex() {
        new File(filepath + "EPUB").mkdir();
        new File(filepath + "EPUB" + File.separator + "images").mkdir();
        new File(filepath + "EPUB" + File.separator + "css").mkdir();
        new File(filepath + "EPUB" + File.separator + "icons").mkdir();
        new File(filepath + "META-INF").mkdir();
    }

    private void copyEpubFiles() {
        Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.CONTAINER_XML), filepath + "META-INF" + File.separator + "container.xml");
        Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.STAR_PNG), filepath + "EPUB" + File.separator + "icons" + File.separator + "star.png");
        Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.STAR_BOARD_PNG), filepath + "EPUB" + File.separator + "icons" + File.separator + "star_board.png");
        Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.EPUB_SPEC_CSS), filepath + "EPUB" + File.separator + "css" + File.separator + "epub-spec.css");
        Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.MIMETYPE), filepath + "mimetype");
    }

    public Book epub2(LinkedHashMap<String, Cookml> sortCmlMap, ArrayList<String> catList) {

        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");

            JDOMResult out;
            Transformer tf;

            XMLOutputter o = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setIndent("\t");
            o.setFormat(format);

            CmlToEpub cml2Epub = new CmlToEpub(sortCmlMap, filepath, catList);
            EpubObjekt epub2 = cml2Epub.getEpub();

            Book book = new Book();
            // epub book
            book.getMetadata().addTitle(ExportInfo.title);
            book.getMetadata().addAuthor(new Author(ExportInfo.firstName, ExportInfo.lastName));
            book.getResources().add(new Resource(Utils.file2Byte(new File(filepath + "EPUB" + File.separator + "css" + File.separator + "epub-spec.css")),
                    "css" + File.separator + "epub-spec.css"));

            //cover
            if (ExportInfo.hasCover && ExportInfo.coverPath != null && !ExportInfo.coverPath.equals("")) {
                book.setCoverImage(new Resource(Utils.file2Byte(new File(filepath + "EPUB" + File.separator + "images" + File.separator + "cover.jpg")),
                        "images" + File.separator + "cover.jpg"));
            }

            //toc.html
            if (ExportInfo.hasCat) {
                out = new JDOMResult();
//                tf = TransformerFactory.newInstance().newTransformer(
//                        new StreamSource(Utils.CALALOGEPUB_XSL));
                tf = TransformerFactory.newInstance().newTransformer(new StreamSource(
                        EpubCreater.class.getClassLoader().getResourceAsStream(Utils.CALALOGEPUB_XSL)
                ));

                tf.transform(new JDOMSource(epub2.getNavDom()), out);
                o.output(out.getDocument(), new FileWriter(filepath + "EPUB" + File.separator + "toc.html"));
                book.addSection("Inhaltsverzeichnis", new Resource(Utils.file2Byte(new File(filepath + "EPUB" + File.separator + "toc.html")), "toc.html"));

            }

            // main content xhtml
            tf = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.COOKML_XSL)));
            for (String cat : epub2.getCookmls().keySet()) {
                JAXBSource source = new JAXBSource(jc, epub2.getCookmls().get(cat));
                String fname = filepath + "EPUB" + File.separator + cat + ".html";
                Result result = new StreamResult(new File(fname));
                tf.transform(source, result);
                book.addSection(cat, new Resource(Utils.file2Byte(new File(fname)), cat + ".html"));
            }

            // index
            if (ExportInfo.hasIndex) {
                out = new JDOMResult();
                tf = TransformerFactory.newInstance().newTransformer(
                        new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.INDEX_XSL)));
                tf.transform(new JDOMSource(epub2.getIndexDom()), out);
                o.output(out.getDocument(), new FileWriter(filepath + "EPUB" + File.separator + "index.html"));
                book.addSection("Index", new Resource(Utils.file2Byte(new File(filepath + "EPUB" + File.separator + "index.html")), "index.html"));
            }

            // pic resources
            if (ExportInfo.hasPic) {
                epub2.getPicList().forEach((pic) -> {
                    String[] picname = pic.split(File.separator);
                    book.getResources().add(new Resource(Utils.file2Byte(new File(pic)), "images" + File.separator + picname[picname.length - 1]));
                });
            }

            //output epub2
            EpubWriter epubWriter = new EpubWriter();
            epubWriter.write(book, new FileOutputStream(ExportInfo.filepath + ExportInfo.bookname + ".epub"));

            return book;
//            Utils.deleteFile(new File(filepath + "EPUB"));
        } catch (IOException | JAXBException | TransformerException ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String epub3(LinkedHashMap<String, Cookml> sortCmlMap, ArrayList<String> catList) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");

            Transformer tf;
            JDOMResult out;

            // xml format
            XMLOutputter o = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setEncoding("UTF-8");
            format.setIndent("\t");
            o.setFormat(format);

            // reformat to epub
            CmlToEpub cml2Epub = new CmlToEpub(sortCmlMap, filepath, catList);
            EpubObjekt epub = cml2Epub.getEpub();
            // main content xhtml
            tf = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.COOKML_XSL)));
            for (String cat : epub.getCookmls().keySet()) {
                JAXBSource source = new JAXBSource(jc, epub.getCookmls().get(cat));
                Result result = new StreamResult(new File(filepath + "EPUB" + File.separator + cat + ".xhtml"));
                tf.transform(source, result);
            }

            if (ExportInfo.hasCover) {
                Utils.writeRessource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.COVER_XHTML), filepath + "EPUB" + File.separator + "cover.xhtml");
            }

            // toc nav
            if (ExportInfo.hasCat) {
                out = new JDOMResult();
                tf = TransformerFactory.newInstance().newTransformer(
                        new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.CALALOGEPUB_XSL)));
                tf.transform(new JDOMSource(epub.getNavDom()), out);
                o.output(out.getDocument(), new FileWriter(filepath + "EPUB" + File.separator + "toc.xhtml"));
            }

            // index
            if (ExportInfo.hasIndex) {
                out = new JDOMResult();
                tf = TransformerFactory.newInstance().newTransformer(
                        new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(Utils.INDEX_XSL)));
                tf.transform(new JDOMSource(epub.getIndexDom()), out);
                o.output(out.getDocument(), new FileWriter(filepath + "EPUB" + File.separator + "index.xhtml"));
            }

            // opf
            o.output(epub.getOpfDom(), new FileWriter(filepath + "EPUB" + File.separator + "package.opf"));

        } catch (IOException | JAXBException | TransformerException ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filepath;
    }
}
