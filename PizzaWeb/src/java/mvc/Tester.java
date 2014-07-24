/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mvc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author mirko
 */
public class Tester {
    public static void main(String[]args){
        DBManager dbman=new DBManager();
        testUser(dbman,false,true,false,false);
    }
    public static void testUser(DBManager dbman,boolean add,boolean get,boolean mod, boolean rem){
        String user=genString(8);
        String pwd=genString(8);
        String ruolo=genString(8);
        int outI;
        String outS;
        boolean outB;
        ResultSet rs;
        try{//test addUser
            try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
            outI=dbman.addUser(user,pwd,ruolo);
            System.out.println("test addUser(>0) = "+outI);
            try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
        }catch(SQLException e){System.out.println(e.getMessage()+" addUser1");}
        System.out.println("\n\n");
        if(add){
            try{//test addUser con user esistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outI=dbman.addUser(user,genString(8),genString(8));
                System.out.println("test addUser con user esistente(-1) = "+outI);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" addUser2");}
            try{//test addUser con pwd esistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outI=dbman.addUser(genString(8),pwd,genString(8));
                System.out.println("test addUser con pwd esistente(>0) = "+outI);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" addUser3");}
            try{//test addUser con ruolo esistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outI=dbman.addUser(genString(8),genString(8),ruolo);
                System.out.println("test addUser con ruolo esistente(>0) = "+outI);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" addUser4");}
            System.out.println("\n\n");
        }
        if(get){
            try{//test getIdUser
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outI=dbman.getIdUser(user);
                System.out.println("test getIdUser(>0) = "+outI);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getIdUser1");}
            try{//test getIdUser con user inesistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outI=dbman.getIdUser(genString(8));
                System.out.println("test getIdUser con user inesistente(-1) = "+outI);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getIdUser2");}
            try{//test getUser(int)
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                rs=dbman.getUser(1);
                outS=stampaUtente(rs);
                System.out.println("test getUser(int) = "+outS);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getUser(int)1");}
            try{//test getUser(int) con id inesistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                rs=dbman.getUser(0);
                outS=stampaUtente(rs);
                System.out.println("test getUser(int) con id inesistente = "+outS);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getUser(int)2");}
            try{//test getUser(String)
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                rs=dbman.getUser(user);
                outS=stampaUtente(rs);
                System.out.println("test getUser(String) = "+outS);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getUser(String)1");}
            try{//test getUser(String) con id inesistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                rs=dbman.getUser(genString(8));
                outS=stampaUtente(rs);
                System.out.println("test getUser(String) con user inesistente = "+outS);
                try{ dbman.closeConnection(); }catch(SQLException e){System.out.println(e.getMessage()+" closeConnection");}
            }catch(SQLException e){System.out.println(e.getMessage()+" getUser(String)2");}
            System.out.println("\n\n");
        }
        if(mod){
            try{//test modUser
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outB=dbman.modUser(user,genString(8),genString(8));
                System.out.println("test modUser = "+outB);
            }catch(SQLException e){System.out.println(e.getMessage()+" modUser1");}
            try{//test modUser con user inesistente
                try{dbman.openConnection();}catch(SQLException e){System.out.println(e.getMessage()+" openConnection");}
                outB=dbman.modUser(user,genString(8),genString(8));
                System.out.println("test modUser con user inesistente= "+outB);
            }catch(SQLException e){System.out.println(e.getMessage()+" modUser2");}
        }
    }
    public static String resToString(ResultSet rs) throws SQLException{
        ResultSetMetaData md=rs.getMetaData();
        int m= md.getColumnCount();
        String out="";
        while(rs.next()){
             out+="//";
             for(int i=1;i<=m;i++){
                out+=rs.getString(i);
                if(i!=m)
                    out+="\t";
             }
        }
        if(out.equals(""))
            out="//NON CI SONO RISULTATI NEL RESULTSET";
        return out+"//";
    
    }
    public static String stampaUtente(ResultSet rs) throws SQLException{
        String out="";
        while(rs.next())
            out+="//"+rs.getInt("IDUSER")+" "+rs.getString("USERNAME")+" "+rs.getString("PASSWORD")+" "+rs.getString("PERMISSION");
        return out;
    }
    public static String genString(int length){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = chars[(int)(Math.random()*chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}

