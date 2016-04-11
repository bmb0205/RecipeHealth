package burciaga.projects.recipehealth;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Created by bburciag on 4/11/16.
 */
public class XMLParser {

    public static void main(String[] args) throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SAXHandler saxHandler = new SAXHandler();
        saxParser.parse(new File(
                "/Users/bburciag/BiSD/KnowledgeBase/Sources/PubMed/inflammation_flavonoid_pubmed.xml"), saxHandler);
        List<PubmedArticle> articleList = saxHandler.getArticleList();


        BufferedWriter meshTermWriter = new BufferedWriter(new FileWriter(
                new File("/Users/bburciag/BiSD/KnowledgeBase/Sources/PubMed/pubmed_meshTerms.txt.out")));
        BufferedWriter pubmedInfoWriter = new BufferedWriter(new FileWriter(
                new File("/Users/bburciag/BiSD/KnowledgeBase/Sources/PubMed/pubmed_info.txt.out")));

        meshTermWriter.write("PMID|MeSH_Term\n");
        pubmedInfoWriter.write("PMID|Title|Abstract\n");
        for (PubmedArticle article : articleList) {
            if (article.getTitle().startsWith("Reply:")) {
                continue;
            }
            pubmedInfoWriter.write(article.getPMID() + "|" + article.getTitle() + "|" + article.getAbs() + "\n");
            for (String meshTerm : article.getMeshSet()) {
                meshTermWriter.write(article.getPMID() + "|" + meshTerm + "\n");
            }
        }
        meshTermWriter.close();
        pubmedInfoWriter.close();

    }
}
