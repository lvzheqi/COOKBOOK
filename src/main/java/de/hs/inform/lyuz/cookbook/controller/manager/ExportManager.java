package de.hs.inform.lyuz.cookbook.controller.manager;

import de.hs.inform.lyuz.cookbook.controller.convert.CmlToTex;
import de.hs.inform.lyuz.cookbook.epubcreater.EPUB3Writer;
import de.hs.inform.lyuz.cookbook.epubcreater.EpubCreater;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import de.hs.inform.lyuz.cookbook.help.ExportInfo;
import de.hs.inform.lyuz.cookbook.help.Utils;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubWriter;

public class ExportManager {

    private final MyBook myBook;
    
    public ExportManager(MyBook myBook) {
        
        this.myBook = new SortManager(myBook).sortCML();
    }
    
    
    
    public void texExport() {
        String text = new CmlToTex(myBook.getSortCmlMap(), ExportInfo.bookname).getTex();
        String vorspann = "\\author{" + ExportInfo.firstName + " " + ExportInfo.lastName + "}\n\\title{" + ExportInfo.title
                + "}\n\\begin{document}\n\\maketitle\n\\tableofcontents";
        Utils.writeTexttoFile(vorspann + text, "LATEX/" + ExportInfo.bookname + "_input.tex");

        String rahmen = "\\input{kochbuchkopf.tex}\n\\input{" + ExportInfo.bookname + "_input.tex}\n\\input{kochbuchfuss.tex}";
        Utils.writeTexttoFile(rahmen, "LATEX/" + ExportInfo.bookname + ".tex");

        System.out.println("Fertig!");

    }

    public void cmlExport() {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.hs.inform.lyuz.cookbook.model.cookml");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "cookml.xsd");

            m.marshal(myBook.getCookml(), new File(ExportInfo.filepath + ExportInfo.bookname + ".cml"));

            System.out.println("Fertig!");

        } catch (JAXBException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void epub2Export(){
        EpubCreater epubCreater = new EpubCreater(ExportInfo.filepath); 
        Book book = epubCreater.epub2(myBook.getSortCmlMap(), myBook.getCatTemplate());
    
        EpubWriter epubWriter = new EpubWriter();
        try {
            epubWriter.write(book, new FileOutputStream(ExportInfo.filepath + ExportInfo.bookname + ".epub"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }

//            Utils.deleteFile(new File(filepath + "EPUB"));
        System.out.println("Fertig!");
    }

    public void epub3Export() {
        EpubCreater epubCreater = new EpubCreater(ExportInfo.filepath); 
        
        String filepath = epubCreater.epub3(myBook.getSortCmlMap(), myBook.getCatTemplate());
        
        EPUB3Writer epub3Writer = new EPUB3Writer();
        try {
            epub3Writer.write(filepath, new FileOutputStream(ExportInfo.filepath + ExportInfo.bookname + ".epub"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Fertig!");
    }

}
