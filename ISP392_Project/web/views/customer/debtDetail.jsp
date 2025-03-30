<%-- 
    Document   : debtDetail
    Created on : Feb 16, 2025, 4:09:27 PM
    Author     : phamh
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer</title>
    </head>
    <body>
        <div class="modal fade" id="debtDetailModal${debt.id}" tabindex="-1" role="dialog" aria-labelledby="debtDetailLabel${debt.id}" aria-hidden="true">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Debt Details - ${debt.id}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Amount:</strong> ${debt.amount}</p>
                        <p><strong>Description:</strong> ${debt.description}</p>
                        <p><strong>Status:</strong> ${debt.status}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    
    </body>
</html>
