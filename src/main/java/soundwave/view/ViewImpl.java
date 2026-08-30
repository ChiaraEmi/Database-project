package soundwave.view;

import javax.swing.*;
import java.awt.*;
import soundwave.controller.Controller;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    public static final String FRAME_NAME = "Soundwave";
    public static final String ROLE_SELECTION_PANEL = "ROLE SELECTION";
    public static final String USER_PANEL = "USER VIEW";
    public static final String ADMIN_PANEL = "ADMIN VIEW";

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
        mainPanel.add(roleSelectionPanel, ROLE_SELECTION_PANEL);

        //this.adminPanel = new AdminPanel();
        //mainPanel.add(adminPanel, ADMIN_PANEL);

        //this.userPanel = new UserPanel();
        //mainPanel.add(userPanel, USER_PANEL);

    }

    @Override
    public void setController(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        setVisible(true);
    }

    public void showPanel(final String panelName) {
        layout.show(mainPanel, panelName);
    }

    public RoleSelectionPanel getRoleSelectionPanel() {
        return roleSelectionPanel;
    }

    /*public UserPanel getUserPanel() {
        return userPanel;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }*/
}
