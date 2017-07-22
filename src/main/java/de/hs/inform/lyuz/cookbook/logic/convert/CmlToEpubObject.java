package de.hs.inform.lyuz.cookbook.logic.convert;

import de.hs.inform.lyuz.cookbook.model.epub.EpubItem;
import de.hs.inform.lyuz.cookbook.model.epub.EpubLink;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import de.hs.inform.lyuz.cookbook.utils.FilesUtils;
import java.io.File;
import java.util.*;
import de.hs.inform.lyuz.cookbook.utils.XalanHelper;
import de.hs.inform.lyuz.cookbook.model.MyBook;
import de.hs.inform.lyuz.cookbook.model.epub.EpubObjekt;
import de.hs.inform.lyuz.cookbook.logic.creater.epubcreater.EpubIndex;
import de.hs.inform.lyuz.cookbook.logic.creater.epubcreater.EpubNav;
import de.hs.inform.lyuz.cookbook.logic.creater.epubcreater.EpubOpf;
import de.hs.inform.lyuz.cookbook.model.cookml.Cookml;
import de.hs.inform.lyuz.cookbook.model.cookml.Head;
import de.hs.inform.lyuz.cookbook.model.cookml.Picbin;
import de.hs.inform.lyuz.cookbook.model.cookml.Picture;
import de.hs.inform.lyuz.cookbook.model.cookml.Recipe;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jdom2.Element;

public class CmlToEpubObject {

    private MyBook myBook = new MyBook();
    private HashMap<String, Integer> heads = new HashMap<>();
    private TreeMap<String, TreeMap<EpubLink, Integer>> indexTreeMap = new TreeMap<>();

    private int linkIndex = 1;
    private int picIndex = 1;
    private int pageNum = 1;

    private EpubObjekt epub;

    private String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public EpubObjekt getEpub() {
        epub.setNavDom(new EpubNav(myBook.getExportInfo().getTitle(), epub.getNavMap()).getDoc());
        epub.setIndexDom(new EpubIndex(epub.getIndexMap()).getDoc());
        epub.setOpfDom(new EpubOpf(myBook.getExportInfo().getBookname(),
                myBook.getExportInfo().getFirstName(),
                myBook.getExportInfo().getLastName(),
                epub.getElements(), epub.getItems()).getOpf());
        return epub;
    }

    private void init() {

        epub = new EpubObjekt();

        XalanHelper.refTitle = new HashMap<>();
        XalanHelper.setTitle(1);
    }

    public CmlToEpubObject(MyBook myBook, String filepath) {
        init();

        this.myBook = myBook;

//        this.myBook.getSortCmlMap().keySet().forEach((category) -> {
        for (String category : this.myBook.getSortCmlMap().keySet()) {
            EpubLink epubLink;
            if (this.myBook.getExportInfo().getExportType().equals("EPUB3")) {
                epubLink = new EpubLink(category + ".xhtml", category);
            } else {
                epubLink = new EpubLink(category + ".html", category);
            }
            setEpubObjekt(this.myBook.getSortCmlMap().get(category), epubLink, filepath);
            //epub cat uper-lowercase
//            this.myBook.getSortCmlMap().get(category).setType(category);
            epub.getNavMap().put(epub.getIndex() + 1, epubLink);
            epub.setIndex(epub.getIndex() + 1);
            epub.getCookmls().put(category, this.myBook.getSortCmlMap().get(category));
            setEpubOpf(category);
        }

        if (this.myBook.getExportInfo().isHasIndex()) {
            EpubLink epubLink;
            if (this.myBook.getExportInfo().getExportType().equals("EPUB3")) {
                epubLink = new EpubLink("index.xhtml", "Index");
            } else {
                epubLink = new EpubLink("index.html", "Index");
            }
            epub.getNavMap().put(epub.getIndex() + 1, epubLink);
            epub.setIndex(epub.getIndex() + 1);
            epub.setIndexMap(sortIndex(indexTreeMap));
            setEpubOpf("index");
        }

    }

    private void setEpubObjekt(Cookml cookml, EpubLink epubLink, String filepath) {

        for (Object object : cookml.getContent()) {
            if (object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")) {
                Recipe recipe = (Recipe) object;

                Head headakt = recipe.getHead();

//                        setHeadQty(headakt);            // head qty
                setHeadTitle(headakt);          //head title
                setSource(headakt);
                setEpubPic(headakt, filepath);            //Epub Cover & Pic
                EpubLink el = setEpubNav(headakt, epubLink);            //add EpubNav link
                setIndexItem(headakt, el);          //Epub Index Hit
                pageNum++;                      //Epub pagenum 

                recipe.getPart().stream().map((part) -> part.getIngredient()).forEachOrdered((ingredients) -> {
                    ingredients.forEach((ingredient) -> {
                        ingredient.setUnit(FormatHelper.formatUnitDE(ingredient.getUnit()));
                    });
                });

                recipe.getRemark().forEach((remark) -> {
                    List<String> lines = new ArrayList<>();
                    remark.getLine().stream().filter((line) -> (!line.trim().equals("")))
                            .forEachOrdered((line) -> {
                                lines.add(line);
                            });
                    if (lines.size() == 0) {
                        remark.setLine(null);

                    } else {
                        remark.setLine(lines);
                    }
                });
            }
        }

    }

    private void setHeadTitle(Head headakt) {
        String ht = checkRepeatHead(headakt.getTitle());
        headakt.setTitle(ht);
    }

    private void setEpubPic(Head headakt, String filepath) {
        String filename;

        if (this.myBook.getExportInfo().isHasCover()
                && this.myBook.getExportInfo().getCoverPath() != null
                && !this.myBook.getExportInfo().getCoverPath().equals("")) {

            File file = new File(filepath + "EPUB" + File.separator + "images" + File.separator + "cover.jpg");
            InputStream inputStream = null;
            try {
                inputStream = FileUtils.openInputStream(new File(this.myBook.getExportInfo().getCoverPath()));

                if (!this.myBook.getExportInfo().isIsColor()) {
                    FilesUtils.changeImgeColor2BW(inputStream, file, "jpg");
                } else {
//                    FileUtils.copyInputStreamToFile(inputStream, file);
                    FilesUtils.changeImgToJPG(inputStream, file);

                }
            } catch (Exception ex) {
                Logger.getLogger(CmlToEpubObject.class.getName()).log(Level.SEVERE, null, ex);
                errorMessage += "Fehler beim Schreiben CML Bild für Rezept:" + headakt.getTitle() + "\n";
                System.err.println("Fehler beim Schreiben Bild -- EPUB");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();

                    } catch (IOException ex) {
                        Logger.getLogger(CmlToEpubObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        for (Picbin pic : headakt.getPicbin()) {
            filename = "Kochbuch_" + picIndex + "."
                    + pic.getFormat().toLowerCase(Locale.getDefault());
            picIndex++;

            String path = filepath + "EPUB" + File.separator + "images" + File.separator + filename;
            File file = new File(path);
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(pic.getValue());

                if (!this.myBook.getExportInfo().isIsColor()) {
                    FilesUtils.changeImgeColor2BW(inputStream, file, pic.getFormat().toLowerCase(Locale.getDefault()));
                } else {
                    FileUtils.copyInputStreamToFile(inputStream, file);
                }
                Picture p = new Picture();
                p.setFile("images/" + filename);
                headakt.getPicture().add(p);

                //pic-List for epub2
                epub.getPicList().add(path);
                EpubItem item;
                if (pic.getFormat().toUpperCase().equals("PNG")) {
                    item = new EpubItem("images/" + filename, "image/png", false);

                } else {
                    item = new EpubItem("images/" + filename, "image/jpeg", false);

                }
                addItem2OPF(item);

            } catch (Exception ex) {
                Logger.getLogger(CmlToEpubObject.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Fehler beim Schreiben Bild -- EPUB");
                errorMessage += "Fehler beim Schreiben CML Bild für Rezept: " + headakt.getTitle() + "\n";
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(CmlToEpubObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void setIndexItem(Head headakt, EpubLink epubLink) {

        if (headakt.getCat().size() > 1) {
            for (int i = 1; i < headakt.getCat().size(); i++) {
                String hit = headakt.getCat().get(i);
                if (indexTreeMap.keySet().contains(hit)) {
                    indexTreeMap.get(hit).put(epubLink, pageNum);
                } else {
                    TreeMap<EpubLink, Integer> subindex = new TreeMap<>();
                    subindex.put(epubLink, pageNum);
                    indexTreeMap.put(hit, subindex);
                }
            }
        }
        headakt.getHint().forEach((hit) -> {
            if (indexTreeMap.keySet().contains(hit)) {
                indexTreeMap.get(hit).put(epubLink, pageNum);
            } else {
                TreeMap<EpubLink, Integer> subindex = new TreeMap<>();
                subindex.put(epubLink, pageNum);
                indexTreeMap.put(hit, subindex);
            }
        });
    }

    private EpubLink setEpubNav(Head headakt, EpubLink epubLink) {
        XalanHelper.refTitle.put(headakt.getTitle(), "title-" + linkIndex);
        EpubLink subEpubLink = new EpubLink(epubLink.getHref() + "#title-" + linkIndex, headakt.getTitle());
        epubLink.getNavMap().put(linkIndex++, subEpubLink);
        return subEpubLink;
    }

    private void setEpubOpf(String content) {
        EpubItem item = new EpubItem(content + ".xhtml", "application/xhtml+xml", true);
        addItem2OPF(item);
    }

    private String checkRepeatHead(String head) {

        //id contains no " ' "
        if (head.contains("'")) {
            head = head.replaceAll("'", "");
        }

        if (heads.containsKey(head)) {

            Integer i = heads.get(head);
            head = head + i++;
            heads.remove(head);
            heads.put(head, i);

        } else {
            heads.put(head, 1);
        }

        return head;
    }

    private void addItem2OPF(EpubItem item) {

        if (item.isSPine()) {
            epub.getItems().add(item);
        }

        Element e = new Element("item");
        e.setAttribute("id", "id_" + item.getId(), e.getNamespace());
        e.setAttribute("href", item.getHref(), e.getNamespace());
        e.setAttribute("media-type", item.getMediatype(), e.getNamespace());

        if (item.getProperties() != null) {
            e.setAttribute("properties", item.getProperties(), e.getNamespace());
        }

        epub.getElements().add(e);

    }

    private TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> sortIndex(
            TreeMap<String, TreeMap<EpubLink, Integer>> indexMap) {

        TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexSort = new TreeMap<>();

        String index;
        for (String hit : indexMap.keySet()) {
            index = getHitIndex(hit);
            if (indexSort.keySet().contains(index)) {
                indexSort.get(index).put(hit, indexMap.get(hit));
            } else {
                TreeMap<String, TreeMap<EpubLink, Integer>> subindex = new TreeMap<>();
                subindex.put(hit, indexMap.get(hit));
                indexSort.put(index, subindex);
            }
        }
        return indexSort;
    }

    private void setSource(Head headakt) {
        String souceText = "";
        for (String sou : headakt.getSourceline()) {
            if (sou != null && !sou.equals("")) {
                if (!sou.endsWith("; ")) {
                    souceText += sou + "; ";
                } else {
                    souceText += sou;
                }

            }
        }
        if (!souceText.equals("")) {
            List<String> souList = new ArrayList<>();
            souList.add(souceText.substring(0, souceText.length() - 2));
            headakt.setSourceline(souList);
        } else {
            headakt.setSourceline(null);
        }
    }

    private String getHitIndex(String hit) {
        String index;
        switch (hit.toUpperCase().charAt(0)) {
            case 'A':
            case 'Ä':
                index = "A";
                break;
            case 'B':
                index = "B";
                break;
            case 'C':
                index = "C";
                break;
            case 'D':
                index = "D";
                break;
            case 'E':
                index = "E";
                break;
            case 'F':
                index = "F";
                break;
            case 'G':
                index = "G";
                break;
            case 'H':
                index = "H";
                break;
            case 'I':
                index = "I";
                break;
            case 'J':
                index = "J";
                break;
            case 'K':
                index = "K";
                break;
            case 'L':
                index = "L";
                break;
            case 'M':
                index = "M";
                break;
            case 'N':
                index = "N";
                break;
            case 'O':
            case 'Ö':
                index = "O";
                break;
            case 'P':
                index = "P";
                break;
            case 'Q':
                index = "Q";
                break;
            case 'R':
                index = "R";
                break;
            case 'S':
                index = "S";
                break;
            case 'T':
                index = "T";
                break;
            case 'U':
            case 'Ü':
                index = "U";
                break;
            case 'V':
                index = "V";
                break;
            case 'W':
                index = "W";
                break;
            case 'X':
                index = "X";
                break;
            case 'Y':
                index = "Y";
                break;
            case 'Z':
                index = "Z";
                break;
            default:
                index = "#";
                break;
        }

        return index;
    }
}
