package de.hs.inform.lyuz.cookbook;

import de.hs.inform.lyuz.cookbook.gui.CookMainJFrame;
import de.hs.inform.lyuz.cookbook.model.cookml.Ingredient;
import de.hs.inform.lyuz.cookbook.utils.FormatHelper;
import java.math.BigInteger;




public class Application {

    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CookMainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>      

//        Ingredient ingredient = FormatHelper.formatIngredient("1 3/4-2 kg Fleisch");
//        System.out.println(ingredient.getQty() + "," + ingredient.getUnit()+","+ingredient.getItem());
        
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new CookMainJFrame().setVisible(true);
        });
    }
}
