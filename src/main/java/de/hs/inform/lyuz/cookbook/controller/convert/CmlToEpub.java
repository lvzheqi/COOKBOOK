package de.hs.inform.lyuz.cookbook.controller.convert;

import de.hs.inform.lyuz.cookbook.model.epub.EpubItem;
import de.hs.inform.lyuz.cookbook.model.epub.EpubLink;
import de.hs.inform.lyuz.cookbook.model.epub.EpubOpf;
import de.hs.inform.lyuz.cookbook.help.FormatHelper;
import de.hs.inform.lyuz.cookbook.help.Utils;
import java.io.File;
import java.util.*;
import de.hs.inform.lyuz.cookbook.help.ExportInfo;
import de.hs.inform.lyuz.cookbook.help.XalanHelper;
import de.hs.inform.lyuz.cookbook.model.cookml.*;
import de.hs.inform.lyuz.cookbook.model.epub.EpubObjekt;
import de.hs.inform.lyuz.cookbook.model.epub.EpubIndex;
import de.hs.inform.lyuz.cookbook.model.epub.EpubNav;
import org.jdom2.Element;

public class CmlToEpub {

    private final HashMap<String, Integer> heads = new HashMap<>();
    private final TreeMap<String, TreeMap<EpubLink, Integer>> indexTreeMap = new TreeMap<>();

    private int linkIndex = 1;
    private int picIndex = 1;
    private int pageNum = 1;

    private EpubObjekt epub;

    public EpubObjekt getEpub() {
        epub.setNavDom(new EpubNav(ExportInfo.title, epub.navMap).getDoc());
        epub.setIndexDom(new EpubIndex(epub.indexMap).getDoc());
        epub.setOpfDom(new EpubOpf(epub.elements, epub.items).getOpf());
        return epub;
    }

    private void init() {
        epub = new EpubObjekt();

        XalanHelper.refTitle = new HashMap<>();
        XalanHelper.setTitle(1);
    }

    public CmlToEpub(LinkedHashMap<String, Cookml> sortCmlMap, String filepath, ArrayList<String> catList) {

        init();

        //olny show the rezept in category 
        sortCmlMap.keySet().forEach((category) -> {
            EpubLink epubLink;
            if (ExportInfo.exportTyp.equals("EPUB3")) {
                epubLink = new EpubLink(category + ".xhtml", category);
            } else {
                epubLink = new EpubLink(category + ".html", category);
            }

            Cookml cookml = setEpubObjekt(sortCmlMap.get(category), epubLink, filepath);
            cookml.setType(category);
            epub.navMap.put(epub.index++, epubLink);
            epub.cookmls.put(category, cookml);
            setEpubOpf(category);
        });

        if (ExportInfo.hasIndex) {
            EpubLink epubLink = new EpubLink("index.xhtml", "Index");
            epub.navMap.put(epub.index++, epubLink);
            epub.indexMap = sortIndex(indexTreeMap);
            setEpubOpf("index");
        }

    }

    private Cookml setEpubObjekt(Cookml cookml, EpubLink epubLink, String filepath) {

        for (Recipe recipe : cookml.getRecipe()) {
            for (Object objakt : recipe.getHeadAndCustomAndPart()) {
                switch (objakt.getClass().getCanonicalName()) {
                    case "de.hs.inform.lyuz.cookbook.model.cookml.Head":
                        Head headakt = (Head) objakt;

                        setHeadQty(headakt);            // head qty
                        setHeadTitle(headakt);          //head title
                        setEpubPic(headakt, filepath);            //Epub Cover & Pic
                        EpubLink el = setEpubNav(headakt, epubLink);            //add EpubNav link
                        setIndexItem(headakt, el);          //Epub Index Hit
                        pageNum++;                      //Epub pagenum 
                        break;

                    case "de.hs.inform.lyuz.cookbook.model.cookml.Recipe.Part":
                        List<Ingredient> ingredients = ((Recipe.Part) objakt).getIngredient();
                        ingredients.forEach((ingredient) -> {
                            ingredient.setUnit(FormatHelper.formatUnit(ingredient.getUnit()));
                        });
                        break;

                    case "de.hs.inform.lyuz.cookbook.model.cookml.Remark":
                        List<String> lines = new ArrayList<>();
                        ((Remark) objakt).getLine().stream().filter((line) -> (!line.equals("")))
                                .forEachOrdered((line) -> {
                                    lines.add(line);
                                });
                        if (lines.size() == 0) {
                            ((Remark) objakt).setLine(null);
                        } else {
                            ((Remark) objakt).setLine(lines);
                        }
                        break;

                    default:
                        break;

                }
            }

        }
        return cookml;
    }

    private void setHeadQty(Head headakt) {
        if (headakt.getServingqty() != null) {
            headakt.setServingqty(FormatHelper.formatQTY(Float.valueOf(headakt.getServingqty())));
        }
    }

    private void setHeadTitle(Head headakt) {
        String ht = checkRepeatHead(headakt.getTitle());
        headakt.setTitle(ht);
    }

    private void setEpubPic(Head headakt, String filepath) {
        String filename;

        if (ExportInfo.hasCover && ExportInfo.coverPath != null && !ExportInfo.coverPath.equals("")) {
            Utils.copyFile(ExportInfo.coverPath, filepath + "EPUB"+File.separator+"images"+File.separator+"cover.jpg");
            if (!ExportInfo.isColor) {
                Utils.changeImgeColor2BW(new File(filepath + "EPUB"+File.separator+"images"+File.separator+"cover.jpg"));
            }
        }
        if (ExportInfo.hasPic) {
            for (Picbin pic : headakt.getPicbin()) {
                filename = "Kochbuch_" + picIndex + "."
                        + pic.getFormat().toLowerCase(Locale.getDefault());
                picIndex++;
                Utils.writePictoFile(pic.getValue(), filepath + "EPUB"+File.separator+"images"+File.separator + filename);

                //color to blackwhite
                if (!ExportInfo.isColor) {
                    Utils.changeImgeColor2BW(new File(filepath + "EPUB"+File.separator+"images"+File.separator + filename));
                }
                Picture p = new Picture();
                p.setFile("images"+File.separator + filename);
                headakt.getPicture().add(p);

                //pic-List for epub2
                epub.getPicList().add(filepath + "EPUB"+File.separator+"images"+File.separator+ filename);

                EpubItem item = new EpubItem("images"+File.separator + filename, "image/"+ pic.getFormat(), false);
                addItem2OPF(item);
            }
        }
    }

    private void setIndexItem(Head headakt, EpubLink epubLink) {
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
            epub.items.add(item);
        }

        Element e = new Element("item");
        e.setAttribute("id", "id_" + item.getId(), e.getNamespace());
        e.setAttribute("href", item.getHref(), e.getNamespace());
        e.setAttribute("media-type", item.getMediatype(), e.getNamespace());

        if (item.getProperties() != null) {
            e.setAttribute("properties", item.getProperties(), e.getNamespace());
        }

        epub.elements.add(e);

    }

    private TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> sortIndex(
            TreeMap<String, TreeMap<EpubLink, Integer>> indexMap) {

        TreeMap<String, TreeMap<String, TreeMap<EpubLink, Integer>>> indexSort = new TreeMap<>();

        String index;
        for (String hit : indexMap.keySet()) {
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
}
