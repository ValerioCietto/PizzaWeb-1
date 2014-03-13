<%@page import="java.util.ArrayList"%>
<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Catalogo</title>
    </head>
    <body>
        <article>
            <% ArrayList<String[]> ris=DBManager.query("SELECT * FROM PIZZE");%>
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
                </tr> 
                <%System.out.println("4");
                for(int i=1;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=0;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}if(ruolo!=null && ruolo.equals("admin")){%>
                            <td>mod</td>
                            <td>
                                <form action="/PizzaWeb/Servlet" method="post">
                                    <input type="hidden" name= "pizza"  value="ris.get(i)[0]">
                                    <input type="submit" name= "action" value="remove">
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
                            <td><input type="text" name= "pizza"        value="nome"></td>
                            <td><input type="text" name= "ingredienti"  value="ingredienti"></td>
                            <td><input type="text" name= "prezzo"       value="prezzo"></td>
                            <td colspan=4><input type="submit" name= "action" value="aggiungi"></td>
                        </tr>
                    </form>
                <%}%>
            </table>
        </article>   
    </body>
</html>

