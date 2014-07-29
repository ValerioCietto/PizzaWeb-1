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
        <%String ruolo=(String)(request.getSession()).getAttribute("ruolo");
        String login=(String)(request.getSession()).getAttribute("username");%>
        <article>
            <%  ArrayList<String[]> ris=(ArrayList<String[]>)(request.getSession()).getAttribute("dati");
                /*if(ruolo!=null && ruolo.equals("admin"))
                    ris=;
                else
                    ris=DBManager.query("SELECT * FROM PRENOT WHERE CLIENTE='"+login+"'", true);
                */
            %>
            <table>
                <tr>
                    
                    <%for(int j=0;ris.size()>0 && j<ris.get(0).length;j++){%>
                        <th><%=ris.get(0)[j]%></th>
                    <%}if(ruolo!=null && ruolo.equals("admin")){%>
                        <th>modifica</th>
                        <th>cancella</th>
                    <%}if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user"))){%>
                        <th>add</th>
                        <th>ordinate</th>
                    <%}%>     
                </tr> 
                <%for(int i=1;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=0;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}if(ruolo!=null && ruolo.equals("admin") || ruolo.equals("user")){%>
                            <td>mod</td>
                            <td>
                                <form action="/PizzaWeb/Servlet" method="get">
                                    <input type="hidden" name="action" value="remPrenotaz">
                                    <input type ="hidden" name="nomecliente" value="<%= ris.get(i)[0]%>">
                                    <input type ="hidden" name="nomepizza" value="<%= ris.get(i)[1]%>">
                                    <input type ="hidden" name="data" value="<%= ris.get(i)[3]%>">
                                    <input type="submit" name="name"   value="rimuovi">
                                </form>
                            </td>
                           
                        <%} if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user"))){%>
                            <td>add</td>
                            <td>att</td>
                        <%}%>
                    </tr>
                <%}if(ruolo!=null && ruolo.equals("admin")){%>
                   
                <%}%>
            </table>
        </article>   
    </body>
</html>

