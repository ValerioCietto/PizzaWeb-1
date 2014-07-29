<%@page import="mvc.*" import="components.*"%>

<!DOCTYPE html>
<html>
    <link rel="stylesheet" href="pizzacss.css" type="text/css"> 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Manager</title>
    </head>
    <body>
        <article>
            <%ArrayList<String[]> ris=(ArrayList<String[]>)(request.getSession()).getAttribute("dati");%>
            <table>
                <tr>
                    <%for(int j=0;j<ris.get(0).length;j++){%>
                        <th><%=ris.get(0)[j]%></th>
                    <%}%>
                </tr>
                <%for(int i=1;i<ris.size();i++){%>
                    <tr>
                        <%for(int j=0;j<ris.get(i).length;j++){%>
                            <td><%=ris.get(i)[j]%></td>
                        <%}%>
                    </tr>
                <%}%>
            </table>
        </article>
    </body>
</html>
