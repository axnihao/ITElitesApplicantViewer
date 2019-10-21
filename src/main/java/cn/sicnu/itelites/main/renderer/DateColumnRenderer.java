package cn.sicnu.itelites.main.renderer;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class DateColumnRenderer extends JLabel implements TableCellRenderer
{
	public DateColumnRenderer()
	{
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFont(this.getFont().deriveFont(Font.PLAIN));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String strValue = sdf.format(value);
		this.setText(strValue);
		
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
