
package data.models.FunFactsModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityFunFactsModel {

    @SerializedName("facts")
    @Expose
    private List<Fact> facts = null;

    public List<Fact> getFacts() {
        return facts;
    }

    public void setFacts(List<Fact> facts) {
        this.facts = facts;
    }

}
