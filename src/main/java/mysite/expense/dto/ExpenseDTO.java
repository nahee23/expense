package mysite.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data //Get,Set 메소드 + toString 등
@AllArgsConstructor //전체 필드 생성자
@NoArgsConstructor //기본 생성자 ()
public class ExpenseDTO {

    private Long id;
    private String expenseId;
    private String name;
    private String description;
    private Long amount;
    private Date date;
    private String dateString; //날짜를 입력받을때 사용

}
