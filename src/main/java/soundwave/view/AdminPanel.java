package soundwave.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel representing the main dashboard for the Administrator, organized in tabs with input forms.
 */
public final class AdminPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final float TITLE_FONT_SIZE = 22f;
    private static final float SECTION_FONT_SIZE = 16f;
    private static final int BORDER_SIZE = 20;
    private static final int TITLE_MARGIN = 10;
    private static final int FIELD_COLUMNS = 15;
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 35;
    private static final int INSET_GAP = 6;

    // --- Campi di testo per Inserimento Artista (OP 7) ---
    private final JTextField txtStageName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtNationality = new JTextField(FIELD_COLUMNS);
    private final JButton btnSaveArtist = new JButton("Salva Artista");

    // --- Campi di testo per inserimento Podcast (OP 9) ---
    private final JTextField txtPodcastArtistCode = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastDescription = new JTextField(FIELD_COLUMNS);
    private final JTextField txtPodcastCategory = new JTextField(FIELD_COLUMNS);
    private final JButton btnSavePodcast = new JButton("Salva Podcast");

    // --- Campi di testo per Inserimento Promozione (OP 6) ---
    private final JTextField txtPromoName = new JTextField(FIELD_COLUMNS);
    private final JTextField txtDiscountPercentage = new JTextField(FIELD_COLUMNS);
    private final JButton btnSavePromotion = new JButton("Salva Promozione");

    // --- Area per Statistiche Globali (OP 22) ---
    private final JButton btnFetchGlobalStats = new JButton("Carica Statistiche Globali");
    private final JTextArea txtStatsOutput = new JTextArea(10, 30);

    private final JButton btnBack = new JButton("Torna alla Selezione Ruolo");

    /**
     * Builds a new AdminPanel.
     */
    public AdminPanel() {
        super();
        this.setLayout(new BorderLayout(0, TITLE_MARGIN));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        // Intestazione principale
        final JLabel titleLabel = new JLabel("Pannello Amministratore", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        this.add(titleLabel, BorderLayout.NORTH);

        // Creazione delle schede principali (Artista e Podcast uniti nella prima scheda)
        final JPanel artistAndPodcastContainer = new JPanel();
        artistAndPodcastContainer.setLayout(new BoxLayout(artistAndPodcastContainer, BoxLayout.PAGE_AXIS));
        artistAndPodcastContainer.add(createArtistFormPanel());
        artistAndPodcastContainer.add(new JSeparator(JSeparator.HORIZONTAL));
        artistAndPodcastContainer.add(createPodcastFormPanel());

        final JScrollPane artistPodcastScrollPane = new JScrollPane(artistAndPodcastContainer);
        artistPodcastScrollPane.setBorder(null);

        final JTabbedPane mainTabbedPane = new JTabbedPane();
        mainTabbedPane.addTab("Inserimento Artista", artistPodcastScrollPane);
        mainTabbedPane.addTab("Inserimento Promozione", createPromotionFormPanel());
        mainTabbedPane.addTab("Statistiche Piattaforma", createStatsPanel());

        this.add(mainTabbedPane, BorderLayout.CENTER);

        // Pulsante inferiore di navigazione
        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(this.btnBack);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the form panel for Artist insertion (OP 7).
     */
    private JPanel createArtistFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Titolo Sezione
        final JLabel sectionLabel = new JLabel("Nuovo Artista");
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(SECTION_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);

        gbc.gridwidth = 1;

        // Nome d'arte
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nome d'arte:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtStageName, gbc);

        // Nazionalità
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nazionalità:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtNationality, gbc);

        // Pulsante Salva
        this.btnSaveArtist.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(this.btnSaveArtist, gbc);

        return panel;
    }

    /**
     * Creates the form panel for Podcast insertion.
     */
    private JPanel createPodcastFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Titolo Sezione
        final JLabel sectionLabel = new JLabel("Nuovo Podcast");
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(SECTION_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);

        gbc.gridwidth = 1;

        // Codice Artista
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Codice Artista:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPodcastArtistCode, gbc);

        // Nome Podcast
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nome Podcast:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPodcastName, gbc);

        // Descrizione
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Descrizione:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPodcastDescription, gbc);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPodcastCategory, gbc);

        // Pulsante Salva
        this.btnSavePodcast.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(this.btnSavePodcast, gbc);

        return panel;
    }

    /**
     * Creates the form panel for Promotion insertion (OP 6).
     */
    private JPanel createPromotionFormPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP);
        gbc.anchor = GridBagConstraints.WEST;

        // Titolo Sezione
        final JLabel sectionLabel = new JLabel("Nuova Promozione");
        sectionLabel.setFont(sectionLabel.getFont().deriveFont(SECTION_FONT_SIZE));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);

        gbc.gridwidth = 1;

        // Nome Promozione
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nome Promozione:"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtPromoName, gbc);

        // Sconto (%)
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Sconto (%):"), gbc);
        gbc.gridx = 1;
        panel.add(this.txtDiscountPercentage, gbc);

        // Pulsante Salva
        this.btnSavePromotion.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(this.btnSavePromotion, gbc);

        return panel;
    }

    /**
     * Creates the panel for Global Statistics (OP 22).
     */
    private JPanel createStatsPanel() {
        final JPanel panel = new JPanel(new BorderLayout(0, INSET_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(INSET_GAP, INSET_GAP, INSET_GAP, INSET_GAP));

        this.btnFetchGlobalStats.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        panel.add(this.btnFetchGlobalStats, BorderLayout.NORTH);

        this.txtStatsOutput.setEditable(false);
        panel.add(new JScrollPane(this.txtStatsOutput), BorderLayout.CENTER);

        return panel;
    }

    /* --- Getter per leggere il testo dai campi negli Handler/Controller --- */

    public String getArtistStageName() {
        return this.txtStageName.getText();
    }

    public String getArtistNationality() {
        return this.txtNationality.getText();
    }

    public String getPodcastArtistCode() {
        return this.txtPodcastArtistCode.getText();
    }

    public String getPodcastName() {
        return this.txtPodcastName.getText();
    }

    public String getPodcastDescription() {
        return this.txtPodcastDescription.getText();
    }

    public String getPodcastCategory() {
        return this.txtPodcastCategory.getText();
    }

    public String getPromoName() {
        return this.txtPromoName.getText();
    }

    public String getDiscountPercentage() {
        return this.txtDiscountPercentage.getText();
    }

    public void setStatsOutputText(final String text) {
        this.txtStatsOutput.setText(text);
    }

    /* --- Metodi per registrare gli Listener --- */

    public void addSaveArtistListener(final ActionListener listener) {
        this.btnSaveArtist.addActionListener(listener);
    }
    
    public void addSavePodcastListener(final ActionListener listener) {
        this.btnSavePodcast.addActionListener(listener);
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
}