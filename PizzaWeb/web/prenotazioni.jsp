<%-- 
    Document   : prenotazioni
    Created on : Mar 18, 2014, 3:26:55 PM
    Author     : valerio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="mypackage.*"%>

<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Catalogo</title>
    </head>
    <body>
        <%String ruolo=(String)(request.getSession()).getAttribute("ruolo");%>
        <article>
            <% 
                String[] res = {"id","Nome","Pizza","quantità","stato"};
                String[] res1 = {"1","Gianni","margherita","3","da accettare"};
                ArrayList<String[]> ris= new ArrayList<String[]>();
                ris.add(res);
                ris.add(res1);
            %>
            <table>
                <tr>
                   <%for(int j=0;j<ris.get(0).length;j++){%>
                        <th><%=ris.get(0)[j]%></th>
                    <%}if(ruolo!=null && ruolo.equals("admin")){%>
                        <th>modifica</th>
                        <th>cancella</th>
                    <%}if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user"))){%>
                        <th>add</th>
                        <th>ordinate</th>
                    <%}%>
                   --/>     
                </tr> 
                <%for(int i=1;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=0;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}if(ruolo!=null && ruolo.equals("admin")){%>
                            <td>mod</td>
                            <td>
                                <form action="/PizzaWeb/Servlet" method="get">
                                    <input type="hidden" name="prenotazione"  value=<%=ris.get(i)[0]%>>
                                    <input type="hidden" name="action" value="remPrenotaz">
                                    <input type="submit" name="name"   value="rimuovi">
                                </form>
                            </td>
                        <%}if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user"))){%>
                            <td>add</td>
                            <td>att</td>
                        <%}%>
                    </tr>
                <%}if(ruolo!=null && ruolo.equals("admin")){%>
                    <form action="/PizzaWeb/Servlet" method="post">
                        <tr>
                            <td><input type="text" name= "idprenot"        value=""></td>
                            <td><input type="text" name= "cliente"  value=""></td>
                            <td><input type="text" name= "pizza"       value=""></td>
                            <td><input type="text" name= "quantita"       value=""></td>
                            <td><input type="text" name= "stato"       value=""></td>
                            <td colspan=4><input type="submit" name= "action" value="addPrenotazione"></td>
                        </tr>
                    </form>
                <%}%>
            </table>
        </article>   
    </body>
</html>

