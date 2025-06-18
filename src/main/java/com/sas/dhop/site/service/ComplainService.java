package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.ComplainRequest;
import com.sas.dhop.site.dto.response.ComplainResponse;

public interface ComplainService {

    ComplainResponse bookingComplains(ComplainRequest complainRequest);

    ComplainResponse cancelBookingComplains(Integer bookingId);

    ComplainResponse approveBookingComplains(Integer bookingId);

    ComplainResponse completeBookingComplains(Integer bookingId);

//    ComplainResponse makeComplainRequestForAnything();
}
