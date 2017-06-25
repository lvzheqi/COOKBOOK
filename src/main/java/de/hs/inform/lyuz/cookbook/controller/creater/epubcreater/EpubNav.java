package de.hs.inform.lyuz.cookbook.controller.creater.epubcreater;


import de.hs.inform.lyuz.cookbook.model.epub.EpubLink;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.HashMap;

public class EpubNav {

    private Document doc;


    public EpubNav(String title, HashMap<Integer, EpubLink> navMap) {

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

    public Document getDoc() {
        return doc;
    }
}
