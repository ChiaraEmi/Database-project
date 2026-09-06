package soundwave.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

import soundwave.data.Plan;

/**
 * Dialog for activating a subscription
 */
public final class ActivateSubscriptionDialog extends JDialog {
    private final JComboBox<String> comboPlans;
    private final JComboBox<String> comboPayment;
    private final JTextField txtPromoCode;
    private final JTextField txtInviteCode;
    private final JCheckBox chkAutoRenew;
    private final JLabel lblTotal;
    private final JButton btnActivate;
    private final JButton btnApplyPromo;
    private final JButton btnVerifyInvite;

    private final List<Plan> plans;
    private final String username;
    private double currentPrice = 0.0;

    private static final String[] PAYMENT_METHODS = {
        "Carta di Credito",
        "Carta di Debito",
        "PayPal",
        "Bonifico Bancario"
    };

    private Consumer<SubscriptionData> onActivate;

    private boolean promoCodeApplied = false;
    private boolean inviteCodeVerified = false;

    /**
     * Constructs a new ActivateSubscriptionDialog.
     *
     * @param parent   the parent frame
     * @param username the username
     * @param plans    the list of available subscription plans
     */
    public ActivateSubscriptionDialog(final JFrame parent, final String username, final List<Plan> plans) {
        super(parent, "Attivazione Sottoscrizione", true);
        this.username = username;
        this.plans = plans;

        this.promoCodeApplied = false;
        this.inviteCodeVerified = false;

        setLayout(new BorderLayout(10,10));
        setSize(600,500);
        setLocationRelativeTo(parent);

        // === Main Panel ===
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // --- Intestazione utente ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        final JLabel lblUser = new JLabel("Utente: " + username);
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(lblUser, gbc);
        gbc.gridwidth = 1;

        // --- Piano abbonamento ---
        row = addSectionHeader(panel, gbc, row, "Piano di abbonamento");

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Piano:"), gbc);

        this.comboPlans = new JComboBox<>();
        for (final Plan plan : plans) {
            this.comboPlans.addItem(plan.getTypePlan() + " (" + plan.getDurationMonths() + " mesi) - €" + String.format("%.2f", plan.getPrice()));
        }
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPlans, gbc);
        gbc.gridwidth = 1;

        // Listener per aggiornare il prezzo quando si cambia piano
        this.comboPlans.addActionListener(e -> updateTotalPrice());

        row++;

        // --- Metodo di pagamento ---
        row = addSectionHeader(panel, gbc, row, "Metodo di pagamento");

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Metodo:"), gbc);

        this.comboPayment = new JComboBox<>(PAYMENT_METHODS);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPayment, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Sconti ---
        row = addSectionHeader(panel, gbc, row, "Sconti (opzionali)");

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Codice Promozionale:"), gbc);

        this.txtPromoCode = new JTextField(30);
        gbc.gridx = 1;
        gbc.weightx = 1.0;          // ← IMPORTANTE! Prende tutto lo spazio extra
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtPromoCode, gbc);

        this.btnApplyPromo = new JButton("Applica");
        gbc.gridx = 2;
        gbc.weightx = 0.0;         
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnApplyPromo, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Codice Invito:"), gbc);

        this.txtInviteCode = new JTextField(30);
        gbc.gridx = 1;
        gbc.weightx = 1.0;          
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtInviteCode, gbc);

        this.btnVerifyInvite = new JButton("Verifica");
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnVerifyInvite, gbc);
        row++;

        // --- Rinnovo automatico ---
        row = addSectionHeader(panel, gbc, row, "Opzioni");

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.chkAutoRenew = new JCheckBox("Rinnovo Automatico (consigliato)", true);
        panel.add(this.chkAutoRenew, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Totale ---
        row = addSectionHeader(panel, gbc, row, "Riepilogo");

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.lblTotal = new JLabel("Totale da pagare: €0.00");
        this.lblTotal.setFont(this.lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        this.lblTotal.setForeground(new Color(0, 120, 0));
        panel.add(this.lblTotal, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Pulsanti ---

        gbc.gridx = 2;
        this.btnActivate = new JButton("Attiva Sottoscrizione");
        this.btnActivate.setBackground(new Color(0, 120, 215));
        this.btnActivate.setForeground(Color.WHITE);
        this.btnActivate.setFont(this.btnActivate.getFont().deriveFont(Font.BOLD));
        panel.add(this.btnActivate, gbc);

        add(panel, BorderLayout.CENTER);

        // Imposta il prezzo iniziale
        updateTotalPrice();

        // Azione predefinita per Invio
        getRootPane().setDefaultButton(this.btnActivate);

    }

    private int addSectionHeader(final JPanel panel, final GridBagConstraints gbc, int row, final String title) {
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        final JLabel lblSection = new JLabel("──── " + title + " ────");
        lblSection.setFont(lblSection.getFont().deriveFont(Font.BOLD, 12f));
        lblSection.setForeground(new Color(100,100,100));
        panel.add(lblSection, gbc);
        gbc.gridwidth = 1;
        return row;
    }

    private void updateTotalPrice() {
        final int selectedIndex = this.comboPlans.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < plans.size()) {
            final Plan plan = plans.get(selectedIndex);
            this.currentPrice = plan.getPrice();
            this.lblTotal.setText("Totale da pagare: €" + String.format("%.2f", currentPrice));
        }
    }

    public void updatePriceWithDiscount(final double discountedPrice) {
        this.currentPrice = discountedPrice;
        this.lblTotal.setText("Totale da pagare: €" + String.format("%.2f", currentPrice));
    }

    public void addActivateListener(final Consumer<SubscriptionData> onActivate) {
        this.onActivate = onActivate;
        this.btnActivate.addActionListener(e -> {

            String promoCode = getPromoCode();
            String inviteCode = getInviteCode();

            // Controllo: codice promozionale non applicato
            if (!promoCode.isEmpty() && !promoCodeApplied) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Hai inserito un codice promozionale ma non l'hai applicato.\n" +
                    "Vuoi applicarlo prima di attivare?",
                    "Promozione non applicata",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Controllo: codice invito non verificato
            if (!inviteCode.isEmpty() && !inviteCodeVerified) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Hai inserito un codice invito ma non l'hai verificato.\n" +
                    "Vuoi verificarlo prima di attivare?",
                    "Invito non verificato",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    return;
                }
            }

            if (this.onActivate != null) {
                final SubscriptionData data = new SubscriptionData(
                    getSelectedPlanCode(),
                    getPaymentMethod(),
                    getPromoCode(),
                    getInviteCode(),
                    isAutoRenew()
                );
                this.onActivate.accept(data);
            }
        });
    }

    public void addApplyPromotionListener(final Consumer<String> onApply) {
        this.btnApplyPromo.addActionListener(e -> {
            if (onApply != null) {
                onApply.accept(getPromoCode());
            }
        });
    }

    public void addVerifyInviteListener(final Consumer<String> onVerify) {
        this.btnVerifyInvite.addActionListener(e -> {
            if (onVerify != null) {
                onVerify.accept(getInviteCode());
            }
        });
    }

    public void setPromoCodeApplied(final boolean applied) {
        this.promoCodeApplied = applied;
    }

    public void setInviteCodeVerified(final boolean verified) {
        this.inviteCodeVerified = verified;
    }

    public String getUsername() { return username; }
    public int getSelectedPlanCode() {
        final int index = this.comboPlans.getSelectedIndex();
        return index >= 0 && index < plans.size() ? plans.get(index).getPlanCode() : -1;
    }

    public Plan getSelectedPlan() {
        final int index = this.comboPlans.getSelectedIndex();
        return index >= 0 && index < plans.size() ? plans.get(index) : null;
    }

    public String getPaymentMethod() { return (String) this.comboPayment.getSelectedItem(); }
    public String getPromoCode() { return this.txtPromoCode.getText().trim(); }
    public String getInviteCode() { return this.txtInviteCode.getText().trim(); }
    public boolean isAutoRenew() { return this.chkAutoRenew.isSelected(); }
    public double getCurrentPrice() { return this.currentPrice; }

    public static final class SubscriptionData {
        public final int planCode;
        public final String paymentMethod;
        public final String promoCode;
        public final String inviteCode;
        public final boolean autoRenew;

        public SubscriptionData(final int planCode, final String paymentMethod, final String promoCode, final String inviteCode, final boolean autoRenew) {
            this.planCode = planCode;
            this.paymentMethod = paymentMethod;
            this.promoCode = promoCode;
            this.inviteCode = inviteCode;
            this.autoRenew = autoRenew;
        }
    }


    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Chiude il dialog.
     */
    public void closeDialog() {
        dispose();
    }
}
