package com.Mini.Mini.service;


import com.Mini.Mini.Entity.OrderItem;
import com.Mini.Mini.Entity.Orders;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.Entity.UserAddress;
import com.Mini.Mini.repository.OrderRepository;
import com.Mini.Mini.repository.PdfInvoiceRepository;
import com.Mini.Mini.repository.UserAddressRepository;
import com.Mini.Mini.repository.UserRepository;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfInvoiceService {

    @Autowired
    PdfInvoiceRepository pdfInvoiceRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAddressRepository userAddressRepository;


    public byte[] generateInvoicePdf(Long orderId) throws DocumentException {
        Orders orders = orderRepository.findById(orderId).orElse(null);
        User user=userRepository.findById(orders.getUser().getId()).orElse(null);
        List<UserAddress> userAddress=userAddressRepository.findByUserId(user.getId());

        String companyName = "StyleSphere";


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        // Header
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell(new Phrase(companyName));
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(headerCell);
        document.add(headerTable);

        // Address Section
        PdfPTable addressTable = new PdfPTable(1);
        addressTable.setWidthPercentage(100);
        PdfPCell addressCell = new PdfPCell(new Phrase("To: " + user.getFirstname()+" \n\n"+ "Address : "+ "\n\n" +
                orders.getAddress().toString()));
        addressCell.setBorder(Rectangle.NO_BORDER);
        addressTable.addCell(addressCell);
        document.add(addressTable);

        // Order Items Section
        PdfPTable orderItemsTable = new PdfPTable(4);
        orderItemsTable.setWidthPercentage(100);
        orderItemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        // Set minimum height for each cell

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

//        orderItemsTable.addCell("Orders id: ");
        orderItemsTable.addCell("Sl.no ");
        orderItemsTable.addCell("Order Items ");
        orderItemsTable.addCell("Quantity");
        orderItemsTable.addCell("Price");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

        int slNo = 1;
        for(OrderItem orderItem : orders.getOrderItems()){

            orderItemsTable.addCell(slNo+" \n");
            orderItemsTable.addCell(orderItem.getProduct().getName()+"\n");
            orderItemsTable.addCell(orderItem.getQuantity()+"\n");
            orderItemsTable.addCell(orderItem.getProduct().getPrice()+"\n");
            slNo++;
        }


        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");


        document.add(orderItemsTable);

        // Total Amount Section
        PdfPTable totalAmountTable = new PdfPTable(2);
        totalAmountTable.setWidthPercentage(100);
        orderItemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//        totalAmountTable.addCell("Discount Amount");
//        totalAmountTable.addCell("₹"+);
        totalAmountTable.addCell("Total Amount:");
        totalAmountTable.addCell("₹"+orders.getTotalAmount());
        document.add(totalAmountTable);

        // Footer
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        PdfPCell footerCell = new PdfPCell(new Phrase("Thank you!"));
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(footerCell);
        document.add(footerTable);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }
}