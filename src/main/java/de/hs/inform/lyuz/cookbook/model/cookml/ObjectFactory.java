
package de.hs.inform.lyuz.cookbook.model.cookml;

import java.io.Serializable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cookml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory implements Serializable{

    private final static QName _Sourceline_QNAME = new QName("", "sourceline");
    private final static QName _Cat_QNAME = new QName("", "cat");
    private final static QName _Hint_QNAME = new QName("", "hint");
    private final static QName _Text_QNAME = new QName("", "text");
    private final static QName _Card_QNAME = new QName("", "card");
    private final static QName _Desc_QNAME = new QName("", "desc");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cookml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Head }
     * 
     */
    public Head createHead() {
        return new Head();
    }

    /**
     * Create an instance of {@link Recipe }
     * 
     */
    public Recipe createRecipe() {
        return new Recipe();
    }

    /**
     * Create an instance of {@link Mcustom }
     * 
     */
    public Mcustom createMcustom() {
        return new Mcustom();
    }

    /**
     * Create an instance of {@link Ingredient }
     * 
     */
    public Ingredient createIngredient() {
        return new Ingredient();
    }

    /**
     * Create an instance of {@link Cookml }
     * 
     */
    public Cookml createCookml() {
        return new Cookml();
    }

    /**
     * Create an instance of {@link Head.Content }
     * 
     */
    public Head.Content createHeadContent() {
        return new Head.Content();
    }

    /**
     * Create an instance of {@link Picture }
     * 
     */
    public Picture createPicture() {
        return new Picture();
    }

    /**
     * Create an instance of {@link Picbin }
     * 
     */
    public Picbin createPicbin() {
        return new Picbin();
    }

    /**
     * Create an instance of {@link Custom }
     * 
     */
    public Custom createCustom() {
        return new Custom();
    }

    /**
     * Create an instance of {@link Recipe.Part }
     * 
     */
    public Recipe.Part createRecipePart() {
        return new Recipe.Part();
    }

    /**
     * Create an instance of {@link Preparation }
     * 
     */
    public Preparation createPreparation() {
        return new Preparation();
    }

    /**
     * Create an instance of {@link Step }
     * 
     */
    public Step createStep() {
        return new Step();
    }

    /**
     * Create an instance of {@link Remark }
     * 
     */
    public Remark createRemark() {
        return new Remark();
    }

    /**
     * Create an instance of {@link Menu }
     * 
     */
    public Menu createMenu() {
        return new Menu();
    }

    /**
     * Create an instance of {@link Mrecipe }
     * 
     */
    public Mrecipe createMrecipe() {
        return new Mrecipe();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sourceline")
    public JAXBElement<String> createSourceline(String value) {
        return new JAXBElement<String>(_Sourceline_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cat")
    public JAXBElement<String> createCat(String value) {
        return new JAXBElement<String>(_Cat_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "hint")
    public JAXBElement<String> createHint(String value) {
        return new JAXBElement<String>(_Hint_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "text")
    public JAXBElement<String> createText(String value) {
        return new JAXBElement<String>(_Text_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "card")
    public JAXBElement<String> createCard(String value) {
        return new JAXBElement<String>(_Card_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "desc")
    public JAXBElement<String> createDesc(String value) {
        return new JAXBElement<String>(_Desc_QNAME, String.class, null, value);
    }

}
