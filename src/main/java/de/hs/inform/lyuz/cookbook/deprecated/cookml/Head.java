//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.06 at 05:42:22 PM CET 
//
package de.hs.inform.lyuz.cookbook.deprecated.cookml;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element ref="{}cat" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}hint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}sourceline" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="content" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}picture" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}picbin" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="card" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="servingqty" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="servingtype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createdate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="createuser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createemail" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="changedate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="changeuser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="changeemail" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timeallqty" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="timeprepqty" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="timecookqty" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="costs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="wwpoints" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cat",
    "hint",
    "sourceline",
    "content",
    "picture",
    "picbin"
})
@XmlRootElement(name = "head")
public class Head implements Serializable {

    protected List<String> cat;
    protected List<String> hint;
    protected List<String> sourceline;
    protected List<Head.Content> content;
    protected List<Picture> picture;
    protected List<Picbin> picbin;
    @XmlAttribute(required = true)
    protected String title;
    @XmlAttribute(required = true)
    protected String rid;
    @XmlAttribute
    protected String card;
    @XmlAttribute(required = true)
    protected String servingqty;
    @XmlAttribute(required = true)
    protected String servingtype;
    @XmlAttribute
    protected XMLGregorianCalendar createdate;
    @XmlAttribute
    protected String createuser;
    @XmlAttribute
    protected String createemail;
    @XmlAttribute
    protected XMLGregorianCalendar changedate;
    @XmlAttribute
    protected String changeuser;
    @XmlAttribute
    protected String changeemail;
    @XmlAttribute
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger timeallqty;
    @XmlAttribute
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger timeprepqty;
    @XmlAttribute
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger timecookqty;
    @XmlAttribute
    protected String costs;
    @XmlAttribute
    protected Float wwpoints;
    @XmlAttribute(name = "unsignedLong")
    protected BigInteger quality;
    @XmlAttribute(name = "unsignedLong")
    protected BigInteger difficulty;

    /**
     * Gets the value of the cat property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the cat property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCat().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getCat() {
        if (cat == null) {
            cat = new ArrayList<String>();
        }
        return this.cat;
    }

    public void setCat(List<String> cat) {
        this.cat = cat;
    }

    /**
     * Gets the value of the hint property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the hint property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHint().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getHint() {
        if (hint == null) {
            hint = new ArrayList<String>();
        }
        return this.hint;
    }

    /**
     * Gets the value of the sourceline property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the sourceline property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceline().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     */
    public List<String> getSourceline() {
        if (sourceline == null) {
            sourceline = new ArrayList<String>();
        }
        return this.sourceline;
    }

    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the content property.
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
     * {@link Head.Content }
     *
     *
     */
    public List<Head.Content> getContent() {
        if (content == null) {
            content = new ArrayList<Head.Content>();
        }
        return this.content;
    }

    /**
     * Gets the value of the picture property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the picture property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPicture().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Picture }
     *
     *
     */
    public List<Picture> getPicture() {
        if (picture == null) {
            picture = new ArrayList<Picture>();
        }
        return this.picture;
    }

    public void setHint(List<String> hint) {
        this.hint = hint;
    }

    public void setSourceline(List<String> sourceline) {
        this.sourceline = sourceline;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public void setPicture(List<Picture> picture) {
        this.picture = picture;
    }

    public void setPicbin(List<Picbin> picbin) {
        this.picbin = picbin;
    }

    /**
     * Gets the value of the picbin property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the picbin property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPicbin().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Picbin }
     *
     *
     */
    public List<Picbin> getPicbin() {
        if (picbin == null) {
            picbin = new ArrayList<Picbin>();
        }
        return this.picbin;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the rid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRid() {
        return rid;
    }

    /**
     * Sets the value of the rid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRid(String value) {
        this.rid = value;
    }

    /**
     * Gets the value of the card property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCard() {
        return card;
    }

    /**
     * Sets the value of the card property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCard(String value) {
        this.card = value;
    }

    /**
     * Gets the value of the servingqty property.
     *
     */
    public String getServingqty() {
        return servingqty;
    }

    /**
     * Sets the value of the servingqty property.
     *
     */
    public void setServingqty(String value) {
        this.servingqty = value;
    }

    /**
     * Gets the value of the servingtype property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getServingtype() {
        return servingtype;
    }

    /**
     * Sets the value of the servingtype property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setServingtype(String value) {
        this.servingtype = value;
    }

    /**
     * Gets the value of the createdate property.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCreatedate() {
        return createdate;
    }

    /**
     * Sets the value of the createdate property.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setCreatedate(XMLGregorianCalendar value) {
        this.createdate = value;
    }

    /**
     * Gets the value of the createuser property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * Sets the value of the createuser property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCreateuser(String value) {
        this.createuser = value;
    }

    /**
     * Gets the value of the createemail property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCreateemail() {
        return createemail;
    }

    /**
     * Sets the value of the createemail property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCreateemail(String value) {
        this.createemail = value;
    }

    /**
     * Gets the value of the changedate property.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getChangedate() {
        return changedate;
    }

    /**
     * Sets the value of the changedate property.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     *
     */
    public void setChangedate(XMLGregorianCalendar value) {
        this.changedate = value;
    }

    /**
     * Gets the value of the changeuser property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getChangeuser() {
        return changeuser;
    }

    /**
     * Sets the value of the changeuser property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setChangeuser(String value) {
        this.changeuser = value;
    }

    /**
     * Gets the value of the changeemail property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getChangeemail() {
        return changeemail;
    }

    /**
     * Sets the value of the changeemail property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setChangeemail(String value) {
        this.changeemail = value;
    }

    /**
     * Gets the value of the timeallqty property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getTimeallqty() {
        return timeallqty;
    }

    /**
     * Sets the value of the timeallqty property.
     *
     * @param value allowed object is {@link BigInteger }
     *
     */
    public void setTimeallqty(BigInteger value) {
        this.timeallqty = value;
    }

    /**
     * Gets the value of the timeprepqty property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getTimeprepqty() {
        return timeprepqty;
    }

    /**
     * Sets the value of the timeprepqty property.
     *
     * @param value allowed object is {@link BigInteger }
     *
     */
    public void setTimeprepqty(BigInteger value) {
        this.timeprepqty = value;
    }

    /**
     * Gets the value of the timecookqty property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getTimecookqty() {
        return timecookqty;
    }

    /**
     * Sets the value of the timecookqty property.
     *
     * @param value allowed object is {@link BigInteger }
     *
     */
    public void setTimecookqty(BigInteger value) {
        this.timecookqty = value;
    }

    /**
     * Gets the value of the costs property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCosts() {
        return costs;
    }

    /**
     * Sets the value of the costs property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCosts(String value) {
        this.costs = value;
    }

    /**
     * Gets the value of the wwpoints property.
     *
     * @return possible object is {@link Float }
     *
     */
    public Float getWwpoints() {
        return wwpoints;
    }

    /**
     * Sets the value of the wwpoints property.
     *
     * @param value allowed object is {@link Float }
     *
     */
    public void setWwpoints(Float value) {
        this.wwpoints = value;
    }

    public BigInteger getQuality() {
        return quality;
    }

    public void setQuality(BigInteger quality) {
        this.quality = quality;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigInteger difficulty) {
        this.difficulty = difficulty;
    }

    
    /**
     * <p>
     * Java class for anonymous complex type.
     *
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Content implements Serializable {

        @XmlAttribute(required = true)
        protected String type;
        @XmlAttribute(required = true)
        protected String value;

        /**
         * Gets the value of the type property.
         *
         * @return possible object is {@link String }
         *
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the value property.
         *
         * @return possible object is {@link String }
         *
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setValue(String value) {
            this.value = value;
        }
        
        

    }

}
