/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generate;

/**
 *
 * @author chanakya
 */
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import static com.opensymphony.xwork2.Action.ERROR;
import com.opensymphony.xwork2.ActionSupport;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ShowArticlesAction extends ActionSupport implements Serializable {
    public String execute() throws ClassNotFoundException, SQLException, IOException {
        List<DBObject> documents = new ArrayList<>();
        System.out.println("Mark 0");
        try {
            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("Major");
            DBCollection collection = db.getCollection("ConceptMap");
            DBCursor cursor = collection.find();
            documents = cursor.toArray();
            cursor.close();
        } catch (MongoException e) {
            System.out.println("ERRRRRORRR: " + e.getMessage());
            return ERROR;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MapGenerateAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Mark 1");
        Map<String, ArrayList<String>> chap_to_section = new HashMap<>();
        Map<String, String> id_to_section = new HashMap<>();
        for (DBObject document : documents) {
            String c_name = document.get("ChapterName").toString();
            boolean has_key = chap_to_section.containsKey(c_name);
            ArrayList<String> sections = new ArrayList<>();
            sections.add(document.get("SectionName").toString());
            if (has_key) {
                sections.addAll(chap_to_section.get(c_name));
                chap_to_section.put(c_name, sections);
            } else {
                chap_to_section.put(c_name, sections);
            }
            id_to_section.put(document.get("SectionName").toString(), document.get("UniqueID").toString());
        }
        FileWriter file = null;
        try {
            file = new FileWriter("/home/chanakya/NetBeansProjects/Concepto/web/Chapters_Sections.json");
        } catch (IOException ex) {
            Logger.getLogger(ShowArticlesAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONArray jarr = new JSONArray();
        
        for(String key : chap_to_section.keySet()){
            JSONObject jobj = new JSONObject();
            JSONArray arr = new JSONArray();
            
            for(String val : chap_to_section.get(key)){
                JSONObject temp = new JSONObject();
                temp.put("name", val);
                temp.put("id", id_to_section.get(val).toString());
                arr.add(temp);
            }
            jobj.put("sname", arr);
            jobj.put("cname", key);
            jarr.add(jobj);
        }
        System.out.println("Mark 2");
        //JSONObject obj = new JSONObject();
        //obj.put("cmap", jarr);
        file.write(jarr.toJSONString());
        file.flush();
        file.close();
        System.out.println("Mark 3");
        return SUCCESS;
    }
}
