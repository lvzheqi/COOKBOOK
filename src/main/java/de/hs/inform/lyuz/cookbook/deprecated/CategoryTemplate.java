package de.hs.inform.lyuz.cookbook.deprecated;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class CategoryTemplate {
    
    private static ArrayList<Cat> catTemplate;

    private static ArrayList<Cat> init() {
        ArrayList<Cat> cats = new ArrayList<>();
        cats.add(new Cat("SALAT"));
        cats.add(new Cat("SUPPE"));
        cats.add(new Cat("PASTA"));
        cats.add(new Cat("AUFLAUF"));
        cats.add(new Cat("FISCH"));
        cats.add(new Cat("FLEISCH"));
        cats.add(new Cat("GEFLUEGEL"));
        cats.add(new Cat("GEMUESE"));
        cats.add(new Cat("VEGETARISCH"));
        cats.add(new Cat("BEILAGE"));
        cats.add(new Cat("SAUCE"));
        cats.add(new Cat("NACHTISCH"));
        cats.add(new Cat("DRINK"));
        cats.add(new Cat("SONSTIGES"));
        return cats;
    }

    public static void save(ArrayList<Cat> cats) {
        try {
            FileOutputStream fileOut
                    = new FileOutputStream("category.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cats);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            Logger.getLogger(CategoryTemplate.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void read() {
        try {
            FileInputStream fileIn = new FileInputStream("category.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            catTemplate = (ArrayList<Cat>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            Logger.getLogger(CategoryTemplate.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CategoryTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public static ArrayList<Cat> getCatTemplate() {
        if(catTemplate == null) {
            save(init());
            read();
        }
        return catTemplate;
    }
}

//public enum CategoryTemplate{
//    SALAT("Salate"),
//    SUPPE("Suppen und Eintöpfe"),
//    PASTA("Pasteten und Terrinen"),
//    AUFLAUF("Aufläufe und Gratins"), 
//    FISCH("Fischgerichte"), 
//    FLEISCH("Fleischgerichte"), 
//    GEFLUEGEL("Geflügelgerichte"), 
//    GEMUESE("Kartoffel- und Gemüsegerichte"), 
//    VEGETARISCH("Vegetarisches"), 
//    BEILAGE("Beilagen"),
//    SAUCE("Saucen, Marinaden und Dips"), 
//    NACHTISCH("Süßes"), 
////    BACKEN("Kuchen und Gebäck"),  
//    DRINK("Getränke"), 
//    SONSTIGES("Sonstiges");
//    
//    private CategoryTemplate(String name) {  
//        this.name = name;  
//    }
//    
//    private String name;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    
//    
//}
