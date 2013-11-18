package com.numerex.micromed.scdl.views;

import gnu.io.CommPortIdentifier;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.numerex.micromed.scdl.actions.FormActionController;
import com.numerex.micromed.scdl.constants.Constants;
import com.numerex.micromed.scdl.constants.PropertyConstants;
import com.numerex.micromed.scdl.core.MicroMedException;
import com.numerex.micromed.scdl.core.SerialPortController;
import com.numerex.micromed.scdl.core.TextFieldConstraints;
import com.numerex.micromed.scdl.core.UserMessage;
import com.numerex.micromed.scdl.helpers.FormHelper;
import com.numerex.micromed.scdl.helpers.PropertyReader;
import com.numerex.micromed.scdl.helpers.PropertyValue;
import com.numerex.micromed.scdl.helpers.properties.CompoundPropertyValue;
import com.numerex.micromed.scdl.helpers.properties.PropertiesGroup;
import com.numerex.micromed.scdl.helpers.properties.SinglePropertyValue;
import com.numerex.micromed.scdl.img.Img;

public class MicroMedFrame3 extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public JLabel lblPumpID;
	public JLabel lblFlowGain;
	public JLabel lblFlowBalance;
	public JLabel lblFlowNormA;
	public JLabel lblFlowNormB;
	public JLabel lblAPN;
	public JLabel lblServer;
	public JTextField txtPumpID;
	public JTextField txtFlowGain;
	public JTextField txtFlowBalance;
	public JTextField txtFlowNormA;
	public JTextField txtFlowNormB;
	public JComboBox cBoxAPNs;
	public JComboBox cBoxServers;
	public JComboBox cBoxPorts;
	public JButton btnConnect = new JButton();
	public JButton btnLoad;
	public JButton btnReset;
	public JButton btnSetup;
	public JButton btnDefault;
	public JButton btnSave;
	public JButton btnLoadFirmware;

	private Map<String, CommPortIdentifier> portMap = new HashMap<String, CommPortIdentifier>();

	private static MicroMedFrame3 instance = null;
	private SerialPortController serialPortController = SerialPortController
			.getInstance();
	private JPanel panel_3;
	public JLabel lblMessageGain;
	public JLabel lblMessageBalance;
	public JLabel lblMessageNormA;
	public JLabel lblMessageNormB;
	public JLabel lblMessageLVAD;
	public JLabel spinner;
	public JLabel lblMessageFirmware;
	public JTextArea log;

	private JScrollPane scrollPane;
	private final Action action = new SwingAction();

	private JLabel label_2;

	private MicroMedFrame3() {

		// Set layout
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());

			Image im = Toolkit.getDefaultToolkit().getImage(
					Img.class.getResource("network_transmit.png"));
			this.setIconImage(im);

			SinglePropertyValue titleProperty = PropertyReader
					.getInstance().getSingleProperty(PropertyConstants.APPLICATION_TITLE);
			this.setTitle(titleProperty.getValue());

			this.setResizable(false);
			initComponents();
			loadInformation();
			setLocationRelativeTo(null);

			// Prompts user to insert SIM card
			JOptionPane
					.showMessageDialog(
							null,
							"Insert SIM Card here, as shown in the image.\nWhen done, click OK to continue",
							"Welcome to the SIM Card Data Loading Program",
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
									Img.class.getResource("devkit.jpg")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static MicroMedFrame3 getFrame() {
		if (instance == null) {
			instance = new MicroMedFrame3();
		}
		return instance;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MicroMedFrame3 frame = MicroMedFrame3.getFrame();
					FormHelper.toggleControls(false);
					// Setting display mode. If no ports was loaded the
					// application displays configuration mode
					FormHelper.setConfigButtonsDisplay();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel();
		// label.setText("MicroMed SIM Card Data Loader");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(259, 24, 223, 17);
		contentPane.add(label);

		panel_3 = new JPanel();

		panel_3.setBorder(BorderFactory.createRaisedBevelBorder());
		panel_3.setBounds(0, 205, 894, 215);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(20, 30, 864, 130);
		panel_3.add(panel);
		panel.setLayout(null);

		lblAPN = new JLabel();
		lblAPN.setBounds(0, 14, 170, 14);

		lblAPN.setText("Select a region: ");
		// lblAPN.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblAPN.setEnabled(false);
		panel.add(lblAPN);

		lblServer = new JLabel();
		lblServer.setBounds(0, 33, 208, 14);
		lblServer.setText("Select a Server: ");
		// lblServer.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblServer.setEnabled(false);

		// panel.add(lblServer);

		lblPumpID = new JLabel();
		lblPumpID.setBounds(418, 14, 112, 14);
		panel.add(lblPumpID);
		lblPumpID.setText("LVAD Pump ID:");
		// lblPumpID.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblPumpID.setEnabled(false);

		lblFlowGain = new JLabel();
		lblFlowGain.setBounds(0, 53, 156, 14);
		panel.add(lblFlowGain);
		lblFlowGain.setText("Flow Probe Characterization Gain:");
		// lblFlowGain.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblFlowGain.setEnabled(false);

		lblFlowBalance = new JLabel();
		lblFlowBalance.setBounds(418, 53, 156, 14);
		panel.add(lblFlowBalance);
		lblFlowBalance.setText("Flow Probe Characterization Balance:");
		// lblFlowBalance.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblFlowBalance.setEnabled(false);

		lblFlowNormA = new JLabel();
		lblFlowNormA.setBounds(0, 93, 170, 14);
		panel.add(lblFlowNormA);
		lblFlowNormA.setText("Flow Probe Characterization Norm A:");
		// lblFlowNormA.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblFlowNormA.setEnabled(false);

		lblFlowNormB = new JLabel();
		lblFlowNormB.setBounds(418, 93, 149, 14);
		panel.add(lblFlowNormB);
		lblFlowNormB.setText("Flow Probe Characterization Norm B:");
		// lblFlowNormB.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 11));
		lblFlowNormB.setEnabled(false);

		cBoxAPNs = new JComboBox();
		cBoxAPNs.setBounds(163, 11, 135, 20);
		panel.add(cBoxAPNs);

		cBoxServers = new JComboBox();
		cBoxServers.setBounds(212, 30, 156, 20);

		// panel.add(cBoxServers);

		txtPumpID = new JTextField();
		txtPumpID.setBounds(584, 10, 135, 20);
		txtPumpID.setDocument(new TextFieldConstraints(18,
				TextFieldConstraints.ALPHA_NUMERIC));
		panel.add(txtPumpID);
		txtPumpID.setEnabled(false);

		txtFlowGain = new JTextField();
		txtFlowGain.setBounds(163, 50, 135, 20);
		txtFlowGain.setDocument(new TextFieldConstraints(3,
				TextFieldConstraints.NUMERIC));
		panel.add(txtFlowGain);
		txtFlowGain.setEnabled(false);

		txtFlowBalance = new JTextField();
		txtFlowBalance.setBounds(584, 50, 135, 20);
		txtFlowBalance.setDocument(new TextFieldConstraints(3,
				TextFieldConstraints.NUMERIC));
		panel.add(txtFlowBalance);
		txtFlowBalance.setEnabled(false);

		txtFlowNormA = new JTextField();
		txtFlowNormA.setBounds(163, 90, 135, 20);
		txtFlowNormA.setDocument(new TextFieldConstraints(3,
				TextFieldConstraints.NUMERIC));
		panel.add(txtFlowNormA);
		txtFlowNormA.setEnabled(false);

		txtFlowNormB = new JTextField();
		txtFlowNormB.setBounds(584, 90, 135, 20);
		txtFlowNormB.setDocument(new TextFieldConstraints(3,
				TextFieldConstraints.NUMERIC));
		panel.add(txtFlowNormB);
		txtFlowNormB.setEnabled(false);

		lblMessageGain = new JLabel("(0-255)");
		lblMessageGain.setBounds(308, 53, 46, 14);
		lblMessageGain.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMessageGain.setEnabled(false);
		panel.add(lblMessageGain);

		lblMessageBalance = new JLabel("(0-255)");
		lblMessageBalance.setBounds(729, 53, 46, 14);
		lblMessageBalance.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMessageBalance.setEnabled(false);
		panel.add(lblMessageBalance);

		lblMessageNormA = new JLabel("(0-255)");
		lblMessageNormA.setBounds(308, 93, 46, 14);
		lblMessageNormA.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMessageNormA.setEnabled(false);
		panel.add(lblMessageNormA);

		lblMessageNormB = new JLabel("(0-255)");
		lblMessageNormB.setBounds(729, 93, 46, 14);
		lblMessageNormB.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMessageNormB.setEnabled(false);
		panel.add(lblMessageNormB);

		lblMessageLVAD = new JLabel("(a-z,A-Z,0-9, - , _  , * )");
		lblMessageLVAD.setBounds(729, 14, 118, 14);
		lblMessageLVAD.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblMessageLVAD.setEnabled(false);
		panel.add(lblMessageLVAD);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(20, 162, 864, 36);
		panel_3.add(panel_4);
		panel_4.setLayout(null);

		btnLoad = new JButton();
		btnLoad.setBounds(664, 11, 90, 23);
		panel_4.add(btnLoad);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread r = new Thread() {
					public void run() {
						FormActionController.dataDownloadAction(null);
					}
				};
				spinner.setVisible(true);
				btnLoad.setEnabled(false);
				btnLoadFirmware.setEnabled(false);
				btnConnect.setEnabled(false);
				btnReset.setEnabled(false);
				FormHelper.toggleControls(false);
				r.start();
			}
		});

		btnLoad.setText("Load");
		btnLoad.setEnabled(false);

		btnReset = new JButton();
		btnReset.setBounds(764, 11, 90, 23);
		panel_4.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FormActionController.resetAction(arg0);
			}
		});
		btnReset.setText("Reset");
		btnReset.setEnabled(false);

		btnSave = new JButton();
		btnSave.setText("Save");
		btnSave.setEnabled(false);
		btnSave.setBounds(464, 11, 90, 23);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread r = new Thread() {
					public void run() {
						FormActionController.saveDefaultFields(null);
					}
				};
				spinner.setVisible(true);
				r.start();
			}
		});
		panel_4.add(btnSave);

		btnDefault = new JButton();
		btnDefault.setText("Default");
		btnDefault.setEnabled(false);
		btnDefault.setBounds(564, 11, 90, 23);
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread r = new Thread() {
					public void run() {
						FormActionController.getDefaultFields(null);
					}
				};
				spinner.setVisible(true);
				r.start();
			}
		});
		panel_4.add(btnDefault);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Img.class.getResource("header.png")));
		lblNewLabel.setBounds(0, 0, 894, 91);
		contentPane.add(lblNewLabel);

		JLayeredPane panel_2 = new JLayeredPane();
		panel_2.setBounds(0, 102, 894, 66);
		contentPane.add(panel_2);
		panel_2.setBorder(new TitledBorder(BorderFactory
				.createRaisedBevelBorder(), "Serial port selection",
				TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Arial", 1, 12), null));
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(21, 20, 863, 39);
		panel_2.add(panel_1);
		panel_1.setLayout(null);

		JLabel label_1 = new JLabel();
		label_1.setBounds(108, 12, 182, 14);
		panel_1.add(label_1);
		label_1.setText("Verify communication port: ");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));

		cBoxPorts = new JComboBox();
		cBoxPorts.setBounds(310, 9, 241, 20);
		panel_1.add(cBoxPorts);

		btnConnect = new JButton();
		btnConnect.setBounds(597, 8, 90, 23);
		panel_1.add(btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread r = new Thread() {
					public void run() {
						try {
							FormActionController.connectAction(false);
						} catch (MicroMedException e) {
							UserMessage userMessage = new UserMessage(e);
							FormHelper.showMessage2User(userMessage);
						}
					}
				};
				spinner.setVisible(true);
				btnConnect.setEnabled(false);
				FormHelper.toggleControls(false);
				r.start();
			}
		});
		btnConnect.setText(Constants.Front.CONNECT_LABEL);

		spinner = new JLabel("");
		spinner.setIcon(new ImageIcon(Img.class.getResource("spinner.gif")));
		spinner.setVisible(false);
		spinner.setBounds(597, 9, 32, 19);
		panel_1.add(spinner);

		btnSetup = new JButton();
		btnSetup.setAction(action);
		btnSetup.setText("Set up");
		btnSetup.setBounds(700, 8, 90, 23);
		btnSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					FormActionController.setupClickBUtton();
				} catch (MicroMedException e) {
					UserMessage userMessage = new UserMessage(e);
					FormHelper.showMessage2User(userMessage);
				}
			}
		});
		panel_1.add(btnSetup);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Log information",
				TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Arial", 1, 12), null));
		panel_5.setLayout(null);
		panel_5.setBounds(0, 545, 894, 130);
		contentPane.add(panel_5);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 24, 874, 95);
		panel_5.add(scrollPane);

		log = new JTextArea();
		scrollPane.setViewportView(log);
		log.setFont(new Font("Arial", Font.PLAIN, 10));
		log.setRows(5);
		log.setEditable(false);
		log.setColumns(20);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(BorderFactory.createRaisedBevelBorder());

		panel_6.setBounds(0, 460, 894, 66);
		panel_6.setLayout(null);
		contentPane.add(panel_6);

		btnLoadFirmware = new JButton();
		btnLoadFirmware.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread r = new Thread() {
					public void run() {
						FormActionController.loadFirmwareAction(null);
					}
				};
				spinner.setVisible(true);
				btnLoad.setEnabled(false);
				btnLoadFirmware.setEnabled(false);
				btnConnect.setEnabled(false);
				btnReset.setEnabled(false);
				FormHelper.toggleControls(false);
				r.start();
			}
		});
		btnLoadFirmware.setText("Load firmware");
		btnLoadFirmware.setEnabled(false);
		btnLoadFirmware.setBounds(550, 27, 187, 23);
		panel_6.add(btnLoadFirmware);

		lblMessageFirmware = new JLabel();
		lblMessageFirmware
				.setText("Please click Load Firmware to load firmware into the Remote Monitoring Module");
		lblMessageFirmware.setEnabled(false);
		lblMessageFirmware.setBounds(162, 31, 357, 14);
		panel_6.add(lblMessageFirmware);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Img.class
				.getResource("commandsheader.png")));
		lblNewLabel_1.setBounds(0, 174, 894, 31);
		contentPane.add(lblNewLabel_1);

		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(Img.class
				.getResource("loaderheader.png")));
		label_2.setBounds(0, 431, 894, 31);
		contentPane.add(label_2);

		scrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						try {
							log.select(log.getHeight() + 1000, 0);
						} catch (Exception e2) {
						}
					}
				});

	}

	/**
	 * This method is called from within the constructor to load the information
	 * of the form.
	 */

	public void loadInformation() {
		// Load ports
		loadPorts();
	}

	public void loadPorts() {

		// Load ports
		if (portMap.size() == 0) {
			List<CommPortIdentifier> lstPorts = serialPortController
					.searchPorts();
			for (CommPortIdentifier commPortIdentifier : lstPorts) {
				cBoxPorts.addItem(commPortIdentifier.getName());
				portMap.put(commPortIdentifier.getName(), commPortIdentifier);
			}

		}

	}

	public void loadAPNs(CompoundPropertyValue selectedValue) {

		cBoxAPNs.removeAllItems();

		// Load APNs
		PropertiesGroup apnConfigurations = PropertyReader
				.getInstance().getPropertiesGroup(PropertyConstants.APN_OPTIONS);

		boolean found = false; // Tracks if the selected value is present in the configured APNs list
		
		for (PropertyValue value : apnConfigurations.getPropertyValues()) {
			cBoxAPNs.addItem(value);
			
			if (value.equals(selectedValue)) {
				found = true;
				cBoxAPNs.setSelectedItem(value);
			}
		}
		
  		if (!found) {
			cBoxAPNs.addItem(selectedValue);
			cBoxAPNs.setSelectedItem(selectedValue);
		}
	}

	public boolean icConnectAction() {
		return this.btnConnect.getText().equals(Constants.Front.CONNECT_LABEL);
	}

	public String getSelectedPortName() {
		return (String) this.cBoxPorts.getSelectedItem();
	}

	public void log(String info) {
		this.log.setText(info);
	}

	public boolean icConfigMode() {
		return this.portMap.size() == 0;
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = -1464359191122873403L;

		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}
