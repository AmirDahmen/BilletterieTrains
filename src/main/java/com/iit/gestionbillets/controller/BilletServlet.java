package com.iit.gestionbillets.controller;



import com.iit.gestionbillets.dao.Interface.IBilletDAO;

import com.iit.gestionbillets.dao.impl.BilletDAOImpl;

import com.iit.gestionbillets.model.Billet;

import com.iit.gestionbillets.model.User;

import com.iit.gestionbillets.model.Role;



import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;



import java.io.*;

import java.time.format.DateTimeFormatter;

import java.util.Optional;

import java.util.Base64;




import com.google.zxing.BarcodeFormat;

import com.google.zxing.WriterException;

import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.google.zxing.common.BitMatrix;

import com.google.zxing.qrcode.QRCodeWriter;




import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;



public class BilletServlet extends HttpServlet {

 private static final long serialVersionUID = 1L;



 private IBilletDAO billetDAO;



 @Override

 public void init() throws ServletException {

 this.billetDAO = new BilletDAOImpl();

 System.out.println("BilletServlet initialized successfully!");

 }



 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

 System.out.println("BilletServlet doGet called with URI: " + request.getRequestURI());

 System.out.println("Query parameters: " + request.getQueryString());

 

 HttpSession session = request.getSession(false);

 User user = (session != null) ? (User) session.getAttribute("user") : null;



 if (user == null) {

 System.out.println("User not logged in, redirecting to auth");

 String queryString = request.getQueryString();

 String redirectUrl = request.getRequestURI() + (queryString != null ? "?" + queryString : "");

 response.sendRedirect(request.getContextPath() + "/auth?action=showLogin&redirect=" + response.encodeURL(redirectUrl));

 return;

 }



 String action = request.getParameter("action");

 String reference = request.getParameter("ref");

 

 System.out.println("Action: " + action + ", Reference: " + reference);



 if (reference == null || reference.trim().isEmpty()) {

 System.out.println("No reference provided, redirecting to dashboard");

 response.sendRedirect(request.getContextPath() + "/client/dashboard");

 return;

 }



 try {

 Optional<Billet> billetOpt = billetDAO.findByReference(reference);



 if (!billetOpt.isPresent()) {

 System.out.println("Billet not found for reference: " + reference);

 request.setAttribute("errorMessage", "Billet non trouvé avec la référence : " + reference);

 request.getRequestDispatcher("/WEB-INF/jsp/error/erreurGenerale.jsp").forward(request, response);

 return;

 }



 Billet billet = billetOpt.get();

 System.out.println("Billet found: " + billet.getReferenceBillet());




 if (!billet.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {

 System.out.println("Access denied for user: " + user.getId());

 request.setAttribute("errorMessage", "Vous n'êtes pas autorisé à accéder à ce billet.");

 request.getRequestDispatcher("/WEB-INF/jsp/error/erreurGenerale.jsp").forward(request, response);

 return;

 }



 if ("view".equals(action)) {

 System.out.println("Calling viewBillet");

 viewBillet(request, response, billet);

 } else if ("downloadPdf".equals(action)) {

 System.out.println("Calling downloadBilletPdf");

 downloadBilletPdf(request, response, billet);

 } else {

 System.out.println("Invalid action, redirecting to dashboard");

 response.sendRedirect(request.getContextPath() + "/client/dashboard");

 }

 } catch (Exception e) {

 System.err.println("Error in BilletServlet: " + e.getMessage());

 e.printStackTrace();

 request.setAttribute("errorMessage", "Une erreur est survenue : " + e.getMessage());

 request.getRequestDispatcher("/WEB-INF/jsp/error/erreurGenerale.jsp").forward(request, response);

 }

 }



 private void viewBillet(HttpServletRequest request, HttpServletResponse response, Billet billet) throws ServletException, IOException {

 System.out.println("viewBillet called for: " + billet.getReferenceBillet());

 

 String qrData = generateQrData(billet);

 System.out.println("QR Data generated: " + qrData);

 

 // Générer le QR code avec ZXing

 String qrCodeBase64 = generateQRCodeWithZXing(qrData);



 request.setAttribute("billet", billet);

 request.setAttribute("qrCodeBase64", qrCodeBase64);

 

 System.out.println("Forwarding to viewBillet.jsp");

 request.getRequestDispatcher("/WEB-INF/jsp/billet/viewBillet.jsp").forward(request, response);

 }



 private void downloadBilletPdf(HttpServletRequest request, HttpServletResponse response, Billet billet) throws IOException {

 System.out.println("downloadBilletPdf called for: " + billet.getReferenceBillet());

 

 // Configurer la réponse pour un PDF

 response.setContentType("application/pdf");

 response.setHeader("Content-Disposition", "attachment; filename=\"billet_" + billet.getReferenceBillet() + ".pdf\"");

 

 try {


 Document document = new Document(PageSize.A4, 50, 50, 50, 50);

 PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

 document.open();

 


 createPdfContent(document, billet);

 

 document.close();

 System.out.println("PDF generated successfully for: " + billet.getReferenceBillet());

 

 } catch (DocumentException e) {

 System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());

 e.printStackTrace();

 


 response.reset();

 response.setContentType("text/html; charset=UTF-8");

 response.getWriter().write("<h1>Erreur lors de la génération du PDF</h1><p>" + e.getMessage() + "</p>");

 }

 }



 private void createPdfContent(Document document, Billet billet) throws DocumentException, IOException {


 Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);

 Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);

 Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

 Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);

 

 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

 DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

 DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");




 Paragraph title = new Paragraph("SAFE TRIP - BILLET ELECTRONIQUE", titleFont);

 title.setAlignment(Element.ALIGN_CENTER);

 title.setSpacingAfter(20);

 document.add(title);




 Paragraph reference = new Paragraph("Reference: " + billet.getReferenceBillet(), headerFont);

 reference.setAlignment(Element.ALIGN_CENTER);

 reference.setSpacingAfter(20);

 document.add(reference);




 document.add(new Paragraph(" "));

 


 document.add(new Paragraph("INFORMATIONS PASSAGER", headerFont));

 document.add(new Paragraph("Nom: " + billet.getUser().getFirstName() + " " + billet.getUser().getLastName(), normalFont));

 document.add(new Paragraph("Email: " + billet.getUser().getEmail(), normalFont));

 document.add(new Paragraph(" "));




 document.add(new Paragraph("INFORMATIONS DE VOYAGE", headerFont));

 


 PdfPTable voyageTable = new PdfPTable(2);

 voyageTable.setWidthPercentage(100);

 voyageTable.setSpacingBefore(10);

 voyageTable.setSpacingAfter(10);

 


 PdfPCell departCell = new PdfPCell();

 departCell.setBorder(Rectangle.BOX);

 departCell.setPadding(10);

 departCell.addElement(new Paragraph("DEPART", headerFont));

 departCell.addElement(new Paragraph(billet.getVoyage().getTrajet().getGareDepart().getNom(), normalFont));

 departCell.addElement(new Paragraph(billet.getVoyage().getTrajet().getGareDepart().getVille(), smallFont));

 departCell.addElement(new Paragraph("Date: " + billet.getVoyage().getHeureDepart().format(dateFormatter), normalFont));

 departCell.addElement(new Paragraph("Heure: " + billet.getVoyage().getHeureDepart().format(timeFormatter), normalFont));

 voyageTable.addCell(departCell);

 


 PdfPCell arriveeCell = new PdfPCell();

 arriveeCell.setBorder(Rectangle.BOX);

 arriveeCell.setPadding(10);

 arriveeCell.addElement(new Paragraph("ARRIVEE", headerFont));

 arriveeCell.addElement(new Paragraph(billet.getVoyage().getTrajet().getGareArrivee().getNom(), normalFont));

 arriveeCell.addElement(new Paragraph(billet.getVoyage().getTrajet().getGareArrivee().getVille(), smallFont));

 arriveeCell.addElement(new Paragraph("Date: " + billet.getVoyage().getHeureArrivee().format(dateFormatter), normalFont));

 arriveeCell.addElement(new Paragraph("Heure: " + billet.getVoyage().getHeureArrivee().format(timeFormatter), normalFont));

 voyageTable.addCell(arriveeCell);

 

 document.add(voyageTable);




 document.add(new Paragraph("INFORMATIONS FINANCIERES", headerFont));

 Font priceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.GREEN);

 document.add(new Paragraph("Prix paye: " + String.format("%.2f EUR", billet.getPrixPaye()), priceFont));

 document.add(new Paragraph("Date de reservation: " + billet.getDateReservation().format(dateTimeFormatter), normalFont));

 document.add(new Paragraph(" "));



 // QR Code

 String qrData = generateQrData(billet);

 byte[] qrCodeBytes = generateQRCodeBytes(qrData);

 

 if (qrCodeBytes != null) {

 try {

 Image qrImage = Image.getInstance(qrCodeBytes);

 qrImage.scaleToFit(150, 150);

 qrImage.setAlignment(Element.ALIGN_CENTER);

 

 document.add(new Paragraph("QR CODE DE VALIDATION", headerFont));

 document.add(qrImage);

 document.add(new Paragraph("Scannez ce code pour valider votre billet", smallFont));

 } catch (Exception e) {

 System.err.println("Erreur lors de l'ajout du QR code: " + e.getMessage());

 document.add(new Paragraph("QR Code non disponible", smallFont));

 }

 }




 document.add(new Paragraph(" "));

 document.add(new Paragraph(" "));

 Paragraph footer1 = new Paragraph("Merci d'avoir choisi nos services ferroviaires", smallFont);

 footer1.setAlignment(Element.ALIGN_CENTER);

 document.add(footer1);

 

 Paragraph footer2 = new Paragraph("Presentez ce billet et une piece d'identite lors du controle", smallFont);

 footer2.setAlignment(Element.ALIGN_CENTER);

 document.add(footer2);

 

 Paragraph footer3 = new Paragraph("Billet genere le " + java.time.LocalDateTime.now().format(dateTimeFormatter), smallFont);

 footer3.setAlignment(Element.ALIGN_CENTER);

 document.add(footer3);

 }



 private String generateQrData(Billet billet) {

 return String.format("BILLET|REF:%s|VOYAGE:%d|CLIENT:%s %s|DEPART:%s|ARRIVEE:%s|DATE:%s|HEURE:%s|PRIX:%.2f EUR",

 billet.getReferenceBillet(),

 billet.getVoyage().getId(),

 billet.getUser().getFirstName(),

 billet.getUser().getLastName(),

 billet.getVoyage().getTrajet().getGareDepart().getNom(),

 billet.getVoyage().getTrajet().getGareArrivee().getNom(),

 billet.getVoyage().getHeureDepart().toLocalDate().toString(),

 billet.getVoyage().getHeureDepart().toLocalTime().toString(),

 billet.getPrixPaye()

 );

 }


 private String generateQRCodeWithZXing(String data) {

 try {

 byte[] qrCodeBytes = generateQRCodeBytes(data);

 return qrCodeBytes != null ? Base64.getEncoder().encodeToString(qrCodeBytes) : null;

 } catch (Exception e) {

 System.err.println("Erreur lors de la génération du QR code: " + e.getMessage());

 e.printStackTrace();

 return null;

 }

 }

 


 private byte[] generateQRCodeBytes(String data) {

 try {

 QRCodeWriter qrCodeWriter = new QRCodeWriter();

 BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);

 

 // Convertir en image

 ByteArrayOutputStream baos = new ByteArrayOutputStream();

 MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

 return baos.toByteArray();

 

 } catch (WriterException | IOException e) {

 System.err.println("Erreur lors de la génération du QR code: " + e.getMessage());

 e.printStackTrace();

 return null;

 }

 }



 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

 doGet(request, response);

 }

}