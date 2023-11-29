package com.Mini.Mini.repository;

import com.Mini.Mini.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfInvoiceRepository extends JpaRepository<Invoice,Long> {
}
