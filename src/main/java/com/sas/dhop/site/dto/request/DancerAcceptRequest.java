package com.sas.dhop.site.dto.request;
// stk, sdt dancer, ten ngan hang
public record DancerAcceptRequest(
        String accountNumber,
        String dancerPhone,
        String bank
) {
}
