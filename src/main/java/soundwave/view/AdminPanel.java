package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

/**
 * Panel representing the main dashboard for the Administrator, organized in tabs with input forms.
 */
public final class AdminPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 20f;
    private static final float SECTION_FONT_SIZE = 15f;
    private static final int BORDER_SIZE = 15;
    private static final int TITLE_MARGIN = 10;
    private static final int FIELD_COLUMNS = 20;
    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 32;
    private static final int INSET_GAP = 8;
    private static final int DEFAULT_SONGS_ROWS = 5;
    private static final int DEFAULT_PADDING = 5;

    // --- Campi di testo per Inserimento Artista (OP 7) ---
    private final JTextField txtStageName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtRealName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtRealSurname = new JTextField(FIELD_COLUMNS);
    private final JTextField txtBirthDate = new JTextField(FIELD_COLUMNS);
    private final JTextField txtProvenanceCountry = new JTextField(FIELD_COLUMNS);
    private final JTextField txtBiography = new JTextField(FIELD_COLUMNS);
    private final JTextField txtStartYear = new JTextField(FIELD_COLUMNS);
    private final JTextField txtArtistType = new JTextField(FIELD_COLUMNS);
    private final JButton btnSaveArtist = new JButton("Salva Artista");

    // --- Campi per Inserimento Album e Brani (OP 8) ---
    private final JTextField txtAlbumArtistCode = new JTextField(FIELD_COLUMNS);
    private final JTextField txtAlbumTitle = new JTextField(FIELD_COLUMNS);
    private final JTextField txtAlbumReleaseDate = new JTextField(FIELD_COLUMNS);
    private final JTextField txtAlbumLabel = new JTextField(FIELD_COLUMNS);
    private final JTextArea txtAlbumSongsInput = new JTextArea(DEFAULT_SONGS_ROWS, 20); 
    private final JButton btnSaveAlbum = new JButton("Salva Album");

    // --- Campi di testo per inserimento Podcast (OP 9) ---
    private final JTextField txtPodcastArtistCode = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastCategory = new JTextField(FIELD_COLUMNS);
    private final JButton btnSavePodcast = new JButton("Salva Podcast");

    // --- Campi di testo per Inserimento Episodio (OP 10) ---
    private final JTextField txtEpisodePodcastCode = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeTitle = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeDuration = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeNumber = new JTextField(FIELD_COLUMNS);
    private final JButton btnSaveEpisode = new JButton("Salva Episodio");

    // --- Campi di testo per Inserimento Promozione (OP 6) ---
    private final JTextField txtPromoName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtDiscountPercentage = new JTextField(FIELD_COLUMNS);
    private final JButton btnSavePromotion = new JButton("Salva Promozione");

    // --- Area per Gestione Utenti (Sola Lettura) ---
    private final JButton btnFetchUsers = new JButton("Carica Utenti");
    private final DefaultTableModel usersTableModel = new DefaultTableModel(
        new Object[]{"Username", "Nome", "Cognome", "Email", "Paese", "Credito Bonus"}, 0) {
        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    };
    private final JTable tableUsers = new JTable(usersTableModel);

    // --- Campi di testo per Statistiche Globali (OP 22) ---
    private final javax.swing.JComboBox<Integer> comboStatsYear = new javax.swing.JComboBox<>(
        new Integer[]{2026, 2025, 2024});
    private final JButton btnFetchGlobalStats = new JButton("Carica Statistiche");
    private final JTextArea txtStatsOutput = new JTextArea(10, 30);

    private final JButton btnBack = new JButton("Disconnetti / Cambia Ruolo");

    /**
     * Builds a new AdminPanel.
     */
    public AdminPanel() {
        super();
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        final JLabel titleLabel = new JLabel("Pannello di Controllo - Amministratore", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        this.add(titleLabel, BorderLayout.NORTH);

        final JTabbedPane insertTabbedPane = new JTabbedPane();
        insertTabbedPane.addTab("Artista", wrapInScrollPane(createArtistFormPanel()));
        insertTabbedPane.addTab("Album & Brani", wrapInScrollPane(createAlbumFormPanel()));
        insertTabbedPane.addTab("Podcast", wrapInScrollPane(createPodcastFormPanel()));
        insertTabbedPane.addTab("Episodio", wrapInScrollPane(createEpisodeFormPanel()));
        insertTabbedPane.addTab("Promozione", wrapInScrollPane(createPromotionFormPanel()));

        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Nuovi Inserimenti", insertTabbedPane);
        mainTabbedPane.addTab("Statistiche Piattaforma", createStatsPanel());
        mainTabbedPane.addTab("Gestione Utenti", createUsersPanel());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_PADDING, 0, 0, 0));
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper to wrap a form panel inside a scrollpane for safety on smaller screens.
     * 
     * @param panel the panel to wrap.
     * 
     * @return the scroll pane containing the panel.
     */
    private JScrollPane wrapInScrollPane(final JPanel panel) {
        final JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    /**
     * Creates the form panel for registering a new artist.
     * 
     * @return the artist form panel.
     */
    private JPanel createArtistFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addSectionHeader(panel, gbc, row, "Registrazione Nuovo Artista");
        row++;

        addFormField(panel, gbc, row, "Nome d'arte:", this.txtStageName);
        row++;
        addFormField(panel, gbc, row, "Nome reale:", this.txtRealName);
        row++;
        addFormField(panel, gbc, row, "Cognome reale:", this.txtRealSurname);
        row++;
        addFormField(panel, gbc, row, "Data di nascita (YYYY-MM-DD):", this.txtBirthDate);
        row++;
        addFormField(panel, gbc, row, "Paese di provenienza:", this.txtProvenanceCountry);
        row++;
        addFormField(panel, gbc, row, "Biografia:", this.txtBiography);
        row++;
        addFormField(panel, gbc, row, "Anno inizio attività:", this.txtStartYear);
        row++;
        addFormField(panel, gbc, row, "Tipo artista:", this.txtArtistType);
        row++;

        addCenteredButton(panel, gbc, row, this.btnSaveArtist);
        return panel;
    }

    /**
     * Creates the form panel for inserting albums and songs.
     * 
     * @return the album form panel.
     */
    private JPanel createAlbumFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addSectionHeader(panel, gbc, row, "Inserimento Album e Brani Correlati");
        row++;

        addFormField(panel, gbc, row, "Codice Artista:", this.txtAlbumArtistCode);
        row++;
        addFormField(panel, gbc, row, "Titolo Album:", this.txtAlbumTitle);
        row++;
        addFormField(panel, gbc, row, "Data Pubblicazione (YYYY-MM-DD):", this.txtAlbumReleaseDate);
        row++;
        addFormField(panel, gbc, row, "Casa Discografica:", this.txtAlbumLabel);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Elenco Brani:"), gbc);

        gbc.gridx = 1;
        this.txtAlbumSongsInput.setLineWrap(true);
        panel.add(new JScrollPane(this.txtAlbumSongsInput), gbc);
        row++;

        addCenteredButton(panel, gbc, row, this.btnSaveAlbum);
        return panel;
    }

    /**
     * Creates the form panel for creating a new podcast.
     * 
     * @return the podcast form panel.
     */
    private JPanel createPodcastFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addSectionHeader(panel, gbc, row, "Creazione Nuovo Podcast");
        row++;

        addFormField(panel, gbc, row, "Codice Artista:", this.txtPodcastArtistCode);
        row++;
        addFormField(panel, gbc, row, "Nome Podcast:", this.txtPodcastName);
        row++;
        addFormField(panel, gbc, row, "Descrizione:", this.txtPodcastDescription);
        row++;
        addFormField(panel, gbc, row, "Categoria:", this.txtPodcastCategory);
        row++;

        addCenteredButton(panel, gbc, row, this.btnSavePodcast);
        return panel;
    }

    /**
     * Creates the form panel for adding an episode to a podcast.
     * 
     * @return the episode form panel.
     */
    private JPanel createEpisodeFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addSectionHeader(panel, gbc, row, "Aggiungi Episodio al Podcast");
        row++;

        addFormField(panel, gbc, row, "Codice Podcast:", this.txtEpisodePodcastCode);
        row++;
        addFormField(panel, gbc, row, "Titolo Episodio:", this.txtEpisodeTitle);
        row++;
        addFormField(panel, gbc, row, "Durata (secondi):", this.txtEpisodeDuration);
        row++;
        addFormField(panel, gbc, row, "Descrizione:", this.txtEpisodeDescription);
        row++;
        addFormField(panel, gbc, row, "Numero Episodio:", this.txtEpisodeNumber);
        row++;

        addCenteredButton(panel, gbc, row, this.btnSaveEpisode);
        return panel;
    }

    /**
     * Creates the form panel for managing promotions and discounts.
     * 
     * @return the promotion form panel.
     */
    private JPanel createPromotionFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addSectionHeader(panel, gbc, row, "Gestione Promozioni e Sconti");
        row++;

        addFormField(panel, gbc, row, "Nome Promozione:", this.txtPromoName);
        row++;
        addFormField(panel, gbc, row, "Sconto (%):", this.txtDiscountPercentage);
        row++;

        addCenteredButton(panel, gbc, row, this.btnSavePromotion);
        return panel;
    }

    /**
     * Creates the panel for user management.
     * 
     * @return the users panel.
     */
    private JPanel createUsersPanel() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        this.btnFetchUsers.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        panel.add(this.btnFetchUsers, BorderLayout.NORTH);

        this.tableUsers.setFillsViewportHeight(true);
        panel.add(new JScrollPane(this.tableUsers), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the panel for platform statistics.
     * 
     * @return the statistics panel.
     */
    private JPanel createStatsPanel() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        final JPanel topPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Anno di riferimento:"), gbc);

        gbc.gridx = 1;
        // Inseriamo la ComboBox al posto del JTextField
        topPanel.add(this.comboStatsYear, gbc);

        this.btnFetchGlobalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(this.btnFetchGlobalStats, gbc);

        panel.add(topPanel, BorderLayout.NORTH);

        this.txtStatsOutput.setEditable(false);
        panel.add(new JScrollPane(this.txtStatsOutput), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Adds a section header label to a form panel.
     * 
     * @param panel the target panel.
     * @param gbc the grid bag constraints.
     * @param row the grid row index.
     * @param title the header title.
     */
    private void addSectionHeader(final JPanel panel, final GridBagConstraints gbc, final int row, final String title) {
        final JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(SECTION_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1; // Reset
    }

    /**
     * Adds a form field with a label and text field to a panel.
     * 
     * @param panel the target panel.
     * @param gbc the grid bag constraints.
     * @param row the grid row index.
     * @param labelText the text for the label.
     * @param field the text field component.
     */
    private void addFormField(final JPanel panel, final GridBagConstraints gbc, final int row, 
                            final String labelText, final JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /**
     * Adds a centered action button to a form panel.
     * 
     * @param panel the target panel.
     * @param gbc the grid bag constraints.
     * @param row the grid row index.
     * @param button the button component.
     */
    private void addCenteredButton(final JPanel panel, final GridBagConstraints gbc, final int row, final JButton button) {
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(button, gbc);
    }

    /**
     * Gets the artist's stage name.
     * 
     * @return the stage name.
     */
    public String getArtistStageName() {
        return this.txtStageName.getText();
    }

    /**
     * Gets the artist's real name.
     * 
     * @return the real name.
     */
    public String getArtistRealName() {
        return this.txtRealName.getText();
    }

    /**
     * Gets the artist's real surname.
     * 
     * @return the real surname.
     */
    public String getArtistRealSurname() {
        return this.txtRealSurname.getText();
    }

    /**
     * Gets the artist's birth date.
     * 
     * @return the birth date.
     */
    public String getArtistBirthDate() {
        return this.txtBirthDate.getText();
    }

    /**
     * Gets the artist's provenance country.
     * 
     * @return the provenance country.
     */
    public String getArtistProvenanceCountry() {
        return this.txtProvenanceCountry.getText();
    }

    /**
     * Gets the artist's biography.
     * 
     * @return the biography.
     */
    public String getArtistBiography() {
        return this.txtBiography.getText();
    }

    /**
     * Gets the artist's start year.
     * 
     * @return the start year.
     */
    public String getArtistStartYear() {
        return this.txtStartYear.getText();
    }

    /**
     * Gets the artist's type.
     * 
     * @return the artist type.
     */
    public String getArtistType() {
        return this.txtArtistType.getText();
    }

    /**
     * Gets the album's artist code.
     * 
     * @return the artist code.
     */
    public String getAlbumArtistCode() {
        return this.txtAlbumArtistCode.getText();
    }

    /**
     * Gets the album title.
     * 
     * @return the album title.
     */
    public String getAlbumTitle() {
        return this.txtAlbumTitle.getText();
    }

    /**
     * Gets the album release date.
     * 
     * @return the release date.
     */
    public String getAlbumReleaseDate() {
        return this.txtAlbumReleaseDate.getText();
    }

    /**
     * Gets the album record label.
     * 
     * @return the record label.
     */
    public String getAlbumLabel() {
        return this.txtAlbumLabel.getText();
    }

    /**
     * Gets the input text for album songs.
     * 
     * @return the songs input text.
     */
    public String getAlbumSongsInput() {
        return this.txtAlbumSongsInput.getText();
    }

    /**
     * Gets the podcast's artist code.
     * 
     * @return the artist code.
     */
    public String getPodcastArtistCode() {
        return this.txtPodcastArtistCode.getText();
    }

    /**
     * Gets the podcast name.
     * 
     * @return the podcast name.
     */
    public String getPodcastName() {
        return this.txtPodcastName.getText();
    }

    /**
     * Gets the podcast description.
     * 
     * @return the podcast description.
     */
    public String getPodcastDescription() {
        return this.txtPodcastDescription.getText();
    }

    /**
     * Gets the podcast category.
     * 
     * @return the podcast category.
     */
    public String getPodcastCategory() {
        return this.txtPodcastCategory.getText();
    }

    /**
     * Gets the episode's podcast code.
     * 
     * @return the podcast code.
     */
    public String getEpisodePodcastCode() {
        return this.txtEpisodePodcastCode.getText();
    }

    /**
     * Gets the episode title.
     * 
     * @return the episode title.
     */
    public String getEpisodeTitle() {
        return this.txtEpisodeTitle.getText();
    }

    /**
     * Gets the episode duration.
     * 
     * @return the episode duration.
     */
    public String getEpisodeDuration() {
        return this.txtEpisodeDuration.getText();
    }

    /**
     * Gets the episode description.
     * 
     * @return the episode description.
     */
    public String getEpisodeDescription() {
        return this.txtEpisodeDescription.getText();
    }

    /**
     * Gets the episode number.
     * 
     * @return the episode number.
     */
    public String getEpisodeNumber() {
        return this.txtEpisodeNumber.getText();
    }

    /**
     * Gets the promotion name.
     * 
     * @return the promotion name.
     */
    public String getPromoName() {
        return this.txtPromoName.getText();
    }

    /**
     * Gets the discount percentage.
     * 
     * @return the discount percentage.
     */
    public String getDiscountPercentage() {
        return this.txtDiscountPercentage.getText();
    }

    /**
     * Sets the statistics output text.
     * 
     * @param text the statistics text to set.
     */
    public void setStatsOutputText(final String text) {
        this.txtStatsOutput.setText(text);
    }

    /**
     * Gets the statistics year from the dropdown menu.
     * 
     * @return the statistics year as a String.
     */
    public String getStatsYear() {
        final Integer selectedYear = (Integer) this.comboStatsYear.getSelectedItem();
        return selectedYear != null ? selectedYear.toString() : "";
    }

    /**
     * Sets the users data in the table.
     * 
     * @param usersData a list of object arrays representing user rows.
     */
    public void setUsersTableData(final java.util.List<Object[]> usersData) {
        this.usersTableModel.setRowCount(0);
        for (final Object[] row : usersData) {
            this.usersTableModel.addRow(row);
        }
    }

    /**
     * Adds an action listener to the save artist button.
     * 
     * @param listener the action listener.
     */
    public void addSaveArtistListener(final ActionListener listener) {
        this.btnSaveArtist.addActionListener(listener);
    }

    /**
     * Adds an action listener to the save album button.
     * 
     * @param listener the action listener.
     */
    public void addSaveAlbumListener(final ActionListener listener) {
        this.btnSaveAlbum.addActionListener(listener);
    }

    /**
     * Adds an action listener to the save podcast button.
     * 
     * @param listener the action listener.
     */
    public void addSavePodcastListener(final ActionListener listener) {
        this.btnSavePodcast.addActionListener(listener);
    }

    /**
     * Adds an action listener to the save episode button.
     * 
     * @param listener the action listener.
     */
    public void addSaveEpisodeListener(final ActionListener listener) {
        this.btnSaveEpisode.addActionListener(listener);
    }

    /**
     * Adds an action listener to the save promotion button.
     * 
     * @param listener the action listener.
     */
    public void addSavePromotionListener(final ActionListener listener) {
        this.btnSavePromotion.addActionListener(listener);
    }

    /**
     * Adds an action listener to the fetch stats button.
     * 
     * @param listener the action listener.
     */
    public void addFetchStatsListener(final ActionListener listener) {
        this.btnFetchGlobalStats.addActionListener(listener);
    }

    /**
     * Adds an action listener to the back button.
     * 
     * @param listener the action listener.
     */
    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }

    /**
     * Adds an action listener to the fetch users button.
     * 
     * @param listener the action listener.
     */
    public void addFetchUsersListener(final ActionListener listener) {
        this.btnFetchUsers.addActionListener(listener);
    }

    /**
     * Clears all text fields and text areas across all forms in the admin panel.
     */
    public void clearAllForms() {
        final JTextComponent[] textComponents = {
            // Artist Form
            this.txtStageName, this.txtRealName, this.txtRealSurname, 
            this.txtBirthDate, this.txtProvenanceCountry, this.txtBiography, 
            this.txtStartYear, this.txtArtistType,
            // Album & Songs Form
            this.txtAlbumArtistCode, this.txtAlbumTitle, this.txtAlbumReleaseDate, 
            this.txtAlbumLabel, this.txtAlbumSongsInput,
            // Podcast Form
            this.txtPodcastArtistCode, this.txtPodcastName, 
            this.txtPodcastDescription, this.txtPodcastCategory,
            // Episode Form
            this.txtEpisodePodcastCode, this.txtEpisodeTitle, 
            this.txtEpisodeDuration, this.txtEpisodeDescription, this.txtEpisodeNumber,
            // Promotion Form
            this.txtPromoName, this.txtDiscountPercentage,
        };

        for (final JTextComponent component : textComponents) {
            component.setText("");
        }
    }
}
