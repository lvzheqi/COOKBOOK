package de.hs.inform.lyuz.cookbook.controller.creater;

import de.hs.inform.lyuz.cookbook.controller.convert.CmlToLatex;
import de.hs.inform.lyuz.cookbook.model.ExportInfo;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.model.exception.SystemErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

public class LatexCreater {

    private ExportInfo exportInfo;

    private String tex;
    private String filepath;

    public LatexCreater(MyBook myBook) throws SystemErrorException {

        this.exportInfo = myBook.getExportInfo();
        this.filepath = exportInfo.getFilepath();
        creatLatexFiles(0);

        CmlToLatex cmlToLatex = new CmlToLatex(myBook, filepath);
        tex = cmlToLatex.getTex();
    }

    public void write() throws ConvertErrorException {

        String rahmen = "\\input{kochbuchkopf.tex}\n\\input{" + exportInfo.getBookname() + "_input.tex}\n\\input{kochbuchfuss.tex}";

        try {
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + "_input.tex"), tex, "UTF-8");
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + ".tex"), rahmen, "UTF-8");

        } catch (Exception ex) {
            Logger.getLogger(LatexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConvertErrorException("Fehler beim Export Latex");
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
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHKOPF_LEX), new File(path + "kochbuchkopf.tex"));
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHFUSS_LEX), new File(path + "kochbuchfuss.tex"));
        } catch (Exception ex) {
            Logger.getLogger(LatexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information");
        }
        try {
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_PNG), new File(path + "images" + File.separator + "star.jpg"));
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.STAR_BOARD_PNG), new File(path + "images" + File.separator + "star_board.jpg"));

        } catch (Exception ex) {
            Logger.getLogger(LatexCreater.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Fehler beim Lesen icons");
        }

    }

    private void setImgPdf(String filepath) {
        try {
            FilesUtils.changeImgToPdf(exportInfo.getCoverPath(), filepath + "cover.pdf");
        } catch (Exception e) {
            Logger.getLogger(LatexCreater.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Fehler beim Erzeugen cover PDF ");
        }
    }
}
