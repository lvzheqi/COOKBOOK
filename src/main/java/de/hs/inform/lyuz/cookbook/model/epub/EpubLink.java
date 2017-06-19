package de.hs.inform.lyuz.cookbook.model.epub;


import java.util.HashMap;

public class EpubLink implements Comparable<EpubLink>{
    private String href;
    private String value;

    private HashMap<Integer, EpubLink> navMap = new HashMap<>();

    public EpubLink() {
    }

    public EpubLink(String href, String value) {
        this.href = href;
        this.value = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap<Integer, EpubLink> getNavMap() {
        return navMap;
    }

    public void setNavMap(HashMap<Integer, EpubLink> navMap) {
        this.navMap = navMap;
    }

    @Override
    public int compareTo(EpubLink o) {
        
        return this.value.compareTo(o.getValue());
    }
    
    
}
