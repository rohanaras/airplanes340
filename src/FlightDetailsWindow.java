import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FlightDetailsWindow  {

	/**
	 * 
	 */
	private JFrame jFrame1;
	private JPanel jPanel1;
	private JLabel jLabel4;
	private JScrollPane jScrollPane1;
	private JTextField jTextField7;
	private JTextField jTextField6;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JTextField jTextField5;
	private JLabel jLabel5;
	private JTextField jTextField4;
	private JTextField jTextField3;
	private JLabel jLabel3;
	private JTextField jTextField2;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JLabel jLabel1;
	private JTable jTable1;
	private DefaultTableModel jTable1Model;
	
	public FlightDetailsWindow (Flight theFlight)
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		JFrame frame = getJFrame1();
		jFrame1.setTitle("Flight Details: " + theFlight.FlightNumber);
		jTextField1.setText(theFlight.DepartureAirport.AirportName);
		jTextField2.setText(theFlight.ArrivalAirport.AirportName);
		jTextField3.setText(df.format(theFlight.DepartureTime));
		jTextField4.setText(df.format(theFlight.ArrivalTime));
		jTextField5.setText(Double.toString(theFlight.BasePrice));
		jTextField6.setText(Double.toString(theFlight.CurrentPrice));
		jTextField7.setText(Integer.toString(theFlight.Capacity));
		
		if (theFlight.Reservations!=null)
		{
			for (int i=0;i<theFlight.Reservations.length;i++)
			{
				Reservation r = theFlight.Reservations[i];
				jTable1Model.addRow(new Object[] {r.Passenger.Name, r.Seat, r.MealOptions, r.PricePaid, r.NotesAboutReservation });
			}
		}

		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}
	
	private JFrame getJFrame1() {
		if(jFrame1 == null) {
			jFrame1 = new JFrame();
			jFrame1.getContentPane().add(getJScrollPane1(), BorderLayout.CENTER);
			jFrame1.getContentPane().add(getJPanel1(), BorderLayout.NORTH);
			jFrame1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jFrame1.setSize(new Dimension(600,400));
		}
		return jFrame1;
	}
	
	private JTable getJTable1() {
		if(jTable1 == null) {
			jTable1Model = 
				new DefaultTableModel(
						new String[] { "Passenger", "Seat", "Meal Option", "Price", "Notes" },0);
			jTable1 = new JTable();
			
			jTable1.setModel(jTable1Model);
			jTable1.setPreferredSize(new java.awt.Dimension(602, 100));
		}
		return jTable1;
	}
	
	private JPanel getJPanel1() {
		if(jPanel1 == null) {
			jPanel1 = new JPanel();
			GridLayout jPanel1Layout = new GridLayout(7, 2);
			jPanel1Layout.setColumns(2);
			jPanel1Layout.setHgap(5);
			jPanel1Layout.setVgap(5);
			jPanel1Layout.setRows(7);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1.add(getJLabel1());
			jPanel1.add(getJTextField1());
			jPanel1.add(getJLabel2());
			jPanel1.add(getJTextField2());
			jPanel1.add(getJLabel3());
			jPanel1.add(getJTextField3());
			jPanel1.add(getJLabel4());
			jPanel1.add(getJTextField4());
			jPanel1.add(getJLabel5());
			jPanel1.add(getJTextField5());
			jPanel1.add(getJLabel6());
			jPanel1.add(getJTextField6());
			jPanel1.add(getJLabel7());
			jPanel1.add(getJTextField7());
		}
		return jPanel1;
	}
	
	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Departure Airport:");
		}
		return jLabel1;
	}
	
	private JTextField getJTextField1() {
		if(jTextField1 == null) {
			jTextField1 = new JTextField();
		}
		return jTextField1;
	}
	
	private JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("Arrival Airport:");
		}
		return jLabel2;
	}
	
	private JTextField getJTextField2() {
		if(jTextField2 == null) {
			jTextField2 = new JTextField();
		}
		return jTextField2;
	}
	
	private JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Departure Date/Time:");
			jLabel3.setPreferredSize(new java.awt.Dimension(300, 10));
		}
		return jLabel3;
	}
	
	private JTextField getJTextField3() {
		if(jTextField3 == null) {
			jTextField3 = new JTextField();
		}
		return jTextField3;
	}
	
	private JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new JLabel();
			jLabel4.setText("Arrival Date/Time:");
		}
		return jLabel4;
	}
	
	private JTextField getJTextField4() {
		if(jTextField4 == null) {
			jTextField4 = new JTextField();
		}
		return jTextField4;
	}
	
	private JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new JLabel();
			jLabel5.setText("Base Price:");
		}
		return jLabel5;
	}
	
	private JTextField getJTextField5() {
		if(jTextField5 == null) {
			jTextField5 = new JTextField();
		}
		return jTextField5;
	}
	
	private JLabel getJLabel6() {
		if(jLabel6 == null) {
			jLabel6 = new JLabel();
			jLabel6.setText("Current Price:");
		}
		return jLabel6;
	}
	
	private JTextField getJTextField6() {
		if(jTextField6 == null) {
			jTextField6 = new JTextField();
		}
		return jTextField6;
	}
	
	private JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("Flight Capacity:");
		}
		return jLabel7;
	}
	
	private JTextField getJTextField7() {
		if(jTextField7 == null) {
			jTextField7 = new JTextField();
		}
		return jTextField7;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new java.awt.Dimension(605, 100));
			jScrollPane1.setViewportView(getJTable1());
		}
		return jScrollPane1;
	}

}
