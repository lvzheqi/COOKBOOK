package de.hs.inform.lyuz.cookbook.gui;

import de.hs.inform.lyuz.cookbook.model.MyBook;
import javax.swing.JPanel;

public abstract class MyPanel extends JPanel {

    protected MyBook myBook;
    protected CookMainJFrame cookConvert;

    public MyPanel() {
    }

    public MyPanel(CookMainJFrame parent) {
        this.cookConvert = parent;
        this.myBook = parent.getMyBook();
    }

    public void update(CookMainJFrame parent) {
        this.cookConvert = parent;
        this.myBook = parent.getMyBook();

        reload();
    }

    protected void reload(){
    }
    

    public MyBook getMyBook() {
        return myBook;
    }

    public void setMyBook(MyBook myBook) {
        this.myBook = myBook;
    }

}
