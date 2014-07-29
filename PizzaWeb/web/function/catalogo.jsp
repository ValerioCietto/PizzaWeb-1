<%@page import="mvc.*" import="components.*"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Catalogo</title>
    </head>
    <body>
        <%ruolo=(String)(request.getSession()).getAttribute("ruolo");%>
        
        
        <article>
            <% ArrayList<String[]> ris=(ArrayList<String[]>)(request.getSession()).getAttribute("dati");%>
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
                <%for(int i=1;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=0;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}
                        if(ruolo!=null && ruolo.equals("admin")){%>
                            
                            <td><form action="/PizzaWeb/Servlet" method="get">
                                    <input type="hidden" name="pizza"  value="<%=ris.get(i)[0]%>" >
                                    <input type="hidden" name="action" value="modPizza">
                                    <input type="submit" name="name"   value="modifica">
                                </form></td>
                            <td>
                                <form action="/PizzaWeb/Servlet" method="get">
                                    <input type="hidden" name="pizza"  value="<%=ris.get(i)[0]%>" >
                                    <input type="hidden" name="action" value="remPizza">
                                    <input type="submit" name="name"   value="remove">
                                </form>
           
                            </td>
                        <%}
                        
                        if(ruolo!=null && (ruolo.equals("admin")||ruolo.equals("user"))){%>
                        <td>  
                            <form action="/PizzaWeb/Servlet" method="get">
                                    <input type="hidden" name="action" value="addPrenotaz">
                                    <input type ="hidden" name="pizza" value="<%= ris.get(i)[0]%>">
                                    <input type ="text" name="data" value="data"> 
                                    <input type="number" name="quant"> <%-- gestire l'eccezione che da' in firefox--%>
                                    <input type="submit" name="name"   value="add">
                            </form>
 
                        </td>
                            
                            <td>att</td>
                        <%}%>
                        </tr>
                <%}
                if(ruolo!=null && ruolo.equals("admin")){%>
                    <tr><th colspan="7">AGGIUNGI</th></tr>
                    <tr>
                        <form action="/PizzaWeb/Servlet" method="post">
                            <td><input type="text" name= "pizza"        value="nome" required></td>
                            <td><input type="text" name= "ingredienti"  value="ingredienti" required></td>
                            <td><input type="text" name= "prezzo"       value="prezzo" required></td>
                            <td colspan=4><input type="submit" name= "action" value="addPizza"></td>
                        </form>
                    </tr>                        
                <%}%>
            </table>
        </article>   
    </body>
</html>

