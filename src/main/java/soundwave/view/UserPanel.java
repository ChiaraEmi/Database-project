package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import soundwave.controller.Controller;
import soundwave.data.Plan;
import soundwave.view.ActivateSubscriptionDialog;

/**
 * Panel representing the main user dashboard.
 */
public final class UserPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 22f;
    private static final int BORDER_SIZE = 20;
    private static final int TITLE_MARGIN = 10;
    private static final int FIELD_COLUMNS = 15;
    private static final int SMALL_FIELD_COLUMNS = 5;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 35;
    private static final int INSET_GAP = 6;

    private transient Controller controller;

    private ActivateSubscriptionDialog activedialog;
    private SubscriptionStatusDialog statusDialog;
    private final String currentUsername;

    // --- Tab 1: Abbonamento ---
    private final JButton btnActivateSubscription = new JButton("Attiva Sottoscrizione");
    private final JButton btnRedeemBonus = new JButton("Riscatto con Crediti Bonus");
    private final JButton btnViewSubscriptionStatus = new JButton("Stato e Storico Transazioni");

    // --- Tab 2: Esplora Catalogo ---
    private final JComboBox<String> comboGenre = new JComboBox<>(new String[]{"Rock", "Pop", "Jazz", "Classica", "Hip Hop"});
    private final JButton btnFilterByGenre = new JButton("Filtra Brani per Genere");
    private final JTextField txtArtistProfileName = new JTextField(FIELD_COLUMNS);
    private final JButton btnSearchArtist = new JButton("Visualizza Profilo Artista");

    // --- Tab 3: Libreria & Playlist ---
    private final JTextField txtPlaylistName = new JTextField(FIELD_COLUMNS);
    private final JComboBox<String> comboVisibility = new JComboBox<>(new String[]{"Privata", "Pubblica"});
    private final JCheckBox chkCollaborative = new JCheckBox("Collaborativa");
    private final JButton btnCreatePlaylist = new JButton("Crea Nuova Playlist");
    private final JButton btnToggleLike = new JButton("Aggiungi / Rimuovi Like");

    // Campi per Aggiunta Brano in Playlist
    private final JTextField txtAddPlaylistCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JTextField txtAddTrackCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JButton btnAddTrack = new JButton("Aggiungi Brano");

    // Campi per Rimozione Brano da Playlist
    private final JTextField txtRemovePlaylistCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JTextField txtRemoveTrackCode = new JTextField(SMALL_FIELD_COLUMNS);
    private final JButton btnRemoveTrack = new JButton("Rimuovi Brano");

    // --- Tab 4: Statistiche & Ascolti ---
    private final JButton btnFetchPersonalStats = new JButton("Visualizza Statistiche Annuali");
    private final JTextArea txtStatsOutput = new JTextArea(8, 30);

    private final JButton btnBack = new JButton("Torna alla Selezione Ruolo");

    /**
     * Builds a new UserPanel for the specified user.
     * 
     * @param username the username of the logged-in user.
     */
    public UserPanel(final String username) {
        super();
        this.currentUsername = username;
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JPanel headerPanel = new JPanel(new BorderLayout());

        final JLabel titleLabel = new JLabel("Area Utente", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        final JLabel userLabel = new JLabel("Utente: " + this.currentUsername);
        headerPanel.add(userLabel, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Abbonamento", createSubscriptionTab());
        mainTabbedPane.addTab("Esplora", createExploreTab());
        mainTabbedPane.addTab("Libreria", createLibraryTab());
        mainTabbedPane.addTab("Statistiche Personali", createStatsTab());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Creates the tab for subscription management (OP 2, 4, 5).
     * 
     * @return the subscription panel.
     */
    private JPanel createSubscriptionTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        final Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        this.btnActivateSubscription.setPreferredSize(buttonSize);
        gbc.gridy = 0;
        panel.add(this.btnActivateSubscription, gbc);

        this.btnRedeemBonus.setPreferredSize(buttonSize);
        gbc.gridy = 1;
        panel.add(this.btnRedeemBonus, gbc);

        this.btnViewSubscriptionStatus.setPreferredSize(buttonSize);
        gbc.gridy = 2;
        panel.add(this.btnViewSubscriptionStatus, gbc);

        return panel;
    }

    /**
     * Creates the tab for exploring catalog (OP 19, 20).
     * 
     * @return the explore panel.
     */
    private JPanel createExploreTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Genere:"), gbc);
        gbc.gridx = 1;
        panel.add(this.comboGenre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(this.btnFilterByGenre, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nome Artista:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtArtistProfileName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(this.btnSearchArtist, gbc);

        return panel;
    }

    /**
     * Creates the tab for personal library and playlists (OP 12, 14).
     * 
     * @return the library panel.
     */
    private JPanel createLibraryTab() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Creazione Playlist
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome Playlist:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPlaylistName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Visibilità:"), gbc);
        gbc.gridx = 1;
        panel.add(this.comboVisibility, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(this.chkCollaborative, gbc);

        gbc.gridy++;
        panel.add(this.btnCreatePlaylist, gbc);

        gbc.gridy++;
        panel.add(this.btnToggleLike, gbc);

        // Sezione: Aggiungi Brano
        gbc.gridy++;
        panel.add(new JLabel("--- Aggiungi Brano a Playlist ---"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Codice Playlist:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtAddPlaylistCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Codice Brano:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtAddTrackCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(this.btnAddTrack, gbc);

        // Sezione: Rimuovi Brano
        gbc.gridy++;
        panel.add(new JLabel("--- Rimuovi Brano da Playlist ---"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Codice Playlist:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtRemovePlaylistCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Codice Brano:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtRemoveTrackCode, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(this.btnRemoveTrack, gbc);

        return panel;
    }

    /**
     * Creates the tab for personal listening statistics.
     * 
     * @return the statistics panel.
     */
    private JPanel createStatsTab() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        this.btnFetchPersonalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        panel.add(this.btnFetchPersonalStats, BorderLayout.NORTH);

        this.txtStatsOutput.setEditable(false);
        panel.add(new JScrollPane(this.txtStatsOutput), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Gets the current logged-in username.
     * 
     * @return the username.
     */
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    /**
     * Gets the selected genre from the combo box.
     * 
     * @return the selected genre string.
     */
    public String getSelectedGenre() {
        return (String) this.comboGenre.getSelectedItem();
    }

    /**
     * Gets the artist name entered in the search field.
     * 
     * @return the searched artist name.
     */
    public String getSearchedArtist() {
        return this.txtArtistProfileName.getText();
    }

    /**
     * Gets the playlist name entered in the text field.
     * 
     * @return the playlist name.
     */
    public String getPlaylistName() {
        return this.txtPlaylistName.getText().trim();
    }

    /**
     * Gets the selected playlist visibility.
     * 
     * @return the visibility string.
     */
    public String getPlaylistVisibility() {
        return (String) this.comboVisibility.getSelectedItem();
    }

    /**
     * Checks whether the playlist is marked as collaborative.
     * 
     * @return true if collaborative, false otherwise.
     */
    public boolean isPlaylistCollaborative() {
        return this.chkCollaborative.isSelected();
    }

    /** 
     * Gets the playlist code for adding a track.
     * 
     * @return playlist code string.
     */
    public String getAddTrackPlaylistCode() {
        return this.txtAddPlaylistCode.getText().trim();
    }

    /** 
     * Gets the track code for adding.
     * 
     * @return track code string.
     */
    public String getAddTrackCode() {
        return this.txtAddTrackCode.getText().trim();
    }

    /** 
     * Gets the playlist code for removing a track.
     * 
     * @return playlist code string.
     */
    public String getRemoveTrackPlaylistCode() {
        return this.txtRemovePlaylistCode.getText().trim();
    }

    /** 
     * Gets the track code for removing.
     * 
     * @return track code string.
     */
    public String getRemoveTrackCode() {
        return this.txtRemoveTrackCode.getText().trim();
    }

    /**
     * Sets the text of the personal stats output area.
     * 
     * @param text the statistics text to display.
     */
    public void setPersonalStatsOutput(final String text) {
        this.txtStatsOutput.setText(text);
    }

    /**
     * Adds a listener for activating a subscription.
     * 
     * @param listener the listener to add.
     */
    public void addActivateSubscriptionListener(final ActionListener listener) {
        this.btnActivateSubscription.addActionListener(listener);
    }

    /**
     * Adds a listener for redeeming bonus credits.
     * 
     * @param listener the listener to add.
     */
    public void addRedeemBonusListener(final ActionListener listener) {
        this.btnRedeemBonus.addActionListener(listener);
    }

    /**
     * Adds a listener for viewing subscription status.
     * 
     * @param listener the listener to add.
     */
    public void addViewSubscriptionStatusListener(final ActionListener listener) {
        this.btnViewSubscriptionStatus.addActionListener(listener);
    }

    /**
     * Adds a listener for filtering tracks by genre.
     * 
     * @param listener the listener to add.
     */
    public void addFilterByGenreListener(final ActionListener listener) {
        this.btnFilterByGenre.addActionListener(listener);
    }

    /**
     * Adds a listener for searching an artist.
     * 
     * @param listener the listener to add.
     */
    public void addSearchArtistListener(final ActionListener listener) {
        this.btnSearchArtist.addActionListener(listener);
    }

    /**
     * Adds a listener for creating a new playlist.
     * 
     * @param listener the listener to add.
     */
    public void addCreatePlaylistListener(final ActionListener listener) {
        this.btnCreatePlaylist.addActionListener(listener);
    }

    /**
     * Adds a listener for toggling a like on a track.
     * 
     * @param listener the listener to add.
     */
    public void addToggleLikeListener(final ActionListener listener) {
        this.btnToggleLike.addActionListener(listener);
    }

    /**
     * Adds a listener for adding a track to a playlist.
     * 
     * @param listener the listener to add.
     */
    public void addAddTrackListener(final ActionListener listener) {
        this.btnAddTrack.addActionListener(listener);
    }

    /**
     * Adds a listener for removing a track from a playlist.
     * 
     * @param listener the listener to add.
     */
    public void addRemoveTrackListener(final ActionListener listener) {
        this.btnRemoveTrack.addActionListener(listener);
    }

    /**
     * Adds a listener for fetching personal statistics.
     * 
     * @param listener the listener to add.
     */
    public void addFetchPersonalStatsListener(final ActionListener listener) {
        this.btnFetchPersonalStats.addActionListener(listener);
    }

    /**
     * Adds a listener for returning to the role selection screen.
     * 
     * @param listener the listener to add.
     */
    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }

    public void showActivateSubscriptionDialog(final String username, final List<Plan> plans, final Consumer<ActivateSubscriptionDialog.SubscriptionData> onActivate) {
        final JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        this.activedialog = new ActivateSubscriptionDialog(parent, username, plans);

        // --- Listener per Codice Promozionale ---
        activedialog.addApplyPromotionListener(promoCode -> {
            // Verifica che il codice promozionale è valido
            if (this.controller != null) {
                int planCode = activedialog.getSelectedPlanCode();
                if (planCode <= 0) {
                    activedialog.showError("Seleziona prima un piano di abbonamento.");
                    return;
                }
                this.controller.verifyPromotionCode(promoCode, planCode, result -> {
                    boolean valid = (boolean) result[0];
                    if (valid) {
                        // Applica promozione
                        double discountValue = (double) result[1];
                        String discountType = (String) result[2];
                        double originalPrice = (double) result[3];

                        double discountedPrice = 0.0;
                        if ("Percentuale".equals(discountType)) {
                            discountedPrice = originalPrice * (1- discountValue / 100.0);
                        } else if ("Fisso".equals(discountType)) {
                            discountedPrice = Math.max(0, originalPrice - discountValue);
                        } 

                        activedialog.updatePriceWithDiscount(discountedPrice);
                        activedialog.setPromoCodeApplied(true);
                        activedialog.showSuccess("Promozione valida");
                    } else {
                        activedialog.showError("Codice promozionale non valido.");
                    }
                });
            }
            
        });

        activedialog.addActivateListener(onActivate);

        // --- Listener per Codice Invito ---
        activedialog.addVerifyInviteListener(inviteCode -> {
            // Verifica che il codice esista
            if (this.controller != null) {
                this.controller.verifyInviteCode(inviteCode, isValid -> {
                    if (isValid) {
                        // Applica sconto del 20%
                        double currentPrice = activedialog.getCurrentPrice();
                        double discountedPrice = currentPrice * 0.80;
                        activedialog.updatePriceWithDiscount(discountedPrice);
                        activedialog.setInviteCodeVerified(true);
                        activedialog.showSuccess("Codice invito valido! Sconto del 20% applicato.");
                    } else {
                        activedialog.showError("Codice invito non valido.");
                    }
                });
            }
        });

        activedialog.setVisible(true);
    }

    public void closeActivateSubscriptionDialog() {
        if (this.activedialog != null) {
            this.activedialog.closeDialog();
            this.activedialog = null;
        }
    }


    public void showSubscriptionStatusDialog(final String username) {
        if (this.statusDialog != null && this.statusDialog.isVisible()) {
            this.statusDialog.dispose();
        }
        
        this.statusDialog = new SubscriptionStatusDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            username
        );
        
        // Carica i dati
        loadSubscriptionData(username);
        
        this.statusDialog.setOnRefresh(() -> {
            loadSubscriptionData(username);
            this.statusDialog.showSuccess("Dati aggiornati");
        });
        
        this.statusDialog.setVisible(true);
    }

    private void loadSubscriptionData(String username) {
        if (this.controller != null) {
            try {
                List<Object[]> data = this.controller.getSubscriptionData(username);
                if (this.statusDialog != null) {
                    this.statusDialog.clear();
                    
                    if (data.isEmpty()) {
                        this.statusDialog.addSubscriptionBlock(
                            0, "Nessuna sottoscrizione trovata", "", "", "", false, "", "", List.of()
                        );
                    } else {
                        for (Object[] sub : data) {
                            int subCode = (int) sub[0];
                            String planType = (String) sub[1];
                            String startDate = (String) sub[2];
                            String endDate = (String) sub[3];
                            String status = (String) sub[4];
                            boolean autoRenew = (boolean) sub[5];
                            String promoCode = (String) sub[6];
                            String inviteCode = (String) sub[7];
                            @SuppressWarnings("unchecked")
                            List<String> transactions = (List<String>) sub[8];
                            
                            this.statusDialog.addSubscriptionBlock(
                                subCode, planType, startDate, endDate, status,
                                autoRenew, promoCode, inviteCode, transactions
                            );
                        }
                    }
                    this.statusDialog.refreshUI();
                }
            } catch (final Exception e) {
                e.printStackTrace();
                if (this.statusDialog != null) {
                    this.statusDialog.showError("Errore durante il caricamento dei dati.");
                }
            }
        }
    }

    public void showRedeemBonusDialog(final String username, final List<Plan> plans, 
                                  final int bonusCredits,
                                  final Consumer<RedeemBonusDialog.RedeemData> onRedeem) {
        
        final JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        final RedeemBonusDialog dialog = new RedeemBonusDialog(parent, username, plans, bonusCredits);
        
        dialog.addRedeemListener(onRedeem);
        dialog.addCancelListener(() -> {
            System.out.println("[DEBUG] Riscatto annullato");
        });
        
        dialog.setVisible(true);
    }


}
