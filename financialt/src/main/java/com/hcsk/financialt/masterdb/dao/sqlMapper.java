package com.hcsk.financialt.masterdb.dao;

import com.hcsk.financialt.pojo.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface sqlMapper {
    //直接执行sql
    @Insert("${insql}")
    int insertsql(@Param("insql") String insql);
    @Select("${insql}")
    String selectsql(@Param("insql") String insql);
    @Update("${insql}")
    int updatesql(@Param("insql") String insql);
    @Delete("${insql}")
    int deletesql(@Param("insql") String insql);
    //批量增加汇率信息
    @Insert("<script>"+
            //"<foreach  collection='exchangerates' item='exchangerate' separator=','>"+
            "insert into exchangerate(bzid,orgid,xhmrj,xcmrj,xhmcj,xcmcj,fbsj,fbsjdate,bwid)values"
            +"<foreach  collection='exchangerates' item='exchangerate' separator=','>"
            +"(#{exchangerate.bzid},#{exchangerate.orgid},#{exchangerate.xhmrj},#{exchangerate.xcmrj},"
            +"#{exchangerate.xhmcj},#{exchangerate.xcmcj},#{exchangerate.fbsj},#{exchangerate.fbsjdate},#{exchangerate.bwid})"
            +"</foreach>"
            +"</script>"
            /*"<foreach  collection='redisvisitrecords' item='redisvisitrecord'>"+
            "INSERT INTO visitrecorddb.visitrecord(pagename,visitpara,userid,visittime)VALUES(#{redisvisitrecord.pagename},#{redisvisitrecord.visitpara},#{redisvisitrecord.userid},#{redisvisitrecord.visittime});" +
            "INSERT INTO visitrecordinfodb.visitrecordinfo(recordid,userip,useragent)VALUES(LAST_INSERT_ID(),#{redisvisitrecord.userip},#{redisvisitrecord.useragent});"
            +"</foreach>"
            +"</script>"*/
    )
    int insertexchangerates(@Param("exchangerates") List<Exchangerate> exchangerates);
    //增加汇率报文内容并返回主键
    @Insert("insert into exchangeratecontent(content,orgid,doctype)values(#{exchangeratecontent.content},#{exchangeratecontent.orgid},#{exchangeratecontent.doctype});")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn="id")
    int insertexchangeratecontent(@Param("exchangeratecontent") Exchangeratecontent exchangeratecontent);
    //查询最近n天的汇率数据
    @Select("SELECT * FROM exchangerate where fbsj>DATE_SUB(CURDATE(), INTERVAL #{days} DAY) order by fbsj asc")
    List<Exchangerate> selectexchangerates(@Param("days") int days);
    //查询大于某个时间点的汇率数据
    @Select("SELECT * FROM exchangerate where fbsj>#{fromdate} order by fbsj asc")
    List<Exchangerate> selectfromdateexchangerates(@Param("fromdate") Date fromdate);
    //根据时间段查询每天每种汇率的最终汇率
    @Select("<script>"+
            "SELECT * FROM exchangerate WHERE CONCAT(bzid,id) IN("+
            "SELECT CONCAT(bzid,id) FROM "+
            "(SELECT bzid, fbsjdate,MAX(id) id FROM exchangerate " +
            "where fbsj <![CDATA[>=]]> #{fromdate} and fbsj <![CDATA[<=]]> #{todate} " +
            "<if test='bizhongidstr!=null and bizhongidstr!=\"\" '>"+
            " and bzid in (#{bizhongidstr})"+
            "</if>"+
            " GROUP BY fbsjdate,bzid)a"+
            ") ORDER BY fbsj ASC"+
            "</script>"
    )
    List<Exchangerate> selecteverydaylastexchangerates(@Param("fromdate") Date fromdate,@Param("todate") Date todate,@Param("bizhongidstr") String bizhongidstr);
    //根据时间段查询每天每种汇率的最高和最低汇率
    @Select("<script>"+
            "SELECT * FROM exchangerate WHERE CONCAT(bzid,',',fbsjdate,',',xhmrj) IN("+
            "SELECT CONCAT(bzid,',',fbsjdate,',',xhmrj) FROM "+
            "(SELECT bzid, fbsjdate,MAX(xhmrj) xhmrj FROM exchangerate " +
            "where fbsj <![CDATA[>=]]> #{fromdate} and fbsj <![CDATA[<=]]> #{todate} " +
            "<if test='bizhongidstr!=null and bizhongidstr!=\"\" '>"+
            " and bzid in (#{bizhongidstr})"+
            "</if>"+
            " GROUP BY fbsjdate,bzid union " +
            "SELECT bzid, fbsjdate,MIN(xhmrj) xhmrj FROM exchangerate " +
            "where fbsj <![CDATA[>=]]> #{fromdate} and fbsj <![CDATA[<=]]> #{todate} " +
                    "<if test='bizhongidstr!=null and bizhongidstr!=\"\" '>"+
                    " and bzid in (#{bizhongidstr})"+
                    "</if>"+
                    " GROUP BY fbsjdate,bzid"+
            ")a"+
            ") ORDER BY fbsj ASC"+
            "</script>"
    )
    List<Exchangerate> selecteverydayhandlhls(@Param("fromdate") Date fromdate,@Param("todate") Date todate,@Param("bizhongidstr") String bizhongidstr);
    //查询每个币种每天汇率最高的一条记录
    @Select("SELECT DISTINCT(CONCAT(bzid,';',DATE_FORMAT(fbsj,'%Y-%m-%d %H'))) FROM financialtdb.exchangerate WHERE CONCAT(bzid,fbsjdate,xhmrj)IN("+
    "SELECT CONCAT(bzid,fbsjdate,xhmrjd) FROM ("+
    "SELECT bzid,fbsjdate,MAX(xhmrj) xhmrjd FROM financialtdb.exchangerate "+
    "GROUP BY bzid,fbsjdate"+
    ") a)"
    )
    List<String> getbzhighhleveryday();
    //查询每个币种每天汇率最低的一条记录
    @Select("SELECT DISTINCT(CONCAT(bzid,';',DATE_FORMAT(fbsj,'%Y-%m-%d %H'))) FROM financialtdb.exchangerate WHERE CONCAT(bzid,fbsjdate,xhmrj)IN("+
            "SELECT CONCAT(bzid,fbsjdate,xhmrjd) FROM ("+
            "SELECT bzid,fbsjdate,MIN(xhmrj) xhmrjd FROM financialtdb.exchangerate "+
            "GROUP BY bzid,fbsjdate"+
            ") a)"
    )
    List<String> getbzlowhleveryday();
    //修改每个币种的汇率分析数据
    @Update("<script>"
            +"<foreach  collection='bizhongs' item='bizhong' separator=';'>"
            +"update bizhong set bzzdgl = #{bizhong.bzzdgl} where id = #{bizhong.id}"
            +"</foreach>"
            +"</script>"
    )
    int updatehlfxsj(@Param("bizhongs") List<Bizhong> bizhongs);
    //获取某个时刻某个币种的汇率记录
    @Select("SELECT * FROM exchangerate WHERE fbsj<=#{fbsj} and bzid=#{bzid} order by fbsj desc limit 1")
    Exchangerate selecthlbydatetime(@Param("fbsj") Date fbsj,@Param("bzid") long bzid);
    //查询所有币种的列表
    @Select("<script>"+
            "select * FROM bizhong"+
            "<where>"+
            "<if test='bzstatus!=null and bzstatus>0 '>"+
            " bzstatus=#{bzstatus}"+
            "</if>"+
            "<if test='bzname!=null and bzname!=\"\" '>"+
            " and bzname like '%#{bzname}%'"+
            "</if>"+
            "</where>"+
            " order by bindex asc,id asc"+
            "</script>"
    )
    List<Bizhong> getbizhongs(@Param("bzstatus") int bzstatus, @Param("bzname") String bzname);
    //修改币种对应的设置
    @Update("update bizhong set xmrhmclvrunlvalavalue=#{xmrhmclvrunlvalavalue},xmrhmclvrunalavalue=#{xmrhmclvrunalavalue}," +
            "xmrhmcyhmrjalavalue=#{xmrhmcyhmrjalavalue},xmrhmcyhmcjalavalue=#{xmrhmcyhmcjalavalue}," +
            "xmchmrlvrunlvalavalue=#{xmchmrlvrunlvalavalue},xmchmrlvrunalavalue=#{xmchmrlvrunalavalue},"+
            "xmchmryhmrjalavalue=#{xmchmryhmrjalavalue},xmchmryhmcjalavalue=#{xmchmryhmcjalavalue}" +
            " where id=#{bzid};")
    int setbzseting(@Param("bzid") long bzid,@Param("xmrhmclvrunlvalavalue") String xmrhmclvrunlvalavalue,@Param("xmrhmclvrunalavalue") String xmrhmclvrunalavalue
            ,@Param("xmrhmcyhmrjalavalue") String xmrhmcyhmrjalavalue,@Param("xmrhmcyhmcjalavalue") String xmrhmcyhmcjalavalue
            ,@Param("xmchmrlvrunlvalavalue") String xmchmrlvrunlvalavalue,@Param("xmchmrlvrunalavalue") String xmchmrlvrunalavalue
            ,@Param("xmchmryhmrjalavalue") String xmchmryhmrjalavalue,@Param("xmchmryhmcjalavalue") String xmchmryhmcjalavalue);
    //查询币种对应的外汇快照
    @Select("select * FROM whkuaizhao where userid=#{userid} and bzid=#{bzid};")
    Whkuaizhao getwhkzbyuseridandbz(@Param("userid") long userid,@Param("bzid") long bzid);
    //更新外汇快照
    @Update("update whkuaizhao set xmrhmcjine=xmrhmcjine+#{xmrhmcjine},xmrhmcrmbcb=xmrhmcrmbcb+#{xmrhmcrmbcb}," +
            "xmchmrjine=xmchmrjine+#{xmchmrjine},xmchmrrmbcb=xmchmrrmbcb+#{xmchmrrmbcb}," +
            "modifytime=now() where userid=#{userid} and bzid=#{bzid};")
    int updatewhkz(@Param("userid") long userid,@Param("bzid") long bzid,@Param("xmrhmcjine") float xmrhmcjine,@Param("xmrhmcrmbcb") float xmrhmcrmbcb,
                   @Param("xmchmrjine") float xmchmrjine,@Param("xmchmrrmbcb") float xmchmrrmbcb);
    //新增外汇快照
    @Insert("insert into whkuaizhao(userid,bzid,xmrhmcjine,xmrhmcrmbcb,xmchmrjine,xmchmrrmbcb)values(" +
            "#{userid},#{bzid},#{xmrhmcjine},#{xmrhmcrmbcb},#{xmchmrjine},#{xmchmrrmbcb})")
    int insertwhkz(@Param("userid") long userid,@Param("bzid") long bzid,@Param("xmrhmcjine") float xmrhmcjine,@Param("xmrhmcrmbcb") float xmrhmcrmbcb,
                   @Param("xmchmrjine") float xmchmrjine,@Param("xmchmrrmbcb") float xmchmrrmbcb);
    //获取外汇快照记录
    @Select("select whkuaizhao.id,userid,bzid,bzname,xmrhmcjine,xmrhmcrmbcb,xmchmrjine,xmchmrrmbcb,modifytime from whkuaizhao left join bizhong on bizhong.id=whkuaizhao.bzid where xmrhmcjine>0 or xmchmrjine>0")
    List<Whkuaizhao> getwhkuaizhaos();
    //删除所有外汇快照
    @Delete("delete from whkuaizhao")
    void deletewhkz();
    //新增外汇交易记录
    @Insert("insert into whjyb(userid,jybzid,jyjine,cjjine,jyleixing,jiaoyisj,remark)values(#{whjyb.userid},#{whjyb.jybzid}," +
            "#{whjyb.jyjine},#{whjyb.cjjine},#{whjyb.jyleixing},#{whjyb.jiaoyisj},#{whjyb.remark})")
    int insertwhjyb(@Param("whjyb") Whjyb whjyb);
    //查询外汇交易记录
    @Select("<script>"
            +"select id,userid,jybzid,(select bzname from bizhong where id=jybzid) jybzname,jyjine," +
            "cjjine,jyleixing,(case jyleixing when 1 then '买入开仓' when 2 then '卖出平仓' when 3 then '卖出开仓' when 4 then '买入平仓' " +
            "when 30 then '交账户外汇保证金' " +
            "when 31 then '交账户贵金属保证金' " +
            "when 40 then '退账户外汇保证金' " +
            "when 41 then '退账户贵金属保证金' " +
            "when 50 then '转微信' when 51 then '转支付宝' when 52 then '转翼支付' " +
            "when 69 then '理财购买' " +
            "when 70 then '微信转入' when 71 then '支付宝转入' when 72 then '翼支付转入' " +
            "when 89 then '理财赎回' " +
            "when 90 then '现金存款' when 91 then '转账入'  when 92 then '利息' " +
            "when 110 then '取现' when 111 then '转账出' when 112 then '消费' when 113 then '短信费' when 114 then '开卡费' " +
            "when 115 then '换卡费' when 116 then '开户费' " +
            "end) jyleixingname," +
            "jiaoyisj,addtime," +
            "(case when (jybzid <![CDATA[>=]]> 107 AND jybzid <![CDATA[<=]]> 152) THEN "+
            "FORMAT(cjjine*100/jyjine,2) "+
            "WHEN (jybzid <![CDATA[>=]]> 162 AND jybzid <![CDATA[<=]]> 197) THEN "+
            "FORMAT(cjjine/jyjine,2) "+
            "else "+
            "FORMAT(cjjine*100/jyjine,2) "+
            "END) "+
            " cjjiage,xjzhyue,whbzjyue,zhgjsbzjyue,remark,jslirun,xmrhmccb FROM whjyb where userid=#{userid} "
            + "<if test='jybzidstr!=null and jybzidstr!=\"\" '>"
            +" and jybzid in (#{jybzidstr})"
            + "</if>"
            +" order by jiaoyisj desc"
            +"</script>"
    )
    List<Whjyb> getwhjyjl(@Param("userid") long userid,@Param("jybzidstr") String jybzidstr);

    //查询外汇交易记录
    @Select("<script>"
            +"select id,userid,jybzid,(select bzname from bizhong where id=jybzid) jybzname,jyjine," +
            "cjjine,jyleixing," +
            "jiaoyisj,addtime,jslirun,xmrhmccb FROM whjyb where userid=#{userid} and jyleixing <![CDATA[>=]]> 1 and jyleixing <![CDATA[<=]]> 4"
            +" order by jiaoyisj asc"
            +"</script>"
    )
    List<Whjyb> getallwhjyjl(@Param("userid") long userid);

    //查询所有交易记录
    @Select("<script>"
            +"select * FROM whjyb where userid=#{userid} and jiaoyisj <![CDATA[>=]]>"
            +" #{kssj} and jiaoyisj <![CDATA[<=]]> #{jssj} order by jiaoyisj asc"
            +"</script>"
    )
    List<Whjyb> getjyjlbytime(@Param("userid") long userid,@Param("kssj") Date kssj,@Param("jssj") Date jssj);

    //查询时间段交易记录
    @Select("<script>"
            +"select * FROM whjyb where userid=#{userid}"
            +" order by jiaoyisj asc"
            +"</script>"
    )
    List<Whjyb> getalljyjl(@Param("userid") long userid);

    //更新交易记录自带的银行现金账户余额及外汇保证金账户余额
    @Update("update whjyb set xjzhyue=#{xjzhyue},whbzjyue=#{whbzjyue},zhgjsbzjyue=#{zhgjsbzjyue},xmrhmccb=#{xmrhmccb} where id=#{whjybid}")
    int updatejyjlrmbyue(@Param("xjzhyue") float xjzhyue,@Param("whbzjyue") float whbzjyue,@Param("zhgjsbzjyue") float zhgjsbzjyue,@Param("xmrhmccb") float xmrhmccb,@Param("whjybid") long whjybid);

    //更新交易记录自带的结算利润数据，结算利润指的是每币种每投资方向（比如先买后卖或先卖后卖）平仓后计算所得的利润
    @Update("update whjyb set jslirun=#{jslirun} where id=#{whjybid}")
    int updatejslirunbyid(@Param("jslirun") float jslirun,@Param("whjybid") long whjybid);

    //根据id查询外汇交易记录
    @Select("select * FROM whjyb where id=#{id}")
    Whjyb getwhjyjlbyid(@Param("id") long id);

    //删除外汇交易记录
    @Delete("delete from whjyb where id=#{whjybid}")
    int deletewhjybbyid(@Param("whjybid") long whjybid);
    //更新用户人民币余额
    @Update("update yonghub set rmbyue=#{rmbyue} where id=#{userid}")
    int updateuserrmbyue(@Param("rmbyue") float rmbyue,@Param("userid") long userid);
    //查询用户人民币余额
    @Select("select rmbyue from yonghub where id=#{userid}")
    float selectrmbyuebyuserid(@Param("userid") long userid);
    //更新用户账户外汇保证金余额
    @Update("update yonghub set whbzjyue=#{whbzjyue} where id=#{userid}")
    int updateuserbzjyue(@Param("whbzjyue") float whbzjyue,@Param("userid") long userid);
    //更新用户账户贵金属保证金余额
    @Update("update yonghub set zhgjsbzjyue=#{zhgjsbzjyue} where id=#{userid}")
    int updateuserzhgjsbzjyue(@Param("zhgjsbzjyue") float zhgjsbzjyue,@Param("userid") long userid);
    //查询用户账户外汇保证金余额
    @Select("select whbzjyue from yonghub where id=#{userid}")
    float selectbzjyuebyuserid(@Param("userid") long userid);
    //查询用户账户贵金属保证金余额
    @Select("select zhgjsbzjyue from yonghub where id=#{userid}")
    float selectzhgjsbzjyuebyuserid(@Param("userid") long userid);
}
