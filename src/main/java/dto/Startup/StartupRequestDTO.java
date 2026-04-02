package dto.Startup;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
public class StartupRequestDTO {
  @NotBlank
  private String companyName;

  private String description;
  private String industry;
  private String stage;
  private LocalDate foundedDate;
  private String size;
  private String logoUrl;
  private String websiteUrl;
  private String linkedinUrl;
  private String city;
  private String state;
  private String country;

  @NotBlank
  @CNPJ
  private String cnpj;
}
