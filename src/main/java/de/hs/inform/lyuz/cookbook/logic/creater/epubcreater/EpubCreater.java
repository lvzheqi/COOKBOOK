package de.hs.inform.lyuz.cookbook.logic.creater.epubcreater;

import de.hs.inform.lyuz.cookbook.logic.convert.CmlToEpubObject;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.epub.EpubObjekt;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.SystemErrorException;
import de.hs.inform.lyuz.cookbook.utils.XalanHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class EpubCreater {

    //system relevant
    private MyBook myBook;
    private String filepath = System.getProperty("user.dir");

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFilepath() {
        return filepath;
    }

    public EpubCreater(MyBook myBook) throws SystemErrorException {
        this.myBook = myBook;
        this.filepath = myBook.getExportInfo().getFilepath();
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

    //create file
    private void creatEpubIndex() {
        new File(filepath + "EPUB").mkdir();
        new File(filepath + "EPUB" + File.separator + "images").mkdir();
        new File(filepath + "EPUB" + File.separator + "css").mkdir();
        new File(filepath + "EPUB" + File.separator + "icons").mkdir();
        new File(filepath + "META-INF").mkdir();
    }

    // file copy
    private void copyEpubFiles() throws SystemErrorException {

        try {
            FileUtils.copyToFile(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.CONTAINER_XML), new File(filepath + "META-INF" + File.separator + "container.xml"));
            FileUtils.copyToFile(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.MIMETYPE), new File(filepath + "mimetype"));
        } catch (Exception ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information", ex.getClass().getName());
        }
        try {
            FileUtils.copyToFile(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.EPUB_SPEC_CSS), new File(filepath + "EPUB" + File.separator + "css" + File.separator + "epub-spec.css"));
            if (myBook.getExportInfo().isIsColor()) {
                FileUtils.copyToFile(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_BOARD_PNG), new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star_board.png"));
                FileUtils.copyToFile(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_PNG), new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star.png"));

            } else {
                FilesUtils.changeImgeColor2BW(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_BOARD_PNG), new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star_board.png"), "png");
                FilesUtils.changeImgeColor2BW(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_PNG), new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star.png"), "png");

            }
        } catch (Exception ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Laden Config.sys Information -- EPUB");
            errorMessage += "Fehler beim Laden Config.sys Information\n";

        }
    }

    public Book epub2() throws ConvertErrorException {

        CmlToEpubObject cml2Epub = new CmlToEpubObject(myBook, filepath);
        EpubObjekt epub2 = cml2Epub.getEpub();

        errorMessage += cml2Epub.getErrorMessage();
        Book book = new Book();
        // epub book
        book.getMetadata().addTitle(myBook.getExportInfo().getTitle());
        book.getMetadata().addAuthor(new Author(myBook.getExportInfo().getFirstName(), myBook.getExportInfo().getLastName()));
        try {
            book.getResources().add(new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "css" + File.separator + "epub-spec.css")),
                    "css/epub-spec.css"));
            book.getResources().add(new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star.png")),
                    "icons/star.png"));
            book.getResources().add(new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "icons" + File.separator + "star_board.png")),
                    "icons/star_board.png"));
        } catch (Exception ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim css Config.sys Information -- EPUB2");
            errorMessage += "Fehler beim Laden Config.sys Information\n";

        }

        //cover
        if (myBook.getExportInfo().isHasCover() && myBook.getExportInfo().getCoverPath() != null && !myBook.getExportInfo().getCoverPath().equals("")) {
            try {
                FileUtils.copyToFile(EpubCreater.class.getClassLoader()
                        .getResourceAsStream(FilesUtils.COVER_HTML), new File(filepath + "EPUB" + File.separator + "cover.html"));
                book.addSection("cover", new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "cover.html")), "cover.html"));

            } catch (Exception ex) {
                Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Schreiben COVER.html -- EPUB2");
                errorMessage += "Fehler beim Schreiben COVERxhtml\n";

            }

            try {
                book.setCoverImage(new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "images" + File.separator + "cover.jpg")),
                        "images/cover.jpg"));
            } catch (Exception ex) {
                Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Cover-Schreiben -- EPUB2");
                errorMessage += "Fehler beim Cover-Schreiben\n";

            }
        }

        // pic resources
        if (myBook.getExportInfo().isHasPic()) {
            for (String pic : epub2.getPicList()) {
                String picname = pic.substring(pic.lastIndexOf(File.separator) + 1);
//                String[] picname = pic.split(File.separator);
                try {
                    book.getResources().add(new Resource(FileUtils.readFileToByteArray(new File(pic)), "images/" + picname));
                } catch (Exception ex) {
                    Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Fehler beim Bild Hochladen -- EPUB2");
                    errorMessage += "Fehler beim Bild Hochladen\n";
                }
            }
        }

        try {
            //toc.html
            if (myBook.getExportInfo().isHasCat()) {
                InputStream inputStream = EpubCreater.class
                        .getClassLoader().getResourceAsStream(FilesUtils.CALALOGEPUB_XSL);

                FilesUtils.writeDOMHTML(inputStream, epub2.getNavDom(), filepath + "EPUB" + File.separator + "toc.html");
                book.addSection("Inhaltsverzeichnis", new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "toc.html")), "toc.html"));
            }

            JAXBContext jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");
            Transformer tf = TransformerFactory.newInstance().newTransformer(new StreamSource(EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.COOKML_XSL)));
            for (String cat : epub2.getCookmls().keySet()) {
                JAXBSource source = new JAXBSource(jc, epub2.getCookmls().get(cat));
                String fname = filepath + "EPUB" + File.separator + cat + ".html";
                XalanHelper.setCategory(cat);
                Result result = new StreamResult(new File(fname));
                tf.transform(source, result);
                book.addSection(cat, new Resource(FileUtils.readFileToByteArray(new File(fname)), cat + ".html"));
            }

            // index
            if (myBook.getExportInfo().isHasIndex()) {
                InputStream inputStream = EpubCreater.class.getClassLoader().getResourceAsStream(FilesUtils.INDEX_XSL);
                FilesUtils.writeDOMHTML(inputStream, epub2.getIndexDom(), filepath + "EPUB" + File.separator + "index.html");
                book.addSection("Index", new Resource(FileUtils.readFileToByteArray(new File(filepath + "EPUB" + File.separator + "index.html")), "index.html"));
            }
        } catch (Exception ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export HTML --EPUB2", ex.getClass().getName());
        }

        return book;
    }

    public void epub3() throws ConvertErrorException {

        CmlToEpubObject cml2Epub = new CmlToEpubObject(myBook, filepath);
        EpubObjekt epub = cml2Epub.getEpub();
        errorMessage += cml2Epub.getErrorMessage();

        if (myBook.getExportInfo().isHasCover()) {
            try {
                FileUtils.copyToFile(EpubCreater.class.getClassLoader()
                        .getResourceAsStream(FilesUtils.COVER_XHTML), new File(filepath + "EPUB" + File.separator + "cover.xhtml"));
            } catch (Exception ex) {
                Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Schreiben COVER.xhtml -- EPUB3");
                errorMessage += "Fehler beim Schreiben COVER.xhtml\n";

            }
        }
        try {
            // toc nav
            if (myBook.getExportInfo().isHasCat()) {
                InputStream inputStream = EpubCreater.class
                        .getClassLoader().getResourceAsStream(FilesUtils.CALALOGEPUB_XSL);
                FilesUtils.writeDOMHTML(inputStream, epub.getNavDom(), filepath + "EPUB" + File.separator + "toc.xhtml");
            }

            // index
            if (myBook.getExportInfo().isHasIndex()) {
                InputStream inputStream = EpubCreater.class
                        .getClassLoader().getResourceAsStream(FilesUtils.INDEX_XSL);
                FilesUtils.writeDOMHTML(inputStream, epub.getIndexDom(), filepath + "EPUB" + File.separator + "index.xhtml");
            }

            // main content xhtml
            JAXBContext jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");
            Transformer tf = TransformerFactory.newInstance().newTransformer(new StreamSource(EpubCreater.class
                    .getClassLoader().getResourceAsStream(FilesUtils.COOKML_XSL)));
            for (String cat : epub.getCookmls().keySet()) {
                JAXBSource source = new JAXBSource(jc, epub.getCookmls().get(cat));
                XalanHelper.setCategory(cat);
                Result result = new StreamResult(new File(filepath + "EPUB" + File.separator + cat + ".xhtml"));
                tf.transform(source, result);

            }
            // opf
            FilesUtils.writeDOMXML(epub.getOpfDom(), new FileOutputStream(filepath + "EPUB" + File.separator + "package.opf"));

        } catch (Exception ex) {
            Logger.getLogger(EpubCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export HTML oder OPF -- EPUB3", ex.getClass().getName());
        }
    }

}
