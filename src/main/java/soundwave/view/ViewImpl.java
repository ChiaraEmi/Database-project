package soundwave.view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import soundwave.controller.Controller;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    private static final long serialVersionUID = 1L;
    public static final String FRAME_NAME = "Soundwave";
    private static final String ROLE_SELECTION_CARD = "ROLE_SELECTION";
    private static final String USER_CARD = "USER";
    private static final String ADMIN_CARD = "ADMIN";

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(layout);

    private final RoleSelectionPanel roleSelectionPanel;
    private final UserPanel userPanel;
    private final AdminPanel adminPanel;

    private Controller controller;

    /**
     * Builds a new ViewImpl with a custom close action.
     *
     * @param onClose the action to execute when the window closes
     */
    public ViewImpl(final Runnable onClose) {
        this(); // Chiama il costruttore base che inizializza i componenti
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(final java.awt.event.WindowEvent e) {
                onClose.run();
            }
        });
    }

    /**
     * Builds a new ViewImpl.
     */
    public ViewImpl() {
        setTitle(FRAME_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.roleSelectionPanel = new RoleSelectionPanel();
        mainPanel.add(roleSelectionPanel, ROLE_SELECTION_CARD);

        this.adminPanel = new AdminPanel();
        mainPanel.add(adminPanel, ADMIN_CARD);

        this.userPanel = new UserPanel();
        mainPanel.add(userPanel, USER_CARD);

        setContentPane(mainPanel);
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    /*@SuppressFBWarnings(
        value = "EI2",
        justification = "View needs reference to controller"
    )*/
    @Override
    public void setController(final Controller controller) {
        this.controller = controller;

        // Navigazione tra i pannelli
        this.roleSelectionPanel.addUtenteListener(e -> showPanel(USER_CARD));
        this.roleSelectionPanel.addAdminListener(e -> showPanel(ADMIN_CARD));
        this.adminPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));
        this.userPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));

        // Caricamento Utenti (Admin)
        this.adminPanel.addFetchUsersListener(e -> {
            if (this.controller != null) {
                this.controller.adminClickedLoadUsers();
            }
        });

        // --- Inserimento Artista (OP 7) ---
        this.adminPanel.addSaveArtistListener(e -> {
            if (this.controller != null) {
                final String stageName = this.adminPanel.getArtistStageName();
                final String name = this.adminPanel.getArtistRealName();
                final String surname = this.adminPanel.getArtistRealSurname();
                final String birthDate = this.adminPanel.getArtistBirthDate();
                final String country = this.adminPanel.getArtistProvenanceCountry();
                final String biography = this.adminPanel.getArtistBiography();
                final String artistType = this.adminPanel.getArtistType();
                
                int startYear = 0;
                try {
                    if (!this.adminPanel.getArtistStartYear().isBlank()) {
                        startYear = Integer.parseInt(this.adminPanel.getArtistStartYear());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato anno
                }

                this.controller.adminClickedSaveArtist(
                    stageName, name, surname, birthDate, country, biography, startYear, artistType
                );
            }
        });

        // --- Inserimento Album e Brani (OP 8) ---
        this.adminPanel.addSaveAlbumListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getAlbumArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getAlbumArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato codice artista
                }

                final String title = this.adminPanel.getAlbumTitle();
                final String releaseDate = this.adminPanel.getAlbumReleaseDate(); // <-- Aggiornato a String / getAlbumReleaseDate()
                final String label = this.adminPanel.getAlbumLabel();
                final String rawSongsText = this.adminPanel.getAlbumSongsInput();

                this.controller.adminClickedSaveAlbumWithSongs(artistCode, title, releaseDate, label, rawSongsText);
            }
        });
        
        // --- Inserimento Podcast (OP 9) ---
        this.adminPanel.addSavePodcastListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getPodcastArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getPodcastArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione errore formato codice artista
                }

                final String name = this.adminPanel.getPodcastName();
                final String description = this.adminPanel.getPodcastDescription();
                final String category = this.adminPanel.getPodcastCategory();

                this.controller.adminClickedSavePodcast(artistCode, name, description, category);
            }
        });

        // --- Statistiche Globali (OP 22) ---
        this.adminPanel.addFetchStatsListener(e -> {
            if (this.controller != null) {
                int year = 2026; // Anno di default
                try {
                    if (!this.adminPanel.getStatsYear().isBlank()) {
                        year = Integer.parseInt(this.adminPanel.getStatsYear());
                    }
                } catch (final NumberFormatException ex) {
                    // Gestione formato anno non valido
                }
                
                // Chiamata unica al controller
                this.controller.adminRequestedGlobalStats(year);
            }
        });
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void showPanel(final String panelName) {
        layout.show(mainPanel, panelName);
    }

    public RoleSelectionPanel getRoleSelectionPanel() {
        return this.roleSelectionPanel;
    }

    @Override
    public void showUsers(final java.util.List<soundwave.data.User> users) {
        final StringBuilder sb = new StringBuilder();
        for (final soundwave.data.User user : users) {
            sb.append(user.toString()).append("\n");
        }
        this.adminPanel.setUsersOutputText(sb.toString());
    }

    @Override
    public void showGlobalStats(final String statsText) {
        this.adminPanel.setStatsOutputText(statsText);
    }

    /*public UserPanel getUserPanel() {
        return userPanel;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }*/
}
