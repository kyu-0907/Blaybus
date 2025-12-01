package politicConnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import politicConnect.domain.Provider;
import politicConnect.domain.RegionCity;

@Data
@Builder
@AllArgsConstructor
public class SocialSignUpRequest {

    private Provider provider;
    private RegionCity regionCity;
    private String nickname;
    private String email;



}
