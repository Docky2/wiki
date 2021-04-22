package com.docky.wiki.mapper;

import com.docky.wiki.domain.Doc;
import com.docky.wiki.domain.DocExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DocMapper {
    long countByExample(DocExample example);

    int deleteByExample(DocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Doc record);

    int insertSelective(Doc record);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByExample(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);

    /* 往下均为自定义方法 */
    @Update("update doc set view_count = view_count+1 where id = #{id}")
    void increaseViewCount(Long id);

    @Update("update doc set vote_count = vote_count+1 where id = #{id}")
    void increaseVoteCount(Long id);

    @Update("update ebook t1,(select ebook_id,count(*) doc_count,sum(view_count) view_count,sum(vote_count) vote_count\n" +
            "from doc group by ebook_id) t2 set t1.doc_count = t2.doc_count,t1.view_count = t2.view_count,t1.vote_count= t2.vote_count\n" +
            "where t1.id = t2.ebook_id\n")
    void updateEbookInfo();

}