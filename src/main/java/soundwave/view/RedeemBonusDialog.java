package soundwave.view;

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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;

import soundwave.data.Plan;

public class RedeemBonusDialog extends JDialog {

    private final JComboBox<String> comboPlans;
    private final JComboBox<String> comboPayment;
    private final JCheckBox chkAutoRenew;
    private final JLabel lblBonusCredits;
    private final JLabel lblTotal;
    private final JButton btnRedeem;
    private final JButton btnCancel;

    private final List<Plan> plans;
    private final String username;
    private final int bonusCredits;
    private boolean redeemed = false;
    private double currentPrice = 0.0;

    private Consumer<RedeemData> onRedeem;

    private static final String[] PAYMENT_METHODS = {
        "Crediti Bonus"
    };

    /**
     * Costruttore del Dialog.
     */
    public RedeemBonusDialog(final JFrame parent, final String username, 
                            final List<Plan> plans, final int bonusCredits) {
        super(parent, "Riscatto con Crediti Bonus", true);
        this.username = username;
        this.plans = plans;
        this.bonusCredits = bonusCredits;

        setLayout(new BorderLayout(10, 10));
        setSize(450, 400);
        setLocationRelativeTo(parent);

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

        // --- Crediti Bonus ---
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        this.lblBonusCredits = new JLabel("Crediti Bonus disponibili: " + bonusCredits);
        this.lblBonusCredits.setFont(this.lblBonusCredits.getFont().deriveFont(Font.BOLD, 14f));
        if (bonusCredits >= 2) {
            this.lblBonusCredits.setForeground(new Color(0, 150, 0));
        } else {
            this.lblBonusCredits.setForeground(new Color(200, 0, 0));
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
        this.lblTotal.setForeground(new Color(0, 120, 0));
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
        this.btnRedeem.setBackground(new Color(0, 120, 215));
        this.btnRedeem.setForeground(Color.WHITE);
        this.btnRedeem.setFont(this.btnRedeem.getFont().deriveFont(Font.BOLD));
        this.btnRedeem.setEnabled(bonusCredits >= 2);
        panel.add(this.btnRedeem, gbc);

        add(panel, BorderLayout.CENTER);

        updateTotalPrice();
        getRootPane().setDefaultButton(this.btnRedeem);
    }

    private int addSectionHeader(final JPanel panel, final GridBagConstraints gbc, 
                                 int row, final String title) {
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        final JLabel lblSection = new JLabel("──── " + title + " ────");
        lblSection.setFont(lblSection.getFont().deriveFont(Font.BOLD, 12f));
        lblSection.setForeground(new Color(100, 100, 100));
        panel.add(lblSection, gbc);
        gbc.gridwidth = 1;
        return row;
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

    public void addRedeemListener(final Consumer<RedeemData> onRedeem) {
        this.onRedeem = onRedeem;
        this.btnRedeem.addActionListener(e -> {
            if (this.onRedeem != null) {
                final RedeemData data = new RedeemData(
                    getSelectedPlanCode(),
                    isAutoRenew()
                );
                this.redeemed = true;
                this.onRedeem.accept(data);
                dispose();
            }
        });
    }

    public void addCancelListener(final Runnable onCancel) {
        this.btnCancel.addActionListener(e -> {
            if (onCancel != null) {
                onCancel.run();
            }
            dispose();
        });
    }

    public String getUsername() { return username; }

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

    public boolean isAutoRenew() { return this.chkAutoRenew.isSelected(); }
    public boolean isRedeemed() { return this.redeemed; }
    public int getBonusCredits() { return this.bonusCredits; }

    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static final class RedeemData {
        public final int planCode;
        public final boolean autoRenew;

        public RedeemData(final int planCode, final boolean autoRenew) {
            this.planCode = planCode;
            this.autoRenew = autoRenew;
        }
    }

}
