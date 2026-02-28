package com.dentalclinic.infrastructure.persistence;

import com.dentalclinic.domain.model.Invoice;
import com.dentalclinic.domain.valueobject.InvoiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaInvoiceRepository extends JpaRepository<Invoice, UUID> {
}