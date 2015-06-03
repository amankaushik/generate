/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chanakya
 */
public class ShowConceptMapAction extends ActionSupport implements Serializable{
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
    public String execute() throws ClassNotFoundException, SQLException, IOException{
         DBObject document = new BasicDBObject();
         System.out.println("Uinque ID Value:" + getIdd());
        try {
            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("Major");
            DBCollection collection = db.getCollection("ConceptMap");
            BasicDBObject query = new BasicDBObject();
            query.put("UniqueID", getIdd());
            document= collection.findOne(query);
        } catch (MongoException e) {
            System.out.println("ERRRRRORRR: " + e.getMessage());
            return ERROR;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MapGenerateAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DB Output: " + document.get("OutputText").toString());
        setData(document.get("OutputText").toString());
        try {
 
		FileWriter file = new FileWriter("/home/chanakya/NetBeansProjects/Concepto/web/new_graph.json");
		file.write(getData());
		file.flush();
		file.close();
 
	} catch (IOException e) {
                                    return ERROR;
		//e.printStackTrace();
	}
 
        return SUCCESS;
    }
}
