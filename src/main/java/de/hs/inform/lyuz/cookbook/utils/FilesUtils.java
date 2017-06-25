package de.hs.inform.lyuz.cookbook.utils;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.apache.commons.io.IOUtils;



public class FilesUtils {

    public static String INDEX_XSL = "META-INF/epub/xsl/index.xsl";
    public static String COOKML_XSL = "META-INF/epub/xsl/cookml.xsl";
    public static String CALALOGEPUB_XSL = "META-INF/epub/xsl/catalogEpub.xsl";
    public static String EPUB_SPEC_CSS = "META-INF/epub/css/epub-spec.css";
    public static String STAR_PNG = "META-INF/epub/icons/star.png";
    public static String STAR_BOARD_PNG = "META-INF/epub/icons/star_board.png";
    public static String CONTAINER_XML = "META-INF/epub/container.xml";
    public static String COVER_XHTML = "META-INF/epub/cover.xhtml";
    public static String MIMETYPE = "META-INF/epub/mimetype";

    public static String CATEGORY_XML = "META-INF/conf/category.xml";
    public static String PROPERTIES_XML = "META-INF/conf/properties.xml";

    public static String KOCHBUCHFUSS_LEX = "META-INF/latex/kochbuchfuss.tex";
    public static String KOCHBUCHKOPF_LEX = "META-INF/latex/kochbuchkopf.tex";

    public static String COOKML_XSD = "META-INF/cml/cookml.xsd";
    public static String COOKML_DTD = "META-INF/cml/cookml.dtd";

    public static void changeImgeColor2BW(InputStream img, File desFile) throws IOException {

        Image image = ImageIO.read(img);
        int srcW = image.getHeight(null);
        int srcH = image.getWidth(null);
        BufferedImage bufferedImage = new BufferedImage(srcW, srcH, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.getGraphics().drawImage(image, 0, 0, srcW, srcH, null);
        bufferedImage = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(bufferedImage, null);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(desFile);
            ImageIO.write(bufferedImage, "jpg", fos);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static String readFile(File f) {
        StringBuilder stringBuilder = null;
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(
                    new FileInputStream(f), StandardCharsets.ISO_8859_1);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            stringBuilder = new StringBuilder();

            while ((lineTxt = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineTxt);
                stringBuilder.append("\n");
            }

        } catch (IOException e) {
            Logger.getLogger(FilesUtils.class
                    .getName()).log(Level.SEVERE, null, e);

        } finally {
            try {
                if (read != null) {
                    read.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FilesUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return String.valueOf(stringBuilder);
    }

    public static void compress(ZipOutputStream out, File sourceFile, String base) throws IOException {

        if (sourceFile.isDirectory()) {
            File[] flist = sourceFile.listFiles();
            if (flist.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/"));
            } else {
                for (File f : flist) {
                    compress(out, f, base + "/" + f.getName());
                }
            }
        } else {
            try (InputStream is = new FileInputStream(sourceFile)) {
                out.putNextEntry(new ZipEntry(base));
                int len;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                out.closeEntry();
            }
        }

    }

    public static void uncompress(File zipFile, String descDir) throws Exception {
        ZipFile zip = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            zip = new ZipFile(zipFile);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();

                in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName);
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }

            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (zip != null) {
                zip.close();
            }
        }

    }

    public static void writeDOMXML(Document document, FileOutputStream w) throws IOException {
        XMLOutputter o = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setIndent("\t");
        o.setFormat(format);
        o.output(document, w);
    }

    public static void writeDOMHTML(InputStream inputStream, Document document, String path) throws TransformerException, IOException {
        JDOMResult out = new JDOMResult();
        Transformer tf;

        tf = TransformerFactory.newInstance().newTransformer(new StreamSource(inputStream));
        tf.transform(new JDOMSource(document), out);
        FilesUtils.writeDOMXML(out.getDocument(), new FileOutputStream(path));
    }

}

// Text in ein File schreiben
//    public static void writeTexttoFile(String text, String filename) throws FileNotFoundException {
//
//        PrintWriter myout = new PrintWriter(filename);
//        Scanner scanner = new Scanner(text);
//        while (scanner.hasNextLine()) {
//            myout.println(scanner.nextLine());
//        }
//        myout.flush();
//    }
// Bild / Bytestrom in File schreiben
//    public static void writeByteToFile(byte[] picbyte, String filename) throws IOException {
////        try (
////                FileOutputStream fileOuputStream = new FileOutputStream(new File(filename))) {
////            fileOuputStream.write(picbyte);
////        }
//
//          FileUtils.writeByteArrayToFile(new File(filename),picbyte);
//    }
//    public static void writeRessource(InputStream inputStream, String newPath) {
//        try {
//            int bytesum = 0;
//            int byteread = 0;
//            if (inputStream != null) {
//                FileOutputStream fs = new FileOutputStream(newPath);
//                byte[] buffer = new byte[1444];
//                while ((byteread = inputStream.read(buffer)) != -1) {
//                    bytesum += byteread;
//                    fs.write(buffer, 0, byteread);
//                }
//                inputStream.close();
//            }
//        } catch (IOException e) {
//            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
//
//        }
//    }
//    public static void copyFile(String oldPath, String newPath) {
//        try {
//            int bytesum = 0;
//            int byteread = 0;
//            File oldfile = new File(oldPath);
//            if (oldfile.exists()) {
//                InputStream inStream = new FileInputStream(oldPath);
//                FileOutputStream fs = new FileOutputStream(newPath);
//                byte[] buffer = new byte[1444];
//                while ((byteread = inStream.read(buffer)) != -1) {
//                    bytesum += byteread;
//                    fs.write(buffer, 0, byteread);
//                }
//                inStream.close();
//            }
//        } catch (IOException e) {
//            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, e);
//
//        }
//    }
//
//    public static byte[] file2Byte(File file) {
//        FileInputStream fis;
//        try {
//            fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int n;
//            while ((n = fis.read(b)) != -1) {
//                bos.write(b, 0, n);
//            }
//            fis.close();
//            bos.close();
//            return bos.toByteArray();
//        } catch (IOException ex) {
//            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    public static void deleteFile(File sourceFile) {
//        File[] files = sourceFile.listFiles();
//        int length = files.length;
//        for (int i = 0; i < length; i++) {
//            if (!files[i].isDirectory() && !files[i].getName().equals("cover.xhtml")) {
//                files[i].delete();
//            } else if (files[i].getName().equals("image")) {
//                File[] image = files[i].listFiles();
//                int size = image.length;
//                for (int j = 0; j < size; j++) {
//                    image[j].delete();
//                }
//            } else if (files[i].getName().equals("cover")) {
//                File[] cover = files[i].listFiles();
//                int size = cover.length;
//                for (int j = 0; j < size; j++) {
//                    cover[j].delete();
//                }
//            }
//        }
//    }
//    public static void executeCommand(String command) {
//
//        StringBuilder output = new StringBuilder();
//
//        Process p;
//        try {
//            p = Runtime.getRuntime().exec(command);
//            p.waitFor();
//            BufferedReader reader
//                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//        } catch (IOException | InterruptedException e) {
//            Logger.getLogger(FilesUtils.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
// Textfile in einen String lesen
//    public static String file2Text(File f) {
//        String readtext = "";
//        try {
//            Scanner scanner = new Scanner(f);
//            while (scanner.hasNextLine()) {
//                readtext += scanner.nextLine() + "\n";
//
//            }
//        } catch (FileNotFoundException e) {
//            Logger.getLogger(FileUtils.class
//                    .getName()).log(Level.SEVERE, null, e);
//        }
//
//        return readtext;
//    }
//    public static String readFile(InputStream f) {
////        InputStream in = con.getInputStream();
//        String result = null;
//        try {
//            result = IOUtils.toString(f, "UTF-8");
//        } catch (IOException ex) {
//            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }
//    public static String readFile(File f) {
//        StringBuilder stringBuilder = null;
//        try {
//            InputStreamReader read = new InputStreamReader(
//                    new FileInputStream(f), StandardCharsets.ISO_8859_1);
//            BufferedReader bufferedReader = new BufferedReader(read);
//            String lineTxt;
//            stringBuilder = new StringBuilder();
//
//            while ((lineTxt = bufferedReader.readLine()) != null) {
//                stringBuilder.append(lineTxt);
//                stringBuilder.append("\n");
//            }
//            read.close();
//
//        } catch (IOException e) {
//            Logger.getLogger(FileUtils.class
//                    .getName()).log(Level.SEVERE, null, e);
//
//        }
//        return String.valueOf(stringBuilder);
//    }
//    public static String ReadFiletoText1(String filename) {
//        String readtext = "";
//        try {
//            readtext = FilesUtils.readFileToString(new File(filename));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return readtext;
//    }
//    public static String ReadFiletoText2(String filename) {
//        String readtext = "";
//        int i = 0;
//        try {
//            BufferedReader in = new BufferedReader(new FileReader(filename));
//            String zeile = null;
//            while ((zeile = in.readLine()) != null) {
//                readtext = readtext + zeile + "\n";
//                i++;
//                if ((i % 5000) == 0) {
//                    System.out.println(i);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(i + " lines");
//
//        return readtext;
//    }
// Bild lesen 
//
//    public static byte[] ReadFiletoBytes(String filename) {
//        byte[] b = null;
//        FileInputStream in = null;
//
//        try {
//            in = new FileInputStream(new File(filename));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            int anz = in.available();
//            b = new byte[anz];
//            in.read(b);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return b;
//    }
//    public static List<String> ReadFiletoList(String filename) {
//
//        List<String> ListText = new ArrayList<String>();
//
//        try {
//            Scanner scanner = new Scanner(new File(filename));
//            while (scanner.hasNextLine()) {
//                ListText.add(scanner.nextLine());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return ListText;
//    }
//    public static void CutInteger(String Inputfile, String Outputfile) {
//        String InputString = ReadFiletoText1(Inputfile);
//        String OutputString = cutIntegerString(InputString);
//        writeTexttoFile(OutputString, Outputfile);
//    }
//
//    public static String cutIntegerString(String InputString) {
//        String OutputString = InputString;
//        int setoff = 0;
//        String anfang = "";
//        Boolean first = true;
//
//        for (int pos = 0; pos < InputString.length() - 1; pos++) {
//            String sub = InputString.substring(pos, pos + 2);
//            //System.out.println(sub);
//            if (sub != null) {
//                if (sub.equals(".0")) {
//                    if (first) {
//                        first = false;
//                    } else {
//                        if (pos == 0) {
//                            anfang = "";
//                        } else {
//                            anfang = OutputString.substring(0, pos + setoff - 1);
//                        }
//                        OutputString = anfang + InputString.substring(pos + 2);
//                        setoff -= 2;
//                    }
//                }
//            }
//        }
//        return OutputString;
//    }

