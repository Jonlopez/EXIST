/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exist_5_7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;


/**
 *
 * @author Jon
 */
public class Exist_5_7 {

    /**
     * @param args the command line arguments
     */
    
    static XPathQueryService servicio;
    static Collection col = null;
    
    public static void main(String[] args) throws XMLDBException {
        // TODO code application logic here        
        
        String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
        //ollection col = null; // Colección
        String URI="xmldb:exist://localhost:8080/exist/xmlrpc/db/ColeccionPruebas"; //URI colección
        String usu="admin"; //Usuario
        String usuPwd="admin"; //Clave
        
        try {
            
            Class cl = Class.forName(driver); //Cargar del driver
            Database database = (Database) cl.newInstance(); //Instancia de la BD
            DatabaseManager.registerDatabase(database); //Registro del driver
            col = DatabaseManager.getCollection(URI, usu, usuPwd);
            
            if(col == null)
                System.out.println(" *** LA COLECCION NO EXISTE. ***");
            else{
                servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
               
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                
                System.out.println("Indica el codigo de departamento: ");
                      
                departamentoByDept_no(Integer.valueOf(br.readLine()));
                
                col.close(); //cerramos
            }
        }        
        catch (Exception e) {
            
            System.out.println("Error al inicializar la BD eXist");
            e.printStackTrace(); 
        }finally{
            if(col != null)
                col.close(); //cerramos
        }

    }
    
    
    public static void departamentoByDept_no(int dept_no) throws XMLDBException{

            ResourceSet result = servicio.query ("for $dep in /departamentos/DEP_ROW[DEPT_NO=" + dept_no + "] return $dep");
            System.out.println("Se han obtenido " + result.getSize() + " elementos.");
            // recorrer los datos del recurso.
            ResourceIterator i = result.getIterator();

            if (!i.hasMoreResources())
                System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
        
            while (i.hasMoreResources()) {
                Resource r = (Resource) i.nextResource();
                System.out.println((String) r.getContent());
            }
            col.close(); //cerramos
    }
}
