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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "create":
                showTicketForm(response);
                break;
            case "view":
                viewTicket(request, response);
                break;
            case "download":
                downloadAttachment(request, response);
                break;
            case "list":
            default:
                listTickets(response);
                break;
        }
    }

    private void showTicketForm(HttpServletResponse response) throws IOException {
        PrintWriter writer = writeHeader(response);

        writer.append("<h2>Create a Ticket</h2>\r\n");
        writer.append("<form method=\"POST\" action=\"tickets\" ")
                .append("enctype=\"multipart/form-data\">\r\n");
        writer.append("<input type=\"hidden\" name=\"action\" ")
                .append("value=\"create\"/>\r\n");
        writer.append("Your Name<br/>\r\n");
        writer.append("<input type=\"text\" name=\"customerName\"/><br/><br/>\r\n");
        writer.append("Subject<br/>\r\n");
        writer.append("<input type=\"text\" name=\"subject\"/><br/><br/>\r\n");
        writer.append("Body<br/>\r\n");
        writer.append("<textarea name=\"body\" rows=\"5\" cols=\"30\">")
                .append("</textarea><br/><br/>\r\n");
        writer.append("<b>Attachments</b><br/>\r\n");
        writer.append("<input type=\"file\" name=\"file1\"/><br/><br/>\r\n");
        writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
        writer.append("</form>\r\n");

        writeFooter(writer);
    }

    private PrintWriter writeHeader(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("    <head>\r\n")
                .append("        <title>Customer Support</title>\r\n")
                .append("    </head>\r\n")
                .append("    <body>\r\n");
        return writer;
    }

    private void writeFooter(PrintWriter writer) {
        writer.append("    </body>\r\n").append("</html>\r\n");
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("ticketId");
        Ticket ticket = getTicket(idString, response);
        if (ticket == null) {
            return;
        }

        PrintWriter writer = writeHeader(response);

        writer.append("<h2>Ticket #").append(idString)
                .append(": ").append(ticket.getSubject()).append("</h2>\r\n");
        writer.append("<i>Customer Name - ").append(ticket.getCustomerName())
                .append("</i><br/><br/>\r\n");
        writer.append(ticket.getBody()).append("<br/><br/>\r\n");

        if (ticket.getNumberOfAttachments() > 0) {
            writer.append("Attachments: ");
            int i = 0;
            for (Attachment attachment : ticket.getAttachments()) {
                if (i++ > 0) {
                    writer.append(", ");
                }
                writer.append("<a href=\"tickets?action=download&ticketId=")
                        .append(idString).append("&attachment=")
                        .append(attachment.getName()).append("\">")
                        .append(attachment.getName()).append("</a>");
            }
            writer.append("<br/><br/>\r\n");
        }
        writer.append("<a href=\"tickets\">Return to list tickets</a>\r\n");

        this.writeFooter(writer);
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

    private void listTickets(HttpServletResponse response) throws IOException {
        PrintWriter writer = writeHeader(response);

        writer.append("<h2>Tickets</h2>\r\n");
        writer.append("<a href=\"tickets?action=create\">Create Ticket")
                .append("</a><br/><br/>\r\n");

        if (ticketDatabase.size() == 0) {
            writer.append("<i>There are no tickets in the system.</i>\r\n");
        } else {
            for (int id : ticketDatabase.keySet()) {
                String idString = Integer.toString(id);
                Ticket ticket = ticketDatabase.get(id);
                writer.append("Ticket #").append(idString)
                        .append(": <a href=\"tickets?action=view&ticketId=")
                        .append(idString).append("\">").append(ticket.getSubject())
                        .append("</a> (customer: ").append(ticket.getCustomerName())
                        .append(")<br/>\r\n");
            }
        }
        writeFooter(writer);
    }

}
