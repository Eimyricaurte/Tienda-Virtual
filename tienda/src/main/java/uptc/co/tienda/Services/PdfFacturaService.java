package uptc.co.tienda.Services;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.VentaEntity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import java.util.Date;

@Service("generarPDFAfilicacionService")
public class PdfFacturaService {

 @Async
public void generarPDF(FacturaEntity facturaEntity) {
    try {
        TimeUnit.SECONDS.sleep(5);
        String fileName = "Factura_" + facturaEntity.getCodigoFactura() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        
        // Logo
        Image logo = Image.getInstance("src/main/resources/static/logo.jpg");
        logo.scaleToFit(100, 100);
        document.add(logo);
        
        // Título
        Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Factura DE Compra", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));
        
        // Fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaActual = dateFormat.format(new Date());
        document.add(new Paragraph("Fecha : " + fechaActual));

        // Información de la factura
        document.add(new Paragraph("Codigo factura: " + facturaEntity.getCodigoFactura()));
        document.add(new Paragraph("Codigo de referencia: " + facturaEntity.getCodigoReferencia()));
        document.add(new Paragraph("Cliente: " + facturaEntity.getUsuario().getNombre()));
        document.add(new Paragraph("Numero de documento: " + facturaEntity.getUsuario().getNumeroDocumento()));
        document.add(new Paragraph("Correo : " + facturaEntity.getUsuario().getCorreo()));
        
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Artículos"));
        
        // VERIFICACIÓN PARA EVITAR NULL POINTER EXCEPTION
        if (facturaEntity.getListaVentaEntity() != null && !facturaEntity.getListaVentaEntity().isEmpty()) {
            // Crear tabla para los productos
            PdfPTable table = new PdfPTable(4); // 4 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Encabezados de la tabla
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            table.addCell(new PdfPCell(new Phrase("Producto", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Cantidad", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Precio Unitario", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Total", headerFont)));
            
            // Llenar la tabla con los productos
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            for (VentaEntity ventaE : facturaEntity.getListaVentaEntity()) {
                table.addCell(new PdfPCell(new Phrase(ventaE.getProducto().getNombre(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ventaE.getCantidad()), cellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ventaE.getProducto().getPrecio()), cellFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ventaE.getPrecioTotal()), cellFont)));
            }
            
            document.add(table);
            
            // Tabla para el total a pagar alineado a la derecha
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setSpacingBefore(10f);
            
            // Celda vacía para ocupar el espacio de las primeras 3 columnas
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            totalTable.addCell(emptyCell);
            
            // Celda con el total alineado a la derecha
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell totalCell = new PdfPCell(new Phrase("Total a pagar: " + facturaEntity.getTotal(), totalFont));
            totalCell.setBorder(PdfPCell.NO_BORDER);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalCell);
            
            document.add(totalTable);
            
        } else {
            document.add(new Paragraph("No hay artículos en esta factura."));
        }
        
        document.add(new Paragraph("Informacion Pago"));
        document.add(new Paragraph("Fecha de la transaccion: " + facturaEntity.getFechaTransaccion()));
        document.add(new Paragraph("Estado de la transaccion: " + facturaEntity.getEstadoTransaccion()));
        document.add(new Paragraph("Id de la transaccion : " + facturaEntity.getTransaccionId()));
        document.add(new Paragraph("Codigo de autorizacion del Banco: " + facturaEntity.getCodigoAutorizacionBanco()));

        document.close();
        System.out.println("PDF generado: " + fileName);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
