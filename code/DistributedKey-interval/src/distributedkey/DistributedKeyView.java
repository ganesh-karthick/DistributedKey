/*
 * DistributedKeyView.java
 */

package distributedkey;


import java.awt.event.WindowEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * The application's main frame.
 */
public class DistributedKeyView extends FrameView implements WindowListener{
GroupMgmt grpmgmt;
groups grps;
member me;
send s;
receive r;
DatagramSocket ds;
String cmd;
int port;
frmlogin flog;
frmaddressbook faddr;
persistentdata pd;
int key=31101;
Boolean isstandalone;
ftpwindow ftp;
nodecache nc;
    public DistributedKeyView(SingleFrameApplication app) {
        super(app);
        grpmgmt=new GroupMgmt();
        initComponents();
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        // </editor-fold>
    }
public DistributedKeyView(SingleFrameApplication app,member m,frmlogin flog) {
        super(app);
        initComponents();
        try{    
        jbtndummy.setVisible(false);
        jcmboffcontacts.setEnabled(false);
        this.me=m;
        System.out.println(me.getid());
        this.flog=flog;
        isstandalone=true;
        port=me.getport();
        nc=new nodecache(5,3);
        jtxtid.setText(me.getid());
        ds=new DatagramSocket(port);
        s=new send(ds,port,me); 
        s.setstandlone(isstandalone);
        grps=new groups(s,nc,me);
        jcmbgroups.removeAllItems();
        jcmbgroups.setEditable(false);
        pd=intializedata(pd,grps,nc);
        this.getFrame().setTitle("Distributed Messaging Application: "+me.getname());
        Object ogrps[]=grps.getallgroups();
        if(ogrps!=null)
        {
            group gtmp;
            for(int i=0;i<ogrps.length;i++)
            {
             gtmp=(group)ogrps[i];   
             jcmbgroups.addItem(gtmp.getid());
            }
        }      
        grpmgmt=new GroupMgmt(ds,s, r,grps,jcmbgroups,jlstgrpmembers,jlstonmembers,me,isstandalone,nc);
        s.setpd(pd);
        r=new receive(ds,jtxtahist,jlstonmembers,jcmbgroups,grpmgmt.getgroupcontainer(),jlstgrpmembers,grpmgmt.getmembercontainer(),jtxtgrpkey,me,grps,nc,s);      
        ftp=new ftpwindow(true,me,me,s,r.getonlinemembers(),"",0);
        s.setreceive(r);
        faddr=new frmaddressbook(me,pd,grps,s,jcmboffcontacts,nc);
        cmd="HELLO:"+me.getid(); //HELLO ,INTIAL BROADCAST
        s.sendmsg(cmd,false);
        r.start();
       }
       catch(Exception e) 
       {
           e.printStackTrace();
           System.out.println(e.getMessage());
       }      
        
        //<editor-fold defaultstate="collapsed" desc="Generated Code">
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DistributedKeyApp.getApplication().getMainFrame();
            aboutBox = new DistributedKeyAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DistributedKeyApp.getApplication().show(aboutBox);
       // </editor-fold> 
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtxtahist = new javax.swing.JTextArea();
        jtxtid = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtxtmsg = new javax.swing.JTextArea();
        jbtnsend = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jlstonmembers = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jcmbgroups = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jlstgrpmembers = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jrdbtoall = new javax.swing.JRadioButton();
        jrdbselected = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jtxtgrpkey = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jrdbmember = new javax.swing.JRadioButton();
        jrdbgroup = new javax.swing.JRadioButton();
        jrdboffline = new javax.swing.JRadioButton();
        jbtnlogout = new javax.swing.JButton();
        jbtndummy = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jcmboffcontacts = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jmnugrp = new javax.swing.JMenu();
        jmnumanage = new javax.swing.JMenuItem();
        jmnuaddrbook = new javax.swing.JMenu();
        jmnucontacts = new javax.swing.JMenuItem();
        jmnuftp = new javax.swing.JMenu();
        jmnuftpstart = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(distributedkey.DistributedKeyApp.class).getContext().getResourceMap(DistributedKeyView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtxtahist.setBackground(resourceMap.getColor("jtxtahist.background")); // NOI18N
        jtxtahist.setColumns(20);
        jtxtahist.setEditable(false);
        jtxtahist.setFont(resourceMap.getFont("jtxtahist.font")); // NOI18N
        jtxtahist.setForeground(resourceMap.getColor("jtxtahist.foreground")); // NOI18N
        jtxtahist.setLineWrap(true);
        jtxtahist.setRows(5);
        jtxtahist.setWrapStyleWord(true);
        jtxtahist.setEnabled(false);
        jtxtahist.setName("jtxtahist"); // NOI18N
        jScrollPane1.setViewportView(jtxtahist);

        jtxtid.setEditable(false);
        jtxtid.setText(resourceMap.getString("jtxtid.text")); // NOI18N
        jtxtid.setName("jtxtid"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jtxtmsg.setColumns(20);
        jtxtmsg.setRows(5);
        jtxtmsg.setName("jtxtmsg"); // NOI18N
        jtxtmsg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtmsgFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtmsgFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(jtxtmsg);

        jbtnsend.setIcon(resourceMap.getIcon("jbtnsend.icon")); // NOI18N
        jbtnsend.setText(resourceMap.getString("jbtnsend.text")); // NOI18N
        jbtnsend.setName("jbtnsend"); // NOI18N
        jbtnsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnsendActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jlstonmembers.setName("jlstonmembers"); // NOI18N
        jScrollPane5.setViewportView(jlstonmembers);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jcmbgroups.setSelectedItem("");
        jcmbgroups.setName("jcmbgroups"); // NOI18N
        jcmbgroups.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcmbgroupsItemStateChanged(evt);
            }
        });
        jcmbgroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmbgroupsActionPerformed(evt);
            }
        });

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jlstgrpmembers.setName("jlstgrpmembers"); // NOI18N
        jScrollPane3.setViewportView(jlstgrpmembers);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        buttonGroup2.add(jrdbtoall);
        jrdbtoall.setSelected(true);
        jrdbtoall.setText(resourceMap.getString("jrdbtoall.text")); // NOI18N
        jrdbtoall.setName("jrdbtoall"); // NOI18N

        buttonGroup2.add(jrdbselected);
        jrdbselected.setText(resourceMap.getString("jrdbselected.text")); // NOI18N
        jrdbselected.setName("jrdbselected"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jrdbtoall, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jrdbselected, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .add(jrdbtoall)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jrdbselected))
        );

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtxtgrpkey.setText(resourceMap.getString("jtxtgrpkey.text")); // NOI18N
        jtxtgrpkey.setName("jtxtgrpkey"); // NOI18N

        jLabel5.setLabelFor(jtxtgrpkey);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel5.setName("jLabel5"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jcmbgroups, 0, 171, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jtxtgrpkey, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jcmbgroups, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtxtgrpkey, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        buttonGroup1.add(jrdbmember);
        jrdbmember.setSelected(true);
        jrdbmember.setText(resourceMap.getString("jrdbmember.text")); // NOI18N
        jrdbmember.setName("jrdbmember"); // NOI18N

        buttonGroup1.add(jrdbgroup);
        jrdbgroup.setText(resourceMap.getString("jrdbgroup.text")); // NOI18N
        jrdbgroup.setName("jrdbgroup"); // NOI18N

        buttonGroup1.add(jrdboffline);
        jrdboffline.setText(resourceMap.getString("jrdboffline.text")); // NOI18N
        jrdboffline.setName("jrdboffline"); // NOI18N
        jrdboffline.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrdbofflineItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jrdbmember)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jrdbgroup)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jrdboffline)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jrdbmember)
                    .add(jrdbgroup)
                    .add(jrdboffline))
                .add(15, 15, 15))
        );

        jbtnlogout.setText(resourceMap.getString("jbtnlogout.text")); // NOI18N
        jbtnlogout.setName("jbtnlogout"); // NOI18N
        jbtnlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnlogoutActionPerformed(evt);
            }
        });

        jbtndummy.setBackground(resourceMap.getColor("jbtndummy.background")); // NOI18N
        jbtndummy.setForeground(resourceMap.getColor("jbtndummy.foreground")); // NOI18N
        jbtndummy.setIcon(resourceMap.getIcon("jbtndummy.icon")); // NOI18N
        jbtndummy.setText(resourceMap.getString("jbtndummy.text")); // NOI18N
        jbtndummy.setName("jbtndummy"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jcmboffcontacts.setEditable(true);
        jcmboffcontacts.setName("jcmboffcontacts"); // NOI18N

        jButton1.setBackground(resourceMap.getColor("jButton1.background")); // NOI18N
        jButton1.setForeground(resourceMap.getColor("jButton1.foreground")); // NOI18N
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10))
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jtxtid, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jbtndummy, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(146, 146, 146)
                                .add(jbtnlogout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 192, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jcmboffcontacts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel2)
                            .add(mainPanelLayout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 287, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jbtnsend))
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 343, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(jbtndummy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(mainPanelLayout.createSequentialGroup()
                                    .add(9, 9, 9)
                                    .add(jLabel1)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jtxtid, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(18, 18, 18)
                                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(12, 12, 12))
                                .add(mainPanelLayout.createSequentialGroup()
                                    .add(jPanel3, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))))
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(11, 11, 11)
                                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel6)
                                    .add(jcmboffcontacts, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(18, 18, 18)
                                .add(jLabel2)
                                .add(40, 40, 40)
                                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(mainPanelLayout.createSequentialGroup()
                                        .add(191, 191, 191)
                                        .add(jLabel3)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                                        .add(jbtnsend, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(27, 27, 27)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jbtnlogout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(76, 76, 76)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 202, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(34, 34, 34)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(distributedkey.DistributedKeyApp.class).getContext().getActionMap(DistributedKeyView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jmnugrp.setText(resourceMap.getString("jmnugrp.text")); // NOI18N
        jmnugrp.setName("jmnugrp"); // NOI18N

        jmnumanage.setText(resourceMap.getString("jmnumanage.text")); // NOI18N
        jmnumanage.setName("jmnumanage"); // NOI18N
        jmnumanage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnumanageActionPerformed(evt);
            }
        });
        jmnugrp.add(jmnumanage);

        menuBar.add(jmnugrp);

        jmnuaddrbook.setText(resourceMap.getString("jmnuaddrbook.text")); // NOI18N
        jmnuaddrbook.setName("jmnuaddrbook"); // NOI18N

        jmnucontacts.setText(resourceMap.getString("jmnucontacts.text")); // NOI18N
        jmnucontacts.setName("jmnucontacts"); // NOI18N
        jmnucontacts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnucontactsActionPerformed(evt);
            }
        });
        jmnuaddrbook.add(jmnucontacts);

        menuBar.add(jmnuaddrbook);

        jmnuftp.setText(resourceMap.getString("jmnuftp.text")); // NOI18N
        jmnuftp.setName("jmnuftp"); // NOI18N

        jmnuftpstart.setText(resourceMap.getString("jmnuftpstart.text")); // NOI18N
        jmnuftpstart.setName("jmnuftpstart"); // NOI18N
        jmnuftpstart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnuftpstartActionPerformed(evt);
            }
        });
        jmnuftp.add(jmnuftpstart);

        menuBar.add(jmnuftp);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 512, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N
        jMenuBar1.add(jMenu2);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jmnumanageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnumanageActionPerformed
        // TODO add your handling code here:
        grpmgmt.setVisible(true);
    }//GEN-LAST:event_jmnumanageActionPerformed
//OBJECT TO STRING
public String[] getstringitems(Object obj[])  
{
    String items[]=new String[obj.length];
    for(int i=0;i<obj.length;i++)
        items[i]=obj[i].toString();
    return items;
}

public persistentdata intializedata(persistentdata pd,groups grps,nodecache nc)
{
    try{
        String tmp;
        FileInputStream fin=new FileInputStream("C:\\"+me.getfname()+".dat");
        if(fin==null)
            return new persistentdata(me.getid());
        ObjectInputStream oin=new ObjectInputStream(fin);
        pd=(persistentdata)oin.readObject();
        oin.close();
        fin.close();
        Object ogrps[],ombrs[];
        persistentgroup pg;
        member mtmp;
        persistentgroups pgrps=pd.getpgroups();
        if(pgrps==null)
            return new persistentdata(me.getid());
        Object obj[]=pd.getaddresses();
        for(int i=0;i<obj.length;i++)
        {
            tmp=(String)obj[i];
            jcmboffcontacts.addItem(tmp);
        }      
        if(pd.getnodecache()!=null)
            nc=pd.getnodecache();
        for(int i=0;i<pgrps.getgroupcount();i++)
        {
            ogrps=pgrps.getgroups();
            pg=(persistentgroup)ogrps[i];
            group g=new group(pg.getid(),s,nc,new member(pd.getidforme()));
            g.setowner(pg.getowner());
            g.setkey(pg.getkey());
            ombrs=pg.getallmembers();
            for(int j=0;j<ombrs.length;j++)
            {
             mtmp=(member)ombrs[j];
             g.addmember(mtmp);   
            }
            grps.addgroup(g);       
        }
        return pd;
   
    }
     catch(FileNotFoundException e) 
       {
           System.out.println("No Intialisation file found..!!");
       }    
     catch(Exception e) 
       {
           e.printStackTrace();
           System.out.println(e.getMessage());
       }    
    
    return new persistentdata(me.getid());
}
    private void jbtnsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnsendActionPerformed
        // TODO add your handling code here:
        boolean isgroup;
        String toip[],m=jtxtmsg.getText(),gname=null;
        Object obj[];
        
        if(jrdboffline.isSelected())
        {
         String toid=(String)jcmboffcontacts.getSelectedItem();
         if(parser.isvaliduserid(toid,true))
         {
             m=jtxtmsg.getText();
             if(m!=null||!m.equals(""))
             {
                 m="ONMBRMSG:F:"+me.getname()+">"+m;
                 s.sendmsg(m,toid,false);
                 jtxtahist.append("\n"+me.getname()+">"+jtxtmsg.getText());
                 jtxtmsg.setText("");
                 return;
             }
         }
         else
         {
             JOptionPane.showMessageDialog(mainPanel,"Enter proper user id,Invalid user id...!!","Distributed Key",2);
             return;
         }
        }
         
        if(jrdbgroup.isSelected())
        {  
           gname=(String)jcmbgroups.getSelectedItem();
           group g=grps.getgroup(gname);
           if(!g.ismember(me.getid()))
           {
               JOptionPane.showMessageDialog(mainPanel,"Your not a member of the group,Join the group to Message..!!","Distributed Key",2);
               return;
           }
           isgroup=true;
           jtxtahist.append("\nTO:"+gname);
            if(jrdbselected.isSelected())
                obj=jlstgrpmembers.getSelectedValues();
            else
            {
                g=grps.getgroup((String)jcmbgroups.getSelectedItem());
                obj=g.getallmembers();
            }
        }   
        else
        {
             obj=jlstonmembers.getSelectedValues();
             isgroup=false;
        }
        toip=getstringitems(obj);
        if(obj.length==0)
        {
            JOptionPane.showMessageDialog(mainPanel,"No Items Selected","Distributed Key",0);
            return;
        }
        if(m!=null||!m.equals(""))
        {
         toip=getstringitems(obj);
         if(isgroup)  //GRPMSG:groupid:encrypted msg
         {
             group g=grps.getgroup(gname);
             int keyg=g.getkey();
             m="GRPMSG:F:"+gname+":"+new RC4(keyg).encrypt(me.getname()+">"+m);
             s.sendmsg(m,toip,false);
         } 
         else
         {           //ONMBRMSG:encryptedmsg
             m="ONMBRMSG:F:"+me.getname()+">"+m;
             s.sendmsg(m,toip,false);   
         }
         jtxtahist.append("\n"+me.getname()+">"+jtxtmsg.getText());
         jtxtmsg.setText("");
        }
        
    }//GEN-LAST:event_jbtnsendActionPerformed

    private void jcmbgroupsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcmbgroupsItemStateChanged
        // TODO add your handling code here:
        try{
         if(jcmbgroups.getSelectedItem()==null)
            return;
         String temp=(String)jcmbgroups.getSelectedItem();
         jlstgrpmembers.removeAll();
         group g = grps.getgroup(temp);
         jlstgrpmembers.setListData(g.getvector());
         jlstgrpmembers.validate();    
         int keyg=g.getkey(); 
         jtxtgrpkey.setText(String.valueOf(keyg));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }   
    }//GEN-LAST:event_jcmbgroupsItemStateChanged

    private void jbtnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnlogoutActionPerformed
        // TODO add your handling code here:
        try{
         cmd="EXIT:"+me.getid(); //EXIT:id
         s.sendmsg(cmd,false);
         FileOutputStream fout=new FileOutputStream("C:\\"+me.getfname()+".txt"),foutdat=new FileOutputStream("C:\\"+me.getfname()+".dat");
         ObjectOutputStream oos=new ObjectOutputStream(foutdat);
         Object ombrs[],ogrps[]=grps.getallgroups();
         group gtmp;
         member mtmp;
         String mbrs=new String();
         pd=faddr.getpd();
         pd.createnewgroup();
         pd.setofflinemsgs(s.getofflinemsgs());
          for(int i=0;i<ogrps.length;i++)
          {
             gtmp=(group)ogrps[i];
             ombrs=gtmp.getallmembers();
             pd.addgroup(gtmp.getid(),gtmp.getowner().getid(),gtmp.getkey(),ombrs); 
             fout.write(new String("NAME:"+gtmp.getid()+"\n").getBytes());
             fout.write(new String("OWNER:"+gtmp.getowner().getid()).getBytes());
             fout.write(new String("MBRCOUNT:"+String.valueOf(gtmp.getcount())+"\n").getBytes());
             fout.write(new String("MEMBERS:\n").getBytes());
             for(int j=0;j<ombrs.length;j++)
             {
                 mtmp=(member)ombrs[j];
                 if(j==0)
                     mbrs=mtmp.getid();
                 else
                     mbrs+=(":"+mtmp.getid());
             }
            fout.write(mbrs.getBytes());
            fout.write("\n".getBytes());     
          } 
         pd.printaddresses();
         pd.setnodecache(nc);
         fout.flush();
         oos.writeObject(pd);
         oos.flush();
         fout.close();
         oos.close();
         grps.close();
         ds.close();
         Application.getInstance().hide(this);
         flog.setVisible(true);
        }
        catch(Exception ex)
        {
          ex.printStackTrace();  
        }    
    }//GEN-LAST:event_jbtnlogoutActionPerformed

    private void jmnucontactsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnucontactsActionPerformed
        // TODO add your handling code here:
        try{
         faddr.setonlinembrs(r.getonlinemembers());
         faddr.setTitle("Address Book:"+me.getname());
         faddr.setcontacts(pd.getaddresses());
         faddr.setVisible(true);
        }
        catch(Exception e)
        {
         e.printStackTrace();
         System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jmnucontactsActionPerformed

    private void jmnuftpstartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnuftpstartActionPerformed
        // TODO add your handling code here:
        try{
        ftp.setmembers(r.getonlinemembers());
        ftp.setVisible(true);
        }
        catch(Exception e)
        {
         e.printStackTrace();
         System.out.println(e.getMessage());
         ftp.setVisible(false);
         ftp=new ftpwindow(true,me,me,s,r.getonlinemembers(),"",0);
        }
    }//GEN-LAST:event_jmnuftpstartActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
        jbtnlogoutActionPerformed(null);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jtxtmsgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtmsgFocusGained
        // TODO add your handling code here:
        jbtnsend.setEnabled(true);
    }//GEN-LAST:event_jtxtmsgFocusGained

    private void jtxtmsgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtmsgFocusLost
        // TODO add your handling code here:
        if(jtxtmsg.getText().equals("")||jtxtmsg.getText()==null)
         jbtnsend.setEnabled(false);
    }//GEN-LAST:event_jtxtmsgFocusLost

    private void jrdbofflineItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrdbofflineItemStateChanged
        // TODO add your handling code here:
        if(jrdboffline.isSelected())
            jcmboffcontacts.setEnabled(true);
        else
            jcmboffcontacts.setEnabled(false);
       
    }//GEN-LAST:event_jrdbofflineItemStateChanged

    private void jcmbgroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmbgroupsActionPerformed
        // TODO add your handling code here:
        try{
         if(jcmbgroups.getSelectedItem()==null)
            return;
         String temp=(String)jcmbgroups.getSelectedItem();
         jlstgrpmembers.removeAll();
         group g = grps.getgroup(temp);
         jlstgrpmembers.setListData(g.getvector());
         jlstgrpmembers.validate();    
         int keyg=g.getkey(); 
         jtxtgrpkey.setText(String.valueOf(keyg));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }   
        
    }//GEN-LAST:event_jcmbgroupsActionPerformed
public void setmember(member m)
{
    this.me=m;
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jbtndummy;
    private javax.swing.JButton jbtnlogout;
    private javax.swing.JButton jbtnsend;
    private javax.swing.JComboBox jcmbgroups;
    private javax.swing.JComboBox jcmboffcontacts;
    private javax.swing.JList jlstgrpmembers;
    private javax.swing.JList jlstonmembers;
    private javax.swing.JMenu jmnuaddrbook;
    private javax.swing.JMenuItem jmnucontacts;
    private javax.swing.JMenu jmnuftp;
    private javax.swing.JMenuItem jmnuftpstart;
    private javax.swing.JMenu jmnugrp;
    private javax.swing.JMenuItem jmnumanage;
    private javax.swing.JRadioButton jrdbgroup;
    private javax.swing.JRadioButton jrdbmember;
    private javax.swing.JRadioButton jrdboffline;
    private javax.swing.JRadioButton jrdbselected;
    private javax.swing.JRadioButton jrdbtoall;
    private javax.swing.JTextArea jtxtahist;
    private javax.swing.JTextField jtxtgrpkey;
    private javax.swing.JTextField jtxtid;
    private javax.swing.JTextArea jtxtmsg;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        try{ 
        jbtnlogoutActionPerformed(null);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }

    public void windowClosed(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        try{
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        try{
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        try{
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        try{
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void windowDeactivated(WindowEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
        try{
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
