package com.ui4j.sample;

import java.util.Iterator;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;

public class PDFPrinting {

    public static void main(String[] args) {

        Printer pdfPrinter = null;

        // Windows: Instal PDF24 from http://en.pdf24.org
        // Linux: Install cups-pdf http://www.cups-pdf.de

        // Optionally PDF24 could be configured for silent mode from
        // "C:\Program Files (x86)\PDF24\pdf24-SettingsUITool.exe" from the Menu "PDF Printer"
        // Set "Automatically save..." checkbox to true

        Iterator<Printer> iter = Printer.getAllPrinters().iterator();
        while (iter.hasNext()) {
            Printer printer = iter.next();
            if (printer.getName().endsWith("PDF")) {
                pdfPrinter = printer;
            }
        }

        String url = PDFPrinting.class.getResource("/sample-page.html").toExternalForm();

        Page page = BrowserFactory
            .getWebKit()
            .navigate(url);

        WebEngine engine = (WebEngine) page.getEngine();

        // Note: Optionally -webkit-print-color-adjust: exact; could be used for fix background-color problem
        // @see http://stackoverflow.com/questions/14987496/background-color-not-showing-in-print-preview

        PrinterJob job = null;
        try {
            page.show();
            // clear margins
            PageLayout layout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, MarginType.HARDWARE_MINIMUM);

            job = PrinterJob.createPrinterJob(pdfPrinter);
            job.getJobSettings().setPageLayout(layout);
            job.getJobSettings().setJobName("Sample Printing Job");

            engine.print(job);
            job.endJob();
        } finally {
            if (job != null) {
                job.endJob();
            }
            page.close();
        }
    }
}
