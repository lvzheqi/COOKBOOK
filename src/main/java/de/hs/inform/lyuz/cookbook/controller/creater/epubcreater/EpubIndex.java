package de.hs.inform.lyuz.cookbook.controller.creater.epubcreater;

import de.hs.inform.lyuz.cookbook.model.epub.EpubLink;
import java.util.TreeMap;
import org.jdom2.Document;
import org.jdom2.Element;

public class EpubIndex {

    private Document doc;

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public EpubIndex(TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexSort) {
        creatDoc(indexSort);
    }

    private void creatDoc(TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexSort) {
        doc = new Document();
        Element root = new Element("index");
        //set Index xml
        indexSort.keySet().stream().map((map1) -> {
            Element igroup = new Element("igroup").setAttribute("value", map1);
            indexSort.get(map1).keySet().stream().map((map2) -> {
                Element ilevel1 = new Element("ilevel1").setAttribute("value", map2);
                indexSort.get(map1).get(map2).keySet().stream().map((map3) -> {
                    int page = indexSort.get(map1).get(map2).get(map3);
                    Element ilevel2 = new Element("ilevel2").setText(map3.getValue())
                            .setAttribute("href", map3.getHref())
                            .setAttribute("page", String.valueOf(page));
                    return ilevel2;
                }).forEachOrdered((ilevel2) -> {
                    ilevel1.addContent(ilevel2);
                });
                return ilevel1;
            }).forEachOrdered((ilevel1) -> {
                igroup.addContent(ilevel1);
            });
            return igroup;
        }).forEachOrdered((igroup) -> {
            root.addContent(igroup);
        });
        doc.setRootElement(root);
    }


}
