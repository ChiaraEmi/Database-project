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
    //private final UserPanel userPanel;
    //private final AdminPanel adminPanel;

    private Controller controller;

    /**
     * Builds a new ViewImpl.
     */
    public ViewImpl() {
        setTitle(FRAME_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.roleSelectionPanel = new RoleSelectionPanel();
        mainPanel.add(roleSelectionPanel, ROLE_SELECTION_CARD);

        //this.adminPanel = new AdminPanel();
        //mainPanel.add(adminPanel, ADMIN_CARD);

        //this.userPanel = new UserPanel();
        //mainPanel.add(userPanel, USER_CARD);

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

        //this.roleSelectionPanel.addUtenteListener(e -> showUserPanel());
        //this.roleSelectionPanel.addAdminListener(e -> showAdminPanel());
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    public void showPanel(final String panelName) {
        layout.show(mainPanel, panelName);
    }

    /*@Override
    public void showUserPanel() {
        SwingUtilities.invokeLater(() -> this.layout.show(this.mainPanel, USER_CARD));
    }

    @Override
    public void showAdminPanel() {
        SwingUtilities.invokeLater(() -> this.layout.show(this.mainPanel, ADMIN_CARD));
    }*/

    public RoleSelectionPanel getRoleSelectionPanel() {
        return this.roleSelectionPanel;
    }

    /*public UserPanel getUserPanel() {
        return userPanel;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }*/
}
