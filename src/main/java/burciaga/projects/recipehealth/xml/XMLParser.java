package burciaga.projects.recipehealth.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;


/**
 * Created by bburciag on 4/11/16.
 * XMLParser will parse XML files specified in main() for this specific instance using the SAX parser.
 *      * To do: remove files in source directory that don't need to be parsed, and iterate files
 *          in directory itself instead of hard coding file paths below
 * Uses StringBuilder to build string of extracted XML attributes before writing to outfile
 */
public class XMLParser {

    public static void main(String[] args) throws Exception {

        BufferedWriter meshTermWriter = new BufferedWriter(new FileWriter(
                new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_meshTerms.out")));
        BufferedWriter pubmedInfoWriter = new BufferedWriter(new FileWriter(
                new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_info.out")));
        List<PubmedArticle> articleList = new ArrayList<>();
        Set<Integer> pmidSet = new HashSet<>();
        List<File> xmlFiles = new ArrayList<>();
        xmlFiles.add(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/flavonoid_inflammation_human.xml"));
        xmlFiles.add(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/proanthocyanidins_inflammation_human.xml"));
        xmlFiles.add(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/blueberry_inflammation.xml"));
        xmlFiles.add(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/blueberry_cardiovascular_disease.xml"));

        for(File file : xmlFiles) {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXHandler saxHandler = new SAXHandler();
            saxParser.parse(file, saxHandler);
            articleList.addAll(saxHandler.getArticleList());
        }
//        meshTermWriter.write("PMID|MeSH_Term\n");
//        pubmedInfoWriter.write("PMID|Title|Abstract\n");

        for (PubmedArticle article : articleList) {
            if (article.getTitle().startsWith("Reply:")) {
                continue;
            }
            StringBuilder builder =
                    new StringBuilder()
                            .append(article.getPMID())
                            .append("|")
                            .append(article.getTitle())
                            .append("|")
                            .append(article.getAbs())
                            .append("\n");
            if (!pmidSet.contains(article.getPMID())) {
                pubmedInfoWriter.write(builder.toString());
                pmidSet.add(article.getPMID());
            }
            else {
                //  do nothing
            }

            for (String meshTerm : article.getMeshSet()) {
                meshTermWriter.write( meshTerm + "|" + article.getPMID() + "|" + UUID.randomUUID() + "\n");
            }
        }
        meshTermWriter.close();
        pubmedInfoWriter.close();
    }
}
