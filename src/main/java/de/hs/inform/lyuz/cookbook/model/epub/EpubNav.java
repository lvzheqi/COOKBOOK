package de.hs.inform.lyuz.cookbook.model.epub;


import org.jdom2.Document;
import org.jdom2.Element;

import java.util.HashMap;

public class EpubNav {

//    public static int index = 1;
//    public static HashMap<Integer, EpubLink> eNavMap;
    private final Document doc;


    public EpubNav(String title, HashMap<Integer, EpubLink> navMap) {

        //DocType
//        Element root = new Element("html");
//        DocType dtype = new DocType(root.getName(),"-//W3C//DTD XHTML 1.0 Transitional//EN",
//                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
//        doc = new Document(root,dtype);
//        root.setAttribute("lang", "en", Namespace.XML_NAMESPACE);

        doc = new Document();

        Element t = new Element("catalog");
        t.addContent(new Element("title").setText(title));


        for (Integer i : navMap.keySet()) {
            EpubLink epubLink = navMap.get(i);
            Element link = new Element("link").setAttribute("href", epubLink.getHref()).setAttribute("value", epubLink.getValue());

            if (epubLink.getNavMap() != null) {
                for (Integer j : epubLink.getNavMap().keySet()) {
                    EpubLink subEpubLink = epubLink.getNavMap().get(j);
                    Element element = new Element("sublink").setAttribute("href", subEpubLink.getHref()).setAttribute("value", subEpubLink.getValue());
                    link.addContent(element);
                }
            }
            t.addContent(link);
        }
        doc.setRootElement(t);
    }


//    public static void addNav2List(EpubLink epubLink) {
//        eNavMap.put(index, epubLink);
//        index++;
//    }

//    public HashMap<Integer, EpubLink> geteNavMap() {
//        return eNavMap;
//    }
//
//    public void seteNavMap(HashMap<Integer, EpubLink> eNavMap) {
//        this.eNavMap = eNavMap;
//    }

    public Document getDoc() {
        return doc;
    }
}
