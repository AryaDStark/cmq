package com.ntu.cmq.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


public class GeneratorForArya {
    public static void mainTTTTTTT(String[] args) {
        //代码生成器对象
        AutoGenerator autoGenerator=new AutoGenerator();

        //全局配置
        GlobalConfig gc= new GlobalConfig();
        String projectPath =System.getProperty("user.dir");

        //※※※※※※※※※※※※※※※※※※※※※※        代码生成的位置              ※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
        gc.setOutputDir(projectPath+"/src/main/java");



        gc.setAuthor("cmq");
        gc.setOpen(false);
        gc.setFileOverride(false);     //是否覆盖代码
        gc.setServiceName("%sService");
        gc.setIdType(IdType.ID_WORKER);  //设置主键默认方式
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        autoGenerator.setGlobalConfig(gc);

        //设置数据源
        DataSourceConfig dsc=new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/cmqq?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("1234");
        dsc.setDbType(DbType.MYSQL);
        autoGenerator.setDataSource(dsc);

        //包的配置
        PackageConfig pc =new PackageConfig();
        pc.setModuleName("cmqq");                   //最外层的包名
         pc.setParent("com.ntu");  //生成在哪个包下
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
//        pc.setController("web");
        autoGenerator.setPackageInfo(pc);

        //策略配置
        StrategyConfig strategy=new StrategyConfig();
        strategy.setInclude("course","courseware","means","signin","student","teach","teacher","test","work","stu_signin","stu_teach","stu_test","stu_work","achieve");                     //设置映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel); //设置驼峰命名法
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体，没有就不用配置");
        strategy.setEntityLombokModel(true);            //自动lombok
        //逻辑删除
        strategy.setLogicDeleteFieldName("deleted");
        //自动填充
//        TableFill gmtCreate= new TableFill("gmt_create", FieldFill.INSERT);
//        TableFill gmtModified=new TableFill("gmt_modified",FieldFill.INSERT_UPDATE);
//        ArrayList<TableFill> tableFills=new ArrayList<>();
//        tableFills.add(gmtCreate);
//        tableFills.add(gmtModified);
//        strategy.setTableFillList(tableFills);

        //乐观锁
//        strategy.setVersionFieldName("version");

        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true); //localhost:8080/hello_id_2  url可以使用下划线
        autoGenerator.setStrategy(strategy);



        autoGenerator.execute();
    }
}
