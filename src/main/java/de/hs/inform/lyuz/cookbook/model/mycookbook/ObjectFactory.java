
package de.hs.inform.lyuz.cookbook.model.mycookbook;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mycookbook package. 
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
public class ObjectFactory {

    private final static QName _Li_QNAME = new QName("", "li");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mycookbook
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Cookbook }
     * 
     */
    public Cookbook createCookbook() {
        return new Cookbook();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe }
     * 
     */
    public Cookbook.Recipe createCookbookRecipe() {
        return new Cookbook.Recipe();
    }

    /**
     * Create an instance of {@link Li }
     * 
     */
    public Li createLi() {
        return new Li();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe.Ingredient }
     * 
     */
    public Cookbook.Recipe.Ingredient createCookbookRecipeIngredient() {
        return new Cookbook.Recipe.Ingredient();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe.Recipetext }
     * 
     */
    public Cookbook.Recipe.Recipetext createCookbookRecipeRecipetext() {
        return new Cookbook.Recipe.Recipetext();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe.Nutrition }
     * 
     */
    public Cookbook.Recipe.Nutrition createCookbookRecipeNutrition() {
        return new Cookbook.Recipe.Nutrition();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe.Comments }
     * 
     */
    public Cookbook.Recipe.Comments createCookbookRecipeComments() {
        return new Cookbook.Recipe.Comments();
    }

    
    /**
     * Create an instance of {@link Cookbook.Recipe.Description }
     * 
     */
    public Cookbook.Recipe.Description createCookbookRecipeDescription() {
        return new Cookbook.Recipe.Description();
    }

    /**
     * Create an instance of {@link Cookbook.Recipe.Source }
     * 
     */
    public Cookbook.Recipe.Source createCookbookRecipeSource() {
        return new Cookbook.Recipe.Source();
    }

    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Li }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "li")
    public JAXBElement<Li> createLi(Li value) {
        return new JAXBElement<Li>(_Li_QNAME, Li.class, null, value);
    }

}
