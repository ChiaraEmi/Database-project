package soundwave.view;

import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Dialog che mostra lo stato delle sottoscrizioni di un utente
 * Include dettagli della sottoscrizione, transazioni e pulsanti per azioni
 */
public final class SubscriptionStatusDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final String FONT_FAMILY = "Segoe UI";
    private static final String STATUS_ATTIVA = "Attiva";
    private static final String STATUS_SCADUTA = "Scaduta";

    private static final int FONT_SIZE_TITLE = 16;
    private static final int FONT_SIZE_STATUS = 14;
    private static final int FONT_SIZE_BOLD = 13;
    private static final int FONT_SIZE_PLAIN = 12;
    private static final int FONT_SIZE_ITALIC = 12;

    private static final int BLOCK_MAX_HEIGHT = 300;
    private static final int BLOCK_PADDING_TOP = 12;
    private static final int BLOCK_PADDING_LEFT = 15;
    private static final int BLOCK_PADDING_BOTTOM = 12;
    private static final int BLOCK_PADDING_RIGHT = 15;
    private static final int BLOCK_BORDER_THICKNESS = 1;

    private static final int DIALOG_WIDTH = 700;
    private static final int DIALOG_HEIGHT = 500;
    private static final int SCROLL_BAR_UNIT = 16;
    private static final int DIALOG_GAP_H = 10;
    private static final int DIALOG_GAP_V = 10;
    private static final int FLOW_GAP_H = 15;
    private static final int FLOW_GAP_V = 10;

    private static final int COLOR_GREEN = 0;
    private static final int COLOR_GREEN_DARK = 150;
    private static final int COLOR_RED = 200;
    private static final int COLOR_RED_DARK = 0;
    private static final int COLOR_YELLOW = 150;
    private static final int COLOR_GRAY = 200;
    private static final int COLOR_LIGHT_GRAY = 150;
    private static final int COLOR_BLUE = 120;
    private static final int COLOR_BLUE_DARK = 215;
    private static final int COLOR_DARK_GRAY = 50;
    private static final int COLOR_DARK_GRAY_2 = 60;
    private static final int COLOR_TEXT_DARK = 30;
    private static final int COLOR_TEXT_LIGHT = 80;

    private static final int FONT_STYLE_BOLD = Font.BOLD;
    private static final int FONT_STYLE_PLAIN = Font.PLAIN;
    private static final int FONT_STYLE_ITALIC = Font.ITALIC;

    private static final int RIGID_AREA_WIDTH = 0;
    private static final int RIGID_AREA_HEIGHT_8 = 8;
    private static final int RIGID_AREA_HEIGHT_10 = 10;
    private static final int RIGID_AREA_HEIGHT_12 = 12;
    private static final int RIGID_AREA_HEIGHT_4 = 4;

    private static final int GRID_GAP_H = 15;
    private static final int GRID_GAP_V = 4;
    private static final int BORDER_EMPTY_SMALL = 5;
    private static final int ACTION_PANEL_GAP = 5;
    private static final int FLOW_LAYOUT_GAP = 10;

    private static final int BORDER_SIZE = 10;
    private static final int BORDER_PADDING = 8;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 32;

    private final JPanel contentPanel;
    private final JScrollPane scrollPane;
    private final JButton btnRefresh;
    private final JButton btnClose;
    private final String username;

    private Consumer<Integer> onRenew;
    private Consumer<Integer> onToggleAutoRenew;
    private Runnable onRefresh;

    /**
     * Costruisce un nuovo dialog per visualizzare lo stato delle sottoscrizioni
     * 
     * @param parent la finestra padre
     * @param username l'username dell'utente
     */
    public SubscriptionStatusDialog(final JFrame parent, final String username) {
        super(parent, "Sottoscrizioni - " + username, true);
        this.username = username;

        setLayout(new BorderLayout(DIALOG_GAP_H, DIALOG_GAP_V));
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);

        // --- Pannello principale con scroll ---
        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));
        this.contentPanel.setBackground(Color.WHITE);

        this.scrollPane = new JScrollPane(this.contentPanel);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_UNIT);
        add(this.scrollPane, BorderLayout.CENTER);

        // --- Pulsanti ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_GAP_H, FLOW_GAP_V));
        this.btnRefresh = new JButton("Aggiorna");
        this.btnClose = new JButton("Chiudi");
        btnPanel.add(this.btnRefresh);
        btnPanel.add(this.btnClose);
        add(btnPanel, BorderLayout.SOUTH);

        this.btnClose.addActionListener(e -> dispose());
        this.btnRefresh.addActionListener(e -> {
            if (this.onRefresh != null) {
                this.onRefresh.run();
            }
        });
    }

    /**
     * Pulisce il pannello rimuovendo tutti i blocchi
     */
    public void clear() {
        this.contentPanel.removeAll();
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }

    /**
     * Aggiunge un blocco di sottoscrizione con le sue transazioni
     */
    public void addSubscriptionBlock(
            final int subCode,
            final String planType,
            final String startDate,
            final String endDate,
            final String status,
            final boolean autoRenew,
            final String promoCode,
            final String inviteCode,
            final List<String> transactions
    ) {
        // Crea il pannello per questa sottoscrizione
        JPanel blockPanel = new JPanel();
        blockPanel.setLayout(new BoxLayout(blockPanel, BoxLayout.Y_AXIS));
        blockPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(COLOR_GRAY, COLOR_GRAY, COLOR_GRAY), BLOCK_BORDER_THICKNESS),
            BorderFactory.createEmptyBorder(BLOCK_PADDING_TOP, BLOCK_PADDING_LEFT, BLOCK_PADDING_BOTTOM, BLOCK_PADDING_RIGHT)
        ));
        blockPanel.setBackground(Color.WHITE);
        blockPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, BLOCK_MAX_HEIGHT));

        // --- Intestazione ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Sottoscrizione #" + subCode);
        lblTitle.setFont(new Font(FONT_FAMILY, FONT_STYLE_BOLD, FONT_SIZE_TITLE));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // Stato (con colore)
        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font(FONT_FAMILY, FONT_STYLE_BOLD, FONT_SIZE_STATUS));
        if ("Attiva".equals(status)) {
            lblStatus.setForeground(new Color(COLOR_GREEN, COLOR_GREEN_DARK, COLOR_GREEN));
        } else if ("Scaduta".equals(status)) {
            lblStatus.setForeground(new Color(COLOR_RED, COLOR_GREEN_DARK, COLOR_RED_DARK));
        } else {
            lblStatus.setForeground(new Color(COLOR_RED, COLOR_YELLOW, COLOR_GREEN));
        }
        headerPanel.add(lblStatus, BorderLayout.EAST);
        blockPanel.add(headerPanel);

        blockPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_WIDTH, RIGID_AREA_HEIGHT_8)));

        // --- Dettagli ---
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, GRID_GAP_H, GRID_GAP_V));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(RIGID_AREA_HEIGHT_8, BORDER_EMPTY_SMALL, RIGID_AREA_HEIGHT_8, BORDER_EMPTY_SMALL));

        detailsPanel.add(createLabel("Piano:"));
        detailsPanel.add(createValueLabel(planType));
        detailsPanel.add(createLabel("Inizio:"));
        detailsPanel.add(createValueLabel(startDate));
        detailsPanel.add(createLabel("Fine:"));
        detailsPanel.add(createValueLabel(endDate));
        detailsPanel.add(createLabel("Rinnovo Automatico:"));
        detailsPanel.add(createValueLabel(autoRenew ? "Attiva" : "Disattivata"));

        if (promoCode != null && !promoCode.isEmpty()) {
            detailsPanel.add(createLabel("Promozione:"));
            detailsPanel.add(createValueLabel(promoCode));
        }
        if (inviteCode != null && !inviteCode.isEmpty()) {
            detailsPanel.add(createLabel("Codice Invito:"));
            detailsPanel.add(createValueLabel(inviteCode));
        }

        blockPanel.add(detailsPanel);
        blockPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_WIDTH, RIGID_AREA_HEIGHT_10)));

        // --- Transazioni ---
        if (!transactions.isEmpty()) {
            JLabel lblTrans = new JLabel("Transazioni:");
            lblTrans.setFont(new Font(FONT_FAMILY, FONT_SIZE_BOLD, FONT_SIZE_TITLE));
            blockPanel.add(lblTrans);
            blockPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_WIDTH, RIGID_AREA_HEIGHT_4)));

            for (String trans : transactions) {
                JLabel lblTransItem = new JLabel("  • " + trans);
                lblTransItem.setFont(new Font(FONT_FAMILY, FONT_STYLE_PLAIN, FONT_SIZE_PLAIN));
                lblTransItem.setForeground(new Color(COLOR_DARK_GRAY_2, COLOR_DARK_GRAY_2, COLOR_DARK_GRAY_2));
                blockPanel.add(lblTransItem);
            }
        } else {
            JLabel lblNoTrans = new JLabel("  Nessuna transazione");
            lblNoTrans.setFont(new Font(FONT_FAMILY, FONT_STYLE_ITALIC, FONT_SIZE_ITALIC));
            lblNoTrans.setForeground(new Color(COLOR_LIGHT_GRAY, COLOR_LIGHT_GRAY, COLOR_LIGHT_GRAY));
            blockPanel.add(lblNoTrans);
        }

        if ("Attiva".equals(status)) {
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_LAYOUT_GAP, ACTION_PANEL_GAP));
            actionPanel.setBackground(Color.WHITE);

            JButton btnRenew = new JButton("Rinnova Ora");
            btnRenew.setBackground(new Color(COLOR_GREEN, COLOR_BLUE, COLOR_BLUE_DARK));
            btnRenew.setForeground(Color.WHITE);
            btnRenew.setFont(btnRenew.getFont().deriveFont(FONT_STYLE_BOLD));
            
            btnRenew.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Vuoi rinnovare la sottoscrizione #" + subCode + "?",
                    "Conferma Rinnovo",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION && onRenew != null) {
                    onRenew.accept(subCode);
                }
            });
            actionPanel.add(btnRenew);

            String toggleText = autoRenew ? "Disattiva Rinnovo" : "Attiva Rinnovo";
            JButton btnToggle = new JButton(toggleText);
            btnToggle.setBackground(autoRenew ? new Color(COLOR_RED, COLOR_DARK_GRAY, COLOR_DARK_GRAY) : new Color(COLOR_GREEN, COLOR_YELLOW, COLOR_GREEN));
            btnToggle.setForeground(Color.WHITE);
            btnToggle.setFont(btnToggle.getFont().deriveFont(FONT_STYLE_BOLD));
            btnToggle.addActionListener(e -> {
                String action = autoRenew ? "disattivare" : "attivare";
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Vuoi " + action + " il rinnovo automatico?",
                    "Conferma " + action,
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION && onToggleAutoRenew != null) {
                    onToggleAutoRenew.accept(subCode);
                }
            });
            
            actionPanel.add(btnToggle);
            blockPanel.add(actionPanel);

        } else {
            System.out.println(" Sottoscrizione NON ATTIVA! Status = '" + status + "'");
        }

        // Aggiungi il blocco al pannello principale
        this.contentPanel.add(blockPanel);
        this.contentPanel.add(Box.createRigidArea(new Dimension(RIGID_AREA_WIDTH, RIGID_AREA_HEIGHT_12)));
    }

    /**
     * Crea un'etichetta per il campo.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, FONT_STYLE_BOLD, FONT_SIZE_PLAIN));
        label.setForeground(new Color(COLOR_TEXT_LIGHT, COLOR_TEXT_LIGHT, COLOR_TEXT_LIGHT));
        return label;
    }

    /**
     * Crea un'etichetta per il valore.
     */
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, FONT_STYLE_PLAIN, FONT_SIZE_PLAIN));
        label.setForeground(new Color(COLOR_TEXT_DARK, COLOR_TEXT_DARK, COLOR_TEXT_DARK));
        return label;
    }

    /**
     * Aggiorna il pannello dopo aver aggiunto i blocchi.
     */
    public void refreshUI() {
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
        this.scrollPane.getVerticalScrollBar().setValue(0);
    }

    /**
     * Imposta il Consumer per l'azione di rinnovo manuale
     * 
     * @param onRenew il Consumer che riceve il codice della sottoscrizione
     */
    public void setOnRenew(Consumer<Integer> onRenew) {
        this.onRenew = onRenew;
    }

    /**
     * Imposta il Consumer per l'azione di attivazione/disattivazione del rinnovo
     * 
     * @param onToggleAutoRenew il Consumer che riceve il codice della sottoscrizione
     */
    public void setOnToggleAutoRenew(Consumer<Integer> onToggleAutoRenew) {
        this.onToggleAutoRenew = onToggleAutoRenew;
    }

    /**
     * Imposta il Runnable per l'azione di refresh
     * 
     * @param onRefresh il Runnable da eseguire al refresh
     */
    public void setOnRefresh(Runnable onRefresh) {
        this.onRefresh = onRefresh;
    }

    /**
     * Mostra un messaggio di errore all'utente
     * 
     * @param message il messaggio da mostrare
     */
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostra un messaggio di successo all'utente
     * 
     * @param message il messaggio da mostrare
     */
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }
}
