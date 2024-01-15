
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultFormatter;


//@author Ethel Densing
public class MyUtilities extends MyDatabase{
    public final Color Color1 = new Color(255,223,223);
    public final Color Color2 = new Color(174,222,252);
    public final Color Color3 = new Color(248,117,170);
    public final Color Color4 = new Color(174,222,252);

    public Object get_RecordID(javax.swing.JTable tb){
        try{
            return tb.getValueAt(tb.getSelectedRow(), 0);
        }catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }    
    
    public Object[] get_RecordIDs(javax.swing.JTable tb){
        Object[] recordIDs = new Object[tb.getSelectedRowCount()];
        int[] selectedRows = tb.getSelectedRows();

        for (int i = 0; i < selectedRows.length; i++) {
            try {
                recordIDs[i] = tb.getValueAt(selectedRows[i], 0);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                recordIDs[i] = 0; 
            }
        }
        return recordIDs;
    } 
    
    public String getEditedValue1(javax.swing.JTable table){
        int editedRow = table.getEditingRow();
        int editedColumn = table.getEditingColumn();
        
        table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        String editedValue = null;
        
        if (editedRow != -1 && editedColumn != -1) {
            TableCellEditor editor = table.getCellEditor(editedRow, editedColumn);
            if (editor != null) {
                editor.stopCellEditing();
            }
            editedValue = model.getValueAt(editedRow, editedColumn).toString();
        }
        
        return editedValue;
    }
    
    public String getEditedValue2(JTable table) {
        int row = table.getSelectedRow();
        TableCellEditor editor = table.getCellEditor(row, 1);    
        if (editor instanceof DefaultCellEditor) {
            Component editorComponent = ((DefaultCellEditor) editor).getComponent();
            if (editorComponent instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) editorComponent;
                Object selectedItem = comboBox.getSelectedItem();
                if (selectedItem != null) {
                    return selectedItem.toString();
                }
            }
        }
        return ""; 
    }
    
    public boolean has_NoZeroVal(Object[] array){
        for(Object i : array){
            if(i == "0"){
                return false;
            }
        }
        return true;
    }
    
    public void customJLabelHoverClick(JLabel label, JLayeredPane layered, JPanel panel) {
        Font originalFont = label.getFont();
        int orgSize = originalFont.getSize();
        int smallSize = orgSize - 2;
        Font smallerFont = new Font(originalFont.getFontName(), originalFont.getStyle(), smallSize);

        label.addMouseListener(new MouseAdapter() {
            boolean isMousePressed = false;
            
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                label.setFont(smallerFont);
                label.setForeground(Color2);
                layered.removeAll();
                layered.add(panel);
                layered.repaint();
                layered.revalidate(); 
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
                label.setFont(originalFont);
                label.setForeground(Color3);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isMousePressed) {
                    label.setForeground(Color1);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isMousePressed) {
                    label.setForeground(Color3);
                }
            }
        });
    }            

    public Object get_AddedDate(JDateChooser g_date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(g_date.getDate() != null){
            return dateFormat.format(g_date.getDate());
        }
        return java.sql.Date.valueOf(LocalDate.now());
    }    
    
    public String first_LetterUpperCase(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
    
    public String[] getAllValue(JTextField addProduct, JTextField addDescription, JTextField addQuantity, JComboBox addCategory, JTextField addPrice, JDateChooser addDate,Component parentComponent) {
        try {
            String getProduct = first_LetterUpperCase(addProduct.getText().trim());
            String getDescription = first_LetterUpperCase(addDescription.getText().trim());
            String getQuantity = addQuantity.getText().trim();
            String getCategory = addCategory.getSelectedItem().toString();
            String getPrice = addPrice.getText().trim();

            if (getProduct.isEmpty()) {
                JOptionPane.showMessageDialog(parentComponent, "Product name cannot be empty.");
                return null;
            } else if (super.isValueExists(getProduct, super.inventoryColumns[2], super.inventoryTable)) {
                JOptionPane.showMessageDialog(parentComponent, "Product with this name already exists.");
                return null;
            }

            if (getDescription.isEmpty()) {
                JOptionPane.showMessageDialog(parentComponent, "Description cannot be empty.");
                return null;
            }

            if (getQuantity.isEmpty() || Integer.parseInt(getQuantity) < 1) {
                JOptionPane.showMessageDialog(parentComponent, "Quantity must be greater than 0.");
                return null;
            }

            if (getPrice.isEmpty() || Double.parseDouble(getPrice) < 1.0) {
                JOptionPane.showMessageDialog(parentComponent, "Price must be greater than 0.0.");
                return null;
            }

            return new String[]{getCategory, getProduct, getDescription, getQuantity, getPrice, get_AddedDate(addDate).toString()};
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parentComponent, "Invalid number format. Please enter valid numeric values.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentComponent, "Invalid input. Please check your entries.");
        }
        return null;
    }
    
    public static class IntCustomJSpinner extends DefaultCellEditor {

        private JSpinner input;

        public IntCustomJSpinner() {
            super(new JCheckBox());
            input = new JSpinner();
            SpinnerNumberModel numberModel = (SpinnerNumberModel) input.getModel();
            numberModel.setMinimum(1);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) input.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
            formatter.setCommitsOnValidEdit(true);
            editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            editor.getTextField().addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                        e.consume();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            super.getTableCellEditorComponent(table, value, isSelected, row, column);
            int qty = Integer.parseInt(value.toString());
            input.setValue(qty);
            return input;
        }

        @Override
        public Object getCellEditorValue() {
            return input.getValue();
        }
    }
    
    public static class DoubleCustomJSpinner extends DefaultCellEditor {

        private JSpinner input;

        public DoubleCustomJSpinner() {
            super(new JCheckBox());
            input = new JSpinner();
            SpinnerNumberModel numberModel = new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.1); // Allow float values
            input.setModel(numberModel);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) input.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
            formatter.setCommitsOnValidEdit(true);
            editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            editor.getTextField().addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.' && c != '-') {
                        e.consume();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            super.getTableCellEditorComponent(table, value, isSelected, row, column);
            float qty = Float.parseFloat(value.toString());
            input.setValue(qty);
            return input;
        }

        @Override
        public Object getCellEditorValue() {
            return input.getValue();
        }
    } 
    
    public static class DateJTextField extends JTextField {
        public DateJTextField() {
            super();
        }

        @Override
        public void processKeyEvent(KeyEvent e) {
            char c = e.getKeyChar();
            if (Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                super.processKeyEvent(e);
            } else {
                e.consume();
            }
        }

        @Override
        public void setText(String text) {
            if (isValidDateFormat(text)) {
                super.setText(text);
            } else {
            }
        }

        private boolean isValidDateFormat(String text) {
            return text.matches("\\d{4}-\\d{2}-\\d{2}");
        }
    }
}
