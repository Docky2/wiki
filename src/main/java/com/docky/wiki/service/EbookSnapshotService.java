package com.docky.wiki.service;

import com.docky.wiki.mapper.EbookSnapshotMapperCust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Docky
 * @date 2021/4/22 19:57
 */
@Service
public class EbookSnapshotService {

    @Autowired(required = false)
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot(){
        ebookSnapshotMapperCust.genSnapshot();
    }

}
