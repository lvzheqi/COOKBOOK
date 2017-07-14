package de.hs.inform.lyuz.cookbook.logic.creater.epubcreater;

import de.hs.inform.lyuz.cookbook.model.exception.ConvertErrorException;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class EPUB3Writer {

    public void write(String filepath, File file) throws ConvertErrorException {

        ZipOutputStream resultStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            resultStream = new ZipOutputStream(fos);

            // mimetype
            ZipEntry mimetypeZipEntry = new ZipEntry("mimetype");
            mimetypeZipEntry.setMethod(ZipEntry.STORED);

//            String mimetype = "application/epub+zip";             // no mimetype file
//            byte[] mimetypeBytes = mimetype.getBytes();
            byte[] mimetypeBytes = FileUtils.readFileToByteArray(new File(filepath + "mimetype"));   //read from file
            mimetypeZipEntry.setSize(mimetypeBytes.length);
            mimetypeZipEntry.setCrc(calculateCrc(mimetypeBytes));
            resultStream.putNextEntry(mimetypeZipEntry);
            resultStream.write(mimetypeBytes);
            
            // container.xml
            File metaFile = new File(filepath + "META-INF");
            FilesUtils.compress(resultStream, metaFile, "META-INF");

            // main file
            File mainFile = new File(filepath + "EPUB");
            FilesUtils.compress(resultStream, mainFile, "EPUB");



        } catch (Exception e) {
            Logger.getLogger(EPUB3Writer.class.getName()).log(Level.SEVERE, null, e);
            throw new ConvertErrorException("Fehler beim Export EPUB3");
        } finally {
            IOUtils.closeQuietly(resultStream);
        }
    }

    private long calculateCrc(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}
