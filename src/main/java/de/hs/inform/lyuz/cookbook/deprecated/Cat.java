package de.hs.inform.lyuz.cookbook.deprecated;

@Deprecated
public class Cat implements java.io.Serializable {
     public String name;

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     
     
}
