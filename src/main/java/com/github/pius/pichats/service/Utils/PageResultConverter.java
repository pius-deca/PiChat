package com.github.pius.pichats.service.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
/** Deconstructs Hibernate Page payload */
public class PageResultConverter {
    private List payload;
    private int payloadLength;
    private int totalPage;
    private int limit;
    private long offset;
    private boolean empty;
    private int currentPage;

    public PageResultConverter(Page page) {
        this.payload = page.getContent();
        this.payloadLength = page.getNumberOfElements();
        this.totalPage = page.getTotalPages();
        this.limit = page.getSize();
        this.offset = page.getPageable().getOffset();
        this.empty = page.isEmpty();
        this.currentPage = page.getPageable().getPageNumber();
        currentPage++;
    }

    public PageResultConverter(List payload, Page page) {
        this(page);
        this.payload = payload;
    }

}
