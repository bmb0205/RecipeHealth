package burciaga.projects.recipehealth;


import java.util.HashSet;

/**
 * Created by bburciag on 4/11/16.
 */
public class PubmedArticle {

    private int pmid;
    private String title;
    private String abs;
    private HashSet<String> meshSet;

    public void PubmedArticle() { }

    public void setPMID(int pmid) {
        this.pmid = pmid;
    }

    public int getPMID() {
        return this.pmid;
    }

    public void setTitle(String title) {
        String cleanedTitle = title.replace("]", "").replace("[", "").replace(".", "");
        this.title = cleanedTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAbs(String abs) {
            this.abs = abs;
    }

    public String getAbs() {
        if (abs == null) {
            return "";
        }
        return this.abs;
    }

    public void setMeshSet(HashSet<String> meshSet) {
        this.meshSet = meshSet;
    }

    public HashSet<String> getMeshSet() {
        return this.meshSet;
    }

}
