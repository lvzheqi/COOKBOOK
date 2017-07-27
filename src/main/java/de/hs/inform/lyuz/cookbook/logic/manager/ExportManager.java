package de.hs.inform.lyuz.cookbook.logic.manager;

import de.hs.inform.lyuz.cookbook.logic.creater.TexCreater;
import de.hs.inform.lyuz.cookbook.logic.creater.CMLCreater;
import de.hs.inform.lyuz.cookbook.logic.creater.epubcreater.EPUB3Writer;
import de.hs.inform.lyuz.cookbook.logic.creater.epubcreater.EpubCreater;
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
import org.apache.commons.io.FileUtils;

public class ExportManager {

    private MyBook myBook;

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public ExportManager(MyBook myBook) {
        this.myBook = new SortManager(myBook).sortBook();

    }

    public void texExport() throws SystemErrorException, ConvertErrorException {

        TexCreater latexCreater = new TexCreater(myBook);
        latexCreater.write();

        errorMessage += latexCreater.getErrorMessage();
        System.out.println("Fertig!");
    }

    public void cmlExport() throws ConvertErrorException {

        CMLCreater cmlCreater = new CMLCreater(myBook);
        cmlCreater.write();

        errorMessage += cmlCreater.getErrorMessage();
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
            throw new ConvertErrorException("Fehler beim Export EPUB2", ex.getClass().getName());
        }
        errorMessage += epubCreater.getErrorMessage();
         try {
            FileUtils.deleteDirectory(new File(epubCreater.getFilepath()));
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Löschen EPUB Hilfeatei ");
            errorMessage += "Fehler beim Löschen EPUB Hilfeatei: " + epubCreater.getFilepath() + "\n";
        }
         
        System.out.println("Fertig!");
    }

    public void epub3Export() throws SystemErrorException, ConvertErrorException {
        EpubCreater epubCreater = new EpubCreater(myBook);
        epubCreater.epub3();

        EPUB3Writer epub3Writer = new EPUB3Writer();
        epub3Writer.write(epubCreater.getFilepath(),
                new File(myBook.getExportInfo().getFilepath() + myBook.getExportInfo().getBookname() + ".epub"));
        errorMessage += epubCreater.getErrorMessage();

        try {
            FileUtils.deleteDirectory(new File(epubCreater.getFilepath()));
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Löschen EPUB Hilfeatei ");
            errorMessage += "Fehler beim Löschen EPUB Hilfeatei: " + epubCreater.getFilepath() + "\n";
        }
        
        System.out.println("Fertig!");
    }

}
