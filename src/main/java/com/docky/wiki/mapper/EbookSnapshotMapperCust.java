package com.docky.wiki.mapper;

import com.docky.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

    /**
     * Custom
     * */
    public void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}