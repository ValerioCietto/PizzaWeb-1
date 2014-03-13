<%@page import="java.util.ArrayList"%>
<%@page import="mypackage.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Manager</title>
    </head>
    <body>
        <article>
            <%ArrayList<String[]> ris=DBManager.query("SELECT * FROM UTENTI");%>
            <table>
                <tr>
                    <%for(int j=0;j<ris.get(0).length;j++){%>
                        <th><%=ris.get(0)[j]%></th>
                    <%}%>
                </tr>
                <%for(int i=0;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=1;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}%>
                    </tr>
                <%}%>
            </table>
        </article>
    </body>
</html>
