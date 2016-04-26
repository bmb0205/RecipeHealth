package burciaga.projects.recipehealth.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import burciaga.projects.recipehealth.xml.PubmedArticle;
import burciaga.projects.recipehealth.xml.SAXHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.UUID;


/**
 * Created by bburciag on 4/11/16.
 */
public class XMLParser {

    public static void main(String[] args) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SAXHandler saxHandler = new SAXHandler();
        saxParser.parse(new File(
                "/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/flavonoid_inflammation_human.xml"), saxHandler);
        List<PubmedArticle> articleList = saxHandler.getArticleList();

        BufferedWriter meshTermWriter = new BufferedWriter(new FileWriter(
                new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_meshTerms.out")));
        BufferedWriter pubmedInfoWriter = new BufferedWriter(new FileWriter(
                new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_info.out")));

//        meshTermWriter.write("PMID|MeSH_Term\n");
//        pubmedInfoWriter.write("PMID|Title|Abstract\n");
        for (PubmedArticle article : articleList) {
            if (article.getTitle().startsWith("Reply:")) {
                continue;
            }
            pubmedInfoWriter.write(article.getPMID() + "|" + article.getTitle() + "|" + article.getAbs() + "\n");
            for (String meshTerm : article.getMeshSet()) {
                meshTermWriter.write( meshTerm + "|" + article.getPMID() + "|" + UUID.randomUUID() + "\n");
            }
        }
        meshTermWriter.close();
        pubmedInfoWriter.close();
    }
}
