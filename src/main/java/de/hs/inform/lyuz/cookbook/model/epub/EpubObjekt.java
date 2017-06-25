package de.hs.inform.lyuz.cookbook.model.epub;

import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import org.jdom2.Document;
import org.jdom2.Element;

public class EpubObjekt {

    private TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexMap;
    private HashMap<Integer, EpubLink> navMap;

    private List<Element> elements;
    private List<EpubItem> items;

    private int index = 1;

    private Document indexDom;
    private Document navDom;
    private Document opfDom;

    private LinkedHashMap<String, Cookml> cookmls;
    private List<String> picList;

    public TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> getIndexMap() {
        return indexMap;
    }

    public void setIndexMap(TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexMap) {
        this.indexMap = indexMap;
    }

    public HashMap<Integer, EpubLink> getNavMap() {
        return navMap;
    }

    public void setNavMap(HashMap<Integer, EpubLink> navMap) {
        this.navMap = navMap;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public List<EpubItem> getItems() {
        return items;
    }

    public void setItems(List<EpubItem> items) {
        this.items = items;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    
    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public LinkedHashMap<String, Cookml> getCookmls() {
        return cookmls;
    }

    public void setCookmls(LinkedHashMap<String, Cookml> cookmls) {
        this.cookmls = cookmls;
    }

    public Document getIndexDom() {
        return indexDom;
    }

    public void setIndexDom(Document indexDom) {
        this.indexDom = indexDom;
    }

    public Document getNavDom() {
        return navDom;
    }

    public void setNavDom(Document navDom) {
        this.navDom = navDom;
    }

    public Document getOpfDom() {
        return opfDom;
    }

    public void setOpfDom(Document opfDom) {
        this.opfDom = opfDom;
    }

    public EpubObjekt() {
        index = 1;
        cookmls = new LinkedHashMap<>();
        navMap = new HashMap<>();
        elements = new ArrayList<>();
        items = new ArrayList<>();
        indexMap = new TreeMap<>();
        picList = new ArrayList<>();
    }
}
