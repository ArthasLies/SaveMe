package crossydupe;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;

public class DeathPointMenu extends JDialog {
    private DoublyLinkedList list;
    private GamePanel gamePanel;
    private JTextField inputField;
    private JButton prevButton, nextButton, selectButton, deleteButton, spawnButton, exitButton;
    private JLabel infoLabel;
    private Node currentNode;
    private JLabel nodeDataLabel;

    public DeathPointMenu(Frame parent, DoublyLinkedList list, GamePanel gamePanel) {
        super(parent, "Select Death Point", true);
        this.list = list;
        this.gamePanel = gamePanel;

        initUI();
    }

    private void initUI() {
        setSize(350, 300);
        setLocationRelativeTo(getOwner());
        
        setContentPane(new JLabel(new ImageIcon(getClass().getResource("/resTiles/bg.gif"))));
        setLayout(new BorderLayout());

        infoLabel = new JLabel("Select through < or >, or search by index in the field.", SwingConstants.CENTER);
        infoLabel.setForeground(Color.BLACK);
        add(infoLabel, BorderLayout.NORTH);

        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setPreferredSize(new Dimension(90, 30));
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInputFieldAction();
            }
        });
        inputField.setEnabled(true);

        nodeDataLabel = new JLabel("", SwingConstants.CENTER);
        nodeDataLabel.setForeground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Dimension buttonSize = new Dimension(80, 30);
        Dimension bs = new Dimension(100, 30);

        prevButton = new JButton("<");
        nextButton = new JButton(">");
        selectButton = new JButton("Select");
        deleteButton = new JButton("Delete");
        spawnButton = new JButton("Spawn");
        exitButton = new JButton("Exit");

        prevButton.setPreferredSize(buttonSize);
        nextButton.setPreferredSize(buttonSize);
        selectButton.setPreferredSize(bs);
        deleteButton.setPreferredSize(buttonSize);
        spawnButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        panel.setBackground(new Color(0, 0, 0, 150));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(prevButton, gbc);

        gbc.gridx = 1;
        panel.add(nextButton, gbc);

        gbc.gridx = 2;       
        panel.add(selectButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        panel.add(inputField, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(deleteButton, gbc);

        gbc.gridx = 1;
        panel.add(spawnButton, gbc);

        gbc.gridx = 2;
        panel.add(exitButton, gbc);

        add(panel, BorderLayout.SOUTH);
        add(nodeDataLabel, BorderLayout.CENTER);

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputField.getText().trim().isEmpty()) {
                    Node previous = list.getPreviousDeathPoint();
                    updateCurrentNode(previous);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputField.getText().trim().isEmpty()) {
                    Node next = list.getNextDeathPoint();
                    updateCurrentNode(next);
                }
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSelectButtonAction();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentNode != null) {
                    int index = getNodeIndex(currentNode);
                    list.removeNodeByIndex(index);
                    infoLabel.setText("Deleted Node: (" + currentNode.getX() + ", " + currentNode.getY() + ") | Index: " + index);
                    currentNode = null;

                    if (list.size() > 0) {
                        updateCurrentNode(list.getDeathPointByIndex(0));
                    } else {
                        infoLabel.setText("No death points available.");
                    }
                } else {
                    infoLabel.setText("No death point selected for deletion.");
                }
            }
        });

        spawnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSpawnButtonAction();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrossyDupe.stopGameAndExit();
            }
        });

        if (list.size() > 0) {
            currentNode = list.getDeathPointByIndex(0);
            updateCurrentNode(currentNode);
        }
    }

    private void handleInputFieldAction() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        selectButton.setEnabled(true);
    }

    private void handleSelectButtonAction() {
        if (inputField.getText().trim().isEmpty()) {
            if (currentNode != null) {
                int index = getNodeIndex(currentNode);
                infoLabel.setText("Selected through < or >: (" + currentNode.getX() + ", " + currentNode.getY() + ") | Index: " + index);
                nodeDataLabel.setText("Node selected: (" + currentNode.getX() + ", " + currentNode.getY() + ") | Index: " + index);
            } else {
                infoLabel.setText("No node selected.");
                nodeDataLabel.setText("");
            }
        } else {
            try {
                int index = Integer.parseInt(inputField.getText().trim());
                Node node = list.getDeathPointByIndex(index);
                if (node != null) {
                    infoLabel.setText("Selected by index: (" + node.getX() + ", " + node.getY() + ") | Index: " + index);
                    nodeDataLabel.setText("Node found: (" + node.getX() + ", " + node.getY() + ") | Index: " + index);
                    updateCurrentNode(node);
                } else {
                    infoLabel.setText("No death point found at index " + index);
                    nodeDataLabel.setText("");
                }
            } catch (NumberFormatException ex) {
                infoLabel.setText("Invalid index entered.");
                nodeDataLabel.setText("");
            }
        }
    }

    private void handleSpawnButtonAction() {
        if (inputField.getText().trim().isEmpty()) {
            if (currentNode != null) {
                gamePanel.ch.setX(currentNode.getX());
                gamePanel.ch.setY(currentNode.getY());
                gamePanel.ch.setAlive(true);
                gamePanel.startGameThread();
                dispose();
            } else {
                infoLabel.setText("No death point selected.");
                nodeDataLabel.setText("");
            }
        } else {
            try {
                int index = Integer.parseInt(inputField.getText().trim());
                Node node = list.getDeathPointByIndex(index);
                if (node != null) {
                    gamePanel.ch.setX(node.getX());
                    gamePanel.ch.setY(node.getY());
                    gamePanel.ch.setAlive(true);
                    gamePanel.startGameThread();
                    dispose();
                } else {
                    infoLabel.setText("No death point found at index " + index);
                    nodeDataLabel.setText("");
                }
            } catch (NumberFormatException ex) {
                infoLabel.setText("Invalid index entered.");
                nodeDataLabel.setText("");
            }
        }
        resetUIState();
    }

    private void resetUIState() {
        inputField.setText("");
        inputField.setEnabled(true);
        prevButton.setEnabled(true);
        nextButton.setEnabled(true);
        selectButton.setEnabled(true);
    }

    private void updateCurrentNode(Node node) {
        if (node != null) {
            currentNode = node;
            int index = getNodeIndex(node);
            infoLabel.setText("Selected through < or >: (" + node.getX() + ", " + node.getY() + ") | Index: " + index);
            nodeDataLabel.setText("Death Point: (" + node.getX() + ", " + node.getY() + ") | Index: " + index);
        } else {
            infoLabel.setText("No death point available.");
            nodeDataLabel.setText("");
        }
    }

    private int getNodeIndex(Node node) {
        Node temp = list.head;
        int index = 0;
        while (temp != null) {
            if (temp.equals(node)) {
                return index;
            }
            temp = temp.next;
            index++;
        }
        return -1;
    }
}
