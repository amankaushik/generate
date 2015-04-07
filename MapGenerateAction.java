/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import java.io.File;
import java.io.IOException;
import java.util.List;
import mapcontent.ConceptMapData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chanakya
 */
public class MapGenerateAction extends ActionSupport implements ModelDriven<ConceptMapData> {
//,ServletRequestAware 
// Model -> ConceptMapData

    private ConceptMapData concept_map = new ConceptMapData();
    @Override
    public ConceptMapData getModel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return concept_map;
    }

    public static com.opensymphony.xwork2.util.logging.Logger getLOG() {
        return LOG;
    }

    public static void setLOG(com.opensymphony.xwork2.util.logging.Logger LOG) {
        ActionSupport.LOG = LOG;
    }
    
    public ConceptMapData getConcept_map() {
        return concept_map;
    }

    public void setConcept_map(ConceptMapData concept_map) {
        this.concept_map = concept_map;
    }
    //@Override
    public String my_validate() {
        if (StringUtils.isEmpty(getConcept_map().getUploadedFileFileName())){
            addFieldError("uploadedFile", "*Uploaded File Cannot be blank.");
            return INPUT;
        }
        if(! StringUtils.isEmpty(getConcept_map().getUploadedFileContentType()) && StringUtils.equalsIgnoreCase(getConcept_map().getUploadedFileContentType(), "txt")){
            addFieldError("uploadedFile", "*Only Text Files allowed");
            return INPUT;
        }
        if (StringUtils.isEmpty(getConcept_map().getChapter_name())) {
            //super.addActionError("Chapter Name Cannot be blank");
            addFieldError("chapter_name", "*Chapter Name Cannot be blank.");
            return INPUT;
        }
        if (StringUtils.isEmpty(getConcept_map().getChapter_number())) {
            //super.addActionError("Chapter Number Cannot be blank");
            addFieldError("chapter_number", "*Chapter Number Cannot be blank.");
            return INPUT;
        }
        if (StringUtils.isEmpty(getConcept_map().getSection_number())) {
            //super.addActionError("Section Number Cannot be blank");
            addFieldError("section_number", "*Section Number Cannot be blank.");
            return INPUT;
        }
        if (StringUtils.isEmpty(getConcept_map().getSection_name())) {
            //super.addActionError("Section Name Cannot be blank");
            addFieldError("section_name", "*Section Name Cannot be blank.");
            return INPUT;
        }
        return "";
    }


    @Override
    public String execute() {
        String tempt = my_validate();
        if(StringUtils.equals(tempt, INPUT))
            return INPUT;
        String file_path = "/home/chanakya/NetBeansProjects/Concepto/UploadedFiles";
        try {
            File fileToCreate = new File(file_path, concept_map.getUploadedFileFileName());
            FileUtils.copyFile(concept_map.getUploadedFile(), fileToCreate);
        } catch (Throwable t) {
            System.out.println("E1: " + t.getMessage());
            //return ERROR;
        }
        try {
            List<String> temp_text = FileUtils.readLines(concept_map.getUploadedFile());
            StringBuilder text = new StringBuilder();
            for (String s : temp_text) {
                text.append(s);
            }
            concept_map.setInput_text(text.toString());
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("E2: " + e.getMessage());
            //return ERROR;
        }
        String temp_filename = concept_map.getUploadedFileFileName().split("\\.(?=[^\\.]+$)")[0];
        temp_filename = temp_filename.trim();

        try {
            String temp = "java -jar /home/chanakya/NetBeansProjects/Concepto/src/java/generate/MajorCore.jar " + file_path + " " + temp_filename;
            System.out.println(temp);
            File jarfile = new File("/home/chanakya/NetBeansProjects/Concepto/src/java/generate/MajorCore.jar");
            JarFile jar = new JarFile(jarfile);
            Manifest manifest = jar.getManifest();
            Attributes attrs = manifest.getMainAttributes();
            String mainClassName = attrs.getValue(Attributes.Name.MAIN_CLASS);
            System.out.println(mainClassName);
            URL url = new URL("file", null, jarfile.getAbsolutePath());
            ClassLoader cl = new URLClassLoader(new URL[]{url});
            Class mainClass = cl.loadClass(mainClassName);
            Method mainMethod = mainClass.getMethod("main", new Class[]{String[].class});
            String[] args = new String[2];
            args[0] = file_path;
            args[1] = temp_filename;
            System.out.println(args[0]);
            System.out.println(args[1]);
            try {
                mainMethod.invoke(mainClass, new Object[]{args});
            } catch (InvocationTargetException e) {
                System.out.println("This is the exception: " + e.getTargetException().toString());
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException | IOException e) {
            System.out.println("E3: " + e.getMessage());
            //return ERROR;
        }

        try {
            String temp2 = "java -jar /home/chanakya/NetBeansProjects/Concepto/src/java/generate/MajorCoreII.jar " + file_path + " " + temp_filename;
            System.out.println(temp2);
            File jarfile = new File("/home/chanakya/NetBeansProjects/Concepto/src/java/generate/MajorCoreII.jar");
            JarFile jar = new JarFile(jarfile);
            Manifest manifest = jar.getManifest();
            Attributes attrs = manifest.getMainAttributes();
            String mainClassName = attrs.getValue(Attributes.Name.MAIN_CLASS);
            System.out.println(mainClassName);
            URL url = new URL("file", null, jarfile.getAbsolutePath());
            ClassLoader cl = new URLClassLoader(new URL[]{url});
            Class mainClass = cl.loadClass(mainClassName);
            Method mainMethod = mainClass.getMethod("main", new Class[]{String[].class});
            String[] args = new String[2];
            args[0] = file_path;
            args[1] = temp_filename;
            mainMethod.invoke(mainClass, new Object[]{args});
        } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException | IOException e) {
            System.out.println("E4: " + e.getMessage());
            //return ERROR;
        }

        String cmd = "python /home/chanakya/NetBeansProjects/Concepto/src/java/generate/add_to_graph.py \"/home/chanakya/NetBeansProjects/Concepto/UploadedFiles/" + temp_filename + "_OllieOutput.txt\"";
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

        cmd = "python /home/chanakya/NetBeansProjects/Concepto/src/java/generate/json_correct.py";
        finalCommand = new String[3];
        finalCommand[0] = "/bin/sh";
        finalCommand[1] = "-c";
        finalCommand[2] = cmd;

        try {
            //Process process = Runtime.getRuntime().exec(finalCommand);

            ProcessBuilder builder = new ProcessBuilder(finalCommand);
            // builder.redirectErrorStream(true);
            Process process = builder.start();
            int exitVal = process.waitFor();
            System.out.println("Process exitValue3: " + exitVal);
        } catch (Throwable t) {
            System.out.println("E6: " + t.getMessage());
            //return ERROR;
        }

        try {
            List<String> temp_text_1 = FileUtils.readLines(FileUtils.getFile("/home/chanakya/NetBeansProjects/Concepto/web", "new_graph.json"));
            StringBuilder text_1 = new StringBuilder();
            for (String s : temp_text_1) {
                text_1.append(s);
            }
            concept_map.setOutput_text(text_1.toString());
        } catch (IOException e) {
            System.out.println("E7: " + e.getMessage());
            //return ERROR;
        }
        Random rand = new Random();
        int unique_id = rand.nextInt(99999999);
        System.out.println("Going In DB");
        try {
            MongoClient mongo = new MongoClient();
            DB db = mongo.getDB("Major");
            DBCollection collection = db.getCollection("ConceptMap");
            BasicDBObject document = new BasicDBObject();
            document.append("InputText", concept_map.getInput_text());
            document.append("OutputText", concept_map.getOutput_text());
            document.append("ChapterName", concept_map.getChapter_name());
            document.append("ChapterNumber", concept_map.getChapter_number());
            document.append("SectionName", concept_map.getSection_name());
            document.append("SectionNumber", concept_map.getSection_number());
            document.append("UniqueID", Integer.toString(unique_id));
            collection.insert(document);
            //collection.save(document);
        } catch (MongoException e) {
            System.out.println("E8: " + e.getMessage());
            //return ERROR;
        } catch (UnknownHostException ex) {
            Logger.getLogger(MapGenerateAction.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("E9");
            //return ERROR;
        }
        System.out.println("Out DB");
        System.out.println(SUCCESS);
        return SUCCESS;
    }

}
