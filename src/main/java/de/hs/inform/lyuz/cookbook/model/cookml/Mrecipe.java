
package de.hs.inform.lyuz.cookbook.model.cookml;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}desc" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="rid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="servingqty" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="servingtype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="persons" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "desc"
})
@XmlRootElement(name = "mrecipe")
public class Mrecipe implements Serializable{

    protected String desc;
    @XmlAttribute(name = "rid", required = true)
    protected String rid;
    @XmlAttribute(name = "title", required = true)
    protected String title;
    @XmlAttribute(name = "servingqty")
    protected String servingqty;
    @XmlAttribute(name = "servingtype")
    protected String servingtype;
    @XmlAttribute(name = "persons")
    protected Long persons;

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the rid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRid() {
        return rid;
    }

    /**
     * Sets the value of the rid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRid(String value) {
        this.rid = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the servingqty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServingqty() {
        return servingqty;
    }

    /**
     * Sets the value of the servingqty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServingqty(String value) {
        this.servingqty = value;
    }

    /**
     * Gets the value of the servingtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServingtype() {
        return servingtype;
    }

    /**
     * Sets the value of the servingtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServingtype(String value) {
        this.servingtype = value;
    }

    /**
     * Gets the value of the persons property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPersons() {
        return persons;
    }

    /**
     * Sets the value of the persons property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPersons(Long value) {
        this.persons = value;
    }

}
