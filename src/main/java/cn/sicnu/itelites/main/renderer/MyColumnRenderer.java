package cn.sicnu.itelites.main.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyColumnRenderer extends JLabel implements TableCellRenderer
{
	public MyColumnRenderer()
	{
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFont(this.getFont().deriveFont(Font.PLAIN));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		this.setText(String.valueOf(value));
		
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
