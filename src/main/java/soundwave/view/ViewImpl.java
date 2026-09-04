package soundwave.view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import soundwave.controller.Controller;
import soundwave.data.Artist;
import soundwave.data.User;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    private static final long serialVersionUID = 1L;
    private static final int CURRENT_YEAR = 2026;
    private static final String FRAME_NAME = "Soundwave";
    private static final String ROLE_SELECTION_CARD = "ROLE_SELECTION";
    private static final String USER_CARD = "USER";
    private static final String ADMIN_CARD = "ADMIN";
    private static final String FORMAT_ERROR = "Format Error";
    private static final Logger LOGGER = Logger.getLogger(ViewImpl.class.getName());

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(layout);

    private final RoleSelectionPanel roleSelectionPanel;
    private final UserPanel userPanel;
    private final AdminPanel adminPanel;

    private transient Controller controller;

    /**
     * Builds a new ViewImpl with a custom close action.
     *
     * @param onClose the action to execute when the window closes
     */
    public ViewImpl(final Runnable onClose) {
        this();
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

    @Override
    public void setController(final Controller controller) {
        this.controller = controller;

        this.roleSelectionPanel.addUtenteListener(e -> showPanel(USER_CARD));
        this.roleSelectionPanel.addAdminListener(e -> {
            showPanel(ADMIN_CARD);
            if (this.controller != null) {
                final List<Artist> authors = this.controller.getPodcastAuthors();
                this.adminPanel.setPodcastAuthors(authors);
            }
        });
        this.adminPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));
        this.userPanel.addBackListener(e -> showPanel(ROLE_SELECTION_CARD));

        this.adminPanel.addFetchUsersListener(e -> {
            if (this.controller != null) {
                this.controller.adminClickedLoadUsers();
            }
        });

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
                    LOGGER.log(Level.SEVERE, "Invalid artist start year format", ex);
                    JOptionPane.showMessageDialog(this, "Please enter a valid start year (e.g., 2020).", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.adminClickedSaveArtist(
                    stageName, name, surname, birthDate, country, biography, startYear, artistType
                );

                if (success) {
                    showSuccess("Artista inserito con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addSaveAlbumListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getAlbumArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getAlbumArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid album artist code format", ex);
                    JOptionPane.showMessageDialog(this, "Artist code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String title = this.adminPanel.getAlbumTitle();
                final String releaseDate = this.adminPanel.getAlbumReleaseDate();
                final String label = this.adminPanel.getAlbumLabel();
                final String rawSongsText = this.adminPanel.getAlbumSongsInput();

                final boolean success = this.controller.adminClickedSaveAlbumWithSongs(
                    artistCode, title, releaseDate, label, rawSongsText
                );

                if (success) {
                    showSuccess("Album e brani inseriti con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addSavePodcastListener(e -> {
            if (this.controller != null) {
                int artistCode = 0;
                try {
                    if (!this.adminPanel.getPodcastArtistCode().isBlank()) {
                        artistCode = Integer.parseInt(this.adminPanel.getPodcastArtistCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid podcast artist code format", ex);
                    JOptionPane.showMessageDialog(this, "Podcast artist code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String name = this.adminPanel.getPodcastName();
                final String description = this.adminPanel.getPodcastDescription();
                final String category = this.adminPanel.getPodcastCategory();

                final boolean success = this.controller.adminClickedSavePodcast(artistCode, name, description, category);

                if (success) {
                    showSuccess("Podcast inserito con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addSaveEpisodeListener(e -> {
            if (this.controller != null) {
                int podcastCode = 0;
                try {
                    if (!this.adminPanel.getEpisodePodcastCode().isBlank()) {
                        podcastCode = Integer.parseInt(this.adminPanel.getEpisodePodcastCode());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode podcast code format", ex);
                    JOptionPane.showMessageDialog(this, "Podcast code must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String title = this.adminPanel.getEpisodeTitle();

                int duration = 0;
                try {
                    if (!this.adminPanel.getEpisodeDuration().isBlank()) {
                        duration = Integer.parseInt(this.adminPanel.getEpisodeDuration());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode duration format", ex);
                    JOptionPane.showMessageDialog(this, "Duration in seconds must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final String description = this.adminPanel.getEpisodeDescription();

                int episodeNumber = 0;
                try {
                    if (!this.adminPanel.getEpisodeNumber().isBlank()) {
                        episodeNumber = Integer.parseInt(this.adminPanel.getEpisodeNumber());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid episode number format", ex);
                    JOptionPane.showMessageDialog(this, "Episode number must be a valid integer.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final boolean success = this.controller.adminClickedSaveEpisode(
                    podcastCode, title, duration, description, episodeNumber
                );

                if (success) {
                    showSuccess("Episodio inserito con successo!");
                    this.adminPanel.clearAllForms();
                }
            }
        });

        this.adminPanel.addFetchStatsListener(e -> {
            if (this.controller != null) {
                int year = CURRENT_YEAR;
                try {
                    if (!this.adminPanel.getStatsYear().isBlank()) {
                        year = Integer.parseInt(this.adminPanel.getStatsYear());
                    }
                } catch (final NumberFormatException ex) {
                    LOGGER.log(Level.SEVERE, "Invalid stats year format", ex);
                    JOptionPane.showMessageDialog(this, "Reference year must be a valid number.", 
                                                FORMAT_ERROR, JOptionPane.ERROR_MESSAGE);
                }

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

    @Override
    public void showUsers(final List<User> users) {
        final List<Object[]> rows = new ArrayList<>();
        for (final User user : users) {
            rows.add(new Object[] {
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getCountry(),
                user.getBonusCredit(),
            });
        }
        this.adminPanel.setUsersTableData(rows);
    }

    @Override
    public void showGlobalStats(final String statsText) {
        this.adminPanel.setStatsOutputText(statsText);
    }

    @Override
    public void setPodcastAuthors(final List<Artist> authors) {
        this.adminPanel.setPodcastAuthors(authors);
    }

    @Override
    public void showError(final String message) {
        JOptionPane.showMessageDialog(this, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override 
    public void showSuccess(final String message) {
        JOptionPane.showMessageDialog(this, message, "Successo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Gets the role selection panel.
     *
     * @return the role selection panel.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    public RoleSelectionPanel getRoleSelectionPanel() {
        return this.roleSelectionPanel;
    }

    /**
     * Gets the user panel.
     *
     * @return the user panel.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    public UserPanel getUserPanel() {
        return userPanel;
    }

    /**
     * Gets the admin panel.
     *
     * @return the admin panel.
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "UI panels are stateful components managed as internal view references."
    )
    public AdminPanel getAdminPanel() {
        return adminPanel;
    }
}
