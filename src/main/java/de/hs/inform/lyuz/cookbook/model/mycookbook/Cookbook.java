package de.hs.inform.lyuz.cookbook.model.mycookbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "recipe"
})
@XmlRootElement(name = "cookbook")
public class Cookbook {

    protected List<Cookbook.Recipe> recipe;
    @XmlAttribute
    protected String version;

    public List<Recipe> getRecipe() {
        if (recipe == null) {
            recipe = new ArrayList<>();
        }
        return recipe;
    }

    public void setRecipe(List<Recipe> recipe) {
        this.recipe = recipe;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {})
    public static class Recipe {

        @XmlElement
        protected String title;
        @XmlElement
        protected String preptime;
        @XmlElement
        protected String cooktime;
        @XmlElement
        protected String totaltime;
        @XmlElement
        protected String url;
        @XmlElement
        protected String video;
        @XmlElement
        protected String imagepath;
        @XmlElement
        protected String imageurl;
        @XmlElement
        protected String quantity;
        @XmlElement
        protected String category;
        @XmlElement
        protected String rating;

        protected Cookbook.Recipe.Ingredient ingredient;
        protected Cookbook.Recipe.Recipetext recipetext;
        protected Cookbook.Recipe.Nutrition nutrition;
        protected Cookbook.Recipe.Comments comments;
        protected Cookbook.Recipe.Description description;
        protected Cookbook.Recipe.Source source;

        public String getTitle() {
            return title;
        }

        public void setTitle(String value) {
            this.title = value;
        }

        public String getPreptime() {
            return preptime;
        }

        public void setPreptime(String value) {
            this.preptime = value;
        }

        public String getCooktime() {
            return cooktime;
        }

        public void setCooktime(String value) {
            this.cooktime = value;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String value) {
            this.url = value;
        }

        public String getImagepath() {
            return imagepath;
        }

        public void setImagepath(String value) {
            this.imagepath = value;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String value) {
            this.imageurl = value;
        }

        public String getTotaltime() {
            return totaltime;
        }

        public void setTotaltime(String totaltime) {
            this.totaltime = totaltime;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String value) {
            this.quantity = value;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String value) {
            this.category = value;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String value) {
            this.rating = value;
        }

        public Cookbook.Recipe.Ingredient getIngredient() {
            return ingredient;
        }

        public void setIngredient(Cookbook.Recipe.Ingredient value) {
            this.ingredient = value;
        }

        public Cookbook.Recipe.Recipetext getRecipetext() {
            return recipetext;
        }

        public void setRecipetext(Cookbook.Recipe.Recipetext value) {
            this.recipetext = value;
        }

        public Cookbook.Recipe.Nutrition getNutrition() {
            return nutrition;
        }

        public void setNutrition(Cookbook.Recipe.Nutrition value) {
            this.nutrition = value;
        }

        public Cookbook.Recipe.Comments getComments() {
            return comments;
        }

        public void setComments(Cookbook.Recipe.Comments value) {
            this.comments = value;
        }

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Comments {

            @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Ingredient extends Li {

            @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Nutrition {

            @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Recipetext {

            @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Source {

           @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class Description {

            @XmlElements(value = {
                @XmlElement(name = "li", type = Li.class)})
            private List<Li> content;

            public List<Li> getContent() {
                return content;
            }

            public void setContent(List<Li> content) {
                this.content = content;
            }
        }

    }

}
