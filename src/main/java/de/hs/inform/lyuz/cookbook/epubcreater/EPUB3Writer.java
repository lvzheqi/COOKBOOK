package de.hs.inform.lyuz.cookbook.epubcreater;

import de.hs.inform.lyuz.cookbook.help.Utils;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EPUB3Writer {


    public void write(String filepath, FileOutputStream fos) {

        ZipOutputStream resultStream;
        try {
            resultStream = new ZipOutputStream(fos);

            // mimetype
            ZipEntry mimetypeZipEntry = new ZipEntry("mimetype");
            mimetypeZipEntry.setMethod(ZipEntry.STORED);

//            String mimetype = "application/epub+zip";             // no mimetype file
//            byte[] mimetypeBytes = mimetype.getBytes();

            byte[] mimetypeBytes = Utils.file2Byte(new File(filepath + "mimetype"));   //read from file
            mimetypeZipEntry.setSize(mimetypeBytes.length);
            mimetypeZipEntry.setCrc(calculateCrc(mimetypeBytes));
            resultStream.putNextEntry(mimetypeZipEntry);
            resultStream.write(mimetypeBytes);

            // main file
            File mainFile = new File(filepath + "EPUB");
            Utils.compress(resultStream, mainFile, "EPUB");

            // container.xml
            File metaFile = new File(filepath + "META-INF");
            Utils.compress(resultStream, metaFile, "META-INF");

            resultStream.close();
            //   Utils.deleteFile(new File(filepath + "EPUB"));

        } catch (IOException ex) {
            Logger.getLogger(EPUB3Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private long calculateCrc(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}
