package soundwave.view;

import javax.swing.*;
import java.awt.*;
import soundwave.controller.Controller;

/**
 * Implementation of the {@link View} interface.
 */
public final class ViewImpl extends JFrame implements View {

    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    public static final String PANEL_ROLE_SELECTION = "ROLE_SELECTION";
    public static final String PANEL_USER_VIEW = "USER_VIEW";
    public static final String PANEL_ADMIN_VIEW = "ADMIN_VIEW";

    private final RoleSelectionPanel roleSelectionPanel;
    private final UserPanel userPanel;
    private final AdminPanel adminPanel;

    private Controller controller;

    public ViewImpl() {
        
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
        cardLayout.show(mainContainer, panelName);
    }

    public RoleSelectionPanel getRoleSelectionPanel() {
        return roleSelectionPanel;
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }
}