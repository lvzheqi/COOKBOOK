//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.06 at 05:42:22 PM CET 
//
package de.hs.inform.lyuz.cookbook.deprecated.cookml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
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
    "recipe",
    "menu"
})
@XmlRootElement(name = "cookml")
public class Cookml implements Serializable{

    protected List<Recipe> recipe;
    protected List<Menu> menu;
    @XmlAttribute(required = true)
    protected String version;
    @XmlAttribute
    protected String name;
    @XmlAttribute(required = true)
    protected String prog;
    @XmlAttribute(required = true)
    protected String progver;
    @XmlAttribute
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the value of the recipe property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the recipe property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecipe().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Recipe }
     *
     *
     */
    public List<Recipe> getRecipe() {
        if (recipe == null) {
            recipe = new ArrayList<Recipe>();
        }
        return this.recipe;
    }

    /**
     * Gets the value of the menu property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the menu property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMenu().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Menu }
     *
     *
     */
    public List<Menu> getMenu() {
        if (menu == null) {
            menu = new ArrayList<Menu>();
        }
        return this.menu;
    }

    /**
     * Gets the value of the version property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the prog property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProg() {
        return prog;
    }

    /**
     * Sets the value of the prog property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setProg(String value) {
        this.prog = value;
    }

    /**
     * Gets the value of the progver property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getProgver() {
        return progver;
    }

    /**
     * Sets the value of the progver property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setProgver(String value) {
        this.progver = value;

    }
}
