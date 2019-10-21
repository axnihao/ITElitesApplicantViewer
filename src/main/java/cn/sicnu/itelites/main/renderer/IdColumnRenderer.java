package cn.sicnu.itelites.main.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;

public class IdColumnRenderer extends JLabel implements TableCellRenderer
{
	public IdColumnRenderer()
	{
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFont(this.getFont().deriveFont(Font.PLAIN));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		this.setText(String.valueOf(row + 1));
		
		// 背景设置
		this.setOpaque(true);
		
	    if (isSelected) {
	    	this.setBackground(table.getSelectionBackground());
	    	this.setForeground(table.getSelectionForeground());
	    	
        } else {
        	this.setBackground(table.getBackground());
        	this.setForeground(table.getForeground());
        }
	
		return this;
	}
	
}
