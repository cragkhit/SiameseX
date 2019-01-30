package crest.siamese.helpers;

import crest.siamese.document.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class JSONFormatter {
    JSONObject jobj;
    JSONArray jclones;

    public JSONFormatter() {
        jobj = new JSONObject();
        jclones = new JSONArray();
    }

    public void addCloneClass(int id, int sim, ArrayList<Document> results) {
        JSONArray jarray = new JSONArray();
        for (crest.siamese.document.Document r: results) {
            jarray.add(createClone(r));
        }
        jclones.add(jarray);
    }

    private JSONObject createClone(crest.siamese.document.Document d) {
        String file = d.getFile().split(".java_")[0] + ".java";
        JSONObject item = new JSONObject();
        item.put("file", file);
        item.put("start", String.valueOf(d.getStartLine()));
        item.put("end", String.valueOf(d.getEndLine()));
        item.put("license", d.getLicense());
        return item;
    }

    public String getJSONString() {
        jobj.put("clones", jclones);
        return jobj.toString();
    }
}
