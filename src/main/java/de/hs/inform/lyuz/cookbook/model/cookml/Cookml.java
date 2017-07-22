
package de.hs.inform.lyuz.cookbook.model.cookml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}recipe" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}menu" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="prog" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="progver" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "cookml")
public class Cookml implements Serializable{

    @XmlElementRefs({
        @XmlElementRef(name = "recipe", type = Recipe.class, required = false),
        @XmlElementRef(name = "menu", type = Menu.class, required = false)
    })
    @XmlMixed
    protected List<Object> content;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "prog", required = true)
    protected String prog;
    @XmlAttribute(name = "progver", required = true)
    protected String progver;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recipe }
     * {@link Menu }
     * {@link String }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the prog property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProg() {
        return prog;
    }

    /**
     * Sets the value of the prog property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProg(String value) {
        this.prog = value;
    }

    /**
     * Gets the value of the progver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgver() {
        return progver;
    }

    /**
     * Sets the value of the progver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgver(String value) {
        this.progver = value;
    }
    
    public List<Recipe> getRecipe(){
        List<Recipe> recipes = new ArrayList<>();
        for(Object object: getContent()){
            if(object.getClass().getCanonicalName().equals("de.hs.inform.lyuz.cookbook.model.cookml.Recipe")){
                recipes.add((Recipe)object);
            }
        }
        return recipes;
    }
}
