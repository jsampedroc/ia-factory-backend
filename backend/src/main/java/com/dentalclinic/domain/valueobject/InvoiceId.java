package com.dentalclinic.domain.valueobject;

import com.dentalclinic.domain.shared.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InvoiceId(@NotNull UUID value) implements ValueObject {}