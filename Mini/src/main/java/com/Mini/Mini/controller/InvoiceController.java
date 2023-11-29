package com.Mini.Mini.controller;

import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.dto.OrderCsvDto;
import com.Mini.Mini.repository.OrderRepository;
import com.Mini.Mini.service.OrdersService;
import com.Mini.Mini.service.PdfInvoiceService;
import com.Mini.Mini.service.SalesReportService;
import com.lowagie.text.DocumentException;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class InvoiceController {

    @Autowired
    private PdfInvoiceService pdfInvoiceService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SalesReportService salesReportService;

    @GetMapping("/generate-invoice")
    @ResponseBody
    public ResponseEntity<byte[]> generateInvoice(@RequestParam("orderId") Long orderId) {
        try {
            byte[] pdfContent = pdfInvoiceService.generateInvoicePdf(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/salesReportCsv")
    public ResponseEntity<byte[]> generateCSVReport(Principal principal , HttpServletResponse response, Model model)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        // Create a temporary file to store the CSV data
        File tempFile = File.createTempFile("salesReport", ".csv");
        List<Orders> orders = orderRepository.findAll();
        try (Writer writer = new FileWriter(tempFile)) {
            StatefulBeanToCsv<OrderCsvDto> csvWriter = new StatefulBeanToCsvBuilder<OrderCsvDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(true)
                    .build();

            List<OrderCsvDto> orderCsvDtoList = new ArrayList<>();
            for (Orders order : orders) {
                String productName = order.getOrderItems().stream()
                        .map(orderItem -> orderItem.getProduct().getName())
                        .collect(Collectors.joining());

                OrderCsvDto orderCsvDto = new OrderCsvDto();
                orderCsvDto.setOrderId(String.valueOf(order.getId()));
                orderCsvDto.setUsername(order.getUser().getEmail());
                orderCsvDto.setTotalPrice((double) order.getTotalAmount());
                orderCsvDto.setOrderDate(order.getOrderDate());
                orderCsvDto.setPaymentMethod(String.valueOf(order.getPaymentMethod()));
                orderCsvDto.setStatus(String.valueOf(order.getStatus()));
                orderCsvDto.setProductName(productName);

                orderCsvDtoList.add(orderCsvDto);
            }

            csvWriter.write(orderCsvDtoList);

            Double totalSales = salesReportService.calculateTotalSales(orders);
            writer.write("TOTAL SALES," + totalSales + "\n");

            int totalOrderCount = orders.size();
            writer.write("TOTAL ORDER COUNT," + totalOrderCount + "\n");
        }

        // Convert the temporary file to a byte array
        byte[] bytes = FileUtils.readFileToByteArray(tempFile);

        // Set the content type and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "salesReport.csv");

        // Return the file as a byte array along with headers
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}