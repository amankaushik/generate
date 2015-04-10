/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chanakya
 */
public class ShowChapterMapAction {

    private String idd;

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String execute() throws ClassNotFoundException, SQLException, IOException {
        ArrayList<String> sections = new ArrayList<>();
        DBObject document = new BasicDBObject();
        System.out.println("Uinque ID Value:" + getIdd());
        try {
            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("Major");
            DBCollection collection = db.getCollection("ConceptMap");
            BasicDBObject query = new BasicDBObject();
            query.put("UniqueID", getIdd());
            document = collection.findOne(query);
        } catch (MongoException e) {
            System.out.println("ERRRRRORRR: " + e.getMessage());
            return ERROR;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MapGenerateAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("DB Output: " + document.get("OutputText").toString());
        //setData(document.get("OutputText").toString());
        setData(document.get("ChapterName").toString());
        try {
            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("Major");
            DBCollection collection = db.getCollection("ConceptMap");
            BasicDBObject query = new BasicDBObject();
            query.put("ChapterName", getData());
            DBCursor cursor = collection.find(query);
            //document= collection.find(query);
            while (cursor.hasNext()) {
                sections.add(cursor.next().get("SectionName").toString());
            }
        } catch (MongoException e) {
            System.out.println("ERRRRRORRR: " + e.getMessage());
            return ERROR;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MapGenerateAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            FileWriter file = new FileWriter("/home/chanakya/NetBeansProjects/Concepto/web/sections.txt");
            for (String s : sections) {
                file.write(s);
                file.write("\n");
            }
            file.flush();
            file.close();

        } catch (IOException e) {
            return ERROR;
            //e.printStackTrace();
        }
        String cmd = "python /home/chanakya/NetBeansProjects/Concepto/src/java/generate/combine.py \"/home/chanakya/NetBeansProjects/Concepto/web/sections.txt\"";
        String[] finalCommand;
        finalCommand = new String[3];
        finalCommand[0] = "/bin/sh";
        finalCommand[1] = "-c";
        finalCommand[2] = cmd;
        System.out.println("CMD: " + cmd);
        try {
            //ProcessBuilder builder = new ProcessBuilder(finalCommand);
            //builder.redirectErrorStream(true);
            //Process process = builder.start();
            Process process = Runtime.getRuntime().exec(finalCommand);
            int exitVal = process.waitFor();
            System.out.println("Process exitValue2: " + exitVal);
        } catch (Throwable t) {
            System.out.println("E5: " + t.getMessage());
            //return ERROR;
        }
        
        return SUCCESS;
    }
}
