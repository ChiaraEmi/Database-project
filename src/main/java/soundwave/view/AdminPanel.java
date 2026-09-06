package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import soundwave.data.Artist;
import soundwave.data.Podcast;

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
    private static final int BUTTON_WIDTH = 220;
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
    private final JComboBox<Artist> comboAlbumArtist = new JComboBox<>();
    private final JTextField txtAlbumTitle = new JTextField(FIELD_COLUMNS);
    private final JTextField txtAlbumReleaseDate = new JTextField(FIELD_COLUMNS);
    private final JTextField txtAlbumLabel = new JTextField(FIELD_COLUMNS);
    private final JTextArea txtAlbumSongsInput = new JTextArea(DEFAULT_SONGS_ROWS, 20); 
    private final JButton btnSaveAlbum = new JButton("Salva Album");

    // --- Campi di testo per inserimento Podcast (OP 9) ---
    private final JComboBox<Artist> comboPodcastArtist = new JComboBox<>();
    private final JTextField txtPodcastName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastCategory = new JTextField(FIELD_COLUMNS);
    private final JButton btnSavePodcast = new JButton("Salva Podcast");

    // --- Campi di testo per Inserimento Episodio (OP 10) ---
    private final JComboBox<Podcast> comboEpisodePodcast = new JComboBox<>();
    private final JTextField txtEpisodeTitle = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeDuration = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtEpisodeNumber = new JTextField(FIELD_COLUMNS);
    private final JButton btnSaveEpisode = new JButton("Salva Episodio");

    // --- Campi di testo per Inserimento Promozione (OP 6) ---
    private final JTextField txtPromoCode = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPromoName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPromoDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPromoStartDate = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPromoEndDate = new JTextField(FIELD_COLUMNS);
    private final JComboBox<String> comboDiscountType = new JComboBox<>(new String[]{"Percentuale", "Fisso"});
    private final JTextField txtDiscountValue = new JTextField(FIELD_COLUMNS);
    private final JTextField txtRequiredMonths = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPromoPlanCodes = new JTextField(FIELD_COLUMNS);
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
    private final JComboBox<Integer> comboStatsYear = new JComboBox<>(
        new Integer[]{2026, 2025, 2024});
    private final JButton btnFetchGlobalStats = new JButton("Carica Statistiche Globali");
    private final JButton btnFetchYearlyStats = new JButton("Carica Statistiche per Anno");
    private final JTextArea txtGlobalAlbumsOutput = new JTextArea(6, 30);
    private final JTextArea txtYearlyStatsOutput = new JTextArea(8, 30);

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
        addFormField(panel, gbc, row, "Nome:", this.txtRealName);
        row++;
        addFormField(panel, gbc, row, "Cognome:", this.txtRealSurname);
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

        this.comboAlbumArtist.setRenderer(new javax.swing.DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list, final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Artist) {
                    setText(((Artist) value).getStageName());
                }
                return this;
            }
        });

        int row = 0;
        addSectionHeader(panel, gbc, row, "Inserimento Album e Brani Correlati");
        row++;
        addFormComboField(panel, gbc, row, "Artista Album:", this.comboAlbumArtist);
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

        this.comboPodcastArtist.setRenderer(new javax.swing.DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list, final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Artist) {
                    setText(((Artist) value).getStageName());
                }
                return this;
            }
        });

        int row = 0;
        addSectionHeader(panel, gbc, row, "Creazione Nuovo Podcast");
        row++;

        addFormComboField(panel, gbc, row, "Autore Podcast:", this.comboPodcastArtist);
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

        this.comboEpisodePodcast.setRenderer(new javax.swing.DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list, final Object value, final int index,
                    final boolean isSelected, final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Podcast) {
                    setText(((Podcast) value).getName());
                }
                return this;
            }
        });

        int row = 0;
        addSectionHeader(panel, gbc, row, "Aggiungi Episodio al Podcast");
        row++;

        addFormComboField(panel, gbc, row, "Podcast:", this.comboEpisodePodcast);
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
     * Creates the form panel for managing promotions and discounts (Imported fields from block 1).
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

        addFormField(panel, gbc, row++, "Codice Promozione:", this.txtPromoCode);
        addFormField(panel, gbc, row++, "Nome Promozione:", this.txtPromoName);
        addFormField(panel, gbc, row++, "Descrizione:", this.txtPromoDescription);
        addFormField(panel, gbc, row++, "Data Inizio (YYYY-MM-DD):", this.txtPromoStartDate);
        addFormField(panel, gbc, row++, "Data Fine (YYYY-MM-DD):", this.txtPromoEndDate);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(new JLabel("Tipo Sconto:"), gbc);
        gbc.gridx = 1;
        
        panel.add(this.comboDiscountType, gbc);

        addFormField(panel, gbc, row++, "Valore Sconto:", this.txtDiscountValue);
        addFormField(panel, gbc, row++, "Mesi Richiesti (opzionale):", this.txtRequiredMonths);
        addFormField(panel, gbc, row++, "Piani Abbonamento (codici separati da virgola):", this.txtPromoPlanCodes);
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

        final JPanel globalPanel = new JPanel(new BorderLayout(0, 5));
        globalPanel.setBorder(BorderFactory.createTitledBorder("Album con Media Voto Superiore alla Media Globale"));

        this.btnFetchGlobalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        final JPanel topGlobalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topGlobalPanel.add(this.btnFetchGlobalStats);
        globalPanel.add(topGlobalPanel, BorderLayout.NORTH);

        this.txtGlobalAlbumsOutput.setEditable(false);
        globalPanel.add(new JScrollPane(this.txtGlobalAlbumsOutput), BorderLayout.CENTER);

        final JPanel yearlyPanel = new JPanel(new BorderLayout(0, 5));
        yearlyPanel.setBorder(BorderFactory.createTitledBorder("Statistiche Annuali"));

        final JPanel topYearPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        topYearPanel.add(new JLabel("Anno di riferimento:"), gbc);

        gbc.gridx = 1;
        topYearPanel.add(this.comboStatsYear, gbc);

        this.btnFetchYearlyStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        topYearPanel.add(this.btnFetchYearlyStats, gbc);

        yearlyPanel.add(topYearPanel, BorderLayout.NORTH);

        this.txtYearlyStatsOutput.setEditable(false);
        yearlyPanel.add(new JScrollPane(this.txtYearlyStatsOutput), BorderLayout.CENTER);

        final JPanel containerPanel = new JPanel(new java.awt.GridLayout(2, 1, 0, INSET_GAP));
        containerPanel.add(globalPanel);
        containerPanel.add(yearlyPanel);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addSectionHeader(final JPanel panel, final GridBagConstraints gbc, final int row, final String title) {
        final JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(SECTION_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
    }

    private void addFormField(final JPanel panel, final GridBagConstraints gbc, final int row, 
                            final String labelText, final JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addFormComboField(final JPanel panel, final GridBagConstraints gbc, final int row, 
                            final String labelText, final JComboBox<?> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    private void addCenteredButton(final JPanel panel, final GridBagConstraints gbc, final int row, final JButton button) {
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(button, gbc);
    }

    public void addFetchGlobalAlbumsListener(final ActionListener listener) {
        this.btnFetchGlobalStats.addActionListener(listener);
    }

    public void addFetchYearlyStatsListener(final ActionListener listener) {
        this.btnFetchYearlyStats.addActionListener(listener);
    }

    public String getArtistStageName() { return this.txtStageName.getText(); }
    public String getArtistRealName() { return this.txtRealName.getText(); }
    public String getArtistRealSurname() { return this.txtRealSurname.getText(); }
    public String getArtistBirthDate() { return this.txtBirthDate.getText(); }
    public String getArtistProvenanceCountry() { return this.txtProvenanceCountry.getText(); }
    public String getArtistBiography() { return this.txtBiography.getText(); }
    public String getArtistStartYear() { return this.txtStartYear.getText(); }
    public String getArtistType() { return this.txtArtistType.getText(); }

    public String getAlbumArtistCode() {
        final Artist selectedArtist = (Artist) this.comboAlbumArtist.getSelectedItem();
        return selectedArtist != null ? String.valueOf(selectedArtist.getArtistCode()) : "";
    }

    public String getAlbumTitle() { return this.txtAlbumTitle.getText(); }
    public String getAlbumReleaseDate() { return this.txtAlbumReleaseDate.getText(); }
    public String getAlbumLabel() { return this.txtAlbumLabel.getText(); }
    public String getAlbumSongsInput() { return this.txtAlbumSongsInput.getText(); }

    public String getPodcastArtistCode() {
        final Artist selectedArtist = (Artist) this.comboPodcastArtist.getSelectedItem();
        return selectedArtist != null ? String.valueOf(selectedArtist.getArtistCode()) : "";
    }

    public String getPodcastName() { return this.txtPodcastName.getText(); }
    public String getPodcastDescription() { return this.txtPodcastDescription.getText(); }
    public String getPodcastCategory() { return this.txtPodcastCategory.getText(); }

    public String getEpisodePodcastCode() {
        final Podcast selectedPodcast = (Podcast) this.comboEpisodePodcast.getSelectedItem();
        return selectedPodcast != null ? String.valueOf(selectedPodcast.getPodcastCode()) : "";
    }

    public String getEpisodeTitle() { return this.txtEpisodeTitle.getText(); }

    // --- Getter Promozione aggiornati al 1° blocco ---
    public String getPromoCode() { return this.txtPromoCode.getText(); }
    public String getPromoDescription() { return this.txtPromoDescription.getText(); }
    public String getPromoStartDate() { return this.txtPromoStartDate.getText(); }
    public String getPromoEndDate() { return this.txtPromoEndDate.getText(); }
    public String getDiscountType() { return (String) this.comboDiscountType.getSelectedItem(); }
    public String getDiscountValue() { return this.txtDiscountValue.getText(); }
    public String getRequiredMonths() { return this.txtRequiredMonths.getText(); }
    public String getPromoPlanCodes() { return this.txtPromoPlanCodes.getText(); }


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
     * Gets the statistics year from the dropdown menu.
     * 
     * @return the statistics year as a String.
     */
    public String getStatsYear() {
        final Integer selectedYear = (Integer) this.comboStatsYear.getSelectedItem();
        return selectedYear != null ? selectedYear.toString() : "";
    }

    /**
     * Sets the available artists for albums in the dropdown menu.
     * 
     * @param artists the list of artist objects eligible for albums.
     */
    public void setAlbumArtists(final List<Artist> artists) {
        this.comboAlbumArtist.removeAllItems();
        for (final Artist artist : artists) {
            this.comboAlbumArtist.addItem(artist);
        }
    }

    public void setPodcastAuthors(final List<Artist> authors) {
        this.comboPodcastArtist.removeAllItems();
        for (final Artist artist : authors) {
            this.comboPodcastArtist.addItem(artist);
        }
    }

    public void setPodcasts(final List<Podcast> podcasts) {
        this.comboEpisodePodcast.removeAllItems();
        for (final Podcast podcast : podcasts) {
            this.comboEpisodePodcast.addItem(podcast);
        }
    }

    public void setGlobalAlbumsOutputText(final String text) {
        this.txtGlobalAlbumsOutput.setText(text);
    }

    public void setYearlyStatsOutputText(final String text) {
        this.txtYearlyStatsOutput.setText(text);
    }
    

    public void setUsersTableData(final List<Object[]> usersData) {
        this.usersTableModel.setRowCount(0);
        for (final Object[] row : usersData) {
            this.usersTableModel.addRow(row);
        }
    }

    public void addSaveArtistListener(final ActionListener listener) {
        this.btnSaveArtist.addActionListener(listener);
    }

    public void addSaveAlbumListener(final ActionListener listener) {
        this.btnSaveAlbum.addActionListener(listener);
    }

    public void addSavePodcastListener(final ActionListener listener) {
        this.btnSavePodcast.addActionListener(listener);
    }

    public void addSaveEpisodeListener(final ActionListener listener) {
        this.btnSaveEpisode.addActionListener(listener);
    }

    public void addSavePromotionListener(final ActionListener listener) {
        this.btnSavePromotion.addActionListener(listener);
    }

    public void addFetchStatsListener(final ActionListener listener) {
        this.btnFetchGlobalStats.addActionListener(listener);
    }

    public void addBackListener(final ActionListener listener) {
        this.btnBack.addActionListener(listener);
    }

    public void addFetchUsersListener(final ActionListener listener) {
        this.btnFetchUsers.addActionListener(listener);
    }

    public void clearAllForms() {
        final JTextComponent[] textComponents = {
            // Artist Form
            this.txtStageName, this.txtRealName, this.txtRealSurname, 
            this.txtBirthDate, this.txtProvenanceCountry, this.txtBiography, 
            this.txtStartYear, this.txtArtistType,
            // Album & Songs Form
            this.txtAlbumTitle, this.txtAlbumReleaseDate, 
            this.txtAlbumLabel, this.txtAlbumSongsInput,
            // Podcast Form
            this.txtPodcastName, 
            this.txtPodcastDescription, this.txtPodcastCategory,
            // Episode Form
            this.txtEpisodeTitle, 
            this.txtEpisodeDuration, this.txtEpisodeDescription, this.txtEpisodeNumber,
            // Promotion Form
            this.txtPromoName, this.txtPromoDescription, this.txtPromoStartDate, txtPromoEndDate, this.txtDiscountValue,
            this.txtRequiredMonths, this.txtPromoPlanCodes,
        };

        for (final JTextComponent component : textComponents) {
            component.setText("");
        }
    }
}
