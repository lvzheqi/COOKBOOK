
package de.hs.inform.lyuz.cookbook.model.cookml;

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
 *         &lt;element name="inote" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="qty" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="item" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bls" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gram" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="shop" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="calc" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ridlink" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="preparation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "inote"
})
@XmlRootElement(name = "ingredient")
public class Ingredient implements Serializable{

    protected List<String> inote;
    @XmlAttribute(name = "qty")
    protected Float qty;
    @XmlAttribute(name = "unit")
    protected String unit;
    @XmlAttribute(name = "item", required = true)
    protected String item;
    @XmlAttribute(name = "bls")
    protected String bls;
    @XmlAttribute(name = "gram")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger gram;
    @XmlAttribute(name = "shop")
    protected Boolean shop;
    @XmlAttribute(name = "calc")
    protected Boolean calc;
    @XmlAttribute(name = "ridlink")
    protected String ridlink;
    @XmlAttribute(name = "preparation")
    protected String preparation;

    /**
     * Gets the value of the inote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInote() {
        if (inote == null) {
            inote = new ArrayList<String>();
        }
        return this.inote;
    }

    /**
     * Gets the value of the qty property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getQty() {
        return qty;
    }

    /**
     * Sets the value of the qty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setQty(Float value) {
        this.qty = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the bls property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBls() {
        return bls;
    }

    /**
     * Sets the value of the bls property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBls(String value) {
        this.bls = value;
    }

    /**
     * Gets the value of the gram property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGram() {
        return gram;
    }

    /**
     * Sets the value of the gram property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGram(BigInteger value) {
        this.gram = value;
    }

    /**
     * Gets the value of the shop property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShop() {
        if (shop == null) {
            return true;
        } else {
            return shop;
        }
    }

    /**
     * Sets the value of the shop property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShop(Boolean value) {
        this.shop = value;
    }

    /**
     * Gets the value of the calc property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCalc() {
        return calc;
    }

    /**
     * Sets the value of the calc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCalc(Boolean value) {
        this.calc = value;
    }

    /**
     * Gets the value of the ridlink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRidlink() {
        return ridlink;
    }

    /**
     * Sets the value of the ridlink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRidlink(String value) {
        this.ridlink = value;
    }

    /**
     * Gets the value of the preparation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreparation() {
        return preparation;
    }

    /**
     * Sets the value of the preparation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreparation(String value) {
        this.preparation = value;
    }

}
