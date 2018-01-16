package com.sunxu;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author 孙许
 * @date 2018/01/14
 * @description
 */
@WebServlet(
        name = "TicketServlet",
        urlPatterns = "/tickets",
        loadOnStartup = 1
)
@MultipartConfig(
        fileSizeThreshold = 5242880, // 5MB
        maxFileSize = 20971520L, // 20MB
        maxRequestSize = 41943040L // 40MB
)
public class TicketServlet extends HttpServlet {

    private volatile int ticketIdSequence = 1;
    private Map<Integer, Ticket> ticketDatabase = new Hashtable<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "create":
                createTicket(request, response);
                break;
            case "list":
            default:
                response.sendRedirect("tickets");
                break;
        }
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Ticket ticket = new Ticket();
        ticket.setCustomerName(request.getParameter("customerName"));
        ticket.setSubject(request.getParameter("subject"));
        ticket.setBody(request.getParameter("body"));

        Part file1 = request.getPart("file1");
        if (file1 != null && file1.getSize() > 0) {
            Attachment attachment = processAttachment(file1);
            if (attachment != null) {
                ticket.addAttachment(attachment);
            }
        }

        int id;
        synchronized (this) {
            id = ticketIdSequence++;
            ticketDatabase.put(id, ticket);
        }

        response.sendRedirect("tickets?action=view&ticketId=" + id);
    }

    private Attachment processAttachment(Part filePart) throws IOException {
        InputStream inputStream = filePart.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read;
        final byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        Attachment attachment = new Attachment();
        attachment.setName(filePart.getSubmittedFileName());
        attachment.setContents(outputStream.toByteArray());

        return attachment;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "create":
                showTicketForm(request, response);
                break;
            case "view":
                viewTicket(request, response);
                break;
            case "download":
                downloadAttachment(request, response);
                break;
            case "list":
            default:
                listTickets(request, response);
                break;
        }
    }

    private void showTicketForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/jsp/view/ticketForm.jsp")
                .forward(request, response);
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idString = request.getParameter("ticketId");
        Ticket ticket = this.getTicket(idString, response);
        if (ticket == null) {
            return;
        }
        request.setAttribute("ticketId", idString);
        request.setAttribute("ticket", ticket);
        request.getRequestDispatcher("/WEB-INF/jsp/view/viewTicket.jsp")
                .forward(request, response);
    }

    private Ticket getTicket(String idString, HttpServletResponse response) throws IOException {
        if (idString == null || idString.length() == 0) {
            response.sendRedirect("tickets");
            return null;
        }

        Ticket ticket;
        try {
            ticket = ticketDatabase.get(Integer.parseInt(idString));
            if (ticket == null) {
                response.sendRedirect("tickets");
                return null;
            }
        } catch (IOException e) {
            response.sendRedirect("tickets");
            return null;
        }
        return ticket;
    }

    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("ticketId");
        Ticket ticket = getTicket(idString, response);
        if (ticket == null) {
            return;
        }

        String name = request.getParameter("attachment");
        if (name == null) {
            response.sendRedirect("tickets?action=view&ticketId=" + idString);
            return;
        }

        Attachment attachment = ticket.getAttachment(name);
        if (attachment == null) {
            response.sendRedirect("tickets?action=view&ticketId=" + idString);
            return;
        }

        response.setHeader("Content-Disposition", "attachmnet;filename=" + parseToISO(attachment.getName()));
        response.setContentType("application/octet-stream");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(attachment.getContents());
    }

    /**
     * 不同浏览器对文件名编码不一样，谷歌是以ISO8859-1编码的
     *
     * @param str 原始字符串
     * @return 转码后的字符串
     */
    private String parseToISO(String str) {
        String str1 = null;
        try {
            str1 = new String(str.getBytes("UTF-8"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str1;
    }

    private void listTickets(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("ticketDatabase", this.ticketDatabase);
        request.getRequestDispatcher("/WEB-INF/jsp/view/listTickets.jsp")
                .forward(request, response);
    }

}
