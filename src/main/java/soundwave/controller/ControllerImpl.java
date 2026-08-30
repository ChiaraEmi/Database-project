package soundwave.controller;

import soundwave.view.View;

/**
 * Implementation of the {@link Controller} interface.
 */
public final class ControllerImpl implements Controller {

    private View view;

    public ControllerImpl() {
        
    }

    @Override
    public void setView(final View view) {
        this.view = view;
        this.view.setController(this);
    }
}
