package IMainView;

import mma.admin.presentations.Presenter;

public interface IView {
    void displayMessage(String message);
    void setPresenter(Presenter presenter);
}
