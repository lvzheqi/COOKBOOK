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

    private final ExportInfo exportInfo;

    private final String tex;
    private String filepath;

    public LatexCreater(MyBook myBook) throws SystemErrorException {

        this.exportInfo = myBook.getExportInfo();
        this.filepath = exportInfo.getFilepath();
        creatLatexFiles(0);

        CmlToLatex cmlToLatex = new CmlToLatex(myBook, filepath);
        tex = cmlToLatex.getTex();
    }

    public void write() throws ConvertErrorException {

        String vorspann = "\\author{" + exportInfo.getFirstName() + " " + exportInfo.getLastName() + "}\n\\title{" + exportInfo.getBookname()
                + "}\n\\begin{document}\n\\maketitle\n\\tableofcontents";

        String rahmen = "\\input{kochbuchkopf.tex}\n\\input{" + exportInfo.getBookname() + "_input.tex}\n\\input{kochbuchfuss.tex}";

        try {
            FileUtils.writeStringToFile(new File(filepath + exportInfo.getBookname() + "_input.tex"), vorspann + tex, "UTF-8");
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
            copyLatexHeadAndFoot(filepath);
        }
    }

    private void copyLatexHeadAndFoot(String path) throws SystemErrorException {
        try {
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHFUSS_LEX), new File(path + "kochbuchfuss.tex"));
            FileUtils.copyToFile(LatexCreater.class.getClassLoader().getResourceAsStream(FilesUtils.KOCHBUCHKOPF_LEX), new File(path + "kochbuchkopf.tex"));

        } catch (Exception ex) {
            Logger.getLogger(LatexCreater.class.getName()).log(Level.SEVERE, null, ex);
            throw new SystemErrorException("Fehler beim Lesen Config.sys Information");
        }

    }

}
