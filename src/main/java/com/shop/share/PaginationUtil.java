package com.shop.share;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;

public final class PaginationUtil {

    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";

    //<http://localhost:8080/api/admin/users?page=0&size=20>; rel="last"; "aaa",<http://localhost:8080/api/admin/users?page=0&size=20>; rel="first"; "aaa"
    private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";
   // private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"; \"{2}\"";

    private PaginationUtil() {
    }

    public static <T>HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder builder, Page<T>page){
        HttpHeaders headers=new HttpHeaders();
        headers.add(HEADER_X_TOTAL_COUNT,Long.toString(page.getTotalElements()));
        int pageNumber=page.getNumber();
        int pageSize=page.getSize();
        StringBuilder link=new StringBuilder();
        if (pageNumber<page.getTotalPages()-1){
            link.append(prepareLink(builder,pageNumber+1,pageSize,"next"))
                    .append(",");
        }
        if (pageNumber>0){
            link.append(prepareLink(builder,pageNumber-1,pageSize,"prev"))
                    .append(",");
        }
        link.append(prepareLink(builder,page.getTotalPages()-1,pageSize,"last"))
                .append(",")
                .append(prepareLink(builder,0,pageSize,"first"));
        headers.add(HttpHeaders.LINK,link.toString());
        return headers;

    }

    private static String prepareLink(UriComponentsBuilder builder, int pageNumber, int pageSize, String next) {
        return MessageFormat.format(HEADER_LINK_FORMAT,preparePageUri(builder,pageNumber,pageSize),next);
    }

    private static String preparePageUri(UriComponentsBuilder builder, int pageNumber, int pageSize) {
        return builder.replaceQueryParam("page",Integer.toString(pageNumber))
                .replaceQueryParam("size",Integer.toString(pageSize))
                .toUriString()
                .replace(",","%2C")
                .replace(";","%3B");
    }
}
