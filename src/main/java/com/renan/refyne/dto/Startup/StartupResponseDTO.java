package com.renan.refyne.dto.Startup;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class StartupResponseDTO {

  String companyName;
  String description;
  String industry;
  String stage;
  LocalDate foundedDate;
  String size;
  String logoUrl;
  String websiteUrl;
  String linkedinUrl;
  String city;
  String state;
  String country;
  String cnpj;

}
