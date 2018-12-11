package crest.siamese.helpers;

import crest.siamese.document.MethodClone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileLevelEvaluator extends Evaluator {

    public FileLevelEvaluator(String clonePairFile, String index, String outputDir, boolean isPrint) {
        super(clonePairFile, index, outputDir, isPrint);
    }

    @Override
    public int generateSearchKey() {
        searchKey = new HashMap<>();
        Iterator it = cloneCluster.entrySet().iterator();
        String textToPrint = "";

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<MethodClone> clones = (ArrayList<MethodClone>) pair.getValue();

            for (int i=0; i<clones.size(); i++) {

                MethodClone clone = clones.get(i);
                String filename = clone.getFile().substring(clone.getFile().lastIndexOf("/") + 1);
                String query = clone.getFile() + "_method";
                textToPrint += query;
                ArrayList<String> relevantResults = new ArrayList<String>();

                // add query as the first relevant result
                relevantResults.add(query);
                textToPrint += "," + query;

                for (int j=0; j<clones.size(); j++) {
                    // other relevant results
                    if (i!=j) {
                        filename = clones.get(j).getFile().substring(clones.get(j).getFile().lastIndexOf("/") + 1);
                        String result = clones.get(j).getFile() + "_method";
                        relevantResults.add(result);
                        textToPrint += "," + result;
                    }
                }
                // add the query, and its relevant results
                searchKey.put(query, relevantResults);
                textToPrint += "\n";
            }
        }

        MyUtils.writeToFile("resources", "searchkey.csv", textToPrint, false);
//        System.out.println("Done generating search key from " + this.clonePairFile + " ... ");

        return searchKey.size();
    }
}
