package uptc.co.tienda.Services;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import uptc.co.tienda.Entities.FacturaEntity;
import uptc.co.tienda.Entities.VentaEntity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.Date;

@Service("generarPDFAfilicacionService")
public class PdfFacturaService {


     @Async
	    public void generarPDF(FacturaEntity facturaEntity) {
	        try {
	            TimeUnit.SECONDS.sleep(5);
	            String fileName = "Factura_" + facturaEntity.getCodigoFactura()+ ".pdf";
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(fileName));
	            document.open();
	            Image logo = Image.getInstance("src/main/resources/static/logo.png");
	            logo.scaleToFit(100, 100);
	            document.add(logo);
	            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
	            Paragraph title = new Paragraph("Factura DE Compra", font);
	            title.setAlignment(Element.ALIGN_CENTER);
	            document.add(title);
	            document.add(new Paragraph("\n"));
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fechaActual = dateFormat.format(new Date());
	            document.add(new Paragraph("Fecha : " + fechaActual));


                  document.add(new Paragraph("Codigo factura: " + facturaEntity.getCodigoFactura()));
	              document.add(new Paragraph("Codigo de referencia: " + facturaEntity.getCodigoReferencia()));
	              document.add(new Paragraph("Cliente: " + facturaEntity.getUsuario().getNombre()));
                  document.add(new Paragraph("Numero de documento: " + facturaEntity.getUsuario().getNumeroDocumento()));
	              document.add(new Paragraph("Correo : " + facturaEntity.getUsuario().getCorreo()));
	              document.add(new Paragraph("Articulos"));	
                  for(VentaEntity ventaE: facturaEntity.getListaVentaEntity()){
                    document.add(new Paragraph("Producto: " + ventaE.getProducto().getNombre()));
                    document.add(new Paragraph("Cantidad: " + ventaE.getCantidad()));
                    document.add(new Paragraph("Precio Unitario: " + ventaE.getProducto().getPrecio()));
                    document.add(new Paragraph("Total : " + ventaE.getPrecioTotal()));
                  }              
                  document.add(new Paragraph(" Total a pagar: " + facturaEntity.getTotal()));
	              document.add(new Paragraph("Informacion Pago"));
                  document.add(new Paragraph("Fecha: " + facturaEntity.getFechaTransaccion()));
	              document.add(new Paragraph("Estado de la transaccion: " + facturaEntity.getEstadoTransaccion()));
	              document.add(new Paragraph("Id de la transaccion : " + facturaEntity.getTransaccionId()));
	              document.add(new Paragraph("Codigo de autorizacion del Banco: " + facturaEntity.getUsuario().getCorreo()));

	            document.close();

	            System.out.println("PDF generado: " + fileName);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
