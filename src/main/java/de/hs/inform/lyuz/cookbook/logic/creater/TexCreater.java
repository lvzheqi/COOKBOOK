package de.hs.inform.lyuz.cookbook.logic.creater;

import de.hs.inform.lyuz.cookbook.logic.convert.CmlToTex;
import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.SystemErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TexCreater {

    private ExportInfo exportInfo;

    private String tex;
    private String filepath;
    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public TexCreater(MyBook myBook) throws SystemErrorException {

        this.exportInfo = myBook.getExportInfo();
        this.filepath = exportInfo.getFilepath();
        creatLatexFiles(0);

        CmlToTex cmlToLatex = new CmlToTex(myBook, filepath);
        tex = cmlToLatex.getTex();
        errorMessage += cmlToLatex.getErrorMessage();
    }

    public void write() throws ConvertErrorException, SystemErrorException {

        try {
            String head = IOUtils.toString(TexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHKOPF_LEX), "UTF-8");
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + ".tex"), head, "UTF-8");
        } catch (Exception ex) {
            Logger.getLogger(TexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information", ex.getClass().getName());
        }

        try {
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + ".tex"), tex, "UTF-8", true);
        } catch (Exception ex) {
            Logger.getLogger(TexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export Latex", ex.getClass().getName());
        }
        
        try {
            String head = IOUtils.toString(TexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHFUSS_LEX), "UTF-8");
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + ".tex"), head, "UTF-8",true);
        } catch (Exception ex) {
            Logger.getLogger(TexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information", ex.getClass().getName());
        }
    }

    private void creatLatexFiles(int i) throws SystemErrorException {
        File file;
        if (i == 0) {
            file = new File(filepath + "LATEX");
        } else {
            file = new File(filepath + "LATEX" + "(" + i + ")");
        }

        if (file.exists() || file.isDirectory()) {
            creatLatexFiles(++i);
        } else {
            file.mkdir();
            filepath = file.getAbsolutePath() + File.separator;
            File imagFiles = new File(filepath + "images");
            imagFiles.mkdir();
            copyRessource(filepath);
            if (exportInfo.isHasCover()) {
                setImgPdf(filepath);
            }
        }
    }

    private void copyRessource(String path) throws SystemErrorException {

        try {
            FileUtils.copyToFile(TexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_PNG), new File(path + "images" + File.separator + "star.png"));
            FileUtils.copyToFile(TexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_BOARD_PNG), new File(path + "images" + File.separator + "star_board.png"));

        } catch (Exception ex) {
            Logger.getLogger(TexCreater.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Lesen icons");
            errorMessage += "Fehler beim Lesen icons \n";

        }

    }

    private void setImgPdf(String filepath) {
        try {
            FilesUtils.changeImgToPdf(exportInfo.getCoverPath(), filepath + "cover.pdf");
        } catch (Exception e) {
            Logger.getLogger(TexCreater.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Fehler beim Erzeugen cover PDF ");
            errorMessage += "Fehler beim Erzeugen cover PDF -- Tex \n";

        }
    }
}
