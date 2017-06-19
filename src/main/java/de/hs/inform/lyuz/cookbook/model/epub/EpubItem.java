package de.hs.inform.lyuz.cookbook.model.epub;


public class EpubItem {

    private String id;
    private String href;
    private String mediatype;
    private String properties = null;
    private static Long uuid = Long.valueOf(1000000);

    private boolean isSPine;

    public EpubItem() {
        this.id = (uuid++).toString();
    }

    public EpubItem(String href, String mediatype, boolean isSPine) {
        this.id = (uuid++).toString();
        this.href = href;
        this.mediatype = mediatype;
        this.isSPine = isSPine;
    }

    public EpubItem(String href, String mediatype, String properties, boolean isSPine) {
        this.id = (uuid++).toString();
        this.href = href;
        this.mediatype = mediatype;
        this.properties = properties;
        this.isSPine = isSPine;
    }

    public String getId() {
        return id;
    }


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }


    public boolean isSPine() {
        return isSPine;
    }

    public void setSPine(boolean SPine) {
        isSPine = SPine;
    }
}
