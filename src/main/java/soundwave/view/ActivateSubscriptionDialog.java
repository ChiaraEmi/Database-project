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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import soundwave.data.Plan;

/**
 * Dialog for activating a subscription.
 */
public final class ActivateSubscriptionDialog extends JDialog {

    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 500;
    private static final int DIALOG_GAP = 10;
    private static final int BORDER_PADDING = 15;
    private static final int INSET_SIZE = 6;
    private static final int TEXT_FIELD_COLUMNS = 30;

    private static final int COLOR_RGB_GREEN = 0;
    private static final int COLOR_RGB_BLUE = 120;
    private static final int COLOR_RGB_DARK_BLUE = 215;
    private static final int COLOR_RGB_100 = 100;

    private static final int FONT_STYLE_BOLD = Font.BOLD;
    
    // --- Costanti per eliminare i magic number e le stringhe hardcoded ---
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 500;
    
    private static final int BORDER_PADDING = 15;
    private static final int GRID_INSET = 6;
    
    private static final int TEXT_FIELD_COLUMNS = 30;
    
    private static final float USER_FONT_SIZE = 14f;
    private static final float SECTION_FONT_SIZE = 12f;
    private static final float TOTAL_FONT_SIZE = 16f;
    
    private static final double INITIAL_PRICE = 0.0;
    private static final int INVALID_PLAN_CODE = -1;
    
    private static final Color TOTAL_LABEL_COLOR = new Color(0, 120, 0);
    private static final Color SECTION_LABEL_COLOR = new Color(100, 100, 100);
    private static final Color ACTIVATE_BTN_BG = new Color(0, 120, 215);

    private static final String DIALOG_TITLE = "Attivazione Sottoscrizione";
    private static final String USER_LABEL_PREFIX = "Utente: ";
    private static final String PLAN_SECTION_TITLE = "Piano di abbonamento";
    private static final String PLAN_LABEL_TEXT = "Piano:";
    private static final String PAYMENT_SECTION_TITLE = "Metodo di pagamento";
    private static final String PAYMENT_LABEL_TEXT = "Metodo:";
    private static final String DISCOUNT_SECTION_TITLE = "Sconti (opzionali)";
    private static final String PROMO_LABEL_TEXT = "Codice Promozionale:";
    private static final String APPLY_BTN_TEXT = "Applica";
    private static final String INVITE_LABEL_TEXT = "Codice Invito:";
    private static final String VERIFY_BTN_TEXT = "Verifica";
    private static final String OPTIONS_SECTION_TITLE = "Opzioni";
    private static final String AUTO_RENEW_TEXT = "Rinnovo Automatico (consigliato)";
    private static final String SUMMARY_SECTION_TITLE = "Riepilogo";
    private static final String TOTAL_LABEL_PREFIX = "Totale da pagare: €";
    private static final String ACTIVATE_BTN_TEXT = "Attiva Sottoscrizione";
    
    private static final String PROMO_WARNING_TITLE = "Promozione non applicata";
    private static final String PROMO_WARNING_MSG = 
        "Hai inserito un codice promozionale ma non l'hai applicato.\n" +
        "Vuoi applicarlo prima di attivare?";
        
    private static final String INVITE_WARNING_TITLE = "Invito non verificato";
    private static final String INVITE_WARNING_MSG = 
        "Hai inserito un codice invito ma non l'hai verificato.\n" +
        "Vuoi verificarlo prima di attivare?";
        
    private static final String ERROR_DIALOG_TITLE = "Errore";
    private static final String SUCCESS_DIALOG_TITLE = "Successo";

    private final JComboBox<String> comboPlans;
    private final JComboBox<String> comboPayment;
    private final JTextField txtPromoCode;
    private final JTextField txtInviteCode;
    private final JCheckBox chkAutoRenew;
    private final JLabel lblTotal;
    private final JButton btnActivate;
    private final JButton btnApplyPromo;
    private final JButton btnVerifyInvite;

    private final transient List<Plan> plans;
    private final String username;
    private double currentPrice;

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
     * @param parent the parent frame.
     * @param username the username.
     * @param plans the list of available subscription plans.
     */
    public ActivateSubscriptionDialog(final JFrame parent, final String username, final List<Plan> plans) {
        super(parent, DIALOG_TITLE, true);
        this.username = username;
        // Copia difensiva per evitare l'avviso EI2
        this.plans = plans != null ? new ArrayList<>(plans) : new ArrayList<>();

        this.promoCodeApplied = false;
        this.inviteCodeVerified = false;
        this.currentPrice = INITIAL_PRICE;

        setLayout(new BorderLayout(GRID_INSET, GRID_INSET));
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);

        // === Main Panel ===
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
        panel.setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GRID_INSET, GRID_INSET, GRID_INSET, GRID_INSET);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // --- Intestazione utente ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        final JLabel lblUser = new JLabel(USER_LABEL_PREFIX + username);
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, USER_FONT_SIZE));
        panel.add(lblUser, gbc);
        gbc.gridwidth = 1;

        // --- Piano abbonamento ---
        row = addSectionHeader(panel, gbc, row, PLAN_SECTION_TITLE);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(PLAN_LABEL_TEXT), gbc);

        this.comboPlans = new JComboBox<>();
        for (final Plan plan : this.plans) {
            this.comboPlans.addItem(plan.getTypePlan() + " (" + plan.getDurationMonths() 
                                    + " mesi) - €" + String.format("%.2f", plan.getPrice()));
        }
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPlans, gbc);
        gbc.gridwidth = 1;

        // Listener per aggiornare il prezzo quando si cambia piano
        this.comboPlans.addActionListener(e -> updateTotalPrice());

        row++;

        // --- Metodo di pagamento ---
        row = addSectionHeader(panel, gbc, row, PAYMENT_SECTION_TITLE);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(PAYMENT_LABEL_TEXT), gbc);

        this.comboPayment = new JComboBox<>(PAYMENT_METHODS);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPayment, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Sconti ---
        row = addSectionHeader(panel, gbc, row, DISCOUNT_SECTION_TITLE);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(PROMO_LABEL_TEXT), gbc);

        this.txtPromoCode = new JTextField(TEXT_FIELD_COLUMNS);
        gbc.gridx = 1;
        gbc.weightx = 1.0;          
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtPromoCode, gbc);

        this.btnApplyPromo = new JButton(APPLY_BTN_TEXT);
        gbc.gridx = 2;
        gbc.weightx = 0.0;         
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnApplyPromo, gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(INVITE_LABEL_TEXT), gbc);

        this.txtInviteCode = new JTextField(TEXT_FIELD_COLUMNS);
        gbc.gridx = 1;
        gbc.weightx = 1.0;          
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(this.txtInviteCode, gbc);

        this.btnVerifyInvite = new JButton(VERIFY_BTN_TEXT);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(this.btnVerifyInvite, gbc);
        row++;

        // --- Rinnovo automatico ---
        row = addSectionHeader(panel, gbc, row, OPTIONS_SECTION_TITLE);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.chkAutoRenew = new JCheckBox(AUTO_RENEW_TEXT, true);
        panel.add(this.chkAutoRenew, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Totale ---
        row = addSectionHeader(panel, gbc, row, SUMMARY_SECTION_TITLE);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.lblTotal = new JLabel(TOTAL_LABEL_PREFIX + "0.00");
        this.lblTotal.setFont(this.lblTotal.getFont().deriveFont(Font.BOLD, TOTAL_FONT_SIZE));
        this.lblTotal.setForeground(TOTAL_LABEL_COLOR);
        this.lblTotal = new JLabel("Totale da pagare: €0.00");
        this.lblTotal.setFont(this.lblTotal.getFont().deriveFont(FONT_STYLE_BOLD, 16f));
        this.lblTotal.setForeground(new Color(COLOR_RGB_BLUE, COLOR_RGB_DARK_BLUE, COLOR_RGB_GREEN));
        panel.add(this.lblTotal, gbc);
        gbc.gridwidth = 1;

        row++;

        // --- Pulsanti ---
        gbc.gridx = 2;
        this.btnActivate = new JButton(ACTIVATE_BTN_TEXT);
        this.btnActivate.setBackground(ACTIVATE_BTN_BG);
        this.btnActivate.setForeground(Color.WHITE);
        this.btnActivate.setFont(this.btnActivate.getFont().deriveFont(FONT_STYLE_BOLD));
        panel.add(this.btnActivate, gbc);

        add(panel, BorderLayout.CENTER);

        // Imposta il prezzo iniziale
        updateTotalPrice();

        // Azione predefinita per Invio
        getRootPane().setDefaultButton(this.btnActivate);
    }

    /**
     * Adds a section header to the dialog panel.
     *
     * @param panel the target panel.
     * @param gbc the grid bag constraints.
     * @param row the current row index.
     * @param title the title of the section.
     * 
     * @return the updated row index.
     */
    private int addSectionHeader(final JPanel panel, final GridBagConstraints gbc, int row, final String title) {
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        final JLabel lblSection = new JLabel("──── " + title + " ────");
        lblSection.setFont(lblSection.getFont().deriveFont(Font.BOLD, SECTION_FONT_SIZE));
        lblSection.setForeground(SECTION_LABEL_COLOR);
        panel.add(lblSection, gbc);
        gbc.gridwidth = 1;
        return row;
    }

    /**
     * Updates the total price based on the selected plan.
     */
    private void updateTotalPrice() {
        final int selectedIndex = this.comboPlans.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < this.plans.size()) {
            final Plan plan = this.plans.get(selectedIndex);
            this.currentPrice = plan.getPrice();
            this.lblTotal.setText(TOTAL_LABEL_PREFIX + String.format("%.2f", this.currentPrice));
        }
    }

    /**
     * Updates the total price label with a discounted price.
     *
     * @param discountedPrice the new discounted price value.
     */
    public void updatePriceWithDiscount(final double discountedPrice) {
        this.currentPrice = discountedPrice;
        this.lblTotal.setText(TOTAL_LABEL_PREFIX + String.format("%.2f", this.currentPrice));
    }

    /**
     * Adds an activation listener to handle subscription data submission.
     *
     * @param onActivate the consumer action triggered upon activation.
     */
    public void addActivateListener(final Consumer<SubscriptionData> onActivate) {
        this.onActivate = onActivate;
        this.btnActivate.addActionListener(e -> {
            final String promoCode = getPromoCode();
            final String inviteCode = getInviteCode();

            if (!promoCode.isEmpty() && !this.promoCodeApplied) {
                final int choice = JOptionPane.showConfirmDialog(
                    this,
                    PROMO_WARNING_MSG,
                    PROMO_WARNING_TITLE,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            if (!inviteCode.isEmpty() && !this.inviteCodeVerified) {
                final int choice = JOptionPane.showConfirmDialog(
                    this,
                    INVITE_WARNING_MSG,
                    INVITE_WARNING_TITLE,
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

    /**
     * Adds a listener for applying a promotional code.
     *
     * @param onApply the consumer action triggered to apply the code.
     */
    public void addApplyPromotionListener(final Consumer<String> onApply) {
        this.btnApplyPromo.addActionListener(e -> {
            if (onApply != null) {
                onApply.accept(getPromoCode());
            }
        });
    }

    /**
     * Adds a listener for verifying an invite code.
     *
     * @param onVerify the consumer action triggered to verify the code.
     */
    public void addVerifyInviteListener(final Consumer<String> onVerify) {
        this.btnVerifyInvite.addActionListener(e -> {
            if (onVerify != null) {
                onVerify.accept(getInviteCode());
            }
        });
    }

    /**
     * Sets whether the promotional code has been applied.
     *
     * @param applied true if the code is applied, false otherwise.
     */
    public void setPromoCodeApplied(final boolean applied) {
        this.promoCodeApplied = applied;
    }

    /**
     * Sets whether the invite code has been verified.
     *
     * @param verified true if the code is verified, false otherwise.
     */
    public void setInviteCodeVerified(final boolean verified) {
        this.inviteCodeVerified = verified;
    }

    /**
     * Returns the current username.
     *
     * @return the username string.
     */
    public String getUsername() { 
        return this.username; 
    }
    
    /**
     * Returns the code of the currently selected plan.
     *
     * @return the plan code, or -1 if none is selected.
     */
    public int getSelectedPlanCode() {
        final int index = this.comboPlans.getSelectedIndex();
        return index >= 0 && index < this.plans.size() ? this.plans.get(index).getPlanCode() : INVALID_PLAN_CODE;
    }

    /**
     * Returns the currently selected Plan object.
     *
     * @return the selected Plan, or null if none is selected.
     */
    public Plan getSelectedPlan() {
        final int index = this.comboPlans.getSelectedIndex();
        return index >= 0 && index < this.plans.size() ? this.plans.get(index) : null;
    }

    /**
     * Returns the selected payment method.
     *
     * @return the payment method string.
     */
    public String getPaymentMethod() { 
        return (String) this.comboPayment.getSelectedItem(); 
    }

    /**
     * Returns the text entered in the promotional code field.
     *
     * @return the promo code string.
     */
    public String getPromoCode() { 
        return this.txtPromoCode.getText().trim(); 
    }

    /**
     * Returns the text entered in the invite code field.
     *
     * @return the invite code string.
     */
    public String getInviteCode() { 
        return this.txtInviteCode.getText().trim(); 
    }

    /**
     * Checks if auto-renewal is selected.
     *
     * @return true if auto-renewal is enabled, false otherwise.
     */
    public boolean isAutoRenew() { 
        return this.chkAutoRenew.isSelected(); 
    }

    /**
     * Returns the current total price.
     *
     * @return the current price value.
     */
    public double getCurrentPrice() { 
        return this.currentPrice; 
    }

    /**
     * Data holder container for subscription activation details.
     */
    public static final class SubscriptionData {
        public final int planCode;
        public final String paymentMethod;
        public final String promoCode;
        public final String inviteCode;
        public final boolean autoRenew;

        /**
         * Constructs a new SubscriptionData instance.
         *
         * @param planCode the plan code.
         * @param paymentMethod the selected payment method.
         * @param promoCode the promotional code used.
         * @param inviteCode the invite code used.
         * @param autoRenew true if auto-renewal is enabled, false otherwise.
         */
        public SubscriptionData(final int planCode, final String paymentMethod, final String promoCode, 
                                final String inviteCode, final boolean autoRenew) {
            this.planCode = planCode;
            this.paymentMethod = paymentMethod;
            this.promoCode = promoCode;
            this.inviteCode = inviteCode;
            this.autoRenew = autoRenew;
        }
    }

    /**
     * Shows an error message dialog.
     *
     * @param message the error message to display.
     */
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message dialog.
     *
     * @param message the success message to display.
     */
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, SUCCESS_DIALOG_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Closes the dialog.
     */
    public void closeDialog() {
        dispose();
    }
}
