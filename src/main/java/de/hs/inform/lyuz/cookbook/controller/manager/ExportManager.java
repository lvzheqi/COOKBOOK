package de.hs.inform.lyuz.cookbook.controller.manager;

import de.hs.inform.lyuz.cookbook.controller.creater.LatexCreater;
import de.hs.inform.lyuz.cookbook.controller.creater.CMLCreater;
import de.hs.inform.lyuz.cookbook.controller.creater.epubcreater.EPUB3Writer;
import de.hs.inform.lyuz.cookbook.controller.creater.epubcreater.EpubCreater;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.SystemErrorException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubWriter;



public class ExportManager {

    private final MyBook myBook;

    public ExportManager(MyBook myBook) {
        this.myBook = new SortManager(myBook).sortCML();
    }

    
    
    public void texExport() throws SystemErrorException, ConvertErrorException {

        LatexCreater latexCreater = new LatexCreater(myBook);
        latexCreater.write();

        System.out.println("Fertig!");
    }

    public void cmlExport() throws ConvertErrorException {
        
        CMLCreater cmlCreater = new CMLCreater(myBook);
        cmlCreater.write();
        
        System.out.println("Fertig!");

    }

    public void epub2Export() throws SystemErrorException, ConvertErrorException {
        EpubCreater epubCreater = new EpubCreater(myBook);
        Book book = epubCreater.epub2();

        EpubWriter epubWriter = new EpubWriter();
        try {
            epubWriter.write(book,
                    new FileOutputStream(myBook.getExportInfo().getFilepath() + myBook.getExportInfo().getBookname() + ".epub"));
        } catch (Exception ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export EPUB2");
        }

        System.out.println("Fertig!");
    }

    public void epub3Export() throws SystemErrorException, ConvertErrorException {
        EpubCreater epubCreater = new EpubCreater(myBook);
        epubCreater.epub3();

        EPUB3Writer epub3Writer = new EPUB3Writer();
        epub3Writer.write(epubCreater.getFilepath(),
                new File(myBook.getExportInfo().getFilepath() + myBook.getExportInfo().getBookname() + ".epub"));

        System.out.println("Fertig!");
    }

}
