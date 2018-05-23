package tie.hackathon.travelguide.Base;

/**
 * Created by zoolander on 10/18/17.
 */

public interface BasePresenter<BaseView> {

    void bind(BaseView view);

    void unbind();

}
