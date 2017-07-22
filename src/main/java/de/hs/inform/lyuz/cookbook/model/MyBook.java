package de.hs.inform.lyuz.cookbook.model;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyBook implements Serializable {

    private List<File> files;
    private Cookml cookml;
    private ArrayList<String> catTemplate;
//    private ArrayList<String> catAll;
    private ArrayList<String> catExtra;
    private LinkedHashMap<String, Cookml> sortCmlMap;

    private ExportInfo exportInfo;

    private String errorMessage="";
    
    
    
    public MyBook(List<File> files, Cookml cookml, ArrayList<String> catTemplate, ArrayList<String> catExtra, LinkedHashMap<String, Cookml> sortCmlMap, ExportInfo exportInfo) {
        this.files = files;
        this.cookml = cookml;
        this.catTemplate = catTemplate;
        this.catExtra = catExtra;
        this.sortCmlMap = sortCmlMap;
        this.exportInfo = exportInfo;
    }

    
    public MyBook() {
        init();
    }

    public MyBook myclone() {
        MyBook outer = null;
        try { 
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            outer = (MyBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return outer;
    }

    public void setMybook(MyBook mb) {
        files = mb.getFiles();
        cookml = mb.getCookml();
        catTemplate = mb.getCatTemplate();
        catExtra = mb.getCatExtra();
        sortCmlMap = mb.getSortCmlMap();
        exportInfo = mb.getExportInfo();

    }

    public void init() {
        files = new ArrayList<>();
        cookml = new Cookml();
//        catAll = new ArrayList<>();
        catTemplate = new ArrayList<>();
        catExtra = new ArrayList<>();
        sortCmlMap = new LinkedHashMap<>();
        exportInfo = new ExportInfo();
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Cookml getCookml() {
        return cookml;
    }

    public void setCookml(Cookml cookml) {
        this.cookml = cookml;
    }

    public ArrayList<String> getCatTemplate() {
        return catTemplate;
    }

    public void setCatTemplate(ArrayList<String> catTemplate) {
        this.catTemplate = catTemplate;
    }

//    public ArrayList<String> getCatAll() {
//        return catAll;
//    }
//
//    public void setCatAll(ArrayList<String> catAll) {
//        this.catAll = catAll;
//    }
    public ArrayList<String> getCatExtra() {
        return catExtra;
    }

    public void setCatExtra(ArrayList<String> catExtra) {
        this.catExtra = catExtra;
    }

    public LinkedHashMap<String, Cookml> getSortCmlMap() {
        return sortCmlMap;
    }

    public void setSortCmlMap(LinkedHashMap<String, Cookml> sortCmlMap) {
        this.sortCmlMap = sortCmlMap;
    }

    public ExportInfo getExportInfo() {
        return exportInfo;
    }

    public void setExportInfo(ExportInfo exportInfo) {
        this.exportInfo = exportInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
