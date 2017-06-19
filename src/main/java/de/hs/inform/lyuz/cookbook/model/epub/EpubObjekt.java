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

//    public TreeMap<String, TreeMap<EpubLink, Integer>> indexMap;
    public TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexMap;
    public HashMap<Integer, EpubLink> navMap;

    public List<Element> elements;
    public List<EpubItem> items;

    public int index = 1;
    
    private Document indexDom;
    private Document navDom;
    private Document opfDom;
    public LinkedHashMap<String, Cookml> cookmls = new LinkedHashMap<>();
    private List<String> picList = new ArrayList<>();

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
        navMap = new HashMap<>();
        elements = new ArrayList<>();
        items = new ArrayList<>();
        indexMap = new TreeMap<>();
    }
}
