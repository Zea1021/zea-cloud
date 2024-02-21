package com.zea.cloud.user.bean.condition;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshCondition {
    @NotBlank(message = "{refresh.token.is.blank}")
    private String refreshToken;
}
