package com.iamnbty.traning.backend.business;

import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductBusiness {

    public String getProductById(String id) throws BaseException {
        //Todo: get data from Database
        if(Objects.equals("1234",id)){
            throw ProductException.notFound();
        }

        return id;
    }
}