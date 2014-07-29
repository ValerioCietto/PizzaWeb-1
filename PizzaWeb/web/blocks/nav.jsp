<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <body>
        <nav>
            <h1>Operazioni disponibili</h1>
            
            <div>
                <form action="/PizzaWeb/Servlet" method="get">
                <input type="hidden" name="action" value="switch">
                <input type="submit" name="name"   value="catalogo">
                </form>
            </div>
            
            
            
            <div>
                    <form action="/PizzaWeb/Servlet" method="get">
                                <input type="hidden" name="action" value="switch">
                                <input type="submit" name="name" value="prenotazioni">
                            </form>
                       </div>
            
            
            
          
            <div>prenotazioni</div>
            <div>loginManager (admin)</div>
        </nav>   
    </body>
</html>

