package soundwave.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;

import soundwave.data.Plan;

/**
 * Dialog for redeeming bonus credits to purchase a subscription.
 * This dialog allows users to select a subscription plan and redeem
 * their bonus credits. The dialog validates that the user has sufficient
 * credits and that the selected plan is eligible for bonus redemption.
 */
public final class RedeemBonusDialog extends JDialog {
    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 400;
    private static final int INSET_GAP_15 = 15;
    private static final int INSET_GAP_6 = 6;
    private static final float FONT_SIZE_14 = 14f;
    private static final float FONT_SIZE_12 = 12f;
    private static final int BUTTON_WIDTH_200 = 200;
    private static final int BUTTON_WIDTH_120 = 120;
    private static final int BUTTON_COLOR_BLUE = 215;

    private static final String[] PAYMENT_METHODS = {
        "Crediti Bonus",
    };

    private final JComboBox<String> comboPlans;
    private final JComboBox<String> comboPayment;
    private final JCheckBox chkAutoRenew;
    private final JLabel lblBonusCredits;
    private final JLabel lblTotal;
    private final JButton btnRedeem;
    private final JButton btnCancel;

    @SuppressFBWarnings("SE_BAD_FIELD")
    private final List<Plan> plans;
    private final String username;
    private final int bonusCredits;
    private boolean redeemed;
    private double currentPrice;

    private transient Consumer<RedeemData> onRedeemListener;

    /**
     * Constructs a new RedeemBonusDialog.
     * 
     * @param parent the parent frame
     * @param username the username of the user
     * @param plans the list of available subscription plans
     * @param bonusCredits the user's current bonus credit balance
     */
    public RedeemBonusDialog(final JFrame parent, final String username, 
                            final List<Plan> plans, final int bonusCredits) {
        super(parent, "Riscatto con Crediti Bonus", true);
        this.username = username;
        this.plans = new ArrayList<>(plans);
        this.bonusCredits = bonusCredits;

        setLayout(new BorderLayout(10, 10));
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);

        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP_15, INSET_GAP_15, INSET_GAP_15, INSET_GAP_15));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP_6, INSET_GAP_6, INSET_GAP_6, INSET_GAP_6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // --- Intestazione utente ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        final JLabel lblUser = new JLabel("Utente: " + username);
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, FONT_SIZE_14));
        panel.add(lblUser, gbc);
        gbc.gridwidth = 1;

        // --- Crediti Bonus ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        this.lblBonusCredits = new JLabel("Crediti Bonus disponibili: " + bonusCredits);
        this.lblBonusCredits.setFont(this.lblBonusCredits.getFont().deriveFont(Font.BOLD, FONT_SIZE_14));
        if (bonusCredits >= 2) {
            this.lblBonusCredits.setForeground(new Color(0, BUTTON_WIDTH_120, 0));
        } else {
            this.lblBonusCredits.setForeground(new Color(BUTTON_WIDTH_200, 0, 0));
        }
        panel.add(this.lblBonusCredits, gbc);
        gbc.gridwidth = 1;

        // --- Separatore ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // --- Piano abbonamento ---
        row = addSectionHeader(panel, gbc, row, "Piano di abbonamento");

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Piano:"), gbc);

        this.comboPlans = new JComboBox<>();
        // Filtra solo i piani mensili
        for (final Plan plan : plans) {
            if (plan.getDurationMonths() == 1) {
                this.comboPlans.addItem(plan.getTypePlan() + " (1 mese) - €" + String.format("%.2f", plan.getPrice()));
            }
        }
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPlans, gbc);
        gbc.gridwidth = 1;
        this.comboPlans.addActionListener(e -> updateTotalPrice());
        row++;

        // --- Metodo di pagamento (solo Crediti Bonus) ---
        row = addSectionHeader(panel, gbc, row, "Metodo di pagamento");

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Metodo:"), gbc);

        this.comboPayment = new JComboBox<>(PAYMENT_METHODS);
        this.comboPayment.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(this.comboPayment, gbc);
        gbc.gridwidth = 1;
        row++;

        // --- Rinnovo automatico ---
        row = addSectionHeader(panel, gbc, row, "Opzioni");

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.chkAutoRenew = new JCheckBox("Rinnovo Automatico", false);
        panel.add(this.chkAutoRenew, gbc);
        gbc.gridwidth = 1;
        row++;

        // --- Totale ---
        row = addSectionHeader(panel, gbc, row, "Riepilogo");

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        this.lblTotal = new JLabel("Totale da pagare: €0.00 (con crediti bonus)");
        this.lblTotal.setFont(this.lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        this.lblTotal.setForeground(new Color(0, BUTTON_WIDTH_120, 0));
        panel.add(this.lblTotal, gbc);
        gbc.gridwidth = 1;
        row++;

        // --- Pulsanti ---
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;

        this.btnCancel = new JButton("Indietro");
        panel.add(this.btnCancel, gbc);

        gbc.gridx = 2;
        this.btnRedeem = new JButton("Riscatta con Crediti Bonus");
        this.btnRedeem.setBackground(new Color(0, BUTTON_WIDTH_120, BUTTON_COLOR_BLUE));
        this.btnRedeem.setForeground(Color.WHITE);
        this.btnRedeem.setFont(this.btnRedeem.getFont().deriveFont(Font.BOLD));
        this.btnRedeem.setEnabled(bonusCredits >= 2);
        panel.add(this.btnRedeem, gbc);

        add(panel, BorderLayout.CENTER);

        updateTotalPrice();
        getRootPane().setDefaultButton(this.btnRedeem);
    }

    private int addSectionHeader(final JPanel panel, final GridBagConstraints gbc, 
                                 final int row, final String title) {
        int newrow = row;
        final GridBagConstraints localGbc = (GridBagConstraints) gbc.clone();
        localGbc.gridx = 0;
        localGbc.gridy = newrow++;
        localGbc.gridwidth = 3;
        final JLabel lblSection = new JLabel("──── " + title + " ────");
        lblSection.setFont(lblSection.getFont().deriveFont(Font.BOLD, FONT_SIZE_12));
        lblSection.setForeground(new Color(100, 100, 100));
        panel.add(lblSection, localGbc);
        return newrow;
    }

    private void updateTotalPrice() {
        final int selectedIndex = this.comboPlans.getSelectedIndex();
        if (selectedIndex >= 0) {
            // Trova il piano selezionato nella lista originale
            int planIndex = 0;
            for (final Plan plan : plans) {
                if (plan.getDurationMonths() == 1) {
                    if (planIndex == selectedIndex) {
                        this.currentPrice = plan.getPrice();
                        break;
                    }
                    planIndex++;
                }
            }
            this.lblTotal.setText("Totale da pagare: €0.00 (con crediti bonus)");
        }
    }

    /**
     * Adds a listener to be called when the redeem button is clicked.
     *
     * @param onRedeem the listener to call on redeem
     */
    public void addRedeemListener(final Consumer<RedeemData> onRedeem) {
        this.onRedeemListener = onRedeem;
        this.btnRedeem.addActionListener(e -> {
            if (this.onRedeemListener != null) {
                final RedeemData data = new RedeemData(
                    getSelectedPlanCode(),
                    isAutoRenew()
                );
                this.redeemed = true;
                this.onRedeemListener.accept(data);
                dispose();
            }
        });
    }

    /**
     * Adds a listener to be called when the cancel button is clicked.
     *
     * @param onCancel the listener to call on cancel
     */
    public void addCancelListener(final Runnable onCancel) {
        this.btnCancel.addActionListener(e -> {
            if (onCancel != null) {
                onCancel.run();
            }
            dispose();
        });
    }

    /**
     * Gets the username of the current user.
     * 
     * @return the username
     */
    public String getUsername() { 
        return username; 
    }

    /**
     * Gets the code of the selected subscription plan.
     *
     * @return the plan code, or 0 if no plan is selected
     */
    public int getSelectedPlanCode() {
        final int selectedIndex = this.comboPlans.getSelectedIndex();
        if (selectedIndex < 0) {
            return -1;
        }
        int planIndex = 0;
        for (final Plan plan : plans) {
            if (plan.getDurationMonths() == 1) {
                if (planIndex == selectedIndex) {
                    return plan.getPlanCode();
                }
                planIndex++;
            }
        }
        return -1;
    }

    /**
     * Checks if auto-renewal is enabled.
     *
     * @return true if auto-renewal is enabled
     */
    public boolean isAutoRenew() {
        return this.chkAutoRenew.isSelected();
    }

    /**
     * Checks if the bonus has been redeemed.
     *
     * @return true if redeemed
     */
    public boolean isRedeemed() { 
        return this.redeemed; 
    }

    /**
     * Gets the bonus credit balance.
     *
     * @return the bonus credits
     */
    public int getBonusCredits() { 
        return this.bonusCredits; 
    }

    /**
     * Shows an error message in the dialog.
     *
     * @param message the error message to display
     */
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success message in the dialog.
     *
     * @param message the success message to display
     */
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Data class containing redemption details.
     * This class is used to transfer redemption data from the dialog
     * to the controller.
     */
    public static final class RedeemData {
        private final int planCode;
        private final boolean autoRenew;

        /**
         * Constructs a new RedeemData instance.
         *
         * @param planCode the plan code.
         * @param autoRenew true if auto-renewal is enabled, false otherwise.
         */
        public RedeemData(final int planCode, final boolean autoRenew) {
            this.planCode = planCode;
            this.autoRenew = autoRenew;
        }

        /**
         * Getter the code of plan selected.
         * 
         * @return code of plan selected
         */
        public int getPlanCode() { 
            return planCode; 
        }

        /**
         * Getter to know is enable automatic renewal.
         * 
         * @return true if is enabled, false otherwise
         */
        public boolean isAutoRenew() { 
            return autoRenew; 
        }
    }

}
