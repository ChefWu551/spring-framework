package com.mountain;

import com.mountain.model.BookNum;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class MainJdbcTemplate {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config.xml");
        JdbcTemplate template = applicationContext.getBean(JdbcTemplate.class);
        System.out.println(template);

    /*    String sqlUpdate = "update bookNum set num=? where id=?";
        int resultUpdate = template.update(sqlUpdate, 100, 1);
        System.out.println("更新了: " + resultUpdate);*/

       /* String sqlInsert = "insert into bookNum (id, num) values(?, ?)";
        List<Object[]> batchArgs = new ArrayList<Object[]>() {{
            add(new Object[]{7, 10}); add(new Object[]{8, 10});
        }};

        int[] resultInsert = template.batchUpdate(sqlInsert, batchArgs);
        template.execute("DELETE from bookNum where id in (7, 8)");
        System.out.println(resultInsert.toString());*/

        /*BookNum book = template.queryForObject("select id, num from bookNum where id = ?", new BeanPropertyRowMapper<>(BookNum.class), 5);
        System.out.println(book.toString());

        List<BookNum> bookNums = template.query("select id, num from bookNum where num = ?", new BeanPropertyRowMapper<>(BookNum.class), 10);
        bookNums.forEach(item -> System.out.println(item.toString()));*/


    }
}
