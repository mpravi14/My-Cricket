package com.pravi.mycricket.util;

import android.os.Environment;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;

public class PDFGenerator {
    public static void generatePDF() throws IOException {
        File Pass_Folder = null;
        try {
            Pass_Folder = new File(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + "/AppFio");
            if (!Pass_Folder.exists()) {
                Pass_Folder.mkdir();
            }
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(Pass_Folder
                    + "/Sweet Memories.pdf"));
            Document doc = new Document(pdfDoc, new PageSize(595, 842));
            doc.setMargins(0, 0, 0, 0);
            float [] pointColumnWidths = {150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);
            table.setMarginTop(0);
            table.setMarginBottom(0);

            // first row
            Cell cell = new Cell(1, 10).add(new Paragraph("DateRange"));
            cell.setTextAlignment(TextAlignment.CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            table.addCell("Calldate");
            table.addCell("Calltime");
            table.addCell("Source");
            table.addCell("DialedNo");
            table.addCell("Extension");
            table.addCell("Trunk");
            table.addCell("Duration");
            table.addCell("Calltype");
            table.addCell("Callcost");
            table.addCell("Site");

            for (int i = 0; i < 100; i++) {
                table.addCell("date" + i);
                table.addCell("time" + i);
                table.addCell("source" + i);
                table.addCell("destination" + i);
                table.addCell("extension" + i);
                table.addCell("trunk" + i);
                table.addCell("dur" + i);
                table.addCell("toc" + i);
                table.addCell("callcost" + i);
                table.addCell("Site" + i);
            }

            doc.add(table);

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
