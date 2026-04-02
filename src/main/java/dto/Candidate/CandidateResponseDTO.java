package dto.Candidate;

import com.renan.refyne.enums.UserType;
import com.renan.refyne.enums.AvailabilityStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CandidateResponseDTO {

  String fullName;
  String headline;
  String bio;
  String city;
  String state;
  String country;
  String profilePhoto;
  String resumeUrl;
  String linkedinUrl;
  String portfolioUrl;
  String githubUrl;
  AvailabilityStatus availabilityStatus;
  String cpf;
  UserType userType;
}
