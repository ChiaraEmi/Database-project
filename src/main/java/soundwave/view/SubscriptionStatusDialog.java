package soundwave.view;

import java.util.List;

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
import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;


public class SubscriptionStatusDialog extends JDialog {
    private final JPanel contentPanel;
    private final JScrollPane scrollPane;
    private final JButton btnRefresh;
    private final JButton btnClose;
    private final String username;

    private Runnable onRefresh;

    public SubscriptionStatusDialog(final JFrame parent, final String username) {
        super(parent, "Sottoscrizioni - " + username, true);
        this.username = username;

        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
        setLocationRelativeTo(parent);

        // --- Pannello principale con scroll ---
        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(this.contentPanel, BoxLayout.Y_AXIS));
        this.contentPanel.setBackground(Color.WHITE);

        this.scrollPane = new JScrollPane(this.contentPanel);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(this.scrollPane, BorderLayout.CENTER);

        // --- Pulsanti ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
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
     * Pulisce il pannello.
     */
    public void clear() {
        this.contentPanel.removeAll();
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }

    /**
     * Aggiunge un blocco di sottoscrizione con le sue transazioni.
     */
    public void addSubscriptionBlock(
            int subCode,
            String planType,
            String startDate,
            String endDate,
            String status,
            boolean autoRenew,
            String promoCode,
            String inviteCode,
            List<String> transactions
    ) {
        // Crea il pannello per questa sottoscrizione
        JPanel blockPanel = new JPanel();
        blockPanel.setLayout(new BoxLayout(blockPanel, BoxLayout.Y_AXIS));
        blockPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        blockPanel.setBackground(Color.WHITE);
        blockPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // --- Intestazione ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Sottoscrizione #" + subCode);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // Stato (con colore)
        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if ("Attiva".equals(status)) {
            lblStatus.setForeground(new Color(0, 150, 0));
        } else if ("Scaduta".equals(status)) {
            lblStatus.setForeground(new Color(200, 0, 0));
        } else {
            lblStatus.setForeground(new Color(200, 150, 0));
        }
        headerPanel.add(lblStatus, BorderLayout.EAST);
        blockPanel.add(headerPanel);

        blockPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // --- Dettagli ---
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 15, 4));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        detailsPanel.add(createLabel("Piano:"));
        detailsPanel.add(createValueLabel(planType));
        detailsPanel.add(createLabel("Inizio:"));
        detailsPanel.add(createValueLabel(startDate));
        detailsPanel.add(createLabel("Fine:"));
        detailsPanel.add(createValueLabel(endDate));
        detailsPanel.add(createLabel("Rinnovo Automatico:"));
        detailsPanel.add(createValueLabel(autoRenew ? "Attivo" : "Disattivato"));

        if (promoCode != null && !promoCode.isEmpty()) {
            detailsPanel.add(createLabel("Promozione:"));
            detailsPanel.add(createValueLabel(promoCode));
        }
        if (inviteCode != null && !inviteCode.isEmpty()) {
            detailsPanel.add(createLabel("Codice Invito:"));
            detailsPanel.add(createValueLabel(inviteCode));
        }

        blockPanel.add(detailsPanel);
        blockPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Transazioni ---
        if (!transactions.isEmpty()) {
            JLabel lblTrans = new JLabel("Transazioni:");
            lblTrans.setFont(new Font("Segoe UI", Font.BOLD, 13));
            blockPanel.add(lblTrans);
            blockPanel.add(Box.createRigidArea(new Dimension(0, 4)));

            for (String trans : transactions) {
                JLabel lblTransItem = new JLabel("  • " + trans);
                lblTransItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblTransItem.setForeground(new Color(60, 60, 60));
                blockPanel.add(lblTransItem);
            }
        } else {
            JLabel lblNoTrans = new JLabel("  Nessuna transazione");
            lblNoTrans.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblNoTrans.setForeground(new Color(150, 150, 150));
            blockPanel.add(lblNoTrans);
        }

        // Aggiungi il blocco al pannello principale
        this.contentPanel.add(blockPanel);
        this.contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    /**
     * Crea un'etichetta per il campo.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    /**
     * Crea un'etichetta per il valore.
     */
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(30, 30, 30));
        return label;
    }

    /**
     * Aggiorna il pannello dopo aver aggiunto i blocchi.
     */
    public void refreshUI() {
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
        // Scrolla in alto
        this.scrollPane.getVerticalScrollBar().setValue(0);
    }

    public void setOnRefresh(Runnable onRefresh) {
        this.onRefresh = onRefresh;
    }

    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }
}
