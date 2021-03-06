package com.ak.spring3.testsuite.controllers;

import com.ak.spring3.testsuite.models.AnnotatedBusiness;
import com.ak.spring3.testsuite.models.Business;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@Api(value = "Businesses", position = 1)
public class BusinessController {

    static AtomicInteger businessIdCount = new AtomicInteger(1);
    static Map<Integer, Business> businessCache = newHashMap();

    static {
        int businessId = businessIdCount.getAndIncrement();
        Business business = new Business();
        business.name = "HTTP Widgets Ltd.";
        business.inception = LocalDate.now();
        business.id = businessId;
        businessCache.put(businessId, business);
    }

    @ApiOperation(value = "get a list of businesses", response = Business.class, notes = "Returns all businesses in " +
            "the cache")
    @RequestMapping(value = "/businesses", method = GET)
    @ResponseBody
    public List<Business> list(HttpServletRequest request) {
        return new ArrayList<Business>(businessCache.values());
    }

    @RequestMapping(value = "/businesses/withColon:{someParam:\\w+}", method = GET)
    @ResponseBody
    public void businessWithSemicolonInPath(@PathVariable("someParam") String someParam) {

    }

    @ApiOperation(value = "get a number of businesses", notes = "Returns all businesses based on the supplied ids")
    @RequestMapping(value = "/businesses/specificBusinesses", method = GET)
    @ResponseBody
    public List<Business> getByIds(@RequestParam String[] ids) {
        return new ArrayList<Business>(businessCache.values());
    }

    @ApiOperation(value = "get all businesses by type", notes = "Returns all businesses based on the business type")
    @RequestMapping(value = "/businesses/product", method = GET)
    @ResponseBody
    public List<Business> getByIds(@RequestParam Business.BusinessType businessType) {
        return new ArrayList<Business>(businessCache.values());
    }

    @ApiOperation(value = "Value is the summary", notes = "Gives more detailed info on the api operation")
    @RequestMapping(value = { "/businesses/{businessId}" }, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Business getBusinessWithHttpMethod(
            @ApiParam(defaultValue = "1", value = "The id of the business to return")
            @PathVariable Integer businessId) {
        return businessCache.get(businessId);
    }

    @RequestMapping(value = { "/businesses/{businessId}" }, method = DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteBusiness(@ApiParam(value = "The id of the business to return") @PathVariable Integer businessId) {
        businessCache.remove(businessId);
    }


    @ApiOperation(value = "Business with annotated model", notes = "Annotated model annotation")
    @RequestMapping(value = { "/businessesAnnotated/{businessId}" }, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AnnotatedBusiness getBusinessAnnotated(
            @ApiParam(defaultValue = "1", value = "The id of the business to return")
            @PathVariable Integer businessId) {
      Business business = businessCache.get(businessId);
      AnnotatedBusiness annotatedBusiness = new AnnotatedBusiness();
      BeanUtils.copyProperties(annotatedBusiness, business);
      return annotatedBusiness;
    }


    @RequestMapping(value = { "/businesses" }, method = POST, consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) {
        int newBusinessId = businessIdCount.getAndIncrement();
        business.id = newBusinessId;
        businessCache.put(business.id, business);
        ResponseEntity<Business> responseEntity = new ResponseEntity<Business>(business, OK);
        return responseEntity;
    }

    @RequestMapping(value = { "/businesses/nickNames" }, method = POST, consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> businessNickName(@RequestBody String business) {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("Hello", OK);
        return responseEntity;
    }

    @RequestMapping(value = { "/businesses/byTypes" }, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Business> businessesByCategories(@RequestParam Business.BusinessType []types) {
        return newArrayList();
    }
}
