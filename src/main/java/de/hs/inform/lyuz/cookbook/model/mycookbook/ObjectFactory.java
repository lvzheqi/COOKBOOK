package de.hs.inform.lyuz.cookbook.model.mycookbook;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _Li_QNAME = new QName("", "li");

    public ObjectFactory() {
    }

    public Cookbook createCookbook() {
        return new Cookbook();
    }

    public Li createLi() {
        return new Li();
    }

    public Cookbook.Recipe.Ingredient createCookbookRecipeIngredient() {
        return new Cookbook.Recipe.Ingredient();
    }

    public Cookbook.Recipe.Comments createCookbookRecipeComments() {
        return new Cookbook.Recipe.Comments();
    }

    public Cookbook.Recipe.Nutrition createCookbookRecipeNutrition() {
        return new Cookbook.Recipe.Nutrition();
    }

    public Cookbook.Recipe.Recipetext createCookbookRecipeRecipetext() {
        return new Cookbook.Recipe.Recipetext();
    }

    public Cookbook.Recipe createCookbookRecipe() {
        return new Cookbook.Recipe();
    }

    public Cookbook.Recipe.Source createCookbookSource() {
        return new Cookbook.Recipe.Source();
    }

    public Cookbook.Recipe.Description createCookbookDescription() {
        return new Cookbook.Recipe.Description();
    }

    @XmlElementDecl(namespace = "", name = "li")
    public JAXBElement<Li> createLi(Li value) {
        return new JAXBElement<>(_Li_QNAME, Li.class, null, value);
    }

}
