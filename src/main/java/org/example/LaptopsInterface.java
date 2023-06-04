package org.example;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface LaptopsInterface {
    @WebMethod
    Object[][] laptopsByProducent(String producent);
    @WebMethod
    Object[][] laptopsByPowierzchnia(String powierzchnia);
    @WebMethod
    Object[][] laptopsByProporcje(String proporcje);
}
