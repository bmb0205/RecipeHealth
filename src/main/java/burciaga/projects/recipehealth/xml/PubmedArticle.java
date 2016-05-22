package burciaga.projects.recipehealth.xml;


import java.util.HashSet;

/**
 * Created by bburciag on 4/11/16.
 * PubmedArticle class creates object that gets and sets pmid, title, abs and meshSet attributes
 */
public class PubmedArticle {

    private int pmid;
    private String title;
    private String abs;
    private HashSet<String> meshSet;

    public void PubmedArticle() { }

    void setPMID(int pmid) {
        this.pmid = pmid;
    }

    public int getPMID() {
        return this.pmid;
    }

    public void setTitle(String title) {
        this.title = title.replace("]", "").replace("[", "").replace(".", "");
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
