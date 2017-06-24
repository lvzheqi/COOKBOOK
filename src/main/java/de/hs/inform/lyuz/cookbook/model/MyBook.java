package de.hs.inform.lyuz.cookbook.model;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyBook {
    
    private List<File> files;
    private Cookml cookml;
    private ArrayList<String> catTemplate;
//    private ArrayList<String> catAll;
    private ArrayList<String> catExtra;
    private LinkedHashMap<String, Cookml> sortCmlMap;
    
    private ExportInfo exportInfo;
    
    
    
    public MyBook(){
        init();
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
    
    
}
