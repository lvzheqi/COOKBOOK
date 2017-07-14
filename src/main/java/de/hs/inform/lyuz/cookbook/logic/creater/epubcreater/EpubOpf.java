package de.hs.inform.lyuz.cookbook.logic.creater.epubcreater;

import org.jdom2.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import de.hs.inform.lyuz.cookbook.model.epub.EpubItem;

public class EpubOpf {

    private final Namespace ns1 = Namespace.getNamespace("http://www.idpf.org/2007/opf");
    private final Namespace ns2 = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
    private final Namespace ns3 = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

    private Document opf;

    public Document getOpf() {
        return opf;
    }

    public EpubOpf(String title, String firstname, String lastname,
            List<Element> elements, List<EpubItem> items) {

        opf = new Document();
        Element pack = new Element("package");

        pack.setNamespace(ns1);
        pack.addNamespaceDeclaration(ns2);
        pack.addNamespaceDeclaration(ns3);

        pack.setAttribute("version", "3.0");
        pack.setAttribute("lang", "en", Namespace.XML_NAMESPACE);
        pack.setAttribute("unique-identifier", "pub-identifier");

        pack.addContent(setMetadata(pack, title, firstname, lastname));
        pack.addContent(setManifest(pack, elements));
        pack.addContent(setSpine(pack, items));

        opf.setRootElement(pack);
    }

    private Element setMetadata(Element pack, String title, String firstname, String lastname) {
        Element metadata = new Element("metadata", pack.getNamespace());

        metadata.addContent(new Element("title", ns2).setAttribute("id", "title").addContent(title));

        metadata.addContent(new Element("identifier", ns2).addContent("urn:isbn:9781449328030"));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String date = df.format(new Date());
        metadata.addContent(new Element("meta", ns1)
                .setAttribute("property", "dcterms:modified")
                .addContent(date));
        metadata.addContent(new Element("meta", ns1)
                .setAttribute("name", "cover")
                .setAttribute("content", "cover-image"));
        metadata.addContent(new Element("language", ns2).addContent("DE"));

        metadata.addContent(new Element("contributor", ns2).addContent(firstname + " " + lastname));

        return metadata;
    }

    private Element setManifest(Element pack, List<Element> elements) {
        Element manifest = new Element("manifest", pack.getNamespace());

        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "epub-css")
                .setAttribute("href", "css/epub-spec.css")
                .setAttribute("media-type", "text/css"));

        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "toc")
                .setAttribute("properties", "nav")
                .setAttribute("href", "toc.xhtml")
                .setAttribute("media-type", "application/xhtml+xml"));

        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "cover")
                .setAttribute("href", "cover.xhtml")
                .setAttribute("media-type", "application/xhtml+xml"));

        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "cover-image")
                .setAttribute("properties", "cover-image")
                .setAttribute("href", "images/cover.jpg")
                .setAttribute("media-type", "image/jpeg"));

        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "icon-star")
                .setAttribute("href", "icons/star.jpg")
                .setAttribute("media-type", "image/jpeg"));
        
        manifest.addContent(new Element("item", manifest.getNamespace())
                .setAttribute("id", "icon-star-board")
                .setAttribute("href", "icons/star_board.jpg")
                .setAttribute("media-type", "image/jpeg"));

        elements.forEach((e) -> {
            manifest.addContent(e.setNamespace(manifest.getNamespace()));
        });

        return manifest;
    }

    private Element setSpine(Element pack, List<EpubItem> items) {
        Element spine = new Element("spine", pack.getNamespace());

        spine.addContent(new Element("itemref").setAttribute("idref", "cover")
                .setAttribute("linear", "no")
                .setNamespace(spine.getNamespace()));

        spine.addContent(new Element("itemref").setAttribute("idref", "toc")
                .setAttribute("linear", "yes")
                .setNamespace(spine.getNamespace()));

        items.forEach((item) -> {
            spine.addContent(new Element("itemref")
                    .setAttribute("idref", "id_" + item.getId())
                    .setNamespace(spine.getNamespace()));
        });

        return spine;
    }

}
