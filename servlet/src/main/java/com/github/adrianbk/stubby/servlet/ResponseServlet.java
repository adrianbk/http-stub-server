package com.github.adrianbk.stubby.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.adrianbk.stubby.service.NotFoundException;

/*
 * Handles operations on responses (eg, 'GET /_control/responses/<index>')
 */
@SuppressWarnings("serial")
public class ResponseServlet extends AbstractStubServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            returnJson(response, service().getResponse(getId(request)));
        } catch (NotFoundException e) {
            returnNotFound(response, e.getMessage());
        }
    }

}
